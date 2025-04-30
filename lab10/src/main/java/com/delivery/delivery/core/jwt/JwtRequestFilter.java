package com.delivery.delivery.core.jwt;

import com.delivery.delivery.core.entity.User;
import com.delivery.delivery.core.exception.DefaultException;
import com.delivery.delivery.core.repository.TokenRepository;
import com.delivery.delivery.core.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JwtRequestFilter extends OncePerRequestFilter {
//    private final JwtTokenUtils jwtTokenUtils;
//    private final UserRepository userRepository;
//    private final TokenRepository tokenRepository;
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            @NotNull HttpServletResponse response,
//            @NotNull FilterChain filterChain
//    ) throws ServletException, IOException {
//        String authHeader = request.getHeader("Authorization");
//        String jwt = null;
//        if (authHeader != null && authHeader.startsWith("Bearer ")) {
//            jwt = authHeader.substring(7);
//            boolean isBanned = tokenRepository.findByToken(jwt).isPresent();
//            if (isBanned) {
//                log.info("Attempt to use a banned token");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "The token is banned");
//                return;
//            }
//            try {
//                Long userId = jwtTokenUtils.getUserIdFromToken(jwt);
//                if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                    User user = userRepository.findById(userId).orElse(null);
//                    if (user != null) {
//                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
//                                user, jwt, user.getAuthorities()
//                        );
//                        SecurityContextHolder.getContext().setAuthentication(token);
//                    }
//                }
//            } catch (DefaultException e) {
//                log.debug("Token is expired :(");
//            } catch (Exception e) {
//                log.error("Error processing JWT", e);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }
//}