package com.jingjia.chengdi;

import android.content.Intent;
import android.content.SyncStatusObserver;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jingjia.chengdi.custom.view.CircleImageView;
import com.jingjia.chengdi.custom.view.OnWheelViewListener;
import com.jingjia.chengdi.data.CDDbSchema;
import com.jingjia.chengdi.data.NetUri;

import com.jingjia.chengdi.data.encapsulation.User;
import com.jingjia.chengdi.dialog.EditContentDialog;
import com.jingjia.chengdi.dialog.OnDialogProvinceListener;
import com.jingjia.chengdi.dialog.PhotoSelectDialog;
import com.jingjia.chengdi.dialog.ProvinceDialog;
import com.jingjia.chengdi.dialog.SelectItemDialog;
import com.jingjia.chengdi.utils.ImageUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetUtils;
import com.jingjia.chengdi.utils.SQLiteDbUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/22.
 */
public class UserInfoActivity extends AppCompatActivity implements View.OnClickListener {
    List<String> sexItems = new ArrayList<>();
    List<String> ageItem = new ArrayList<>();
    private SelectItemDialog itemDialog;
    private TextView tvAge, tvUserName, tvPhone, tvSex, tvHometown, tvJob, tvSubscript;
    private CircleImageView civHead;
    private PhotoSelectDialog photoDialog;
    private ImageUtils imUtils;
    private Intent clipIntent;
    private User user;
    private boolean haveChange;//是否改变

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        imUtils = new ImageUtils();
        civHead = (CircleImageView) findViewById(R.id.info_set_head);
        tvUserName = (TextView) findViewById(R.id.info_set_name);
        tvPhone = (TextView) findViewById(R.id.info_set_phone);
        tvSex = (TextView) findViewById(R.id.info_set_sex);
        tvAge = (TextView) findViewById(R.id.info_set_age);
        tvHometown = (TextView) findViewById(R.id.info_set_hometown);
        tvJob = (TextView) findViewById(R.id.info_set_job);
        tvSubscript = (TextView) findViewById(R.id.info_set_subscript);
        initPhotoDialog();
        initDate();
    }

    /**
     * 初始化头像设置对话框
     */
    private void initPhotoDialog() {
        photoDialog = new PhotoSelectDialog(this);
        photoDialog.setBnAlbumListener(this);
        photoDialog.setBnCameraListener(this);
        photoDialog.setBnDefaultListener(this);
    }

    /**
     * 初始化用于对话框选项的数据
     */
    private void initDate() {
        user = MyApplication.user;
        tvUserName.setText(user.getUsername());
        tvSex.setText(user.getSex());
        tvAge.setText(user.getAge() + "0后");
        tvPhone.setText(user.getPhone());
        tvHometown.setText(user.getHometown());
        tvJob.setText(user.getJob());
        tvSubscript.setText(user.getSubScript());

        sexItems.add("男");
        sexItems.add("女");
        ageItem.add("00后");
        for (int i = 9; i > 3; i--) {
            ageItem.add(i + "0后");
        }
    }

    /**
     * 设置头像
     *
     * @param view
     */
    public void setHead(View view) {
        haveChange = true;
        photoDialog.show();
    }

    public void setName(View view) {
        haveChange = true;

    }

    public void setSex(View view) {
        haveChange = true;
        itemDialog = new SelectItemDialog(this);
        itemDialog.setItem(sexItems);
        itemDialog.show();
        itemSelect(tvSex);

    }

    public void setAge(View view) {
        haveChange = true;
        itemDialog = new SelectItemDialog(this);
        itemDialog.setItem(ageItem);
        itemDialog.show();
        itemSelect(tvAge);
    }

    public void setPhone(View view) {
        //  itemDialog = new SelectItemDialog(this);
    }

    public void setHometown(View view) {
        haveChange = true;
        ProvinceDialog provinceDialog = new ProvinceDialog(this, new OnDialogProvinceListener() {
            @Override
            public void getProvince(String province, String city, String district) {
                String s;
                if (province.equals(city))
                    s = province + district;
                else
                    s = province + city + district;
                tvHometown.setText(s);
            }
        });
        provinceDialog.show();
    }

    public void setJob(View view) {
        haveChange = true;
        itemDialog = new SelectItemDialog(this);
    }

    public void setSubScript(View view) {
        haveChange = true;
        final EditContentDialog dialog = new EditContentDialog(this);
        dialog.setBnConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置我的签名内容
                tvSubscript.setText(dialog.getEditContent());
                dialog.cancel();
            }
        });
        dialog.show();
    }


    public void setAuthentication(View view) {

    }

    private void itemSelect(final TextView tv) {
        itemDialog.setWheelViewListener(new OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                tv.setText(item);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_photo_camera://打开相机
                photoDialog.cancel();
                Intent cameraIntent = imUtils.getOpenCameraIntent();
                startActivityForResult(cameraIntent, ImageUtils.REQUESTCODE_CAMERA);

                break;
            case R.id.dialog_photo_album:
                photoDialog.cancel();
                Intent albumIntent = imUtils.getOpenAlbumIntent();
                startActivityForResult(albumIntent, ImageUtils.REQUESTCODE_ALBUM);
                break;
            case R.id.dialog_photo_default:

                photoDialog.cancel();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (requestCode == ImageUtils.REQUESTCODE_CAMERA) {
            clipIntent = imUtils.getClipPhotoIntent(imUtils.getUri());
            startActivityForResult(clipIntent, ImageUtils.REQUESTCODE_CLIP);
        } else if (requestCode == ImageUtils.REQUESTCODE_ALBUM) {
            Uri uri = data.getData();
            clipIntent = imUtils.getClipPhotoIntent(uri);
            startActivityForResult(clipIntent, ImageUtils.REQUESTCODE_CLIP);
        } else if (requestCode == ImageUtils.REQUESTCODE_CLIP) {
            // Bitmap bitmap = imUtils.getBitmap(clipIntent);
            Bitmap bitmap = data.getParcelableExtra("data");//正确做法
            if (bitmap != null) {
                /**
                 * 此处可以得到用户从相册或照相得到剪切后的缩图，然后上传到服务器，更新图片
                 */
                civHead.setImageBitmap(bitmap);
            }
        }
    }

    private void hand2ServerAndSql() {
        user.setUsername(tvUserName.getText().toString().trim());
        user.setPhone(tvPhone.getText().toString().trim());
        String age = tvAge.getText().toString().trim();
        // System.out.println("age:"+age+"  "+age.charAt(0));
        int vAge = Integer.valueOf(String.valueOf(age.charAt(0)));//注意char
        System.out.println(vAge);
        user.setAge(vAge);
        user.setSex(tvSex.getText().toString().trim());
        user.setJob(tvJob.getText().toString().trim());
        user.setHometown(tvHometown.getText().toString().trim());
        user.setSubScript(tvSubscript.getText().toString().trim());
        user.setHaveAuthentication(0);
        Map<String, String> params = new HashMap<>();
        params.put("username", user.getUsername());
        params.put("sex", user.getSex());
        params.put("age", "" + user.getAge());
        params.put("phone", user.getPhone());
        params.put("hometown", user.getHometown());
        params.put("subScript", user.getSubScript());
        params.put("job", user.getJob());
        params.put("haveAuthentication", "" + user.getHaveAuthentication());

        NetUtils.post(NetUri.URI_UPDATE_USERINFO, params, new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int i) {

            }

            @Override
            public void onResponse(String s, int i) {

            }
        });

        //更新本地数据库存储的用户信息,根据手机号
//        new SQLiteDbUtils(getApplicationContext()).update(CDDbSchema.ChengdiTable.TABLE_USER,
//                SQLiteDbUtils.getUserContentValues(user), CDDbSchema.ChengdiTable.User.PHONE + "=?", new String[]{user.getPhone()});
        new SQLiteDbUtils(getApplicationContext()).updateUser(user, user.getPhone());
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (haveChange) {
            hand2ServerAndSql();
        }
        System.out.println("stop");
    }

//    @Override
//    public void finish() {
//        if (haveChange) {
//            hand2ServerAndSql();
//        }
//        super.finish();
//        System.out.println("finishInfo");
//    }
}
