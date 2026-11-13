package ca.sheridancollege.khvostiv.tests;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ca.sheridancollege.khvostiv.controllers.HomeController;

@SpringBootTest
public class HomeControllerIntegrationTests {

	@Autowired
	private HomeController controller;

	@Test
	public void contextLoads() throws Exception {

		assertThat(controller).isNotNull();
	}
}
