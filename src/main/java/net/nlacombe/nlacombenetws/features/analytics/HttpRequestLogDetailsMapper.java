package net.nlacombe.nlacombenetws.features.analytics;

import net.nlacombe.nlacombenetws.features.httplog.HttpRequestLogEntity;
import net.nlacombe.nlacombenetws.shared.ApplicationConstants;
import net.nlacombe.nlacombenetws.shared.BeanMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class HttpRequestLogDetailsMapper extends BeanMapper<HttpRequestLogDetailsDto, HttpRequestLogEntity> {

    public HttpRequestLogDetailsMapper() {
        super(HttpRequestLogDetailsDto.class, HttpRequestLogEntity.class);
    }

    @Override
    public HttpRequestLogDetailsDto mapToDto(HttpRequestLogEntity entity) {
        var dto = super.mapToDto(entity);
        dto.setCreatedAtDatetime(entity.getCreatedAtTimestamp().atZone(ApplicationConstants.DEFAULT_USER_ZONE));
        dto.setHost(entity.getHeaderWithOnlyOneValue("host"));
        dto.setRequestHeaders(toHeadersMapWithOnlyFirstValue(entity.getHeadersMap()));

        return dto;
    }

    private Map<String, String> toHeadersMapWithOnlyFirstValue(Map<String, List<String>> headersMap) {
        var headersWithOnlyFirstValue = new HashMap<String, String>();

        headersMap.forEach((key, value) -> headersWithOnlyFirstValue.put(key, value.iterator().next()));

        return headersWithOnlyFirstValue;
    }
}
