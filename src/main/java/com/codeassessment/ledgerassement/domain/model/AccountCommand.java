package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;


@SuppressWarnings({"unused"})
public final class AccountCommand {

    private Action action;
    @Getter
    @Setter
    private String comment;
    @Getter
    @Setter
    private String createdOn;
    @Getter
    @Setter
    private String createdBy;

    public AccountCommand() {
        super();
    }

    public String getAction() {
        return this.action.name();
    }

    public void setAction(final String action) {
        this.action = Action.valueOf(action);
    }

    @SuppressWarnings("WeakerAccess")
    public enum Action {
        LOCK,
        UNLOCK,
        CLOSE,
        REOPEN
    }
}
