package com.example.lmorda.rxrepos.data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Repo {
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

    public Repo() {

    }

    public Repo(Integer itemId, String name, String description, String html_url, String language) {
        this.id = itemId;
        this.name = name;
        this.description = description;
        this.html_url = html_url;
        this.language = language;
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
                Objects.equal(language, repo.language);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id, name, description, html_url, language);
    }

    @Override
    public String toString() {
        return "Repo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", html_url='" + html_url + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
