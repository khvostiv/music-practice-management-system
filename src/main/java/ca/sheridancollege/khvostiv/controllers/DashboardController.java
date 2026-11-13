package ca.sheridancollege.khvostiv.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import ca.sheridancollege.khvostiv.services.DashboardService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DashboardController {

	private DashboardService dashboardService;

	@GetMapping("/dashboard")
	public String dashboard(Authentication authentication, Model model) {

		model.addAttribute("dashboard", dashboardService.getDashboard(authentication.getName()));
		model.addAttribute("email", authentication.getName());

		return "dashboard";
	}
}