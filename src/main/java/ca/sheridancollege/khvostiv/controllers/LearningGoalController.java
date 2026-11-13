package ca.sheridancollege.khvostiv.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.khvostiv.models.LearningGoalRequest;
import ca.sheridancollege.khvostiv.services.LearningGoalService;
import ca.sheridancollege.khvostiv.services.PianoPieceService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class LearningGoalController {

	private LearningGoalService learningGoalService;
	private PianoPieceService pianoPieceService;

	@GetMapping("/goals")
	public String goals(Authentication authentication, Model model) {

		model.addAttribute("goals", learningGoalService.findAllByUser(authentication.getName()));
		model.addAttribute("pieces", pianoPieceService.findAllByUser(authentication.getName()));
		model.addAttribute("learningGoalRequest", new LearningGoalRequest());
		model.addAttribute("email", authentication.getName());
		

		return "goals";
	}

	@PostMapping("/goals/save")
	public String saveGoal(@ModelAttribute LearningGoalRequest learningGoalRequest, Authentication authentication) {

		learningGoalService.save(learningGoalRequest, authentication.getName());

		return "redirect:/goals?created";
	}

	@PostMapping("/goals/complete/{id}")
	public String completeGoal(@PathVariable Long id, Authentication authentication) {

		learningGoalService.complete(id, authentication.getName());

		return "redirect:/goals?completed";
	}

	@PostMapping("/goals/delete/{id}")
	public String deleteGoal(@PathVariable Long id, Authentication authentication) {

		learningGoalService.delete(id, authentication.getName());

		return "redirect:/goals?deleted";
	}
}