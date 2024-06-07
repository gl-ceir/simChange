package com.gl.eirs.simchange;

import com.gl.eirs.simchange.service.impl.MainService;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class SimChangeApplication implements CommandLineRunner {

	@Autowired
	MainService mainService;

	public static void main(String[] args) {
		SpringApplication.run(SimChangeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mainService.simChangeProcess();
	}
}
