package net.nlacombe.nlacombenetws.features.markdownpages;

import com.vladsch.flexmark.ast.Heading;
import com.vladsch.flexmark.util.ast.Document;
import com.vladsch.flexmark.util.ast.NodeVisitor;
import com.vladsch.flexmark.util.ast.VisitHandler;

public class MarkdownHeadingFinder {

    private Heading headingLevel1;

    public Heading getHeadingLevel1(Document document) {
        headingLevel1 = null;

        var visitor = new NodeVisitor(new VisitHandler<>(Heading.class, heading -> {
            if (heading.getLevel() != 1)
                return;

            if (headingLevel1 != null)
                throw new RuntimeException("Markdown page contains more than one level 1 heading.");

            headingLevel1 = heading;
        }));

        visitor.visit(document);

        return headingLevel1;
    }
}
