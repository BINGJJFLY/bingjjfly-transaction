package com.wjz.hmily.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.dromara.hmily.core.service.HmilyApplicationService;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
@MapperScan(value = "com.wjz.hmily.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfiguration {
	
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource, MybatisProperties mybatisProperties) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setMapperLocations(resolver.getResources(mybatisProperties.getMapperLocations()[0]));
        factoryBean.setConfigLocation(resolver.getResource(mybatisProperties.getConfigLocation()));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return factoryBean.getObject();
    }
    
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
    
    @Bean
    public HmilyApplicationService hmilyApplicationService() {
    	return () -> "hmily-finance";
    }
}
