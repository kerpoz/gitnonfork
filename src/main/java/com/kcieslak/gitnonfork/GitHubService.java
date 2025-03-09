package com.kcieslak.gitnonfork;

import com.kcieslak.gitnonfork.model.BranchInfo;
import com.kcieslak.gitnonfork.model.GitHubBranch;
import com.kcieslak.gitnonfork.model.GitHubRepositoriesResponse;
import com.kcieslak.gitnonfork.model.RepositoryInfo;
import io.smallrye.mutiny.Uni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GitHubService {

    private static final String GITHUB_API_BASE_URL = "https://api.github.com";
    private final RestTemplate restTemplate;

    @Autowired
    public GitHubService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Uni<GitHubRepositoriesResponse> getNonForkRepositories(String username) {
        return Uni.createFrom().item(() -> {
            try {
                // Get all repositories for the user
                String repoUrl = GITHUB_API_BASE_URL + "/users/" + username + "/repos";
                GitHubRepository[] allRepos = restTemplate.getForObject(repoUrl, GitHubRepository[].class);

                // Filter out forks
                List<GitHubRepository> nonForkRepos = Arrays.stream(allRepos)
                        .filter(repo -> !repo.isFork())
                        .collect(Collectors.toList());

                List<RepositoryInfo> repositoryInfoList = new ArrayList<>();

                // For each non-fork repository, get branch information
                for (GitHubRepository repo : nonForkRepos) {
                    String branchesUrl = GITHUB_API_BASE_URL + "/repos/" + username + "/" + repo.getName() + "/branches";
                    GitHubBranch[] branches = restTemplate.getForObject(branchesUrl, GitHubBranch[].class);

                    List<BranchInfo> branchInfoList = Arrays.stream(branches)
                            .map(branch -> new BranchInfo(branch.getName(), branch.getCommit().getSha()))
                            .collect(Collectors.toList());

                    repositoryInfoList.add(new RepositoryInfo(repo.getName(), repo.getOwner().getLogin(), branchInfoList));
                }

                return new GitHubRepositoriesResponse(repositoryInfoList);
            } catch (HttpClientErrorException e) {
                if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GitHub user not found", e);
                }
                throw e;
            }
        });
    }
}