package com.mygdx.genexotrudnypacjent.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.mygdx.genexotrudnypacjent.R;
import com.mygdx.genexotrudnypacjent.model.Row;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Radek on 14.07.2016.
 */
public abstract class StanowiskoRecyclerViewAdaoter  extends RecyclerView.Adapter<StanowiskoRecyclerViewAdaoter.StoreViewHolder> implements View.OnClickListener, SectionIndexer {

    private List<String> mStores = new ArrayList<>();
    private List<String> mStoresCache = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    private int mLastPosition = -1;

    public StanowiskoRecyclerViewAdaoter() {
    }

    public StanowiskoRecyclerViewAdaoter(List<String> stores) {
        mStores.clear();
        mStoresCache.clear();
        Collections.sort(stores, getRowComparator());
        mStores.addAll(stores);
        mStoresCache.addAll(mStores);
    }

    public Comparator<String> getRowComparator() {
        return new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareToIgnoreCase(rhs);
            }
        };
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    public boolean isTwoLinesAdapter() {
        return false;
    }
    @Override
    public StoreViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(isTwoLinesAdapter()) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_two_lines, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        }
        v.setOnClickListener(this);
        return new StoreViewHolder(v);
    }

    public abstract String getTextFromRow(String row);

    @Override
    public void onBindViewHolder(StoreViewHolder holder, int position) {
        String store = mStores.get(position);
        holder.mStoreName.setText(store);



        holder.itemView.setTag(store);
    }

    @Override
    public int getItemCount() {
        return mStores.size();
    }

    @Override
    public void onClick(final View v) {
        // Give some time to the ripple to finish the effect
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, ((String) v.getTag()));
        }
    }

    @Override
    public Object[] getSections() {
        return mStores.toArray();
    }

    @Override
    public int getPositionForSection(int i) {
        return 0;
    }

    @Override
    public int getSectionForPosition(int i) {
        if (i >= mStores.size() - 1) {
            return mStores.size() - 1;
        } else {
            return i;
        }
    }

    public void setStores(List<String> stores) {
        mStores.clear();
        mStoresCache.clear();
        Collections.sort(stores, getRowComparator());
        mStores.addAll(stores);
        mStoresCache.addAll(mStores);
        notifyDataSetChanged();
    }

    public void sortStores(Comparator<String> comparator) {
        Collections.sort(mStores, comparator);
        notifyDataSetChanged();
    }

    public void filter(String query) {
        mStores.clear();
        for (String store : mStoresCache) {
            if (query.length() < 2 || getTextFromRow(store).toLowerCase().contains(query.toLowerCase())) {
                mStores.add(store);
            }
        }
        notifyDataSetChanged();
    }



    protected static class StoreViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        public TextView mStoreName;

        @Nullable
        @Bind(R.id.secondLine)
        public TextView mSecondLine;
        public StoreViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, String row);
    }
}

