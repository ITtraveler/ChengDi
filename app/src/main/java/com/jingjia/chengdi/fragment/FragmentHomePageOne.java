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
import android.widget.ImageView;
import android.widget.TextView;

import com.jingjia.chengdi.HomeListDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.phoenix.PullToRefreshView;
import com.jingjia.chengdi.data.Category;
import com.jingjia.chengdi.data.encapsulation.DemandInfo;
import com.jingjia.chengdi.data.listener.AroundDemandListener;
import com.jingjia.chengdi.utils.MyApplication;
import com.jingjia.chengdi.utils.NetStatus;
import com.jingjia.chengdi.utils.UpdateData;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class FragmentHomePageOne extends Fragment implements View.OnTouchListener, AroundDemandListener {
    private static FragmentHomePageOne fragmentHomePageOne;
    public long REFRESH_DELAY = 300;
    private RecyclerView homeRecycleView;
    private RecycleViewAdapter myRVAdapter;
    private PullToRefreshView mPullToRefreshView;
    private LinearLayoutManager llManager;

    public static FragmentHomePageOne newInstance() {
        if (fragmentHomePageOne == null)
            fragmentHomePageOne = new FragmentHomePageOne();
        return fragmentHomePageOne;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.newInstance().addAroundDemandListener(this);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_one, container, false);
        initPullToRefresh(view);
        initRecycleView(view);

        return view;
    }

    /**
     * 下拉刷新
     *
     * @param view
     */
    private void initPullToRefresh(View view) {
        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.home_pull_to_refresh);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {

            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        UpdateData.initAroundDemands();
                        myRVAdapter.notifyDataSetChanged();
                        //homeRecycleView.refreshDrawableState();
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void initRecycleView(View view) {
        homeRecycleView = (RecyclerView) view.findViewById(R.id.home_recycleView);
        homeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //System.out.println(MyApplication.aroundDemandList.size());
        myRVAdapter = new RecycleViewAdapter(MyApplication.aroundDemandList);
        homeRecycleView.setAdapter(myRVAdapter);
        homeRecycleView.setOnTouchListener(this);
        llManager = (LinearLayoutManager) homeRecycleView.getLayoutManager();
        //myRVAdapter.notifyDataSetChanged();
        mPullToRefreshView.setRefreshing(true);
        handler.sendEmptyMessageDelayed(0x1, 100);//处理数据获取延迟，没能及时刷新问题
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x1) {
                System.out.println("handler AROUND SIZE:" + MyApplication.aroundDemandList.size());
                if (MyApplication.aroundDemandList.size() > 0) {
                    mPullToRefreshView.setRefreshing(false);
                    myRVAdapter.notifyDataSetChanged();
                } else{
                    UpdateData.initAroundDemands();
                    handler.sendEmptyMessageDelayed(0x1, 500);
                }
            }
        }
    };

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

        System.out.println(llManager.findFirstVisibleItemPosition() + "  " + llManager.findLastVisibleItemPosition());
        int position = llManager.findLastVisibleItemPosition();
        if (position == (llManager.getItemCount() - 1)) {
            if (UpdateData.haveAroundDemands && !MyApplication.netStatus.equals(NetStatus.NET_DISCONNECTED)) {
                UpdateData.newUpdateDate().updateAroundDemandData();
                UpdateData.haveAroundDemands = false;
                System.out.println("hhhhhhhhhhhhhhhhhh" + UpdateData.aroundDemandsPage);
            }
        }
        System.out.println("AROUND SIZE:" + MyApplication.aroundDemandList.size());
//        if(MyApplication.aroundDemandList.size() > 0){
//            aroundDemandList.addAll(MyApplication.aroundDemandList);
//        }
//        myRVAdapter.notifyDataSetChanged();
        return false;
    }

    @Override
    public void haveAroundDemand(List<DemandInfo> demandInfoList) {
        System.out.println("haveAroundDemand....");
        UpdateData.haveAroundDemands = true;
        myRVAdapter.notifyDataSetChanged();
    }

    @Override
    public void haveAroundDemandRemoved(int position) {
        myRVAdapter.notifyItemChanged(position);
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
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            DemandInfo demandInfo = demandInfoList.get(position);
            holder.setOnItemClick(demandInfo,position);//传入DemandInfo数据
            if (demandInfo.getUser() != null)
                holder.username.setText(demandInfo.getUser().getUsername());
            holder.category.setText("" + Category.values()[Integer.valueOf(demandInfo.getCategory())]);
            holder.time.setText(demandInfo.getPublishTime());
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
        public TextView username, category, time, task, commission, destination;
        public ImageView head;

        public MyViewHold(View itemView) {
            super(itemView);
            username = (TextView) itemView.findViewById(R.id.home_list_username);//用户名
            category = (TextView) itemView.findViewById(R.id.home_list_category);//类别
            time = (TextView) itemView.findViewById(R.id.home_list_time);//状态
            commission = (TextView) itemView.findViewById(R.id.home_list_commission);//佣金
            task = (TextView) itemView.findViewById(R.id.home_list_task);//任务
            destination = (TextView) itemView.findViewById(R.id.home_list_destination);//目的地
            head = (ImageView) itemView.findViewById(R.id.home_list_head);//头像
        }

        public void setOnItemClick(final DemandInfo demandInfo, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), HomeListDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("position",position);
                    bundle.putSerializable("demandInfo", demandInfo);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }
}
