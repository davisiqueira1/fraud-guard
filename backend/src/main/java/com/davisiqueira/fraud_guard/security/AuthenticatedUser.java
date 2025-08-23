package com.davisiqueira.fraud_guard.security;

import com.davisiqueira.fraud_guard.service.user.UserDetailsImpl;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
// This bean is request-scoped: a new real instance exists for each HTTP request.
// Because controllers/services are singletons, Spring injects a proxy here
// (proxyMode = TARGET_CLASS). At runtime, the proxy delegates calls to the
// correct request-scoped instance bound to the current request.
//
// Without proxyMode, Spring would try to inject the real request-scoped bean
// into a singleton during application startup (when no request is active),
// causing: "Scope 'request' is not active for the current thread".
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthenticatedUser {
    public UserDetailsImpl get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (UserDetailsImpl) auth.getPrincipal();
    }
}
