package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jingjia.chengdi.HomeListDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.custom.phoenix.PullToRefreshView;
import com.jingjia.chengdi.data.TestData;
import com.jingjia.chengdi.encapsulation.HomeListContent;
import com.jingjia.chengdi.utils.ActivityUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */
public class FragmentHomePageOne extends Fragment{
    private static FragmentHomePageOne fragmentHomePageOne;
    public long REFRESH_DELAY = 300;
    private static List<HomeListContent> homeLists;
    private RecyclerView homeRecycleView;
    private RecycleViewAdapter myRVAdapter;
    private PullToRefreshView mPullToRefreshView;
    public static FragmentHomePageOne newInstance(List<HomeListContent> homeLists){
        if(fragmentHomePageOne == null)
            fragmentHomePageOne = new FragmentHomePageOne();
        //FragmentHomePageOne.homeLists = homeLists;
        return fragmentHomePageOne;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        homeLists = TestData.homeListContents;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_one,container,false);
        initPullToRefresh(view);
        initRecycleView(view);

        return view;
    }

    /**
     * 下拉刷新
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
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void initRecycleView(View view) {
        homeRecycleView = (RecyclerView)view.findViewById(R.id.home_recycleView);
        homeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRVAdapter = new RecycleViewAdapter(homeLists);
        homeRecycleView.setAdapter(myRVAdapter);

    }



    /**
     * RecycleViewAdapter
     */
    class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHold> {
        private List<HomeListContent> homeListContents;
        public RecycleViewAdapter(List<HomeListContent> homeListContents) {
            this.homeListContents = homeListContents;
        }

        @Override
        public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.home_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            HomeListContent homelist = homeListContents.get(position);
            holder.username.setText(homelist.getUsername());
            holder.category.setText(homelist.getCategory());
            holder.time.setText(homelist.getTime());
            holder.task.setText(homelist.getTask());
            holder.commission.setText(homelist.getCommission());
            holder.destination.setText(homelist.getDestination());

        }

        @Override
        public int getItemCount() {
            return homeLists.size();
        }
    }

    class MyViewHold extends RecyclerView.ViewHolder {
        public TextView username,category, time, task, commission, destination;
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
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityUtils.startActivity(getActivity(), HomeListDetailActivity.class);
                }
            });
        }
    }
}
