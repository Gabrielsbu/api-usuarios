package com.electr.users;

import com.electr.users.core.FileStorageConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
        FileStorageConfiguration.class
})
public class ElectrUsersApplication {

    public static void main(String[] args) {
        SpringApplication.run(ElectrUsersApplication.class, args);
    }

}
