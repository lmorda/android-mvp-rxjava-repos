package com.example.lmorda.rxrepos.util.schedulers;

import android.support.annotation.NonNull;

import io.reactivex.Scheduler;

/**
 * Allow providing different types of {@link Scheduler}s.
 */
public interface BaseSchedulerProvider {

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}