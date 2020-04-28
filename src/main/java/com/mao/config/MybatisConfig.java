package com.mao.config;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * mybatis配置
 * 使用非XML方式配置
 * 非XML方式配置时，可以使用注解方式编写SQL，
 * 如果想要使用XML方式编写的话，mapper.xml需要与mapper.class同名、同路径
 * 详细配置参见 [mybatis文档](https://mybatis.org/mybatis-3/zh/getting-started.html)
 * @author zongx at 2020/4/7 22:03
 */
public class MybatisConfig {

    //====================================参数配置================================================//
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/keep_younger";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static final String MAPPER_PACKAGE = "com.mao.mapper";
    //====================================参数配置================================================//

    private static final String URL_PARAM = "?characterEncoding=utf-8&serverTimezone=Asia/Shanghai";
    private static final String ENVIRONMENT_ID = "development";
    private static final SqlSessionFactory sqlSessionFactory;

    static {
        Properties properties = new Properties();
        properties.setProperty("driver",DRIVER);
        properties.setProperty("url",URL+URL_PARAM);
        properties.setProperty("username",USERNAME);
        properties.setProperty("password",PASSWORD);
        PooledDataSourceFactory factory = new PooledDataSourceFactory();
        factory.setProperties(properties);
        DataSource dataSource = factory.getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment(ENVIRONMENT_ID,transactionFactory,dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMappers(MAPPER_PACKAGE);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    public static SqlSession getSession(){
        return sqlSessionFactory.openSession();
    }

}
