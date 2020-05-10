package net.nlacombe.nlacombenetws;

import net.nlacombe.nlacombenetws.httplog.HttpRequestLogEntity;
import net.nlacombe.nlacombenetws.httplog.HttpRequestLogJpaRepository;
import net.nlacombe.nlacombenetws.security.AuthorizationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping("/analytics")
public class AnalyticsWebService {

    private static final ZoneId DEFAULT_USER_ZONE = ZoneId.of("America/Toronto");

    private HttpRequestLogJpaRepository httpRequestLogJpaRepository;

    @Inject
    public AnalyticsWebService(HttpRequestLogJpaRepository httpRequestLogJpaRepository) {
        this.httpRequestLogJpaRepository = httpRequestLogJpaRepository;
    }

    @RequestMapping("/httpRequestLogs")
    public ResponseEntity<String> get() {
        AuthorizationUtil.validateUserIsNicolasLacombeSuperAdmin();

        var responseText = httpRequestLogJpaRepository.findAll().stream()
            .sorted(Comparator.comparing(HttpRequestLogEntity::getCreatedAtTimestamp).reversed())
            .map(httpRequestLogEntity ->
                httpRequestLogEntity.getCreatedAtTimestamp().atZone(DEFAULT_USER_ZONE) + " " +
                httpRequestLogEntity.getMethod() + " " + httpRequestLogEntity.getUri() + " " + httpRequestLogEntity.getResponseStatus())
            .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(responseText, HttpStatus.OK);
    }
}
