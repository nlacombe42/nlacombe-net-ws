package net.nlacombe.nlacombenetws.features.markdownpages;

import net.nlacombe.nlacombenetws.httpfilter.HttpFilterOrder;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Order(HttpFilterOrder.MARKDOWN_PAGE)
public class MarkdownPagesFilter implements Filter {

    private final MarkdownPageService markdownPageService;

    public MarkdownPagesFilter(MarkdownPageService markdownPageService) {
        this.markdownPageService = markdownPageService;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        var httpServletRequest = (HttpServletRequest) servletRequest;
        var httpServletResponse = (HttpServletResponse) servletResponse;
        var requestUri = httpServletRequest.getRequestURI();
        var charset = StandardCharsets.UTF_8;

        if (markdownPageService.pageWithRelativeUriExists(requestUri)) {
            var markdownPage = markdownPageService.getPage(requestUri);

            httpServletResponse.setStatus(HttpStatus.OK.value());
            httpServletResponse.setContentType("text/html; charset=" + charset.name());

            var outputStream = httpServletResponse.getOutputStream();
            writeHtmlPage(markdownPage, outputStream, charset);
            outputStream.flush();
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private void writeHtmlPage(MarkdownPage markdownPage, OutputStream outputStream, Charset charset) {
        try {
            var rootPages = markdownPageService.getRootPages();
            var htmlHeader = getHtmlDocumentHeader(markdownPage.getTitle(), charset);
            var navigationMenu = getNavigationMenuHtml(rootPages);

            var htmlDocument = "<!DOCTYPE html>\n<html lang=\"en\">\n"
                + htmlHeader +
                "<body>\n<div class=\"wrapper\">\n"
                + navigationMenu
                + "<main>\n"
                + markdownPage.getMarkdownContentHtml()
                + "</main>\n</div>\n</body>\n</html>\n";

            outputStream.write(htmlDocument.getBytes(charset));
        } catch (IOException e) {
            throw new RuntimeException("Error writing html page to http response OutputStream", e);
        }
    }

    @NotNull
    private String getHtmlDocumentHeader(String pageTitle, Charset charset) {
        var htmlHeader = "<head>\n";
        htmlHeader += "<meta charset=\"" + charset.name() + "\">\n";
        htmlHeader += "<title>" + pageTitle + "</title>\n";
        htmlHeader += "<link rel=\"stylesheet\" type=\"text/css\" href=\"style.css\">\n";
        htmlHeader += "</head>\n";
        return htmlHeader;
    }

    @NotNull
    private String getNavigationMenuHtml(List<MarkdownPage> rootPages) {
        var navigationMenu = "<nav>\n";

        navigationMenu += rootPages.stream()
            .map((rootPage) -> "<a href=\"/" + rootPage.getSlug() + "\">" + rootPage.getTitle() + "</a>")
            .collect(Collectors.joining(" | "));

        navigationMenu += "</nav>\n";

        return navigationMenu;
    }
}
