package com.jingjia.chengdi;

import android.util.Log;

import com.jingjia.chengdi.data.NetUri;
import com.jingjia.chengdi.utils.NetStatus;
import com.jingjia.chengdi.utils.NetUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testNetUtils(){
        Map<String,String> param = new HashMap<>();
        NetUtils.get(NetUri.URI_GET_REQUIRES, param, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {
                System.out.println(s);
                Log.i("",s);
            }
        });
    }
}