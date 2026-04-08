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
	CommandLineRunner initAdmin(AppUserRepository repo, PasswordEncoder encoder) {
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
	@Bean
	CommandLineRunner initStaff(AppUserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("staff@test.com").isEmpty()) {
				AppUser staff = new AppUser();
				staff.setEmail("staff@test.com");
				staff.setPassword(encoder.encode("this-is-a-very-long-secret-key-for-jwt-token"));
				staff.setAppUserRole(AppUserRole.STAFF);
				repo.save(staff);
			}
		};
	}
	@Bean
	CommandLineRunner initManager(AppUserRepository repo, PasswordEncoder encoder) {
		return args -> {
			if (repo.findByEmail("manager@test.com").isEmpty()) {
				AppUser manager = new AppUser();
				manager.setEmail("manager@test.com");
				manager.setPassword(encoder.encode("this-is-a-very-long-secret-key-for-jwt-token"));
				manager.setAppUserRole(AppUserRole.MANAGER);
				repo.save(manager);
			}
		};
	}

}
