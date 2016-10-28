package com.jingjia.chengdi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.jingjia.chengdi.utils.BDLocationUtils;
import com.jingjia.chengdi.utils.MyApplication;

import java.util.List;

/**
 * Created by Administrator on 2016/9/24.
 */
public class LocationSelectActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String LOCATION = "LOCATION";

    private BDLocationUtils bdLocationUtils;
    private List<String> locationList;
    private TextView tv_address;
    private EditText et_locationDescribe;
    private ListView poi_listView;
    private ArrayAdapter<String> listAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        bdLocationUtils = BDLocationUtils.newInstance(getApplicationContext());
        locationList = bdLocationUtils.getLocationList();
        initView();
    }


    private void initView() {
        //获取位置按钮
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout_location);
        assert linearLayout != null;
        linearLayout.setOnClickListener(this);
        //定位得到的位置
        tv_address = (TextView) findViewById(R.id.location_address);
        et_locationDescribe = (EditText) findViewById(R.id.locationDescribe);
        poi_listView = (ListView) findViewById(R.id.poi_listView);
        listAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, locationList);
        poi_listView.setAdapter(listAdapter);
        poi_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                et_locationDescribe.setText(locationList.get(position));
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.linearLayout_location) {
            bdLocationUtils.startLocation();
            locationList = bdLocationUtils.getLocationList();
            tv_address.setText(bdLocationUtils.getAddress());
            listAdapter.notifyDataSetChanged();
            toastDescribe();
        }
    }

    public void confirm(View view) {
        String describe = et_locationDescribe.getText().toString();
        if (!describe.isEmpty() && describe.length() > 0 && describe != null) {
            Intent intent = new Intent();
            intent.putExtra(LOCATION, et_locationDescribe.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(this, "请填写区域详细位置！", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {
        super.finish();
        bdLocationUtils.stopLocation();
        MyApplication.location.setLocationdescribe(et_locationDescribe.getText().toString().trim());
    }

    private void toastDescribe() {
        Toast.makeText(LocationSelectActivity.this, bdLocationUtils.getDescribe(), Toast.LENGTH_SHORT).show();
    }
}
