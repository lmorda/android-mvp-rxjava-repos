/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
        repo1.html_url = "http://www.github.com/lmorda/android-mvp-rxjava-repos";
        repo1.language = "Java";
        repo1.created_at = "2018-12-13T08:34:04Z";
        repo1.pushed_at = "2018-12-12T08:34:04Z";
        REPOS_SERVICE_DATA.items.add(repo1);
        Repo repo2 = new Repo();
        repo2.id = 2;
        repo2.description = "Retrofit + MVP example";
        repo2.name = "android-mvp-retrofit-repos";
        repo2.html_url = "http://www.github.com/lmorda/android-mvp-retrofit-repos";
        repo2.language = "Java";
        repo2.created_at = "2018-12-13T08:34:04Z";
        repo2.pushed_at = "2018-12-11T08:34:04Z";
        Repo repo3 = new Repo();
        repo3.id = 3;
        repo3.description = "Kotlin + Retrofit + Coroutines example";
        repo3.name = "kotlin-mvp-coroutines-retrofit";
        repo3.html_url = "https://github.com/lmorda/kotlin-mvp-coroutines-retrofit";
        repo3.language = "Kotlin";
        repo3.created_at = "2018-12-13T08:34:04Z";
        repo3.pushed_at = "2018-12-10T08:34:04Z";
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
