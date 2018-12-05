package com.example.lmorda.rxrepos.data.source;

import com.example.lmorda.rxrepos.data.Repo;
import com.google.common.base.Optional;

import android.support.annotation.NonNull;
import java.util.List;

import io.reactivex.Flowable;

public interface ReposDataSource {

    Flowable<List<Repo>> getRepos();

    Flowable<Optional<Repo>> getRepo(@NonNull Integer repoId);

    void saveRepo(@NonNull Repo repo);

    void refreshRepos();

}
