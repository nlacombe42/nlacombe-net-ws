package net.nlacombe.nlacombenetws.features.markdownpages;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class MarkdownPageService {

    private static final String MARKDOWN_PAGES_CLASSPATH_FOLDER = "/markdownPages/";
    private static final Pattern MARKDOWN_PAGE_NAME_FORMAT_REGX = Pattern.compile("^(\\d+)_([a-zA-Z0-9\\-]+)\\.md$");

    private final Parser markdownParser;
    private final HtmlRenderer markdownHtmlRenderer;
    private final PathMatchingResourcePatternResolver resourceResolver;
    private final List<MarkdownPage> rootPages;

    public MarkdownPageService() {
        markdownParser = Parser.builder().build();
        markdownHtmlRenderer = HtmlRenderer.builder().build();
        resourceResolver = new PathMatchingResourcePatternResolver();
        rootPages = readRootPages();
    }

    public boolean pageWithRelativeUriExists(String relativeUri) {
        validateRelativeUri(relativeUri);

        if (!isUriToRootPage(relativeUri))
            return false;

        var pageSlug = getPageSlugFromRelativeUri(relativeUri);

        return rootPages.stream().anyMatch(page -> page.getSlug().equals(pageSlug));
    }

    public MarkdownPage getPage(String relativeUri) {
        var pageNotFoundException = new RuntimeException("Markdown page does not exists for URI: " + relativeUri);

        validateRelativeUri(relativeUri);

        if (!isUriToRootPage(relativeUri))
            throw new RuntimeException("Getting non root markdown pages is not supported yet.");

        if (!pageWithRelativeUriExists(relativeUri))
            throw pageNotFoundException;

        var pageSlug = getPageSlugFromRelativeUri(relativeUri);

        return rootPages.stream()
            .filter(page -> page.getSlug().equals(pageSlug))
            .findAny().orElseThrow(() -> pageNotFoundException);
    }

    public List<MarkdownPage> getRootPages() {
        return rootPages;
    }

    private List<MarkdownPage> readRootPages() {
        try {
            var resources = resourceResolver.getResources(MARKDOWN_PAGES_CLASSPATH_FOLDER + "*");

            return Arrays.stream(resources)
                .filter(resource -> resource.getFilename() != null)
                .filter(resource -> MARKDOWN_PAGE_NAME_FORMAT_REGX.matcher(resource.getFilename()).matches())
                .map(this::getMarkdownPage)
                .sorted(Comparator.comparing(MarkdownPage::getOrder))
                .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException("Error getting root markdown pages.", e);
        }
    }

    private MarkdownPage getMarkdownPage(Resource resource) {
        if (resource == null || resource.getFilename() == null)
            throw new IllegalArgumentException("resource must not be null and must have a filename");

        var matcher = MARKDOWN_PAGE_NAME_FORMAT_REGX.matcher(resource.getFilename());

        if (!matcher.matches())
            throw new IllegalArgumentException("resource must have a file name that follows the markdown page filename pattern. Filename provided: " + resource.getFilename());

        try {
            var order = Integer.parseInt(matcher.group(1));
            var pageSlug = matcher.group(2);
            var resourceInputStream = resource.getInputStream();

            return getMarkdownPage(resourceInputStream, pageSlug, order);
        } catch (IOException e) {
            throw new RuntimeException("Error getting markdown page.", e);
        }
    }

    private MarkdownPage getMarkdownPage(InputStream resourceInputStream, String pageSlug, int order) {
        try {
            var markdownDocument = markdownParser.parseReader(new InputStreamReader(resourceInputStream));
            var pageTitle = getPageTitle(markdownDocument);

            return new MarkdownPage(pageTitle, pageSlug, order, () -> markdownHtmlRenderer.render(markdownDocument));
        } catch (IOException e) {
            throw new RuntimeException("Error getting markdown page.", e);
        }
    }

    private String getPageSlugFromRelativeUri(String relativeUri) {
        return relativeUri.substring(1);
    }

    private boolean isUriToRootPage(String relativeUri) {
        return !relativeUri.substring(1).contains("/");
    }

    private void validateRelativeUri(String relativeUri) {
        if (!StringUtils.startsWith(relativeUri, "/"))
            throw new IllegalArgumentException("uri must be a relative uri: " + relativeUri);
    }

    private String getPageTitle(Document markdownDocument) {
        var headingLevel1 = new MarkdownHeadingFinder().getHeadingLevel1(markdownDocument);
        return headingLevel1.getText().toString();
    }
}
