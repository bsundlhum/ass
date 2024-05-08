package com.codeassessment.ledgerassement.config;

import com.codeassessment.ledgerassement.app.excutor.util.TenantContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@SuppressWarnings("WeakerAccess")
public final class TenantHeaderFilter extends OncePerRequestFilter {

    public static final String TENANT_HEADER = "X-Tenant-Identifier";

    public TenantHeaderFilter() {
        super();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final String tenantHeaderValue = request.getHeader(TenantHeaderFilter.TENANT_HEADER);

        if (tenantHeaderValue == null || tenantHeaderValue.isEmpty()) {
            response.sendError(400, "Header [" + TENANT_HEADER + "] must be given!");
        } else {
            TenantContextHolder.clear();
            TenantContextHolder.setIdentifier(tenantHeaderValue);
        }

        try {
            filterChain.doFilter(request, response);
        } finally {
            TenantContextHolder.clear();
        }
    }
}
