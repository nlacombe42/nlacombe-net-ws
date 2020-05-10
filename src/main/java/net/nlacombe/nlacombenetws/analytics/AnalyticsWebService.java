package net.nlacombe.nlacombenetws.analytics;

import net.nlacombe.nlacombenetws.httplog.HttpRequestLogJpaRepository;
import net.nlacombe.nlacombenetws.security.AuthorizationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.transaction.Transactional;
import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@Transactional
@PreAuthorize("isAuthenticated()")
@RequestMapping("/analytics")
public class AnalyticsWebService {

    private final HttpRequestLogJpaRepository httpRequestLogJpaRepository;
    private final HttpRequestLogSummaryMapper httpRequestLogSummaryMapper;
    private final HttpRequestLogDetailsMapper httpRequestLogDetailsMapper;

    @Inject
    public AnalyticsWebService(HttpRequestLogJpaRepository httpRequestLogJpaRepository, HttpRequestLogSummaryMapper httpRequestLogSummaryMapper, HttpRequestLogDetailsMapper httpRequestLogDetailsMapper) {
        this.httpRequestLogJpaRepository = httpRequestLogJpaRepository;
        this.httpRequestLogSummaryMapper = httpRequestLogSummaryMapper;
        this.httpRequestLogDetailsMapper = httpRequestLogDetailsMapper;
    }

    @RequestMapping("/httpRequestLogs")
    public ResponseEntity<List<HttpRequestLogSummaryDto>> searchHttpRequestLogs(
        @RequestParam(defaultValue = "false") boolean noLocalhost,
        @RequestParam(defaultValue = "false") boolean no404
    ) {
        AuthorizationUtil.validateUserIsNicolasLacombeSuperAdmin();

        var httpRequestSumamries = httpRequestLogJpaRepository.findAllByOrderByCreatedAtTimestampDesc()
            .map(httpRequestLogSummaryMapper::mapToDto)
            .filter(requestDto -> !noLocalhost || !requestDto.getHost().startsWith("localhost"))
            .filter(requestDto -> !no404 || !Objects.equals(requestDto.getResponseStatus(), 404))
            .limit(1000)
            .collect(Collectors.toList());

        return new ResponseEntity<>(httpRequestSumamries, HttpStatus.OK);
    }

    @RequestMapping("/resourceListFromLoggedRequests")
    public ResponseEntity<List<String>> searchHttpRequestLogs() {
        AuthorizationUtil.validateUserIsNicolasLacombeSuperAdmin();

        var httpRequestSumamries = httpRequestLogJpaRepository.findAllByOrderByCreatedAtTimestampDesc()
            .map(httpRequestLogSummaryMapper::mapToDto)
            .filter(requestDto -> !requestDto.getHost().startsWith("localhost"))
            .filter(requestDto -> requestDto.getMethod().toLowerCase().equals("get"))
            .filter(requestDto -> {
                var status = requestDto.getResponseStatus();

                return status != null && status < 400 && status >= 200;
            })
            .map(requestDto -> URI.create(requestDto.getUri()).getPath())
            .distinct()
            .collect(Collectors.toList());

        return new ResponseEntity<>(httpRequestSumamries, HttpStatus.OK);
    }

    @RequestMapping("/httpRequestLogs/{httpRequestId}")
    public ResponseEntity<HttpRequestLogDetailsDto> getHttpRequestLogDetails(@PathVariable long httpRequestId) {
        AuthorizationUtil.validateUserIsNicolasLacombeSuperAdmin();

        var httpRequestLogEntity = httpRequestLogJpaRepository.getOne(httpRequestId);
        var httpRequestDetails = httpRequestLogDetailsMapper.mapToDto(httpRequestLogEntity);

        return new ResponseEntity<>(httpRequestDetails, HttpStatus.OK);
    }
}
