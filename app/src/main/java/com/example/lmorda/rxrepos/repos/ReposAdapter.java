/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.lmorda.rxrepos.repos;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

        TextView titleTV = rowView.findViewById(R.id.repo_name);
        titleTV.setText(repo.name);

        TextView descriptionTV = rowView.findViewById(R.id.repo_description);
        descriptionTV.setText(repo.description);

        rowView.setOnClickListener(view1 -> openChrome(repo.html_url, viewGroup.getContext()));

        return rowView;
    }

    private void openChrome(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.android.chrome");
        try {
            context.startActivity(intent);
        } catch (ActivityNotFoundException exception) {
            intent.setPackage(null);
            context.startActivity(intent);
        }
    }
}
