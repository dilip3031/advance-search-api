package com.qburst.genericsearch;

import com.qburst.genericsearch.entitys.UsersEntity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GenericSearchApplication {

	public static void main(String[] args) {
		SpringApplication.run(GenericSearchApplication.class, args);
	}

	/*public static void main(String[] args) {
		System.out.println(UsersEntity.builder().email("dilip@gmail.com").userName("Dileep Kumar").phoneNumber("8800880088").build().toJson());
	}*/
}
