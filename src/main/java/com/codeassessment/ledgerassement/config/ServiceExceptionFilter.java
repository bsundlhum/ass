package com.codeassessment.ledgerassement.config;

import com.codeassessment.ledgerassement.app.excutor.util.ServiceError;
import com.codeassessment.ledgerassement.app.excutor.util.ServiceException;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

final class ServiceExceptionFilter extends OncePerRequestFilter {

    ServiceExceptionFilter() {
        super();
    }

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (final NestedServletException ex) {
            if (ServiceException.class.isAssignableFrom(ex.getCause().getClass())) {
                @SuppressWarnings("ThrowableResultOfMethodCallIgnored") final ServiceException serviceException = ServiceException.class.cast(ex.getCause());
                final ServiceError serviceError = serviceException.serviceError();
                logger.info("Responding with a service error " + serviceError);
                response.sendError(serviceError.getCode(), serviceError.getMessage());
            } else {
                logger.info("Unexpected exception caught " + ex);
                throw ex;
            }
        }
    }
}
