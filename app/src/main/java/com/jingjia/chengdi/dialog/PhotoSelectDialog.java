package com.jingjia.chengdi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;

import com.jingjia.chengdi.R;

/**
 * Created by Administrator on 2016/9/28.
 */
public class PhotoSelectDialog extends AlertDialog {

    private View bnCamera, bnAlbum, bnDefault;

    public PhotoSelectDialog(Context context) {
        super(context);
        init();
    }

    private void init() {
        View v = getLayoutInflater().inflate(R.layout.dialog_select_photo, null);
        bnCamera = v.findViewById(R.id.dialog_photo_camera);
        bnAlbum = v.findViewById(R.id.dialog_photo_album);
        bnDefault = v.findViewById(R.id.dialog_photo_default);
        setView(v);
    }

    public void setBnCameraListener(View.OnClickListener listener) {
        bnCamera.setOnClickListener(listener);
    }

    public void setBnAlbumListener(View.OnClickListener listener) {
        bnAlbum.setOnClickListener(listener);
    }

    public void setBnDefaultListener(View.OnClickListener listener) {
        bnDefault.setOnClickListener(listener);
    }

    public void cancelListener(){
        cancelListener();
    }

}
