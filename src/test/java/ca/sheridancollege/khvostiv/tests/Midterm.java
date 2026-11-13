package ca.sheridancollege.khvostiv.tests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import ca.sheridancollege.khvostiv.controllers.HomeController;

@WebMvcTest(HomeController.class)
public class Midterm {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testPageLoads() throws Exception {
		mockMvc.perform(get("/")).andExpect(status().isOk());
	}
}
