package com.kcieslak.gitnonfork.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchInfo {
    private String name;
    private String lastCommitSha;

    public BranchInfo(String name, String lastCommitSha) {
        this.name = name;
        this.lastCommitSha = lastCommitSha;
    }

}
