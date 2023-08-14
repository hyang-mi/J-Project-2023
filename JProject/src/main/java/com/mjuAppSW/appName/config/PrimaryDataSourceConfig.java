package com.mjuAppSW.appName.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.mjuAppSW.appName.domain",
        entityManagerFactoryRef = "primaryEntityManagerFactory",
        transactionManagerRef = "primaryTransactionManager")
public class PrimaryDataSourceConfig {

    @Primary
    @Bean(name = "primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "primaryEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean primaryEntityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(primaryDataSource());
        em.setPackagesToScan(new String[] {"com.mjuAppSW.appName.domain"});
        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, String> properties = new HashMap<String, String>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.hbm2ddl.auto", "create");

        em.setJpaPropertyMap(properties);
        return em;
    }

    @Primary
    @Bean(name = "primaryTransactionManager")
    PlatformTransactionManager primaryTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(primaryEntityManagerFactory().getObject());
        return transactionManager;
    }
}