package com.example.lmorda.rxrepos;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.data.FakeReposRemoteDataSource;
import com.example.lmorda.rxrepos.data.source.remote.GithubApiService;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.example.lmorda.rxrepos.util.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static GithubApiService provideReposRemoteDataSource(@NonNull Context context) {
        checkNotNull(context);
        return FakeReposRemoteDataSource.getInstance();
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
