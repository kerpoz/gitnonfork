package com.kcieslak.gitnonfork.model;

import lombok.Getter;

import java.util.List;

@Getter
public class GitHubRepositoriesResponse {
    private List<RepositoryInfo> repositories;

    public GitHubRepositoriesResponse(List<RepositoryInfo> repositories) {
        this.repositories = repositories;
    }

}
