package com.project.backend.common.models;

import lombok.Getter;

@Getter
public enum SpringProfiles {

    DEVELOPMENT("dev"),
    STAGING("stg"),
    PRODUCTION("prod");

    private final String name;

    SpringProfiles(String name) {
        this.name = name;
    }
}
