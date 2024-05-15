package com.liu.system.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Description:
 *
 * @author 杰
 * @version 1.0
 * @since 2024/05/12 19:53
 */
@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = {"com.liu.db.**.repository"})
@EnableTransactionManagement
public class MongoDBConfig {
}
