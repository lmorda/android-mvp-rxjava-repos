package com.example.lmorda.rxrepos.data.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lmorda.rxrepos.Injection;
import com.example.lmorda.rxrepos.RepoConstants;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReposRemoteDataSource {

    @Nullable
    private static ReposRemoteDataSource INSTANCE;

    @NonNull
    private final Retrofit retrofit;

    private ReposRemoteDataSource(@NonNull Context context,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context, "context cannot be null");
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");
        retrofit = buildRetrofit();
    }

    private OkHttpClient buildOkHttpClient() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        // Connect / endpoint response timeouts for API Server
        okHttpClientBuilder.readTimeout(10, TimeUnit.SECONDS);
        okHttpClientBuilder.connectTimeout(10, TimeUnit.SECONDS);

        // OkHttp Logging Interceptor
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        return okHttpClientBuilder.build();
    }

    private Retrofit buildRetrofit() {
        // New Retrofit 2.0 builder
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();

        // Halarious converter for JSON serialization / deserialization
        retrofitBuilder.addConverterFactory(JacksonConverterFactory.create());
        retrofitBuilder.client(buildOkHttpClient());

        // Set endpoint base URL
        try {
            retrofitBuilder.baseUrl(RepoConstants.BASE_URL);
        } catch (IllegalArgumentException iae) {
            Log.e("okhttp", iae.getMessage());
        }

        // Build and create Retrofit service
        return retrofitBuilder.build();
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
}
