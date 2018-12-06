package com.example.lmorda.rxrepos.repos;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
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
            case R.id.menu_search:
                showSearchBar();
                break;
            case R.id.menu_sort:
                sortRepos();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_repos_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

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
            Snackbar.make(getView(), "Could not get repositories!", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void showNoRepos() {
        mReposView.setVisibility(View.GONE);
        mNoReposView.setVisibility(View.VISIBLE);
    }

}
