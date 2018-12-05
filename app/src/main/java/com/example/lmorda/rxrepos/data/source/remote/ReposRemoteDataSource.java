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

    @NonNull
    private final GithubApiService githubApiService;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;


    private ReposRemoteDataSource(@NonNull Context context,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context, "context cannot be null");
        mSchedulerProvider = checkNotNull(schedulerProvider, "scheduleProvider cannot be null");
        retrofit = buildRetrofit();
        githubApiService = retrofit.create(GithubApiService.class);
    }

    public static ReposRemoteDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new ReposRemoteDataSource(context, schedulerProvider);
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
        // tasks from all the available data sources.
    }
}
