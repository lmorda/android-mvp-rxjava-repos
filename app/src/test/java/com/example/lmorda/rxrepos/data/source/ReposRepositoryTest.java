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
package com.example.lmorda.rxrepos.data.source;

import android.content.Context;

import com.example.lmorda.rxrepos.data.Repo;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import io.reactivex.Flowable;
import io.reactivex.subscribers.TestSubscriber;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReposRepositoryTest {

    private final static String REPO_NAME = "name";

    private static List<Repo> REPOS = Lists.newArrayList(
            new Repo(1, "Name1", "Description1", "Url1", "Language1", "2018-12-13T08:34:04Z"),
            new Repo(2, "Name2", "Description2", "Url2", "Language2", "2018-12-13T08:34:04Z"));

    private ReposRepository mReposRepository;

    private TestSubscriber<List<Repo>> mReposTestSubscriber;

    @Mock
    private ReposDataSource mReposRemoteDataSource;

    @Mock
    private ReposDataSource mReposLocalDataSource;

    @Mock
    private Context mContext;

    @Before
    public void setupReposRepository() {
        MockitoAnnotations.initMocks(this);
        mReposRepository = ReposRepository.getInstance(mReposRemoteDataSource, mReposLocalDataSource);

        mReposTestSubscriber = new TestSubscriber<>();
    }

    @After
    public void destroyRepositoryInstance() {
        ReposRepository.destroyInstance();
    }

    @Test
    public void getRepos_repositoryCachesAfterFirstSubscription_whenReposAvailableInLocalStorage() {
        // Given that the local data source has data available
        setReposAvailable(mReposLocalDataSource, REPOS);
        // And the remote data source does not have any data available
        setReposNotAvailable(mReposRemoteDataSource);

        // When two subscriptions are set
        TestSubscriber<List<Repo>> testSubscriber1 = new TestSubscriber<>();
        mReposRepository.getRepos().subscribe(testSubscriber1);

        TestSubscriber<List<Repo>> testSubscriber2 = new TestSubscriber<>();
        mReposRepository.getRepos().subscribe(testSubscriber2);

        // Then repos were only requested once from remote and local sources
        verify(mReposRemoteDataSource).getRepos();
        verify(mReposLocalDataSource).getRepos();

        assertFalse(mReposRepository.mCacheIsDirty);
        testSubscriber1.assertValue(REPOS);
        testSubscriber2.assertValue(REPOS);
    }

    @Test
    public void getRepos_repositoryCachesAfterFirstSubscription_whenReposAvailableInRemoteStorage() {
        // Given that the remote data source has data available
        setReposAvailable(mReposRemoteDataSource, REPOS);
        // And the local data source does not have any data available
        setReposNotAvailable(mReposLocalDataSource);

        // When two subscriptions are set
        TestSubscriber<List<Repo>> testSubscriber1 = new TestSubscriber<>();
        mReposRepository.getRepos().subscribe(testSubscriber1);

        TestSubscriber<List<Repo>> testSubscriber2 = new TestSubscriber<>();
        mReposRepository.getRepos().subscribe(testSubscriber2);

        // Then repos were only requested once from remote and local sources
        verify(mReposRemoteDataSource).getRepos();
        verify(mReposLocalDataSource).getRepos();
        assertFalse(mReposRepository.mCacheIsDirty);
        testSubscriber1.assertValue(REPOS);
        testSubscriber2.assertValue(REPOS);
    }

    @Test
    public void getRepos_requestsAllReposFromLocalDataSource() {
        // Given that the local data source has data available
        setReposAvailable(mReposLocalDataSource, REPOS);
        // And the remote data source does not have any data available
        setReposNotAvailable(mReposRemoteDataSource);

        // When repos are requested from the repos repository
        mReposRepository.getRepos().subscribe(mReposTestSubscriber);

        // Then repos are loaded from the local data source
        verify(mReposLocalDataSource).getRepos();
        mReposTestSubscriber.assertValue(REPOS);
    }

    @Test
    public void saveRepo_savesRepoToServiceAPI() {
        // Given a stub repo with name
        Repo newRepo = new Repo(0, REPO_NAME, "", "", "", "");

        // When a repo is saved to the repos repository
        mReposRepository.saveRepo(newRepo);

        // Then the service API and persistent repository are called and the cache is pushed
        verify(mReposRemoteDataSource).saveRepo(newRepo);
        verify(mReposLocalDataSource).saveRepo(newRepo);
        assertThat(mReposRepository.mCachedRepos.size(), is(1));
    }

    @Test
    public void getRepo_requestsSingleRepoFromLocalDataSource() {
        // Given a stub repo with name in the local repository
        Repo repo = new Repo(0, REPO_NAME, "", "", "", "");
        Optional<Repo> repoOptional = Optional.of(repo);
        setRepoAvailable(mReposLocalDataSource, repoOptional);
        // And the repo not available in the remote repository
        setRepoNotAvailable(mReposRemoteDataSource, repoOptional.get().getId());

        // When a repo is requested from the repos repository
        TestSubscriber<Optional<Repo>> testSubscriber = new TestSubscriber<>();
        mReposRepository.getRepo(repo.getId()).subscribe(testSubscriber);

        // Then the repo is loaded from the database
        verify(mReposLocalDataSource).getRepo(eq(repo.getId()));
        testSubscriber.assertValue(repoOptional);
    }

    @Test
    public void getRepo_whenDataNotLocal_fails() {
        // Given a stub repo with name in the remote repository
        Repo repo = new Repo(0, REPO_NAME, "", "", "", "");
        Optional<Repo> repoOptional = Optional.of(repo);
        setRepoAvailable(mReposRemoteDataSource, repoOptional);
        // And the repo not available in the local repository
        setRepoNotAvailable(mReposLocalDataSource, repo.getId());

        // When a repo is requested from the repos repository
        TestSubscriber<Optional<Repo>> testSubscriber = new TestSubscriber<>();
        mReposRepository.getRepo(repo.getId()).subscribe(testSubscriber);

        // then empty Optional is returned
        testSubscriber.assertValue(Optional.absent());
    }

    @Test
    public void getReposWithDirtyCache_reposAreRetrievedFromRemote() {
        // Given that the remote data source has data available
        setReposAvailable(mReposRemoteDataSource, REPOS);

        // When calling getRepos in the repository with dirty cache
        mReposRepository.refreshRepos();
        mReposRepository.getRepos().subscribe(mReposTestSubscriber);

        // Verify the repos from the remote data source are returned, not the local
        verify(mReposLocalDataSource, never()).getRepos();
        verify(mReposRemoteDataSource).getRepos();
        mReposTestSubscriber.assertValue(REPOS);
    }

    @Test
    public void getReposWithLocalDataSourceUnavailable_reposAreRetrievedFromRemote() {
        // Given that the local data source has no data available
        setReposNotAvailable(mReposLocalDataSource);
        // And the remote data source has data available
        setReposAvailable(mReposRemoteDataSource, REPOS);

        // When calling getRepos in the repository
        mReposRepository.getRepos().subscribe(mReposTestSubscriber);

        // Verify the repos from the remote data source are returned
        verify(mReposRemoteDataSource).getRepos();
        mReposTestSubscriber.assertValue(REPOS);
    }

    @Test
    public void getReposWithBothDataSourcesUnavailable_firesOnDataUnavailable() {
        // Given that the local data source has no data available
        setReposNotAvailable(mReposLocalDataSource);
        // And the remote data source has no data available
        setReposNotAvailable(mReposRemoteDataSource);

        // When calling getRepos in the repository
        mReposRepository.getRepos().subscribe(mReposTestSubscriber);

        // Verify no data is returned
        mReposTestSubscriber.assertNoValues();
        // Verify that error is returned
        mReposTestSubscriber.assertError(NoSuchElementException.class);
    }

    @Test
    public void getRepoWithBothDataSourcesUnavailable_firesOnError() {
        // Given a repo id
        final Integer repoId = 123;
        // And the local data source has no data available
        setRepoNotAvailable(mReposLocalDataSource, repoId);
        // And the remote data source has no data available
        setRepoNotAvailable(mReposRemoteDataSource, repoId);

        // When calling getRepo in the repository
        TestSubscriber<Optional<Repo>> testSubscriber = new TestSubscriber<>();
        mReposRepository.getRepo(repoId).subscribe(testSubscriber);

        // Verify that error is returned
        testSubscriber.assertValue(Optional.absent());
    }

    @Test
    public void getRepos_refreshesLocalDataSource() {
        // Given that the remote data source has data available
        setReposAvailable(mReposRemoteDataSource, REPOS);

        // Mark cache as dirty to force a reload of data from remote data source.
        mReposRepository.refreshRepos();

        // When calling getRepos in the repository
        mReposRepository.getRepos().subscribe(mReposTestSubscriber);

        // Verify that the data fetched from the remote data source was saved in local.
        verify(mReposLocalDataSource, times(REPOS.size())).saveRepo(any(Repo.class));
        mReposTestSubscriber.assertValue(REPOS);
    }

    private void setReposNotAvailable(ReposDataSource dataSource) {
        when(dataSource.getRepos()).thenReturn(Flowable.just(Collections.emptyList()));
    }

    private void setReposAvailable(ReposDataSource dataSource, List<Repo> repos) {
        when(dataSource.getRepos()).thenReturn(Flowable.just(repos).concatWith(Flowable.never()));
    }

    private void setRepoNotAvailable(ReposDataSource dataSource, Integer repoId) {
        when(dataSource.getRepo(eq(repoId))).thenReturn(Flowable.just(Optional.absent()));
    }

    private void setRepoAvailable(ReposDataSource dataSource, Optional<Repo> repoOptional) {
        when(dataSource.getRepo(eq(repoOptional.get().getId()))).thenReturn(Flowable.just(repoOptional).concatWith(Flowable.never()));
    }
}
