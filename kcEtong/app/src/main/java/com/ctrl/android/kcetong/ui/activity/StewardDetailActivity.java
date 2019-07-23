package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.EmessagingDetailBean;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.D;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StewardDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)//标题
            TextView tv_title;
    @BindView(R.id.tv_time)//时间
            TextView tv_time;
    @BindView(R.id.tv_content)//内容
            TextView tv_content;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_steward_detail);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.E_STEWARD, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.stopSpeaking();
                StewardDetailActivity.this.finish();
            }
        });
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(StewardDetailActivity.this, mTtsInitListener);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
    }

    @Override
    protected void initData() {
        questionDetail(getIntent().getStringExtra("id"));
    }

    /**
     * 问题详情列表
     */
    private void questionDetail(String id) {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantsData.METHOD, Url.EStewardListDetailUrl);
        map.put("id", id);
        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);

        RetrofitUtil.Api().EmessagingDetail(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<EmessagingDetailBean>(this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
            }

            @Override
            public void onNext(EmessagingDetailBean emessagingDetailBean) {
                super.onNext(emessagingDetailBean);

                if (TextUtils.equals(ConstantsData.success, emessagingDetailBean.getCode())) {
                    tv_title.setText(emessagingDetailBean.getData().getQuestionAnswer().getProblemName());
                    tv_content.setText(emessagingDetailBean.getData().getQuestionAnswer().getQuestionAnswer());
                    if (emessagingDetailBean.getData().getQuestionAnswer().getCreateTime() != null)
                        tv_time.setText(D.getDateStrFromStamp("yyyy-MM-dd HH:mm", emessagingDetailBean.getData().getQuestionAnswer().getCreateTime()));
                    speak(emessagingDetailBean.getData().getQuestionAnswer().getProblemName()
                          + emessagingDetailBean.getData().getQuestionAnswer().getQuestionAnswer());
                }
            }
        });
    }

    private void speak(String text){
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(StewardDetailActivity.this, "tts_play");

        // 设置参数
        setParam();
        int code = mTts.startSpeaking(text, mTtsListener);
//			/**
//			 * 只保存音频不进行播放接口,调用此接口请注释startSpeaking接口
//			 * text:要合成的文本，uri:需要保存的音频全路径，listener:回调接口
//			*/
//			String path = Environment.getExternalStorageDirectory()+"/tts.pcm";
//			int code = mTts.synthesizeToUri(text, path, mTtsListener);

        if (code != ErrorCode.SUCCESS) {
            if(code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED){
                //未安装则跳转到提示安装页面
//                mInstaller.install();
            }else {
                showTip("语音合成失败,错误码: " + code);
            }
        }
    }

    /**
     * 参数设置
     *
     */
    private void setParam(){
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if(mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        }else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /*
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE,  "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.wav");
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            Log.d(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
//            showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
//            showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                String info) {
            // 合成进度
            mPercentForBuffering = percent;
//            showTip(String.format(getString(R.string.tts_toast_format),mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            mPercentForPlaying = percent;
//            showTip(String.format(getString(R.string.tts_toast_format), mPercentForBuffering, mPercentForPlaying));
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
        }
    };
    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mTts.stopSpeaking();
    }
}
