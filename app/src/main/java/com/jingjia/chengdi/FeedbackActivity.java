package com.jingjia.chengdi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/3.
 */
public class FeedbackActivity extends AppCompatActivity implements TextWatcher {
    private EditText et_content;
    private TextView tv_count;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        et_content = (EditText) findViewById(R.id.feedback_content);
        tv_count = (TextView) findViewById(R.id.feedback_count);
        et_content.addTextChangedListener(this);
    }


    public void hand(View view) {
        String feedbackContent = et_content.getText().toString().trim();
        String jsonContent = "{\"username\":\"" + MyApplication.user.getPhone() + "\",\"content\":\"" +
                "" + feedbackContent + "\",}";

        NetUtils.postJson(NetUri.URI_FEEDBACK, jsonContent, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {

            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        int num = et_content.getText().length();
        tv_count.setText(num + "/120");
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
