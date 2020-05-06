package ua.nure.movenko.summaryTask4.tag;

import ua.nure.movenko.summaryTask4.entities.User;
import ua.nure.movenko.summaryTask4.enums.Role;
import ua.nure.movenko.summaryTask4.security.Authorization;
import ua.nure.movenko.summaryTask4.security.AuthorizationValues;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.ConditionalTagSupport;

/**
 * This class contains the logic of custom tag named <>IfAuth</>
 *
 * @author M.Movenko.
 */
public class IfAuthorization extends ConditionalTagSupport {

    private String path;

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Defines if the particular user is allowed to request using defined {@code path}
     *
     * @return {@code true} if user is allowed to request using defined {@code path}
     */
    @Override
    protected boolean condition() {
        Authorization map = (Authorization) pageContext.getServletContext().getAttribute("authorization");
        HttpServletRequest theRequest = (HttpServletRequest) pageContext.getRequest();
        HttpSession theSession = theRequest.getSession();
        User theUser = (User) (theSession != null ? theSession.getAttribute("userInfo") : null);
        Role theRole = theUser == null ? Role.GUEST : theUser.getRole();

        return map.isAuthorize(path, theRole) == AuthorizationValues.ALLOWED;
    }

}
