package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jingjia.chengdi.GoAcceptsDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.data.TestData;
import com.jingjia.chengdi.encapsulation.GoListPublish;
import com.jingjia.chengdi.utils.ActivityUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class FragmentGoPublishContent extends Fragment {
    private static int curMode;
    private static final int MODE_ING = 0;
    private static final int MODE_COMPLETE = 1;

    //    private static FragmentGoAcceptanceContent fragment;
//
//    public static FragmentGoAcceptanceContent newInstance(int mode) {//单例
//        if (fragment == null) {
//            fragment = new FragmentGoAcceptanceContent();
//        }
//        curMode = mode;
//        return fragment;
//    }
//    public FragmentGoPublishContent(int curMode) {
//        super();
//        this.curMode = curMode;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.go_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecycleViewAdapter(TestData.goListPublishs));//测试数据
        return view;
    }

    /**
     * RecycleViewAdapter
     */
    class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHold> {
        private List<GoListPublish> listPublishs;

        public RecycleViewAdapter(List<GoListPublish> listPublishs) {
            this.listPublishs = listPublishs;
        }

        @Override
        public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.go_publish_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            GoListPublish goListPublish = listPublishs.get(position);
            holder.publishTime.setText(goListPublish.getTime());
            holder.state.setText(goListPublish.getState());
            holder.task.setText(goListPublish.getTask());
            holder.commission.setText(goListPublish.getCommission());
            holder.destination.setText(goListPublish.getDestination());
            holder.acceptorPhoneNum = goListPublish.getAcceptorphone();
        }

        @Override
        public int getItemCount() {
            return listPublishs.size();
        }
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        public TextView publishTime, state, task, commission, destination, acceptor;
        public Button acceptorPhone;
        public String acceptorPhoneNum;

        public MyViewHold(View itemView) {
            super(itemView);
            publishTime = (TextView) itemView.findViewById(R.id.go_publish_time);//类别
            state = (TextView) itemView.findViewById(R.id.go_publish_state);//状态
            commission = (TextView) itemView.findViewById(R.id.go_publish_commission);//佣金
            task = (TextView) itemView.findViewById(R.id.go_publish_task);//任务
            destination = (TextView) itemView.findViewById(R.id.go_publish_destination);//目的地
            acceptor = (TextView) itemView.findViewById(R.id.go_publish_acceptor);
            acceptorPhone = (Button) itemView.findViewById(R.id.go_publish_bn_acceptor_phone);
            acceptorPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(), "打接单人电话:" + acceptorPhoneNum, Toast.LENGTH_SHORT).show();
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startActivity(getActivity(), GoAcceptsDetailActivity.class);
                }
            });
        }
    }
}
