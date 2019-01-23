package com.example.lmorda.rxrepos.data;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repo implements Comparable<Repo> {
    @JsonProperty("id")
    public Integer id;
    @JsonProperty("name")
    public String name;
    @JsonProperty("description")
    public String description;
    @JsonProperty("html_url")
    public String html_url;
    @JsonProperty("language")
    public String language;
    @JsonProperty("created_at")
    public String created_at;
    @JsonProperty("pushed_at")
    public String pushed_at;


    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHtml_url() {
        return html_url;
    }

    public String getLanguage() {
        return language;
    }

    public String getCreatedAt() {
        return created_at;
    }

    public String getUpdatedAt() {
        return pushed_at;
    }

    public Repo() {

    }

    public Repo(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Repo(Integer itemId, String name, String description, String html_url, String language, String pushed_at) {
        this.id = itemId;
        this.name = name;
        this.description = description;
        this.html_url = html_url;
        this.language = language;
        this.pushed_at = pushed_at;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Repo repo = (Repo) o;
        return Objects.equal(id, repo.id) &&
                Objects.equal(name, repo.name) &&
                Objects.equal(description, repo.description) &&
                Objects.equal(html_url, repo.html_url) &&
                Objects.equal(language, repo.language) &&
                Objects.equal(created_at, repo.created_at) &&
                Objects.equal(pushed_at, repo.pushed_at);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, html_url, language, pushed_at);
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", html_url='" + html_url + '\'' +
                ", language='" + language + '\'' +
                ", created_at='" + created_at + '\'' +
                ", pushed_at='" + pushed_at + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Repo repo) {
        return this.name.toLowerCase().compareTo(repo.name.toLowerCase());
    }
}
