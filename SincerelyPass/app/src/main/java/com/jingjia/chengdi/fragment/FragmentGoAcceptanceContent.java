package com.jingjia.chengdi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingjia.chengdi.GoAcceptsDetailActivity;
import com.jingjia.chengdi.R;
import com.jingjia.chengdi.data.TestData;
import com.jingjia.chengdi.encapsulation.GoListAcceptance;
import com.jingjia.chengdi.utils.ActivityUtils;

import java.util.List;

/**
 * Created by Administrator on 2016/9/19.
 */
public class FragmentGoAcceptanceContent extends Fragment {
    private static int curMode;
//    private static FragmentGoAcceptanceContent fragment;
//
//    public static FragmentGoAcceptanceContent newInstance(int mode) {//单例
//        if (fragment == null) {
//            fragment = new FragmentGoAcceptanceContent();
//        }
//        curMode = mode;
//        return fragment;
//    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("list.................list");
        View view = inflater.inflate(R.layout.recycleview_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.go_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecycleViewAdapter(TestData.goListAcceptances));
        return view;
    }

    /**
     * RecycleViewAdapter
     */
    class RecycleViewAdapter extends RecyclerView.Adapter<MyViewHold> {
        private List<GoListAcceptance> listAcceptances;

        public RecycleViewAdapter(List<GoListAcceptance> listAcceptances) {
            this.listAcceptances = listAcceptances;
        }

        @Override
        public MyViewHold onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.go_acceptance_list_content, parent, false);
            return new MyViewHold(view);
        }

        @Override
        public void onBindViewHolder(MyViewHold holder, int position) {
            GoListAcceptance goListAcceptance = listAcceptances.get(position);
            holder.category.setText(goListAcceptance.getCategory());
            holder.status.setText(goListAcceptance.getStatus());
            holder.task.setText(goListAcceptance.getTask());
            holder.commission.setText(goListAcceptance.getCommission());
            holder.destination.setText(goListAcceptance.getDestination());
        }

        @Override
        public int getItemCount() {
            return listAcceptances.size();
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
    }
}
