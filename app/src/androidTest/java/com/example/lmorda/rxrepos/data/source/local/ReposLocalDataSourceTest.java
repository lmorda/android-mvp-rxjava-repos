package com.example.lmorda.rxrepos.data.source.local;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.example.lmorda.rxrepos.util.schedulers.ImmediateSchedulerProvider;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.subscribers.TestSubscriber;

import static junit.framework.TestCase.assertNotNull;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ReposLocalDataSourceTest {
    private final static String REPO = "repo";

    private final static String REPO2 = "repo2";

    private final static String REPO3 = "repo3";

    private BaseSchedulerProvider mSchedulerProvider;

    private ReposLocalDataSource mLocalDataSource;

    @Before
    public void setup() {
        ReposLocalDataSource.destroyInstance();
        mSchedulerProvider = new ImmediateSchedulerProvider();
        mLocalDataSource = ReposLocalDataSource.getInstance(
                InstrumentationRegistry.getTargetContext(), mSchedulerProvider);

    }

    @After
    public void cleanUp() {
        mLocalDataSource.deleteAllRepos();
    }

    @Test
    public void testPreConditions() {
        assertNotNull(mLocalDataSource);
    }

    @Test
    public void saveRepo_retrievesRepo() {
        // Given a new repo
        final Repo newRepo = new Repo(1, REPO);
        final Optional<Repo> newRepoOptional = Optional.of(newRepo);

        // When saved into the persistent repository
        mLocalDataSource.saveRepo(newRepo);

        // Then the repo can be retrieved from the persistent repository
        TestSubscriber<Optional<Repo>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getRepo(newRepo.getId()).subscribe(testSubscriber);
        testSubscriber.assertValue(newRepoOptional);
    }

    @Test
    public void deleteAllTasks_emptyListOfRetrievedTask() {
        // Given a new repo in the persistent repository and a mocked callback
        Repo newRepo = new Repo(1, REPO);
        mLocalDataSource.saveRepo(newRepo);

        // When all repos are deleted
        mLocalDataSource.deleteAllRepos();

        // Then the retrieved repos is an empty list
        TestSubscriber<List<Repo>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getRepos().subscribe(testSubscriber);
        List<Repo> result = testSubscriber.values().get(0);
        assertThat(result.isEmpty(), is(true));
    }

    @Test
    public void getTasks_retrieveSavedTasks() {
        // Given 2 new repos in the persistent repository
        Repo newRepo1 = new Repo(1, REPO);
        mLocalDataSource.saveRepo(newRepo1);
        Repo newRepo2 = new Repo(2, REPO2);
        mLocalDataSource.saveRepo(newRepo2);

        // Then the repos can be retrieved from the persistent repository
        TestSubscriber<List<Repo>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getRepos().subscribe(testSubscriber);
        List<Repo> result = testSubscriber.values().get(0);
        assertThat(result, hasItems(newRepo1, newRepo2));
    }

    @Test
    public void getTask_whenTaskNotSaved() {
        //Given that no repo has been saved
        //When querying for a repo, null is returned.
        TestSubscriber<Optional<Repo>> testSubscriber = new TestSubscriber<>();
        mLocalDataSource.getRepo(1).subscribe(testSubscriber);
        testSubscriber.assertValue(Optional.absent());
    }
}
