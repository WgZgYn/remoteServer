package org.scu301.remoteserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class RemoteServerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RemoteServerApplication.class, args);
	}
}
