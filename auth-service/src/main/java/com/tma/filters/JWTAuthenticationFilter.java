package com.tma.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tma.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTService jwtService;

    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        System.out.println("JWTAuth Filter called!");
        String requestUri = request.getRequestURI();
        final String authHeader = request.getHeader("Authorization");
        final String token;
        if (authHeader == null || !authHeader.startsWith("Bearer") || requestUri.contains("auth")){
            filterChain.doFilter(request, response);
            return;
        }

        final String username;
        token = authHeader.substring(7);
        System.out.println(token);
        try{
            username = jwtService.extractUserName(token);
        }
        catch (ExpiredJwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "You are not authenticated");
            response.setContentType("application/json");

            // Write the response body
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            mapper.writeValue(out, responseBody);
            out.flush();
            out.close();
            return;
        }
        catch (SignatureException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("message", "Invalid Token");
            response.setContentType("application/json");

            // Write the response body
            ObjectMapper mapper = new ObjectMapper();
            PrintWriter out = response.getWriter();
            mapper.writeValue(out, responseBody);
            out.flush();
            out.close();
            return;
        }
        // If username is not null and hasn't been authenticated
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null ){
            // Get User Details from DB
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if (jwtService.isTokenValid(token, userDetails)){
                System.out.println(userDetails);

                // If token is valid -> update SecurityContextHolder
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Send to next filter chain
        filterChain.doFilter(request, response);
    }
}
