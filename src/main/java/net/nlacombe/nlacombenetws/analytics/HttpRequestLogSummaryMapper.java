package net.nlacombe.nlacombenetws.analytics;

import net.nlacombe.nlacombenetws.ApplicationConstants;
import net.nlacombe.nlacombenetws.httplog.HttpRequestLogEntity;
import net.nlacombe.nlacombenetws.shared.BeanMapper;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestLogSummaryMapper extends BeanMapper<HttpRequestLogSummaryDto, HttpRequestLogEntity> {

    public HttpRequestLogSummaryMapper() {
        super(HttpRequestLogSummaryDto.class, HttpRequestLogEntity.class);
    }

    @Override
    public HttpRequestLogSummaryDto mapToDto(HttpRequestLogEntity entity) {
        var dto = super.mapToDto(entity);
        dto.setCreatedAtDatetime(entity.getCreatedAtTimestamp().atZone(ApplicationConstants.DEFAULT_USER_ZONE));
        dto.setHost(entity.getHeadersMap().get("host"));

        return dto;
    }
}
