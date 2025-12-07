package com.management.authorization.interceptor;

import com.management.authorization.service.AuthorizationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Component
public class FinancialAccessInterceptor implements HandlerInterceptor {

    @Autowired
    private AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String personIdHeader = request.getHeader("X-Mock-User-ID");
        if (personIdHeader == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
        Integer personId = Integer.parseInt(personIdHeader);

        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        String projectIdString = pathVariables.get("projectId");
        if (projectIdString == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return false;
        }
        Integer projectId = Integer.parseInt(projectIdString);

        if (!authorizationService.hasFinancialAccess(personId, projectId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return false;
        }

        return true;
    }
}
