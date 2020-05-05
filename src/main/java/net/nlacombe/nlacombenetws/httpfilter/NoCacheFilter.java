package net.nlacombe.nlacombenetws.httpfilter;

import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class NoCacheFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpServletResponse = (HttpServletResponse) servletResponse;

        httpServletResponse.setHeader("Cache-Control", "no-store");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
