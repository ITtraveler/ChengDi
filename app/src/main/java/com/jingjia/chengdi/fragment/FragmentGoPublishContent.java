package com.jingjia.chengdi.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingjia.chengdi.GoPublishDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.phoenix.PullToRefreshView;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.data.listener.PublishDemandListener;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetStatus;
import com.jingjia.chengdi.utils.UpdateData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 * 此fragment分成两种模式一种未完成（1）和 已完成 （2）
 * (0待接单 1未完成，2接单人申请完成，3确认完成，4申请撤销订单，5是已撤销订单，6，申请放弃接单)
 */
public class FragmentGoPublishContent extends Fragment implements PublishDemandListener, View.OnTouchListener {
    private int curMode = 0;

    private RecycleViewAdapter mRecycleAdapter;
    private MyApplication myApplication;
    private RecyclerView recyclerView;
    private PullToRefreshView mPullToRefreshView;
    private LinearLayoutManager llManager;
    private UpdateData updateData;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curMode = getArguments().getInt("MODE");
        updateData = UpdateData.newUpdateDate();
        myApplication = MyApplication.newInstance();
        //实现监听器
        myApplication.addDemandListener(this);
        if (curMode == 0) {
            UpdateData.initMyPublishing();
            updateData.updateMyPublishingData();
        } else {
            UpdateData.initMyPublished();
            updateData.updateMyPublishData();
        }
        System.out.println("gp create Fragment ");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview_layout, container, false);
        System.out.println("gp create View");
        recyclerView = (RecyclerView) view.findViewById(R.id.go_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //根据Mode改变不同的数据
        if (curMode == 0) {
            mRecycleAdapter = new RecycleViewAdapter(MyApplication.myDemandList);
        } else
            mRecycleAdapter = new RecycleViewAdapter(MyApplication.myDemandListComp);

        recyclerView.setAdapter(mRecycleAdapter);//测试数据
        llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setOnTouchListener(this);
        initPullToRefresh(view);
        return view;
    }

//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0x1) {
//                mRecycleAdapter.notifyDataSetChanged();
//                System.out.println("refresh   " + MyApplication.myDemandList.size());
//            }
//        }
//    };

    /**
     * 下拉刷新跟新数据，重新从服务器获取数据
     *
     * @param view
     */
    private void initPullToRefresh(View view) {
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.go_RecycleView_fresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curMode == 0) {
                            UpdateData.initMyPublishing();
                            updateData.updateMyPublishingData();
                        } else {
                            UpdateData.initMyPublished();
                            updateData.updateMyPublishData();
                        }//更新数据

                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 500);
            }
        });
    }

    /**
     * 在发布应用时会调用
     *
     * @param demandInfo
     */
    @Override
    public void haveMyDemand(DemandInfo demandInfo) {
        if (curMode == 0) {
            mRecycleAdapter.notifyItemInserted(0);
            recyclerView.scrollToPosition(0);
        }
    }

    @Override
    public void haveMyDemands(List<DemandInfo> demandInfoList) {
        // System.out.println("haveDemandsssss:" + MyApplication.myDemandList.size());
        mRecycleAdapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
        UpdateData.haveMyDemand = true;//来标注可能还有数据，即可在次尝试加载数据
    }


    @Override
    public void haveMyDemandComps(List<DemandInfo> demandInfoList) {
        mRecycleAdapter.notifyDataSetChanged();
        UpdateData.haveMyDemandComp = true;
        recyclerView.refreshDrawableState();
        System.out.println("size::"+MyApplication.myDemandListComp.size());
    }

    /**
     * 在登录，或者注册登录时会调用
     */
    @Override
    public void removeAll() {
        mRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void notificationFlash() {
        if (curMode == 0) {
            UpdateData.initMyPublishing();
            updateData.updateMyPublishingData();
        } else {
            UpdateData.initMyPublished();
            updateData.updateMyPublishData();
        }//更新数据
        System.out.println("flash");
    }

    /**
     * recycleView的触摸事件，用于在item为最后一个时，加载新数据，相当与上啦加载
     *
     * @param v
     * @param event
     * @return
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {

        //  LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();

      //  System.out.println(llManager.findFirstVisibleItemPosition() + "  " + llManager.findLastVisibleItemPosition());
        int position = llManager.findLastVisibleItemPosition();
        if (position == (llManager.getItemCount() - 1)) {
            if (curMode == 0 && UpdateData.haveMyDemand && !MyApplication.netStatus.equals(NetStatus.NET_DISCONNECTED)) {
                updateData.updateMyPublishData(0);
//                updateData.updateMyPublishData(1);
//                updateData.updateMyPublishData(2);
//                updateData.updateMyPublishData(4);
//                updateData.updateMyPublishData(6);
                UpdateData.haveMyDemand = false;

            } else if (curMode == 1 && UpdateData.haveMyDemandComp && !MyApplication.netStatus.equals(NetStatus.NET_DISCONNECTED)) {
                updateData.updateMyPublishData(3);
                updateData.updateMyPublishData(5);
                UpdateData.haveMyDemandComp = false;
                System.out.println(",,,,,,,"+MyApplication.myDemandListComp.size());
            }
        }
        return false;
    }


    /**
     * RecycleViewAdapter
     */
    class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHold> {
        private List<DemandInfo> demandInfoList;

        public RecycleViewAdapter(List<DemandInfo> demandInfoList) {
            this.demandInfoList = demandInfoList;
        }

        @Override
        public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.go_publish_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            DemandInfo demandInfo = demandInfoList.get(position);
            holder.setOnItemClick(demandInfo, position);
            holder.publishTime.setText(demandInfo.getPublishTime());
            holder.state.setText(demandInfo.getStatus());
            holder.task.setText(demandInfo.getContent());
            holder.commission.setText("" + demandInfo.getMoney());
            holder.destination.setText(demandInfo.getDestination());
//            if (demandInfo.getUser() != null && demandInfo.getAcceptancePhone() != null && !demandInfo.getAcceptancePhone().isEmpty()) {
////                holder.acceptor.setText(demandInfo.getUser().getUsername());
////                holder.acceptorPhoneNum = demandInfo.getAcceptancePhone();
////                holder.acceptedBar.setVisibility(View.VISIBLE);
//            }

        }

        @Override
        public int getItemCount() {
            return demandInfoList.size();
        }
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        //  public View acceptedBar;
        public TextView publishTime, state, task, commission, destination, acceptor;
        // public Button acceptorPhone;
        // public String acceptorPhoneNum;
        private View itemView;

        public MyViewHold(View itemView) {
            super(itemView);
            this.itemView = itemView;
            publishTime = (TextView) itemView.findViewById(R.id.go_publish_time);//类别
            state = (TextView) itemView.findViewById(R.id.go_publish_state);//状态
            commission = (TextView) itemView.findViewById(R.id.go_publish_commission);//佣金
            task = (TextView) itemView.findViewById(R.id.go_publish_task);//任务
            destination = (TextView) itemView.findViewById(R.id.go_publish_destination);//目的地
            //acceptor = (TextView) itemView.findViewById(R.id.go_publish_acceptor);
            // acceptorPhone = (Button) itemView.findViewById(R.id.go_publish_bn_acceptor_phone);
            //acceptedBar = itemView.findViewById(R.id.go_publish_accepted_bar);
//            acceptorPhone.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(getActivity(), "打接单人电话:" + acceptorPhoneNum, Toast.LENGTH_SHORT).show();
//                }
//            });

        }

        public void setOnItemClick(final DemandInfo demandInfo, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoPublishDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("demandInfo", demandInfo);
                    bundle.putInt("position", position);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start fgpc");
    }
}
