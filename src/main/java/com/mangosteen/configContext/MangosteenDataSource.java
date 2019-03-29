package com.mangosteen.configContext;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages="com.mangosteen.dao",sqlSessionFactoryRef="mangosteenSqlSessionFactory")
public class MangosteenDataSource {

    @Bean(name = "mangosteenMysqlDataSource")
    @ConfigurationProperties("datasource")
    public DataSource initDataSouurce(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setTimeBetweenConnectErrorMillis(300000);
        dataSource.setMinEvictableIdleTimeMillis(600000);
        return dataSource;
    }

    @Bean(name="mangosteenSqlSessionFactory")
    public SqlSessionFactoryBean createSqlSessionFactoryBean(@Qualifier("mangosteenMysqlDataSource") DataSource mangosteenDataSource){
        SqlSessionFactoryBean sqlSessionFactoryBean=new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(mangosteenDataSource);
        return sqlSessionFactoryBean;
    }

}
