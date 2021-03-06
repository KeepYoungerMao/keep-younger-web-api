package com.mao.config;

import com.mao.Main;
import com.mao.util.SecretUtil;
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
    private static final String PASSWORD = "0C63AB7CF5178804AB4166D919F3AF83";
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
        properties.setProperty("password", SecretUtil.decrypt(PASSWORD,Main.key,SecretUtil.SecretEnum.AES));
        PooledDataSourceFactory factory = new PooledDataSourceFactory();
        factory.setProperties(properties);
        DataSource dataSource = factory.getDataSource();
        TransactionFactory transactionFactory = new JdbcTransactionFactory();
        Environment environment = new Environment(ENVIRONMENT_ID,transactionFactory,dataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMappers(MAPPER_PACKAGE);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
    }

    /**
     * 从session工厂中获取session
     * 默认不自动提交
     * @return SqlSession
     */
    public static SqlSession getSession(){
        return getSession(false);
    }

    /**
     * 从session工厂中获取session
     * 当执行insert、update、delete或建表、建索引语句的时候可使用此方法
     * 并设置自动提交，如果不自动提交，数据并不会保存到数据库中
     * @param autoCommit 是否自动提交
     * @return SqlSession
     */
    public static SqlSession getSession(boolean autoCommit){
        return sqlSessionFactory.openSession(autoCommit);
    }

}
