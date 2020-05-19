package net.nlacombe.nlacombenetws.httpfilter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(HttpFilterOrder.HSTS)
public class HstsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpServletResponse = (HttpServletResponse) servletResponse;

        httpServletResponse.setHeader("Strict-Transport-Security", "max-age=15768000; includeSubDomains");

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
