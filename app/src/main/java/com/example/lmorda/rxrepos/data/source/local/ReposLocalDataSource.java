package com.example.lmorda.rxrepos.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.example.lmorda.rxrepos.data.Repo;
import com.example.lmorda.rxrepos.data.source.ReposDataSource;
import com.example.lmorda.rxrepos.data.source.local.ReposPersistenceContract.RepoEntry;
import com.example.lmorda.rxrepos.util.schedulers.BaseSchedulerProvider;
import com.google.common.base.Optional;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.google.common.base.Preconditions.checkNotNull;


public class ReposLocalDataSource implements ReposDataSource {

    @Nullable
    private static ReposLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase mDatabaseHelper;

    @NonNull
    private Function<Cursor, Repo> mRepoMapperFunction;

    private ReposLocalDataSource(@NonNull Context context,
                                 @NonNull BaseSchedulerProvider schedulerProvider) {

        checkNotNull(context, "context cannot be null");
        checkNotNull(schedulerProvider, "scheduleProvider cannot be null");

        ReposDbHelper dbHelper = new ReposDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();

        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());

        mRepoMapperFunction = this::getRepo;
    }

    @NonNull
    private Repo getRepo(@NonNull Cursor c) {
        Integer itemId = c.getInt(c.getColumnIndexOrThrow(RepoEntry.COLUMN_NAME_ENTRY_ID));
        String name = c.getString(c.getColumnIndexOrThrow(RepoEntry.COLUMN_NAME_NAME));
        String description = c.getString(c.getColumnIndexOrThrow(RepoEntry.COLUMN_NAME_DESCRIPTION));
        String url = c.getString(c.getColumnIndexOrThrow(RepoEntry.COLUMN_NAME_URL));
        String language = c.getString(c.getColumnIndexOrThrow(RepoEntry.COLUMN_NAME_LANGUAGE));
        return new Repo(itemId, name, description, url, language);
    }


    public static ReposLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new ReposLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<Optional<Repo>> getRepo(@NonNull Integer repoId) {
        String[] projection = {
                RepoEntry.COLUMN_NAME_ENTRY_ID,
                RepoEntry.COLUMN_NAME_NAME,
                RepoEntry.COLUMN_NAME_DESCRIPTION,
                RepoEntry.COLUMN_NAME_URL,
                RepoEntry.COLUMN_NAME_LANGUAGE
        };

        //TODO: CAN I USE REPO NAME INSTEAD OF ID TO CREATEQUERY() ??  CANT USE ID BECAUSE ITS AN INTEGER
        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?",
                TextUtils.join(",", projection), RepoEntry.TABLE_NAME, RepoEntry.COLUMN_NAME_ENTRY_ID);
        return mDatabaseHelper.createQuery(RepoEntry.TABLE_NAME, sql, repoId.toString())
                .mapToOneOrDefault(cursor -> Optional.of(mRepoMapperFunction.apply(cursor)), Optional.<Repo>absent())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void saveRepo(@NonNull Repo message) {
        checkNotNull(message);
        ContentValues values = new ContentValues();
        values.put(RepoEntry.COLUMN_NAME_ENTRY_ID, message.getId());
        values.put(RepoEntry.COLUMN_NAME_NAME, message.getName());
        values.put(RepoEntry.COLUMN_NAME_DESCRIPTION, message.getDescription());
        values.put(RepoEntry.COLUMN_NAME_URL, message.getUrl());
        values.put(RepoEntry.COLUMN_NAME_LANGUAGE, message.getLanguage());
        mDatabaseHelper.insert(RepoEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public Flowable<List<Repo>> getRepos() {
        String[] projection = {
                RepoEntry.COLUMN_NAME_ENTRY_ID,
                RepoEntry.COLUMN_NAME_NAME,
                RepoEntry.COLUMN_NAME_DESCRIPTION,
                RepoEntry.COLUMN_NAME_URL,
                RepoEntry.COLUMN_NAME_LANGUAGE
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), RepoEntry.TABLE_NAME);
        return mDatabaseHelper.createQuery(RepoEntry.TABLE_NAME, sql)
                .mapToList(mRepoMapperFunction)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void refreshRepos() {
        // Not required because the {@link ReposRepository} handles the logic of refreshing the
        // messages from all the available data sources.
    }
}
