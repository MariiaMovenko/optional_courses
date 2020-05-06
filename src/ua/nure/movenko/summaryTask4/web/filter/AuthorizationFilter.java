package ua.nure.movenko.summaryTask4.web.filter;

import ua.nure.movenko.summaryTask4.constants.Params;
import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.security.Authorization;
import ua.nure.movenko.summaryTask4.security.AuthorizationValues;
import ua.nure.movenko.summaryTask4.services.user.UserService;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Security filter.
 *
 * @author M.Movenko
 *
 */
public class AuthorizationFilter extends MainFilter {

    private Authorization authorizationMap;

    private final Map<Integer, Integer> statusCodes = new HashMap<>();

    private final List<String> patternsToSkip = new ArrayList<>() {{
        add(".*\\.css");
        add(".*\\.js");
        add(".*\\.png");
        add(".*\\.jpg");
    }};

    @Override
    public void init(FilterConfig fConfig) {
        authorizationMap = (Authorization) fConfig.getServletContext().getAttribute("authorization");
        statusCodes.put(AuthorizationValues.NOT_FOUND, HttpServletResponse.SC_NOT_FOUND);
        statusCodes.put(AuthorizationValues.UNAUTHORIZED, HttpServletResponse.SC_UNAUTHORIZED);
        statusCodes.put(AuthorizationValues.FORBIDDEN, HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = request.getServletPath();
        boolean shouldExclude = patternsToSkip.stream()
                .anyMatch(path::matches);
        if (shouldExclude) {
            chain.doFilter(request, response);
            return;
        }
        Role role = Optional.ofNullable(request.getSession())
                .map(session -> (User) session.getAttribute(Params.USER_INFO))
                .map(User::getRole)
                .orElse(Role.GUEST);

        int authValue = authorizationMap.isAuthorize(path, role);
        Integer statusCode = statusCodes.get(authValue);
        if (statusCode != null) {
            response.sendError(statusCode);
            return;
        }
        chain.doFilter(request, response);
    }

}
