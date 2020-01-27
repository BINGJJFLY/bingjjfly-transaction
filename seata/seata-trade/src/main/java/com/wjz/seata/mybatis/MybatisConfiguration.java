package com.wjz.seata.mybatis;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;

import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;

@Configuration
@Import(DruidDataSourceAutoConfigure.class)
@MapperScan(value = "com.wjz.seata.mapper", sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfiguration {

    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy, MybatisProperties mybatisProperties) throws Exception {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
        factoryBean.setDataSource(dataSourceProxy);
        factoryBean.setMapperLocations(resolver.getResources(mybatisProperties.getMapperLocations()[0]));
        factoryBean.setConfigLocation(resolver.getResource(mybatisProperties.getConfigLocation()));
        factoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
        return factoryBean.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DataSourceProxy dataSourceProxy) {
        return new DataSourceTransactionManager(dataSourceProxy);
    }
    
    @Bean
    public DataSourceProxy dataSourceProxy(DataSource dataSource) {
    	return new DataSourceProxy(dataSource);
    }
    
    @Bean
    public GlobalTransactionScanner globalTransactionScanner() {
        return new GlobalTransactionScanner("trade-gts-seata", "my_test_tx_group");
    }
}
