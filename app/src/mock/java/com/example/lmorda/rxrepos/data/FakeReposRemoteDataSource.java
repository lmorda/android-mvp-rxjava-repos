package com.example.lmorda.rxrepos.data;

import com.example.lmorda.rxrepos.data.source.GithubApiService;

import java.util.ArrayList;
import java.util.Collection;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import retrofit2.Response;

public class FakeReposRemoteDataSource implements GithubApiService {

    private static FakeReposRemoteDataSource INSTANCE;

    private static final GithubRepos REPOS_SERVICE_DATA;

    static {
        REPOS_SERVICE_DATA = new GithubRepos();
        REPOS_SERVICE_DATA.items = new ArrayList<>();
        Repo repo1 = new Repo();
        repo1.description = "RxJava2 + Retrofit + MVP example";
        repo1.name = "android-mvp-rxjava-repos";
        repo1.url = "http://www.github.com/lmorda/android-mvp-rxjava-repos";
        repo1.language = "java";
        REPOS_SERVICE_DATA.items.add(repo1);
        Repo repo2 = new Repo();
        repo2.description = "Retrofit + MVP example";
        repo2.name = "android-mvp-retrofit-repos";
        repo2.url = "http://www.github.com/lmorda/android-mvp-retrofit-repos";
        repo2.language = "java";
        REPOS_SERVICE_DATA.items.add(repo2);
    }

    private FakeReposRemoteDataSource() {
    }

    public static FakeReposRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeReposRemoteDataSource();
        }
        return INSTANCE;
    }

    public Observable<GithubRepos> getRepos(String url) {
        return Observable.just(REPOS_SERVICE_DATA);
    }

}
