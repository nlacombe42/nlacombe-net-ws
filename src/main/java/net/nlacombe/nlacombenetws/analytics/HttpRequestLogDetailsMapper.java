package net.nlacombe.nlacombenetws.analytics;

import net.nlacombe.nlacombenetws.shared.ApplicationConstants;
import net.nlacombe.nlacombenetws.httplog.HttpRequestLogEntity;
import net.nlacombe.nlacombenetws.shared.BeanMapper;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestLogDetailsMapper extends BeanMapper<HttpRequestLogDetailsDto, HttpRequestLogEntity> {

    public HttpRequestLogDetailsMapper() {
        super(HttpRequestLogDetailsDto.class, HttpRequestLogEntity.class);
    }

    @Override
    public HttpRequestLogDetailsDto mapToDto(HttpRequestLogEntity entity) {
        var dto = super.mapToDto(entity);
        dto.setCreatedAtDatetime(entity.getCreatedAtTimestamp().atZone(ApplicationConstants.DEFAULT_USER_ZONE));
        dto.setHost(entity.getHeadersMap().get("host"));
        dto.setRequestHeaders(entity.getHeadersMap());

        return dto;
    }
}
