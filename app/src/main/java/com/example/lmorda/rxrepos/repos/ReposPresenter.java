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
package com.example.lmorda.rxrepos.repos;

import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposRepository;
import com.example.lmorda.rxrepos.util.EspressoIdlingResource;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReposPresenter implements ReposContract.Presenter {

    @NonNull
    private final ReposRepository mReposRepository;

    @NonNull
    private final ReposContract.View mReposView;

    @NonNull
    private final BaseSchedulerProvider mSchedulerProvider;

    @NonNull
    private ReposFilterType mCurrentFiltering = ReposFilterType.ALL_REPOS;

    @NonNull
    private String mCurrentSearch = "";

    private boolean mFirstLoad = true;

    @NonNull
    private CompositeDisposable mCompositeDisposable;

    public ReposPresenter(@NonNull ReposRepository reposRepository,
                          @NonNull ReposContract.View reposView,
                          @NonNull BaseSchedulerProvider schedulerProvider) {
        mReposRepository = checkNotNull(reposRepository, "reposRepository cannot be null");
        mReposView = checkNotNull(reposView, "ReposView cannot be null!");
        mSchedulerProvider = checkNotNull(schedulerProvider, "schedulerProvider cannot be null");

        mCompositeDisposable = new CompositeDisposable();
        mReposView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadRepos(false);
    }

    @Override
    public void unsubscribe() {
        mCompositeDisposable.clear();
    }

    @Override
    public void loadRepos(boolean forceUpdate) {
        loadRepos(forceUpdate || mFirstLoad, true);

        mFirstLoad = false;
    }

    private void loadRepos(final boolean forceUpdate, final boolean showLoadingUI) {

        if (showLoadingUI) {
            mReposView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            mReposRepository.refreshRepos();
        }

        EspressoIdlingResource.increment();

        mCompositeDisposable.clear();
        Disposable repoItemsFlowable = mReposRepository
                .getRepos()
                .flatMap(Flowable::fromIterable)
                .filter(repo -> {
                    switch (mCurrentFiltering) {
                        case JAVA_REPOS:
                            return repo.language.equals("Java") && repo.name.contains(mCurrentSearch);
                        case KOTLIN_REPOS:
                            return repo.language.equals("Kotlin") && repo.name.contains(mCurrentSearch);
                        case ALL_REPOS:
                            return repo.name.contains(mCurrentSearch);
                        default:
                            return true;
                    }
                })
                .sorted()
                .toList()
                .subscribeOn(mSchedulerProvider.io())
                .observeOn(mSchedulerProvider.ui())
                .doFinally(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                })
                .subscribe(
                        // onNext
                        this::processRepos,
                        // onError
                        throwable -> {
                            mReposView.setLoadingIndicator(false);
                            mReposView.showLoadingReposError("Could not get repositories!");
                        });



        mCompositeDisposable.add(repoItemsFlowable);

    }

    @Override
    public void setFiltering(ReposFilterType requestType) {
        mCurrentFiltering = requestType;
    }

    @Override
    public void setSearchString(String searchString) {
        mCurrentSearch = searchString;
    }

    @Override
    public ReposFilterType getFiltering() {
        return mCurrentFiltering;
    }

    private void processRepos(@NonNull List<Repo> repos) {
        mReposView.setLoadingIndicator(false);
        if (repos.isEmpty()) {
            mReposView.showNoRepos();
        }
        else {
            mReposView.showRepos(repos);
        }
    }

}
