package com.codeassessment.ledgerassement.app.excutor.util;

import jakarta.annotation.Nonnull;
import org.springframework.util.Assert;
import java.util.Optional;

public final class TenantContextHolder {

    private static final InheritableThreadLocal<String> THREAD_LOCAL = new InheritableThreadLocal<>();

    private TenantContextHolder() {
        super();
    }

    @Nonnull
    public static Optional<String> identifier() {
        return Optional.ofNullable(TenantContextHolder.THREAD_LOCAL.get());
    }

    @SuppressWarnings("WeakerAccess")
    public static String checkedGetIdentifier() {
        return identifier().orElseThrow(() -> new IllegalStateException("Tenant context not set."));
    }

    public static void setIdentifier(@Nonnull final String identifier) {
        Assert.notNull(identifier, "A tenant identifier must be given.");
        Assert.hasLength(identifier, "A tenant identifier must have at least one character.");
        Assert.isNull(TenantContextHolder.THREAD_LOCAL.get(), "Tenant identifier already set.");
        TenantContextHolder.THREAD_LOCAL.set(identifier);
    }

    public static void clear() {
        TenantContextHolder.THREAD_LOCAL.remove();
    }
}
