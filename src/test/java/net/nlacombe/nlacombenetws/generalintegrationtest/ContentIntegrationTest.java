package net.nlacombe.nlacombenetws.generalintegrationtest;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.nlacombe.nlacombenetws.test.TestConfig;
import net.nlacombe.nlacombenetws.test.TestHttpClient;
import org.jsoup.Jsoup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, args = "--spring.config.location=" + TestConfig.TESTED_APPLICATION_SPRING_CONFIG_FILE_PATH)
@TestPropertySource(locations = "file:" + TestConfig.TEST_APPLICATION_CONFIG_FILE_PATH)
public class ContentIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ContentIntegrationTest.class);

    @Inject
    private Environment environment;

    @LocalServerPort
    private int applicationPort;

    private TestHttpClient testHttpClient;

    @BeforeEach
    public void init() {
        var testConfig = new TestConfig(environment);

        testHttpClient = new TestHttpClient(testConfig, applicationPort, new ObjectMapper());
    }

    @Test
    public void should_not_have_any_broken_links() {
        var uriPrefixesToIgnore = Set.of("https://www.linkedin.com");
        var brokenLinks = getBrokenLinks(null, List.of("/summary"), new HashSet<>(), uriPrefixesToIgnore);

        assertThat(brokenLinks).isEmpty();
    }

    private List<BrokenLink> getBrokenLinks(String originalPageUri, List<String> uris, Set<String> alreadyCheckedPages, Set<String> uriPrefixesToIgnore) {
        return uris.stream().flatMap(uri -> {
            if (alreadyCheckedPages.contains(uri) || uriPrefixesToIgnore.stream().anyMatch(uri::startsWith))
                return Stream.empty();

            logger.debug("getting uri to test for broken links: " + uri);

            var response = testHttpClient.getResourceResponse(uri);

            if (response.statusCode() < 200 || response.statusCode() > 299)
                return Stream.of(new BrokenLink(originalPageUri, uri, response.statusCode()));

            alreadyCheckedPages.add(uri);

            try {
                if (uri.startsWith("https://nlacombe.net/") || uri.startsWith("/")) {
                    var linkUris = Jsoup.parse(response.body(), StandardCharsets.UTF_8.name(), "/")
                        .select("a")
                        .eachAttr("href");

                    return getBrokenLinks(uri, linkUris, alreadyCheckedPages, uriPrefixesToIgnore).stream();
                } else
                    return Stream.empty();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }
        }).collect(Collectors.toList());
    }
}

class BrokenLink {
    private final String pageUri;
    private final String linkTargetUri;
    private final Integer linkTargetHttpStatus;

    public BrokenLink(String pageUri, String linkTargetUri, Integer linkTargetHttpStatus) {
        this.pageUri = pageUri;
        this.linkTargetUri = linkTargetUri;
        this.linkTargetHttpStatus = linkTargetHttpStatus;
    }

    @Override
    public String toString() {
        return "BrokenLink{" +
            "pageUri='" + pageUri + '\'' +
            ", linkTargetUri='" + linkTargetUri + '\'' +
            ", linkTargetHttpStatus=" + linkTargetHttpStatus +
            '}';
    }

    public String getPageUri() {
        return pageUri;
    }

    public String getLinkTargetUri() {
        return linkTargetUri;
    }

    public Integer getLinkTargetHttpStatus() {
        return linkTargetHttpStatus;
    }
}
