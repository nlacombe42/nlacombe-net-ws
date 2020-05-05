package net.nlacombe.nlacombenetws.httplog;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HttpRequestLogJpaRepository extends JpaRepository<HttpRequestLogEntity, Integer> {
}
