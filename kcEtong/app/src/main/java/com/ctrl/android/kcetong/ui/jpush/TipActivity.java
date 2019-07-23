package com.ctrl.android.kcetong.ui.jpush;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ctrl.android.kcetong.R;
import com.ctrl.android.kcetong.ui.activity.StartActivity;
import com.ctrl.third.common.library.utils.AnimUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class TipActivity extends Activity {

    @BindView(R.id.title)
    TextView title_txt;
    @BindView(R.id.message)
    TextView message_txt;
    @BindView(R.id.positiveButton)
    Button   positiveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tip_activity);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle  = getIntent().getExtras();
	        String title   = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);

            title_txt.setText(getResources().getString(R.string.app_tip));
            message_txt.setText(content);



            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(TipActivity.this, StartActivity.class);
                    startActivity(intent);
                    AnimUtil.intentSlidIn(TipActivity.this);
                    finish();
                }
            });

        }

    }


   /* private void showPropertiorDialog(String content){
        final CustomDialog.Builder builder = new CustomDialog.Builder(TipActivity.this);
        builder.setTitle(getResources().getString(R.string.app_tip));
        builder.setMessage(content);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.hidenNegativeButton();

        builder.create().show();
    }*/


}
