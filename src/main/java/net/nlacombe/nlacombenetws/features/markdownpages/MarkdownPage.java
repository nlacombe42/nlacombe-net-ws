package net.nlacombe.nlacombenetws.features.markdownpages;

import java.util.function.Supplier;

public class MarkdownPage {

    private final String title;
    private final String slug;
    private final int order;
    private final Supplier<String> markdownContentHtmlSupplier;
    private String markdownContentHtml;

    public MarkdownPage(String title, String slug, int order, Supplier<String> markdownContentHtmlSupplier) {
        this.title = title;
        this.slug = slug;
        this.order = order;
        this.markdownContentHtmlSupplier = markdownContentHtmlSupplier;
    }

    public String getTitle() {
        return title;
    }

    public String getSlug() {
        return slug;
    }

    public int getOrder() {
        return order;
    }

    public String getMarkdownContentHtml() {
        if (markdownContentHtml == null)
            markdownContentHtml = markdownContentHtmlSupplier.get();

        return markdownContentHtml;
    }
}
