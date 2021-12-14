//package com.bsuir.config;
//
//import com.mongodb.client.MongoClients;
//import de.flapdoodle.embed.mongo.MongodStarter;
//import de.flapdoodle.embed.mongo.config.IMongodConfig;
//import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
//import de.flapdoodle.embed.mongo.config.Net;
//import de.flapdoodle.embed.mongo.distribution.Version;
//import de.flapdoodle.embed.process.runtime.Network;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.data.mongodb.core.MongoTemplate;
//import org.springframework.util.SocketUtils;
//
//import java.io.IOException;
//
//@Configuration
//public class MongoConfig {
//
//    @Bean
//    @Primary
//    public MongoTemplate mongoTemplate1() throws IOException {
//        String ip = "localhost";
//        int randomPort = SocketUtils.findAvailableTcpPort();
//
//        IMongodConfig mongodConfig = new MongodConfigBuilder().version(Version.Main.PRODUCTION)
//                .net(new Net(ip, randomPort, Network.localhostIsIPv6()))
//                .build();
//
//        MongodStarter starter = MongodStarter.getDefaultInstance();
//        mongodExecutable = starter.prepare(mongodConfig);
//        mongodExecutable.start();
//        return new MongoTemplate(MongoClients.create(String.format(MONGO_DB_URL, ip, randomPort)),MONGO_DB_NAME);
//    }
//
//}
