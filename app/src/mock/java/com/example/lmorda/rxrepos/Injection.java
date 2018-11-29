package com.example.lmorda.rxrepos;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.data.source.ReposRepository;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.example.lmorda.rxrepos.util.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static ReposRepository provideReposRepository(@NonNull Context context) {
        checkNotNull(context);
        return ReposRepository.getInstance(FakeReposRemoteDataSource.getInstance());
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
