package com.example.restaurant.service;

import com.example.restaurant.dto.RestaurantCreateCommand;
import com.example.restaurant.dto.RestaurantInfo;
import com.example.restaurant.dto.TableCreateCommand;
import com.example.restaurant.dto.TableInfo;
import com.example.restaurant.exceptionhandling.RestaurantNameDuplicationException;
import com.example.restaurant.exceptionhandling.TableNumberDuplicationException;
import com.example.restaurant.model.Restaurant;
import com.example.restaurant.model.RestaurantTable;
import com.example.restaurant.model.TableStatus;
import com.example.restaurant.repository.RestaurantRepository;
import com.example.restaurant.repository.TableRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class RestaurantServiceIntegrationTest {

	@Autowired
	private RestaurantService restaurantService;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private TableRepository tableRepository;

	@Test
	void save_shouldPersistRestaurant() {
		RestaurantCreateCommand command = new RestaurantCreateCommand();
		command.setName("BurgerGo");
		command.setCapacity(50);
		command.setStar(4);

		RestaurantInfo result = restaurantService.save(command);

		assertNotNull(result);
		assertEquals("BurgerGo", result.getName());

		List<Restaurant> restaurants = restaurantRepository.findAll();
		assertEquals(1, restaurants.size());
	}

	@Test
	void save_shouldThrowException_whenNameExists() {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("BurgerGo");
		restaurant.setCapacity(50);
		restaurant.setStar(4);
		restaurantRepository.save(restaurant);

		RestaurantCreateCommand command = new RestaurantCreateCommand();
		command.setName("BurgerGo");
		command.setCapacity(30);
		command.setStar(3);

		assertThrows(RestaurantNameDuplicationException.class,() -> restaurantService.save(command));
	}

	@Test
	void installTable_shouldCreateTableWithDefaults() {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("BurgerGo");
		restaurant.setCapacity(100);
		restaurant.setStar(5);
		restaurantRepository.save(restaurant);

		TableCreateCommand command = new TableCreateCommand();
		command.setNumber(10);
		command.setRestaurantId(restaurant.getId());

		TableInfo result = restaurantService.installTable(command);

		assertNotNull(result);
		assertEquals("BurgerGo", result.getRestaurantName());

		List<RestaurantTable> tables = tableRepository.findAll();
		assertEquals(1, tables.size());

		RestaurantTable saved = tables.get(0);
		assertEquals(TableStatus.FREE, saved.getTableStatus());
		assertEquals(2, saved.getCapacity());
	}

	@Test
	void installTable_shouldThrowException_whenNumberExists() {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("BurgerGo");
		restaurantRepository.save(restaurant);

		RestaurantTable table = new RestaurantTable();
		table.setNumber(10);
		table.setRestaurant(restaurant);
		tableRepository.save(table);

		TableCreateCommand command = new TableCreateCommand();
		command.setNumber(10);
		command.setRestaurantId(restaurant.getId());

		assertThrows(TableNumberDuplicationException.class,() -> restaurantService.installTable(command));
	}

	@Test
	void getTableById_shouldReturnTableInfo() {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("BurgerGo");
		restaurantRepository.save(restaurant);

		RestaurantTable table = new RestaurantTable();
		table.setNumber(5);
		table.setRestaurant(restaurant);
		table.setTableStatus(TableStatus.FREE);
		tableRepository.save(table);

		TableInfo result = restaurantService.getTableById(table.getId());

		assertNotNull(result);
		assertEquals("BurgerGo", result.getRestaurantName());
		assertEquals(5, result.getNumber());
	}
}
