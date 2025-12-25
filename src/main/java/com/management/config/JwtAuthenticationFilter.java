package com.management.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = getJwtFromRequest(request);

            // LOG 1: Verificar se o token foi extraído do Header
            System.out.println("--- INÍCIO DA VALIDAÇÃO DO TOKEN ---");
            System.out.println("Token extraído: " + (jwt != null ? jwt : "NULO - Header Authorization não encontrado ou sem 'Bearer '"));

            if (StringUtils.hasText(jwt)) {
                boolean isValid = tokenProvider.validateToken(jwt);

                // LOG 2: Verificar se a assinatura/validade do token passou
                System.out.println("Token é válido pela assinatura? " + isValid);

                if (isValid) {
                    String userEmail = tokenProvider.getEmailFromJWT(jwt);
                    System.out.println("E-mail extraído do Token: " + userEmail);
                    UserDetails userDetails = customUserDetailsService.loadUserByUsername(userEmail);
                    System.out.println("Usuário carregado do banco: " + userDetails.getUsername());
                    System.out.println("Roles/Permissões: " + userDetails.getAuthorities());

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("SUCESSO: Autenticação definida no contexto de segurança.");
                } else {
                    System.out.println("ERRO: O token foi enviado, mas a validação (assinatura ou expiração) falhou.");
                }
            }
            System.out.println("--- FIM DA VALIDAÇÃO ---");

        } catch (Exception ex) {
            logger.error("Falha ao configurar a autenticação", ex);
            System.out.println("EXCEÇÃO: " + ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
