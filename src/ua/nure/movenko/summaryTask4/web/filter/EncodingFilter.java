package ua.nure.movenko.summaryTask4.web.filter;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Encoding filter.
 *
 * @author M.Movenko
 */
public class EncodingFilter extends MainFilter {

    private String encoding;

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String requestEncoding = request.getCharacterEncoding();
        if (requestEncoding == null) {
            request.setCharacterEncoding(encoding);
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) {
        encoding = filterConfig.getInitParameter("encoding");
    }
}
