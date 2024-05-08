package com.codeassessment.ledgerassement.config;

import com.codeassessment.ledgerassement.domain.model.MariaDBTenantFreeJavaConfiguration;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

import java.util.HashSet;
import java.util.Set;

class MariaDBJavaConfigurationImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        final boolean forTenantContext = (boolean)importingClassMetadata
                .getAnnotationAttributes(EnableMariaDB.class.getTypeName())
                .get("forTenantContext");

        final Set<Class> classesToImport = new HashSet<>();
        final String prop = System.getProperty("mariadb.enabled");
        if (prop == null || "true".equals(prop)) {
            classesToImport.add(MariaDBJavaConfiguration.class);
            if (forTenantContext) {
                classesToImport.add(MariaDBTenantBasedJavaConfiguration.class);
            }
            else {
                classesToImport.add(MariaDBTenantFreeJavaConfiguration.class);
            }
        }
        return classesToImport.stream().map(Class::getCanonicalName).toArray(String[]::new);
    }
}
