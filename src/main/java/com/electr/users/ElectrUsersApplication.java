package com.electr.users;

import com.electr.users.core.FileStorageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageConfiguration.class
})
@EnableEurekaClient
public class ElectrUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectrUsersApplication.class, args);
    }

}
