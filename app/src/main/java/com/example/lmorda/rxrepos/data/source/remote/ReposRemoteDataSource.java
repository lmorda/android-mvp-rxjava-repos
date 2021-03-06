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
package com.example.lmorda.rxrepos.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.lmorda.rxrepos.RepoConstants;
import com.example.lmorda.rxrepos.data.GithubRepos;
import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposDataSource;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.google.common.base.Optional;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReposRemoteDataSource implements ReposDataSource {

    @Nullable
    private static ReposRemoteDataSource INSTANCE;

    @NonNull
    private final Retrofit retrofit;

    private ReposRemoteDataSource(@NonNull Context context) {
        checkNotNull(context, "context cannot be null");
        retrofit = buildRetrofit();
        retrofit.create(GithubApiService.class);
    }

    public static ReposRemoteDataSource getInstance(
            @NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ReposRemoteDataSource(context);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    private OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
        return okHttpClientBuilder.build();
    }

    private Retrofit buildRetrofit() {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(RepoConstants.BASE_URL)
                .client(buildOkHttpClient());
        return retrofitBuilder.build();
    }

    @Override
    public Flowable<List<Repo>> getRepos() {
        return retrofit.create(GithubApiService.class).getRepos(RepoConstants.TRENDING_URL)
                .concatMap( repos -> Flowable.just(repos.items));
    }

    @Override
    public Flowable<Optional<Repo>> getRepo(@NonNull Integer repoId) {
        return retrofit.create(GithubApiService.class).getRepo(RepoConstants.GET_REPO_BY_ID_BASE_URL + repoId);
    }

    @Override
    public void saveRepo(@NonNull Repo repo) {

    }

    @Override
    public void refreshRepos() {
        // Not required because the {@link ReposRepository} handles the logic of refreshing the
        // repos from all the available data sources.
    }

    @Override
    public void deleteAllRepos() {

    }
}
