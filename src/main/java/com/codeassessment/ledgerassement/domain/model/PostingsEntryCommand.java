package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostingsEntryCommand {

    private Action action;
    private String comment;
    private String createdOn;
    private String createdBy;

    public PostingsEntryCommand() {
        super();
    }

    @SuppressWarnings("WeakerAccess")
    public enum Action {
        RELEASE
    }
}
