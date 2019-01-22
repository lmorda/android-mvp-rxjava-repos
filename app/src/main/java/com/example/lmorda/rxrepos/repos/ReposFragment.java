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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.data.Repo;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class ReposFragment extends Fragment implements ReposContract.View {

    private ReposContract.Presenter mPresenter;

    private ReposAdapter mListAdapter;

    private LinearLayout mReposView;

    private LinearLayout mNoReposView;

    public ReposFragment() { }

    public static ReposFragment newInstance() {
        return new ReposFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mListAdapter = new ReposAdapter(new ArrayList<>(0));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_repos, container, false);
        ListView listView = root.findViewById(R.id.repos_list);
        listView.setAdapter(mListAdapter);
        mReposView = root.findViewById(R.id.reposLL);
        mNoReposView = root.findViewById(R.id.norepos);
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark)
        );
        swipeRefreshLayout.setScrollUpChild(listView);
        swipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadRepos(false));
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.unsubscribe();
    }

    @Override
    public void setPresenter(@NonNull ReposContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_repos_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mPresenter.setSearchString(s);
                mPresenter.loadRepos(false);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_repos, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.allRepos:
                    mPresenter.setFiltering(ReposFilterType.ALL_REPOS);
                    break;
                case R.id.kotlinRepos:
                    mPresenter.setFiltering(ReposFilterType.KOTLIN_REPOS);
                    break;
                case R.id.javaRepos:
                    mPresenter.setFiltering(ReposFilterType.JAVA_REPOS);
                    break;
            }
            mPresenter.loadRepos(false);
            return true;
        });

        popup.show();
    }

    @Override
    public void setLoadingIndicator(final boolean active) {

        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout srl = getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done with everything else.
        srl.post(() -> srl.setRefreshing(active));
    }

    @Override
    public void showRepos(List<Repo> repos) {
        mReposView.setVisibility(View.VISIBLE);
        mNoReposView.setVisibility(View.GONE);
        mListAdapter.replaceData(repos);
    }

    @Override
    public void showLoadingReposError(String error) {
        if (getView() != null) {
            Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNoRepos() {
        mReposView.setVisibility(View.GONE);
        mNoReposView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

}
