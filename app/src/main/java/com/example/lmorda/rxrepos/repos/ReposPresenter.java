package com.example.lmorda.rxrepos.repos;

import android.support.annotation.NonNull;

import com.example.lmorda.rxrepos.RepoConstants;
import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposRepository;
import com.example.lmorda.rxrepos.data.source.remote.GithubApiService;
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
                            return repo.language.equals("Java");
                        case KOTLIN_REPOS:
                            return repo.language.equals("Kotlin");
                        case ALL_REPOS:
                        default:
                            return true;
                    }
                })
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
