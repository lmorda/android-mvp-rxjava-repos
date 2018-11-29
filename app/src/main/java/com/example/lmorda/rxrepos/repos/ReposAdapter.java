package com.example.lmorda.rxrepos.repos;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.data.Repo;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;

public class ReposAdapter extends BaseAdapter {

    private List<Repo> mRepos;

    public ReposAdapter(List<Repo> repos) {
        setList(repos);
    }

    public void replaceData(List<Repo> repos) {
        setList(repos);
        notifyDataSetChanged();
    }

    private void setList(List<Repo> repos) {
        mRepos = checkNotNull(repos);
    }

    @Override
    public int getCount() {
        return mRepos.size();
    }

    @Override
    public Repo getItem(int i) {
        return mRepos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            rowView = inflater.inflate(R.layout.repo_item, viewGroup, false);
        }

        final Repo repo = getItem(i);

        TextView titleTV = rowView.findViewById(R.id.title);
        return rowView;
    }
}
