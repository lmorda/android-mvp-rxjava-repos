package com.example.lmorda.rxrepos.data.source.remote;

import com.example.lmorda.rxrepos.data.GithubRepos;

import io.reactivex.Flowable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GithubApiService {

    @GET
    Flowable<GithubRepos> getTrendingRepos(@Url String url);

}
