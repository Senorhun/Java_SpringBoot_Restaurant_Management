package com.example.restaurant;

import com.example.restaurant.model.AppUser;
import com.example.restaurant.model.AppUserRole;
import com.example.restaurant.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class RestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantApplication.class, args);
	}
	@Bean
	CommandLineRunner init(AppUserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("admin@test.com").isEmpty()) {
				AppUser admin = new AppUser();
				admin.setEmail("admin@test.com");
				admin.setPassword(encoder.encode("this-is-a-very-long-secret-key-for-jwt-token"));
				admin.setAppUserRole(AppUserRole.ADMIN);
				repo.save(admin);
			}
		};
	}

}
