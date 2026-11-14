package com.edifika.forum.infrastructure.authorization.sfs;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

/** Usuario autenticado de la peticion actual (lo llena JwtAuthenticationFilter). */
public final class CurrentUser {

    private static final List<String> ROLE_PRIORITY = List.of("ADMIN", "OWNER", "TENANT");

    private CurrentUser() {
    }

    public static Long id() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getDetails() instanceof Long id ? id : null;
    }

    /** Rol principal del token: ADMIN, OWNER o TENANT (null si no tiene ninguno). */
    public static String role() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        var roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(a -> a.startsWith("ROLE_") ? a.substring(5) : a)
                .toList();
        return ROLE_PRIORITY.stream().filter(roles::contains).findFirst().orElse(null);
    }
}
