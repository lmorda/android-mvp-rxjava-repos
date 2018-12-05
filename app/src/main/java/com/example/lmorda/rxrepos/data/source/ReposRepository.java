package com.example.lmorda.rxrepos.data.source;

import com.example.lmorda.rxrepos.data.Repo;
import com.google.common.base.Optional;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;

import static com.google.common.base.Preconditions.checkNotNull;

public class ReposRepository implements ReposDataSource {

    @Nullable
    private static ReposRepository INSTANCE = null;

    @NonNull
    private final ReposDataSource mReposLocalDataSource;

    @NonNull
    private final ReposDataSource mReposRemoteDataSource;

    @VisibleForTesting
    @Nullable
    Map<Integer, Repo> mCachedRepos;

    @VisibleForTesting
    boolean mCacheIsDirty = false;

    private ReposRepository(@NonNull ReposDataSource reposRemoteDataSource,
                            @NonNull ReposDataSource reposLocalDataSource) {
        mReposRemoteDataSource = checkNotNull(reposRemoteDataSource);
        mReposLocalDataSource = checkNotNull(reposLocalDataSource);
    }
    
    public static ReposRepository getInstance(@NonNull ReposDataSource reposRemoteDataSource,
                                              @NonNull ReposDataSource reposLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ReposRepository(reposRemoteDataSource, reposLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<Repo>> getRepos() {
        if (mCachedRepos != null && !mCacheIsDirty) {
            return Flowable.fromIterable(mCachedRepos.values()).toList().toFlowable();
        } else if (mCachedRepos == null) {
            mCachedRepos = new LinkedHashMap<>();
        }

        Flowable<List<Repo>> remoteRepos = getAndSaveRemoteRepos();

        if (mCacheIsDirty) {
            return remoteRepos;
        } else {
            // Query the local storage if available. If not, query the network.
            Flowable<List<Repo>> localRepos = getAndCacheLocalRepos();
            return Flowable.concat(localRepos, remoteRepos)
                    .filter(tasks -> !tasks.isEmpty())
                    .firstOrError()
                    .toFlowable();
        }
    }


    public Flowable<List<Repo>> getAndCacheLocalRepos() {
        return null;
    }

    private Flowable<List<Repo>> getAndSaveRemoteRepos() {
        return mReposRemoteDataSource
                .getRepos()
                .flatMap(repos -> Flowable.fromIterable(repos).doOnNext(repo -> {
                    mReposLocalDataSource.saveRepo(repo);
                    mCachedRepos.put(repo.id, repo);
                }).toList().toFlowable())
                .doOnComplete(() -> mCacheIsDirty = false);
    }

    @Override
    public void saveRepo(@NonNull Repo repo) {
        checkNotNull(repo);
        mReposRemoteDataSource.saveRepo(repo);
        mReposLocalDataSource.saveRepo(repo);

        if (mCachedRepos == null) {
            mCachedRepos = new LinkedHashMap<>();
        }
        mCachedRepos.put(repo.getId(), repo);
    }

    @Override
    public Flowable<Optional<Repo>> getRepo(@NonNull final Integer repoId) {
        checkNotNull(repoId);

        final Repo cachedRepo = getRepoWithId(repoId);

        // Respond immediately with cache if available
        if (cachedRepo != null) {
            return Flowable.just(Optional.of(cachedRepo));
        }

        // Load from server/persisted if needed.

        // Do in memory cache update to keep the app UI up to date
        if (mCachedRepos == null) {
            mCachedRepos = new LinkedHashMap<>();
        }

        // Is the repo in the local data source? If not, query the network.
        Flowable<Optional<Repo>> localRepo = getRepoWithIdFromLocalRepository(repoId);
        Flowable<Optional<Repo>> remoteRepo = mReposRemoteDataSource
                .getRepo(repoId)
                .doOnNext(repoOptional -> {
                    if (repoOptional.isPresent()) {
                        Repo repo = repoOptional.get();
                        mReposLocalDataSource.saveRepo(repo);
                        mCachedRepos.put(repo.getId(), repo);
                    }
                });

        return Flowable.concat(localRepo, remoteRepo)
                .firstElement()
                .toFlowable();
    }

    @Nullable
    private Repo getRepoWithId(@NonNull Integer id) {
        checkNotNull(id);
        if (mCachedRepos == null || mCachedRepos.isEmpty()) {
            return null;
        } else {
            return mCachedRepos.get(id);
        }
    }

    @NonNull
    private Flowable<Optional<Repo>> getRepoWithIdFromLocalRepository(@NonNull final Integer repoId) {
        return mReposLocalDataSource
                .getRepo(repoId)
                .doOnNext(repoOptional -> {
                    if (repoOptional.isPresent()) {
                        mCachedRepos.put(repoId, repoOptional.get());
                    }
                })
                .firstElement().toFlowable();
    }

    @Override
    public void refreshRepos() {
        mCacheIsDirty = true;
    }

}
