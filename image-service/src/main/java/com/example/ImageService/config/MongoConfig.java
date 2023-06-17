package com.example.ImageService.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.util.Collection;
import java.util.Collections;

@Configuration
public class MongoConfig extends AbstractMongoClientConfiguration{
    @Value("${spring.data.mongodb.username}")
    private String username;
    @Value("${spring.data.mongodb.password}")
    private String password;
    @Value("${spring.data.mongodb.database}")
    private String database;
    @Value("${spring.data.mongodb.port}")
    private String port;
    @Value("${spring.data.mongodb.host}")
    private String host;
    @Value("${spring.data.mongodb.authentication-database}")
    private String authenticationDB;
    @Override
    public MongoClient mongoClient() {
        String mongoUriString = String.format("mongodb://%s:%s@%s:%s/%s?authSource=%s",
                username, password, host, port, database, authenticationDB);
        ConnectionString connectionString =
                new ConnectionString(mongoUriString);
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();

        return MongoClients.create(mongoClientSettings);
    }
    @Bean
    public GridFsTemplate gridFsTemplate() throws ClassNotFoundException {
        return new GridFsTemplate(mongoDbFactory(), super.mappingMongoConverter(mongoDbFactory(),
                                this.customConversions(),
                                this.mongoMappingContext(this.customConversions(), this.mongoManagedTypes())));
    }

    @Override
    protected String getDatabaseName() {
        return database;
    }
}