package com.example.lmorda.rxrepos.repos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.example.lmorda.rxrepos.Injection;
import com.example.lmorda.rxrepos.R;
import com.example.lmorda.rxrepos.util.ActivityUtils;

public class ReposActivity extends AppCompatActivity {

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
            // Create the fragment
            reposFragment = ReposFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), reposFragment, R.id.contentFrame);
        }

        // Create the presenter
        mReposPresenter = new ReposPresenter(
                Injection.provideReposRepository(getApplicationContext()),
                reposFragment,
                Injection.provideSchedulerProvider());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_repos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
//                        case R.id.list_navigation_menu_item:
//                            // Do nothing, we're already on that screen
//                            break;
//                        case R.id.statistics_navigation_menu_item:
//                            Intent intent =
//                                    new Intent(ReposActivity.this, StatisticsActivity.class);
//                            startActivity(intent);
//                            break;
                        default:
                            break;
                    }
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }
}
