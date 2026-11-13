package ca.sheridancollege.khvostiv.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.khvostiv.models.PracticeSessionRequest;
import ca.sheridancollege.khvostiv.services.PianoPieceService;
import ca.sheridancollege.khvostiv.services.PracticeSessionService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PracticePageController {

	private PianoPieceService pianoPieceService;
	private PracticeSessionService practiceSessionService;

	@GetMapping("/practice")
	public String practicePage(Authentication authentication, Model model) {

		model.addAttribute("pieces", pianoPieceService.findAllByUser(authentication.getName()));

		model.addAttribute("email", authentication.getName());

		model.addAttribute("practiceSessionRequest", new PracticeSessionRequest());

		return "practice";
	}

	@PostMapping("/practice/save")
	public String savePracticeSession(@ModelAttribute PracticeSessionRequest practiceSessionRequest,
			Authentication authentication) {

		practiceSessionService.save(practiceSessionRequest, authentication.getName());

		return "redirect:/practice?created";
	}

	@GetMapping("/practice/history")
	public String practiceHistory(Authentication authentication, Model model) {

		model.addAttribute("sessions", practiceSessionService.findAllByUser(authentication.getName()));

		model.addAttribute("email", authentication.getName());

		return "practice-history";
	}

	@PostMapping("/practice/history/delete/{id}")
	public String deletePracticeSession(@PathVariable Long id, Authentication authentication) {

		practiceSessionService.delete(id, authentication.getName());

		return "redirect:/practice/history?deleted";
	}
}