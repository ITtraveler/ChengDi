package com.jingjia.chengdi.utils;

import android.content.Context;
import android.view.View;

import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.dialog.EditContentDialog;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/10/24.
 */
public class ActionUtils {
    private static ActionUtils actionUtils;

    public static ActionUtils newInstance() {
        if (actionUtils == null) {
            actionUtils = new ActionUtils();
        }
        return actionUtils;
    }

    /**
     * 用户投诉动作
     */
    public void actionComplaint(final Context context, final String userId, final String requireId) {
        final EditContentDialog contentDialog = new EditContentDialog(context);
        contentDialog.show();
        contentDialog.setBnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = contentDialog.getEditContent();
                if (check(context, content)) {
                    Map<String, String> param = new HashMap<>();
                    param.put("userPhone", userId);
                    param.put("context", content);
                    param.put("requireId", requireId);

                    NetUtils.get(NetUri.URI_ISSUE_COMPLAIN, param, new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            contentDialog.cancel();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            if (s.equals("" + NetUri.Status.SUCCESS)) {
                                ActivityUtils.toast(context, "投诉成功，我们会及时处理。");
                            } else {
                                ActivityUtils.toast(context, "投诉失败。");
                            }
                            contentDialog.cancel();
                        }
                    });
                }
            }
        });

    }

    private boolean check(Context context, String content) {
        if (content.length() < 5) {
            ActivityUtils.toast(context, "请输入详细内容");
            return false;
        } else
            return true;
    }
}
