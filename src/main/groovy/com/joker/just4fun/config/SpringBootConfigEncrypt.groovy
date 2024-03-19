package com.joker.just4fun.config

import org.jasypt.encryption.StringEncryptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.context.ApplicationContext
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

/**
 *
 * 配置项参数加密
 *
 * @author Joker.Y
 * @since 2024/3/1
 * @version 1.0
 */
@Component
class SpringBootConfigEncrypt implements CommandLineRunner {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private StringEncryptor encryptor;

    @Override
    void run(String... args) throws Exception {
//        def envConf = context.getBean(Environment.class)
//        def mysqlPwd = envConf.getProperty('spring.datasource.password')
//        def redisPwd = envConf.getProperty('spring.data.redis.password')
//        def mysqlEncPwd = encryptor.encrypt(mysqlPwd)
//        def redisEncPwd = encryptor.encrypt(redisPwd)
//        println "==========mysql pwd：${mysqlEncPwd}"
//        println "==========redis pwd：${redisEncPwd}"
    }

    private String encrypt(String sourceStr){
        return encryptor.encrypt(sourceStr)
    }

    private String decrypt(String encStr){
        return encryptor.decrypt(encStr)
    }
}
