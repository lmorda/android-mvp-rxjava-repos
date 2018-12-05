package com.example.lmorda.rxrepos;

public class RepoConstants {

    public static final String BASE_URL = "https://api.github.com/";

    //TODO: Build URL with retrofit @Path, @Query, {param}
    public static final String TRENDING_URL = "/search/repositories?q=android+language:java+language:kotlin&sort=stars&order=desc";

    public static final String GET_REPO_BY_ID_BASE_URL = "/repositories/";

}
