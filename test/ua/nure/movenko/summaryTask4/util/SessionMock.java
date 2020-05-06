package ua.nure.movenko.summaryTask4.util;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import java.util.Enumeration;
import java.util.Map;

public class SessionMock implements HttpSession {

    private final Map<String, Object> attrMap;

    public SessionMock(Map<String, Object> attrMap) {
        this.attrMap = attrMap;
    }

    @Override
    public long getCreationTime() {
        return 30;
    }

    @Override
    public String getId() {
        return "100";
    }

    @Override
    public long getLastAccessedTime() {
        return 12;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public void setMaxInactiveInterval(int i) {

    }

    @Override
    public int getMaxInactiveInterval() {
        return 0;
    }

    @Override
    public HttpSessionContext getSessionContext() {
        return null;
    }

    @Override
    public Object getAttribute(String s) {
        return attrMap.get(s);
    }

    @Override
    public Object getValue(String s) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }

    @Override
    public String[] getValueNames() {
        return new String[0];
    }

    @Override
    public void setAttribute(String s, Object o) {
        attrMap.put(s, o);
    }

    @Override
    public void putValue(String s, Object o) {

    }

    @Override
    public void removeAttribute(String s) {
        attrMap.remove(s);

    }

    @Override
    public void removeValue(String s) {
    }

    @Override
    public void invalidate() {

    }

    @Override
    public boolean isNew() {
        return false;
    }
}
