package com.gorbatenko.testtask.filter;

import com.gorbatenko.testtask.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.secret.key}")
    private String secret;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");

        if (jwt == null || jwt.isEmpty()) {
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "Missing header Authorization");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        Claims claims;

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (SignatureException ex) {
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", ex.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String username = String.valueOf(claims.get("username"));

        if(! userRepository.findByUsernameIgnoreCase(username).isPresent()) {
            response.setContentType("application/json");
            response.setHeader("WWW-Authenticate", "User not found");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        GrantedAuthority a = new SimpleGrantedAuthority("user");
        var auth = new UsernamePasswordAuthenticationToken(username, null, List.of(a));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/user/");
    }
}
