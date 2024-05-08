package com.codeassessment.ledgerassement.domain.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@SuppressWarnings("unused")
public class ChartOfAccountEntry {

    private String code;
    private String name;
    private String description;
    private String type;
    private Integer level;

    public ChartOfAccountEntry() {
        super();
    }

}