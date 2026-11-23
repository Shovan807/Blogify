package com.ShovanParia.Blogify.Security;

import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenHelper jwtTokenHelper;

    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenHelper jwtTokenHelper,
            CustomUserDetailsService userDetailsService) {

        this.jwtTokenHelper = jwtTokenHelper;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Get Authorization header
        String authHeader =
                request.getHeader("Authorization");

        // 2. Check Bearer token
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract JWT
        String token = authHeader.substring(7);

        try {

            // 4. Get username/email from token
            String email =
                    jwtTokenHelper.getUsernameFromToken(token);
            System.out.println("JWT EMAIL: " + email);
            // 5. Check existing authentication
            if (email != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                // 6. Load associated user from database
                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                // 7. Validate token
                if (jwtTokenHelper.validateToken(
                        token, userDetails)) {

                    // 8. Create authentication object
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request)
                    );

                    // 9. Set authentication in SecurityContext
                    SecurityContext context =
                            SecurityContextHolder.createEmptyContext();

                    context.setAuthentication(authentication);

                    SecurityContextHolder.setContext(context);
                }
            }

        } catch (JwtException |
                 IllegalArgumentException |
                 AuthenticationException ex) {

            // Invalid token or user not found
            SecurityContextHolder.clearContext();
            System.out.println("JWT ERROR: " + ex.getMessage());

            String email =
                    jwtTokenHelper.getUsernameFromToken(token);

            System.out.println("TOKEN EMAIL: [" + email + "]");

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(email);

            System.out.println(
                    "DATABASE EMAIL: [" + userDetails.getUsername() + "]"
            );

            boolean isValid =
                    jwtTokenHelper.validateToken(token, userDetails);

            System.out.println("TOKEN VALID: " + isValid);

            if (isValid) {

                System.out.println("JWT AUTHENTICATION SUCCESS");

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                SecurityContext context =
                        SecurityContextHolder.createEmptyContext();

                context.setAuthentication(authentication);

                SecurityContextHolder.setContext(context);

            } else {
                System.out.println("JWT AUTHENTICATION FAILED");
            }

        }

        // 10. Continue filter chain
        filterChain.doFilter(request, response);

    }
}