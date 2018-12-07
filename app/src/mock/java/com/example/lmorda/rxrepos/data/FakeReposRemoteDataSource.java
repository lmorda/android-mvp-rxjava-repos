package com.example.lmorda.rxrepos.data;

import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.data.source.ReposDataSource;
import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public class FakeReposRemoteDataSource implements ReposDataSource {

    private static FakeReposRemoteDataSource INSTANCE;

    private static final GithubRepos REPOS_SERVICE_DATA;

    static {
        REPOS_SERVICE_DATA = new GithubRepos();
        REPOS_SERVICE_DATA.items = new ArrayList<>();
        Repo repo1 = new Repo();
        repo1.id = 1;
        repo1.description = "RxJava2 + Retrofit + MVP example";
        repo1.name = "android-mvp-rxjava-repos";
        repo1.url = "http://www.github.com/lmorda/android-mvp-rxjava-repos";
        repo1.language = "Java";
        REPOS_SERVICE_DATA.items.add(repo1);
        Repo repo2 = new Repo();
        repo2.id = 2;
        repo2.description = "Retrofit + MVP example";
        repo2.name = "android-mvp-retrofit-repos";
        repo2.url = "http://www.github.com/lmorda/android-mvp-retrofit-repos";
        repo2.language = "Java";
        Repo repo3 = new Repo();
        repo3.id = 3;
        repo3.description = "Kotlin + Retrofit + MVI example";
        repo3.name = "kotlin-mvi-rxjava-retrofit";
        repo3.url = "http://www.github.com/lmorda/kotlin-mvi-rxjava-retrofit";
        repo3.language = "Kotlin";
        REPOS_SERVICE_DATA.items.add(repo3);
    }

    private FakeReposRemoteDataSource() {
    }

    public static FakeReposRemoteDataSource getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeReposRemoteDataSource();
        }
        return INSTANCE;
    }

    @Override
    public Flowable<List<Repo>> getRepos() {
        return Flowable.just(REPOS_SERVICE_DATA.items);
    }

    @Override
    public Flowable<Optional<Repo>> getRepo(@NonNull Integer repoId) {
        return null;
    }

    @Override
    public void saveRepo(@NonNull Repo repo) {

    }

    @Override public void refreshRepos() {

    }
}
