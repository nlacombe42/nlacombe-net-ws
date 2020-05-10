package net.nlacombe.nlacombenetws.httplog;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.stream.Stream;

public interface HttpRequestLogJpaRepository extends JpaRepository<HttpRequestLogEntity, Long> {

    Stream<HttpRequestLogEntity> findAllByOrderByCreatedAtTimestampDesc();

}
