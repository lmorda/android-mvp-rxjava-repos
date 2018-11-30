package com.example.lmorda.rxrepos.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubRepos {

    @JsonProperty("total_count")
    public Integer totalCount;
    @JsonProperty("incomplete_results")
    public Boolean incompleteResults;
    @JsonProperty("items")
    public List<Repo> items;

}
