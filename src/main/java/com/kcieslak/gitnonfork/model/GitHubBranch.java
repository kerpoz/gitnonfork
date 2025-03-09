package com.kcieslak.gitnonfork.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GitHubBranch {
    private String name;
    private Commit commit;

}
