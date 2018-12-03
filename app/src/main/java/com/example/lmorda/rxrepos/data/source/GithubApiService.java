package com.example.lmorda.rxrepos.data.source;

import com.example.lmorda.rxrepos.data.GithubRepos;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface GithubApiService {

    @GET
    Observable<GithubRepos> getRepos(@Url String url);

}
