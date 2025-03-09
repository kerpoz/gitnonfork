package com.kcieslak.gitnonfork;

import com.kcieslak.gitnonfork.model.GitHubRepositoriesResponse;
import io.smallrye.mutiny.Uni;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/github")
public class GitHubRepositoryController {

    private final GitHubService gitHubService;

    @Autowired
    public GitHubRepositoryController(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @GetMapping(value = "/{username}/repositories", produces = MediaType.APPLICATION_JSON_VALUE)
    public Uni<GitHubRepositoriesResponse> getRepositories(@PathVariable String username) {
        return gitHubService.getNonForkRepositories(username)
                .onFailure(ResponseStatusException.class)
                .recoverWithItem(this::handleGitHubUserNotFound);
    }

    private GitHubRepositoriesResponse handleGitHubUserNotFound(Throwable throwable) {
        if (throwable instanceof ResponseStatusException ex && ex.getStatusCode() == HttpStatus.NOT_FOUND) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "GitHub user not found");
        }
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred", throwable);
    }

}
