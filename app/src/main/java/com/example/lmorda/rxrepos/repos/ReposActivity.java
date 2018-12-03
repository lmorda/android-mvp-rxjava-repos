package com.example.lmorda.rxrepos.repos;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.test.espresso.IdlingResource;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.lmorda.rxrepos.Injection;
import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.util.ActivityUtils;
import com.example.lmorda.rxrepos.util.EspressoIdlingResource;
import android.support.annotation.VisibleForTesting;

public class ReposActivity extends AppCompatActivity {

    private static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout mDrawerLayout;

    private ReposPresenter mReposPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        ReposFragment reposFragment = (ReposFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (reposFragment == null) {
            reposFragment = ReposFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), reposFragment, R.id.contentFrame);
        }

        mReposPresenter = new ReposPresenter(
                Injection.provideReposRemoteDataSource(getApplicationContext()),
                reposFragment,
                Injection.provideSchedulerProvider());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, mReposPresenter.getFiltering());

        super.onSaveInstanceState(outState);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
                        case R.id.list_navigation_menu_item:
//                            // Do nothing, we're already on that screen
                            break;
                        default:
                            break;
                    }
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }


    @VisibleForTesting
    public IdlingResource getCountingIdlingResource() {
        return EspressoIdlingResource.getIdlingResource();
    }
}
