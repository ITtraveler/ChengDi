package com.jingjia.chengdi.fragment;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.jingjia.chengdi.GoAcceptsDetailActivity;
import com.jingjia.chengdi.GoPublishDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.phoenix.PullToRefreshView;
import com.jingjia.chengdi.data.Category;
import com.jingjia.chengdi.data.TestData;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.data.listener.AcceptedDemandChangeListener;
import com.jingjia.chengdi.encapsulation.GoListAcceptance;
import com.jingjia.chengdi.utils.ActivityUtils;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetStatus;
import com.jingjia.chengdi.utils.UpdateData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class FragmentGoAcceptanceContent extends Fragment implements AcceptedDemandChangeListener, View.OnTouchListener {
    private int curMode;
    private PullToRefreshView mPullToRefreshView;
    private RecycleViewAdapter mRecycleAdapter;
    private UpdateData updateData;
    private LinearLayoutManager llManager;
    private AnimatorSet animCategoryBarShow, animCategoryBarhide;
    private RadioGroup rbGroupCategory;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        curMode = getArguments().getInt("MODE");
        MyApplication.newInstance().addAcceptedChangeListener(this);
        updateData = UpdateData.newUpdateDate();
        if (curMode == 0) {
            UpdateData.initMyAccepting();
            updateData.updateMyAcceptingData();
        } else if (curMode == 1) {
            UpdateData.initMyAccepted();
            updateData.updateMyAcceptedData();//更新所有我的发布的数据
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycleview_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.go_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //根据Mode改变不同的数据
        if (curMode == 0) {
            mRecycleAdapter = new RecycleViewAdapter(MyApplication.myAcceptedList);
        } else
            mRecycleAdapter = new RecycleViewAdapter(MyApplication.myAcceptedListComp);
        recyclerView.setAdapter(mRecycleAdapter);
        llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.setOnTouchListener(this);

        initCategoryBar();
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//            }
//        });

        initPullToRefresh(view);
        // handler.sendEmptyMessageDelayed(0x1, 500);

        return view;
    }

    /**
     * categoryBar 的动画
     */
    private boolean categoryBarShow = true;
    private void initCategoryBar() {
        rbGroupCategory = (RadioGroup) getActivity().findViewById(R.id.go_rb);
        initAnimator();
    }

    private void initAnimator() {
        animCategoryBarShow = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.ic_right_out);
        animCategoryBarhide = (AnimatorSet) AnimatorInflater.loadAnimator(getActivity(), R.animator.ic_right_in);
        animCategoryBarShow.setTarget(rbGroupCategory);
        animCategoryBarhide.setTarget(rbGroupCategory);
    }



    /*************************************/
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                mRecycleAdapter.notifyDataSetChanged();
            }
        }
    };

    private void initPullToRefresh(View view) {
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.go_RecycleView_fresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curMode == 0) {
                            UpdateData.initMyAccepting();
                            updateData.updateMyAcceptingData();
                        } else if (curMode == 1) {
                            UpdateData.initMyAccepted();
                            updateData.updateMyAcceptedData();//更新所有我的发布的数据
                        }
                        //handler.sendEmptyMessageDelayed(0x1, 1000);
                        mPullToRefreshView.setRefreshing(false);

                    }
                }, 500);
            }
        });
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

       // System.out.println(llManager.findFirstVisibleItemPosition() + "  " + llManager.findLastVisibleItemPosition());
        int position = llManager.findLastVisibleItemPosition();
        if (position == (llManager.getItemCount() - 1)) {
            if (curMode == 0 && UpdateData.haveMyAccepted && !MyApplication.netStatus.equals(NetStatus.NET_DISCONNECTED)) {
                updateData.updateMyAcceptedData(1);
                updateData.updateMyAcceptedData(2);
                updateData.updateMyAcceptedData(4);
                updateData.updateMyAcceptedData(6);
                UpdateData.haveMyAccepted = false;
                System.out.println("aaaaaaaaaaaaaaaa");
            } else if (curMode == 1 && UpdateData.haveMyAcceptedComp && !MyApplication.netStatus.equals(NetStatus.NET_DISCONNECTED)) {
                updateData.updateMyAcceptedData(3);
                updateData.updateMyAcceptedData(5);
                UpdateData.haveMyAcceptedComp = false;
            }
        }

        return false;
    }

    @Override
    public void acceptedDemandChange() {
        mRecycleAdapter.notifyDataSetChanged();
        UpdateData.haveMyAccepted = true;
    }

    @Override
    public void acceptingDemandChange() {
        mRecycleAdapter.notifyDataSetChanged();
        UpdateData.haveMyAcceptedComp = true;
    }

    @Override
    public void removeAll() {
        mRecycleAdapter.notifyDataSetChanged();
    }

    @Override
    public void notificationFlash() {//重新从服务器获取数据
        if (curMode == 0) {
            UpdateData.initMyAccepting();
            updateData.updateMyAcceptingData();
        } else if (curMode == 1) {
            UpdateData.initMyAccepted();
            updateData.updateMyAcceptedData();//更新所有我的发布的数据
        }
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.go_acceptance_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            DemandInfo demandInfo = demandInfoList.get(position);
            holder.setOnItemClick(demandInfo);
            holder.category.setText(Category.values()[Integer.parseInt(demandInfo.getCategory())].name());
            holder.status.setText(demandInfo.getStatus());
            holder.task.setText(demandInfo.getContent());
            holder.commission.setText("" + demandInfo.getMoney());
            holder.destination.setText(demandInfo.getDestination());
        }

        @Override
        public int getItemCount() {
            return demandInfoList.size();
        }
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        public TextView category, status, task, commission, destination;

        public MyViewHold(View itemView) {
            super(itemView);
            category = (TextView) itemView.findViewById(R.id.go_list_category);//类别
            status = (TextView) itemView.findViewById(R.id.go_list_status);//状态
            commission = (TextView) itemView.findViewById(R.id.go_list_commission);//佣金
            task = (TextView) itemView.findViewById(R.id.go_list_task);//任务
            destination = (TextView) itemView.findViewById(R.id.go_list_destination);//目的地
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startActivity(getActivity(), GoAcceptsDetailActivity.class);
                }
            });
        }

        public void setOnItemClick(final DemandInfo demandInfo) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), GoAcceptsDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("demandInfo", demandInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        System.out.println("start fgac");
    }
}
