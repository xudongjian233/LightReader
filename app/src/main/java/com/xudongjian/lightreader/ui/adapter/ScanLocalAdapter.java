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
 * Created by xudongjian on 17/4/10.
 * 本地文件RecyclerView适配器
 */

public class ScanLocalAdapter extends RecyclerView.Adapter<ScanLocalAdapter.Holder> {

    private List<Book> mList;

    public ScanLocalAdapter(List<Book> list) {
        mList = list;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scan_local, parent, false);

        return new Holder(view);
    }

    class Holder extends RecyclerView.ViewHolder {

        private ViewGroup vg;
        private TextView tv_bookName;
        private TextView tv_bookPath;
        private TextView tv_bookSize;
        private TextView tv_hasCollect;

        Holder(View itemView) {
            super(itemView);
            vg = (ViewGroup) itemView.findViewById(R.id.ll_parent);
            tv_bookName = (TextView) itemView.findViewById(R.id.tv_bookName);
            tv_bookPath = (TextView) itemView.findViewById(R.id.tv_bookPath);
            tv_bookSize = (TextView) itemView.findViewById(R.id.tv_bookSize);
            tv_hasCollect = (TextView) itemView.findViewById(R.id.tv_hasCollect);
        }
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.tv_bookName.setText("书名:" + mList.get(position).getName());
        holder.tv_bookSize.setText("大小:" + mList.get(position).getSizeString());
        holder.tv_bookPath.setText("位置:" + mList.get(position).getPath());


        if (mList.get(position).isCollect()) {
            holder.tv_hasCollect.setVisibility(View.VISIBLE);
        }else {
            holder.tv_hasCollect.setVisibility(View.INVISIBLE);
        }

        holder.vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    if (mOnItemClickListener.onClick(position)) {
                        holder.tv_hasCollect.setVisibility(View.VISIBLE);
                    } else {
                        holder.tv_hasCollect.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        boolean onClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


}
