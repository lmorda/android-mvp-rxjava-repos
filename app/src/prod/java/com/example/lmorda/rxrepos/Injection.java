package com.example.lmorda.rxrepos;

public class RepoConstants {

    public static ReposRepository provideReposRepository(@NonNull Context context) {
        checkNotNull(context);
        return ReposRepository.getInstance(ReposRemoteDataSource.getInstance(),
                ReposLocalDataSource.getInstance(context, provideSchedulerProvider()));
    }

    public static BaseSchedulerProvider provideSchedulerProvider() {
        return SchedulerProvider.getInstance();
    }
}
