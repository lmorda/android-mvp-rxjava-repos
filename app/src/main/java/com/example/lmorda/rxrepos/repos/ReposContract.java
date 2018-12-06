package com.example.lmorda.rxrepos.repos;

import com.example.lmorda.rxrepos.BasePresenter;
import com.example.lmorda.rxrepos.BaseView;
import com.example.lmorda.rxrepos.data.Repo;

import java.util.List;

public interface ReposContract {

    interface View extends BaseView<Presenter> {

        void setLoadingIndicator(boolean active);

        void showRepos(List<Repo> repos);

        void showLoadingReposError(String error);

        void showNoRepos();

    }


    interface Presenter extends BasePresenter {

        void loadRepos(boolean forceUpdate);

        void setFiltering(ReposFilterType requestType);

        void setSearchString(String searchString);

        ReposFilterType getFiltering();

    }
}
