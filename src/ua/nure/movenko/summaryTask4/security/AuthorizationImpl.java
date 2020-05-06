package ua.nure.movenko.summaryTask4.security;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ua.nure.movenko.summaryTask4.enums.Role;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * AuthorizationImpl based  on implementation of the {@code Authorization} interface.  This
 * implementation provides parsing of xml file  to the {@code authorizationMap} and Computes
 * if  particular user  is allowed to make some requests based on the created {@code authorizationMap}.
 *
 * @ author M.Movenko
 */
public class AuthorizationImpl implements Authorization {

    private static final String PATH = "path";
    private static final String NAME = "name";
    private static final String ACTION = "action";
    private static final String ROLE = "role";

    private final Map<String, Set<Role>> authorizationMap = new HashMap<>();

    private AuthorizationImpl() {
    }

    /**
     * Computes if user with particular {@code role} is allowed to request defined action by defined {@code path}
     *
     * @param path is path to action that user requests for executing
     * @param role is extracted from {@code HttpSession theSession} information on user role in the system
     * @return {@code int} value that corresponds to one of authorization conditions defined in {@code AuthorizationValues class}
     */
    @Override
    public int isAuthorize(String path, Role role) {
        Set<Role> theRoleSet = authorizationMap.get(path);
        if (theRoleSet == null) {
            return AuthorizationValues.NOT_FOUND;
        }
        if (theRoleSet.contains(role)) {
            return AuthorizationValues.ALLOWED;
        } else if (role == Role.GUEST) {
            return AuthorizationValues.UNAUTHORIZED;
        }
        return AuthorizationValues.FORBIDDEN;
    }

    /**
     * Returns new instance of AuthorizationImpl object with fulfilled {@code authorizationMap}
     *
     * @param xml is xml file to be parsed
     * @throws ParserConfigurationException when a serious configuration error was occurred during executing
     * @throws IOException                  if I/O operation was failed or interrupted
     * @throws SAXException                 if some errors were occurred during parsing the xml document
     */
    public static AuthorizationImpl newInstance(File xml) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory theFactory = DocumentBuilderFactory.newInstance();

        DocumentBuilder theBuilder = theFactory.newDocumentBuilder();

        Document theDocument = theBuilder.parse(xml);

        Element theRoot = theDocument.getDocumentElement();

        NodeList actionList = theRoot.getElementsByTagName(ACTION);
        AuthorizationImpl authorizationImpl = new AuthorizationImpl();
        for (int i = 0; i < actionList.getLength(); i++) {
            authorizationImpl.parseAction(actionList.item(i));
        }
        return authorizationImpl;
    }

    private void parseAction(Node action) {
        String thePath = ((Element) action).getAttribute(PATH);
        Set<Role> theRoleSet = authorizationMap.computeIfAbsent(thePath, k -> new HashSet<>());

        NodeList roleList = ((Element) action).getElementsByTagName(ROLE);
        for (int i = 0; i < roleList.getLength(); i++) {
            Element node = (Element) roleList.item(i);
            String r = node.getAttribute(NAME).toUpperCase();
            Role theRole = Role.valueOf(r);
            theRoleSet.add(theRole);
        }
    }

    @Override
    public String toString() {
        return authorizationMap.toString();
    }

}
