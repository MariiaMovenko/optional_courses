package ua.nure.movenko.summaryTask4.security;

import ua.nure.movenko.summaryTask4.enums.Role;

public interface Authorization {
    /**
     * Computes if user with particular {@code role} is allowed to request defined action by defined {@code path}
     *
     * @param path is path to action that user requests for executing
     * @param role is extracted from {@code HttpSession theSession} information on user role in the system
     * @return {@code int} value that corresponds to one of authorization conditions defined in {@code AuthorizationValues class}
     */
    int isAuthorize(String path, Role role);

}
