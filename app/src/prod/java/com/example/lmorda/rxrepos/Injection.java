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
package com.example.lmorda.rxrepos;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.data.source.ReposRepository;
import com.example.lmorda.rxrepos.data.source.local.ReposLocalDataSource;
import com.example.lmorda.rxrepos.data.source.remote.ReposRemoteDataSource;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.example.lmorda.rxrepos.util.schedulers.SchedulerProvider;

import static com.google.common.base.Preconditions.checkNotNull;

public class Injection {

    public static ReposRepository provideReposRepository(@NonNull Context context) {
        checkNotNull(context);
        return ReposRepository.getInstance(ReposRemoteDataSource.getInstance(context),
                ReposLocalDataSource.getInstance(context, provideSchedulerProvider()));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
