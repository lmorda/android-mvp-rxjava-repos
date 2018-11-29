package com.example.lmorda.rxrepos.data.source;

import com.example.lmorda.rxrepos.data.Repo;

import java.util.List;

import io.reactivex.Flowable;

public interface ReposDataSource {

    Flowable<List<Repo>> getRepos();

}
