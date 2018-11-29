package com.example.lmorda.rxrepos.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lmorda.rxrepos.data.Repo;

import java.util.List;

import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReposRepository implements ReposDataSource {
    @Nullable
    private static ReposRepository INSTANCE = null;

    @NonNull
    private final ReposDataSource mReposRemoteDataSource;

    // Prevent direct instantiation.
    private ReposRepository(@NonNull ReposDataSource reposRemoteDataSource) {
        mReposRemoteDataSource = checkNotNull(reposRemoteDataSource);
    }

    public static ReposRepository getInstance(@NonNull ReposDataSource reposRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ReposRepository(reposRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<Repo>> getRepos() {
        return null;
    }
}
