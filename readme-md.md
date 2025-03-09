# GitHub Repository Fetcher

A Spring Boot application that fetches non-fork GitHub repositories with branch information for a specified user.

## Features

- Lists all non-fork repositories for a GitHub user
- Provides repository name and owner login for each repository
- Returns branch names and last commit SHA for each branch
- Includes proper error handling for non-existent users (404 response)

## Requirements

- Java 17 or higher
- Maven 3.6.0 or higher

## Setup and Installation

1. Build the application:
   ```
   mvn clean install
   ```

2. Run the application:
   ```
   mvn spring-boot:run
   ```

The application will start on port 8080 by default.

## API Usage

### Get User Repositories

**Endpoint:** `GET /api/github/{username}/repositories`

**Parameters:**
- `username`: GitHub username to fetch repositories for

**Example Success Response:**
```json
{
  "repositories": [
    {
      "repositoryName": "example-repo",
      "ownerLogin": "kerpoz",
      "branches": [
        {
          "name": "main",
          "lastCommitSha": "3f7ce4e3c0e7c73d4b3f6e718e8a3e19c09b1a45"
        },
        {
          "name": "development",
          "lastCommitSha": "a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0"
        }
      ]
    }
  ]
}
```

**Example Error Response (User Not Found):**
```json
{
  "status": 404,
  "message": "GitHub user not found"
}
```

## Technical Details

This application uses:
- Spring Boot 3.4.3
- GitHub API v3 (https://developer.github.com/v3)
- Mutiny for reactive types (Uni/Multi)
- RestTemplate for HTTP requests

## Testing

Run the tests using:
```
mvn test
```

The test suite includes an integration test that verifies:
- Successfully retrieving repositories for an existing user
- Proper error response for a non-existent user
