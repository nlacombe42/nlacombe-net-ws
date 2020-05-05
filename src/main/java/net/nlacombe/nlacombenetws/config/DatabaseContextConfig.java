package net.nlacombe.nlacombenetws.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseContextConfig
{
	@Bean
	public Flyway flyway(DataSource datasource)
	{
        var flyway = Flyway.configure()
            .dataSource(datasource)
            .load();
        flyway.migrate();

		return flyway;
	}
}
