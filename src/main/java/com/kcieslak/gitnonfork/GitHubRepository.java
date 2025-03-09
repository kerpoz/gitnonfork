// GitHubRepository.java
package com.kcieslak.gitnonfork;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kcieslak.gitnonfork.model.Owner;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubRepository {
    private String name;
    private boolean fork;
    private Owner owner;
}

