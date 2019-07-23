package com.ctrl.android.kcetong.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.model.AppHolder;
import com.ctrl.android.kcetong.model.Managment;
import com.ctrl.android.kcetong.model.ManagmentBean;
import com.ctrl.android.kcetong.model.Problem;
import com.ctrl.android.kcetong.toolkit.Url.Url;
import com.ctrl.android.kcetong.toolkit.util.AopUtils;
import com.ctrl.android.kcetong.toolkit.util.ConstantsData;
import com.ctrl.android.kcetong.toolkit.util.InputMethodUtils;
import com.ctrl.android.kcetong.toolkit.util.JsonParser;
import com.ctrl.android.kcetong.toolkit.util.LLog;
import com.ctrl.android.kcetong.toolkit.util.MessageUtils;
import com.ctrl.android.kcetong.toolkit.util.StrConstant;
import com.ctrl.android.kcetong.toolkit.webutils.BaseTSubscriber;
import com.ctrl.android.kcetong.toolkit.webutils.RetrofitUtil;
import com.ctrl.android.kcetong.ui.adapter.MangmentAdapter;
import com.ctrl.android.kcetong.ui.base.BaseActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class EStewardActivity extends BaseActivity {

    @BindView(R.id.listView)//商品列表
            ListView listView;
    @BindView(R.id.et_e_house_keeper)//输入框
            EditText etEHouseKeeper;
    @BindView(R.id.tv_e_house_keeper_send)//发送
            TextView tvEHouseKeeperSend;
    @BindView(R.id.iv_voice)
    ImageView iv_voice;//语音
    private List<Managment> mManagmentList = new ArrayList<>();
    private MangmentAdapter mMangmentAdapter;

    // 语音合成对象
    private SpeechSynthesizer mTts;
    // 默认发音人
    private String voicer = "xiaoyan";

    // 缓冲进度
    private int mPercentForBuffering = 0;
    // 播放进度
    private int mPercentForPlaying = 0;

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;

    private Toast mToast;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<>();

    // 语音听写对象
    private SpeechRecognizer mIat;
    // 语音听写UI
    private RecognizerDialog mIatDialog;

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_estewar);
        ButterKnife.bind(this);
        toolbarBaseSetting(StrConstant.E_STEWARD, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTts.stopSpeaking();
                EStewardActivity.this.finish();
            }
        });
        // 初始化合成对象
        mTts = SpeechSynthesizer.createSynthesizer(EStewardActivity.this, mTtsInitListener);
        mToast = Toast.makeText(this,"",Toast.LENGTH_SHORT);

        // 初始化识别无UI识别对象
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面
        mIat = SpeechRecognizer.createRecognizer(EStewardActivity.this, mInitListener);

        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(EStewardActivity.this, mInitListener);
    }

    @Override
    protected void initData() {
        mMangmentAdapter = new MangmentAdapter(this);
        Managment managment = new Managment();
        managment.setType(1);
        List<Problem> list    = new ArrayList<>();
        Problem       problem = new Problem();
        problem.setNoneInfo("5");
        list.add(problem);
        managment.setListProblem(list);
        mManagmentList.add(managment);
        mMangmentAdapter.setList(mManagmentList);
        listView.setAdapter(mMangmentAdapter);

        speak(StrConstant.ESTEWARD_WELCOME);
        tvEHouseKeeperSend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                int index = tvEHouseKeeperSend.getSelectionStart() - 1;
                if (index > 0) {
                    if (InputMethodUtils.isEmojiCharacter(s.charAt(index))) {
                        Editable edit = (Editable) tvEHouseKeeperSend.getText();
                        edit.delete(index, index + 1);
                    }
                }
            }
        });
    }

    private void speak(String text){
        // 移动数据分析，收集开始合成事件
        FlowerCollector.onEvent(EStewardActivity.this, "tts_play");

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
     * 参数设置
     *
     */
    public void setTXParam() {
        // 清空参数
        mIat.setParameter(SpeechConstant.PARAMS, null);

        // 设置听写引擎
        mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

        // 设置语言
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mIat.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mIat.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        mIat.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
        mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * e管家列表
     *
     */
    private void getMessageList(String ownerProblem, String communityId) {
        Map<String, String> map = new HashMap<>();
        map.put(ConstantsData.METHOD, Url.EStewardListUrl);
        map.put("ownerProblem", ownerProblem);
        map.put("communityId", communityId);
        map.putAll(ConstantsData.getSystemParams());
        String sign = AopUtils.sign(map, ConstantsData.SECRET_VALUE);
        map.put("sign", sign);
        map.remove(ConstantsData.METHOD);
        LLog.d(map + "");
        RetrofitUtil.Api().Emessaging(map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new BaseTSubscriber<ManagmentBean>(EStewardActivity.this) {
            @Override
            public void onResponseCallback(JSONObject response, String resultCode) {
                LLog.d("onResponseCallback: " + response);
            }

            @Override
            public void onNext(ManagmentBean managmentBean) {
                super.onNext(managmentBean);
                if (TextUtils.equals("000", managmentBean.getCode())) {
                    Managment managment = new Managment();
                    managment.setType(1);
                    managment.setListProblem(managmentBean.getData().getProblemList());
                    mManagmentList.add(managment);
                    mMangmentAdapter.setList(mManagmentList);
                    //listView.setSelection(listView.getBottom());
                    listView.smoothScrollToPosition(listView.getCount() - 1);//移动到尾部
                    mMangmentAdapter.notifyDataSetChanged();

                    mTts.stopSpeaking();
                    if(managment.getListProblem().get(0).getNoneInfo().equals("0")){
                        String content = "";
                        for(int i = 0; i < managment.getListProblem().size(); i ++){
                            content += managment.getListProblem().get(i).getProblemName() ;
                        }
                        speak(StrConstant.ESTEWARD_ANSWER + content);
                    }else {
                        speak(StrConstant.ESTEWARD_NO_ANSWER);
                    }

                }
            }
        });

    }

    @OnClick({R.id.tv_e_house_keeper_send,R.id.iv_voice})
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.tv_e_house_keeper_send:
                if (TextUtils.isEmpty(etEHouseKeeper.getText().toString())) {
                    MessageUtils.showShortToast(this, "请输入内容");
                } else {
                    Managment managment = new Managment();
                    managment.setType(0);
                    managment.setQuestion(etEHouseKeeper.getText().toString().trim());
                    mManagmentList.add(managment);
                    getMessageList(etEHouseKeeper.getText().toString().trim(), AppHolder.getInstance().getHouse().getCommunityId());
                    etEHouseKeeper.setText("");
                    mMangmentAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_voice:
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(EStewardActivity.this, "iat_recognize");

                mIatResults.clear();
                // 设置参数
                setTXParam();

                // 显示听写对话框
                mIatDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                mIatDialog.setListener(mRecognizerDialogListener);

                mIatDialog.show();
                showTip(getString(R.string.text_begin));
                break;
        }

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etEHouseKeeper.setText(resultBuffer.toString());
        etEHouseKeeper.setSelection(etEHouseKeeper.length());
    }
    /**
     * 初始化监听器。
     */
    private InitListener             mInitListener             = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("---------", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败，错误码：" + code);
            }
        }
    };
    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {

            printResult(results);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {

            showTip(error.getPlainDescription(true));
        }

    };
    /**
     * 初始化监听。
     */
    private InitListener             mTtsInitListener          = new InitListener() {
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
    private SynthesizerListener mTtsListener     = new SynthesizerListener() {

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
