package com.example.lmorda.rxrepos.repos;

import com.example.lmorda.rxrepos.BasePresenter;
import com.example.lmorda.rxrepos.BaseView;
import com.example.lmorda.rxrepos.data.Repo;

import java.util.List;

public interface ReposContract {

    interface View extends BaseView<Presenter> {
        void showRepos(List<Repo> repos);
    }


    interface Presenter extends BasePresenter {
        void loadRepos();

    }
}
