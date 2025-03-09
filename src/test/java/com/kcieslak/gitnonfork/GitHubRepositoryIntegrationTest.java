package com.kcieslak.gitnonfork;


import com.kcieslak.gitnonfork.model.ErrorResponse;
import com.kcieslak.gitnonfork.model.GitHubRepositoriesResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class GitHubRepositoryIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetRepositoriesIntegration() {
        // Given
        String username = "kerpoz";

        // When
        ResponseEntity<GitHubRepositoriesResponse> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/github/" + username + "/repositories",
                GitHubRepositoriesResponse.class);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        assertNotNull(response.getBody().getRepositories());
        assertFalse(response.getBody().getRepositories().isEmpty());

        response.getBody().getRepositories().forEach(repo -> {
            assertNotNull(repo.getRepositoryName());
            assertFalse(repo.getRepositoryName().isEmpty());

            assertEquals(username, repo.getOwnerLogin());

            assertNotNull(repo.getBranches());

            repo.getBranches().forEach(branch -> {
                assertNotNull(branch.getName());
                assertFalse(branch.getName().isEmpty());

                assertNotNull(branch.getLastCommitSha());
                assertFalse(branch.getLastCommitSha().isEmpty());
            });
        });
    }

    @Test
    public void testGetRepositoriesForNonExistentUser() {
        // Given
        String username = "this-user-definitely-does-not-exist-12345678910";

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/github/" + username + "/repositories",
                ErrorResponse.class);

        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());

        assertEquals(404, response.getBody().getStatus());
        assertEquals("GitHub user not found", response.getBody().getMessage());
    }
}