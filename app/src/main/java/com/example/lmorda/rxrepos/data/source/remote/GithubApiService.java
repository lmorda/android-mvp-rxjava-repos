package com.example.lmorda.rxrepos.data.source.remote;

import com.example.lmorda.rxrepos.data.GithubRepos;
import com.example.lmorda.rxrepos.data.Repo;
import com.google.common.base.Optional;


import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GithubApiService {

    @GET
    Flowable<GithubRepos> getRepos(@Url String url);

    @GET
    Flowable<Optional<Repo>> getRepo(@Url String url);
}
