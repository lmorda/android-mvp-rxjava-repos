package com.example.lmorda.rxrepos.data.source.local;

import android.provider.BaseColumns;

public final class ReposPersistenceContract {

    private ReposPersistenceContract() {
    }

    public static abstract class RepoEntry implements BaseColumns {
        public static final String TABLE_NAME = "repos";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_URL = "url";
        public static final String COLUMN_NAME_LANGUAGE = "language";
    }

}
