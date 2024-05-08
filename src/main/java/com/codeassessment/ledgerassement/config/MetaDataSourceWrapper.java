package com.codeassessment.ledgerassement.config;

import com.jolbox.bonecp.BoneCPDataSource;

public class MetaDataSourceWrapper {
    private final BoneCPDataSource metaDataSource;

    public MetaDataSourceWrapper(final BoneCPDataSource metaDataSource) {
        this.metaDataSource = metaDataSource;
    }

   public BoneCPDataSource getMetaDataSource() {
        return metaDataSource;
    }
}
