package com.xudongjian.lightreader.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xudongjian.lightreader.R;
import com.xudongjian.lightreader.bean.Book;

import java.util.List;

/**
 * Created by xudongjian on 17/4/18.
 */

public class CollectAdapter extends RecyclerView.Adapter<CollectAdapter.Holder> {

    private List<Book> mList;

    public CollectAdapter(List<Book> list) {
        mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collect, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int position) {
        holder.tv_name.setText(mList.get(position).getName());
        holder.vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class Holder extends RecyclerView.ViewHolder {

        private ViewGroup vg;
        private TextView tv_name;

        Holder(View itemView) {
            super(itemView);
            vg = (ViewGroup) itemView.findViewById(R.id.ll_collect);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }

    public void refresh(List<Book> list) {
        mList.clear();
        mList = list;
        if (mList != null && mList.size() > 0) {
            notifyDataSetChanged();
        }
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
