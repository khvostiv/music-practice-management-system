package ca.sheridancollege.khvostiv.controllers;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ca.sheridancollege.khvostiv.beans.Composer;
import ca.sheridancollege.khvostiv.beans.Genre;
import ca.sheridancollege.khvostiv.beans.LearningStatus;
import ca.sheridancollege.khvostiv.beans.PianoPiece;
import ca.sheridancollege.khvostiv.models.AuthenticationRequest;
import ca.sheridancollege.khvostiv.models.AuthenticationResponse;
import ca.sheridancollege.khvostiv.repositories.ComposerRepository;
import ca.sheridancollege.khvostiv.services.AuthenticationService;
import ca.sheridancollege.khvostiv.services.PianoPieceService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;
import ca.sheridancollege.khvostiv.services.SheetMusicService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
@AllArgsConstructor
public class HomeController {

	private AuthenticationService authenticationService;
	private PianoPieceService pianoPieceService;
	private ComposerRepository composerRepository;
	private SheetMusicService sheetMusicService;

	@GetMapping("/")
	public String home(Authentication authentication) {

		if (authentication != null && authentication.isAuthenticated()) {
			return "redirect:/pieces";
		}

		return "redirect:/login";
	}

	@GetMapping("/login")
	public String login(Model model) {

		model.addAttribute("authenticationRequest", new AuthenticationRequest());

		return "login";
	}

	@PostMapping("/login")
	public String processLogin(@ModelAttribute AuthenticationRequest request, HttpServletResponse response,
			Model model) {

		try {

			AuthenticationResponse authenticationResponse = authenticationService.authenticate(request);
			addJwtCookie(response, authenticationResponse.getToken());

			return "redirect:/pieces";

		} catch (BadCredentialsException exception) {

			model.addAttribute("loginError", "Invalid email or password.");

			return "login";
		}
	}

	@GetMapping("/register")
	public String register(Model model) {

		model.addAttribute("authenticationRequest", new AuthenticationRequest());

		return "register";
	}

	@PostMapping("/register")
	public String processRegistration(@ModelAttribute AuthenticationRequest request, HttpServletResponse response,
			Model model) {

		try {

			AuthenticationResponse authenticationResponse = authenticationService.register(request);
			addJwtCookie(response, authenticationResponse.getToken());

			return "redirect:/pieces";

		} catch (IllegalArgumentException exception) {

			model.addAttribute("registrationError", exception.getMessage());

			return "register";
		}
	}

	@GetMapping("/pieces")
	public String pieces(Authentication authentication, Model model) {

		model.addAttribute("pieces", pianoPieceService.findAllByUser(authentication.getName()));
		model.addAttribute("email", authentication.getName());

		return "pieces";
	}

	@GetMapping("/pieces/new")
	public String newPiece(Model model) {

		model.addAttribute("pianoPiece", new PianoPiece());
		addPieceFormInformation(model);

		return "piece-form";
	}

	@PostMapping("/pieces/save")
	public String savePiece(@ModelAttribute PianoPiece pianoPiece, @RequestParam String composerName,
			@RequestParam MultipartFile sheetMusicFile, Authentication authentication) throws IOException {

		Composer composer = composerRepository.findByName(composerName)
				.orElseGet(() -> composerRepository.save(Composer.builder().name(composerName).build()));

		pianoPiece.setComposer(composer);
		sheetMusicService.saveSheetMusic(sheetMusicFile, pianoPiece);
		pianoPieceService.save(pianoPiece, authentication.getName());

		return "redirect:/pieces?created";
	}

	@GetMapping("/pieces/edit/{id}")
	public String editPiece(@PathVariable Long id, Authentication authentication, Model model) {

		PianoPiece pianoPiece = pianoPieceService.findByIdAndUser(id, authentication.getName());
		model.addAttribute("pianoPiece", pianoPiece);
		addPieceFormInformation(model);

		return "piece-form";
	}

	@PostMapping("/pieces/update/{id}")
	public String updatePiece(@PathVariable Long id, @ModelAttribute PianoPiece pianoPiece,
			@RequestParam String composerName, @RequestParam("sheetMusicFile") MultipartFile sheetMusicFile,
			Authentication authentication) throws IOException {

		Composer composer = composerRepository.findByName(composerName)
				.orElseGet(() -> composerRepository.save(Composer.builder().name(composerName).build()));

		pianoPiece.setComposer(composer);

		PianoPiece existingPiece = pianoPieceService.findByIdAndUser(id, authentication.getName());

		pianoPiece.setSheetMusicFileName(existingPiece.getSheetMusicFileName());

		pianoPiece.setSheetMusicPath(existingPiece.getSheetMusicPath());

		if (sheetMusicFile != null && !sheetMusicFile.isEmpty()) {
			sheetMusicService.saveSheetMusic(sheetMusicFile, pianoPiece);
		}

		pianoPieceService.update(id, pianoPiece, authentication.getName());

		return "redirect:/pieces?updated";
	}

	@PostMapping("/pieces/delete/{id}")
	public String deletePiece(@PathVariable Long id, Authentication authentication) throws IOException {

		PianoPiece deletedPiece = pianoPieceService.delete(id, authentication.getName());

		sheetMusicService.deleteSheetMusic(deletedPiece);

		return "redirect:/pieces?deleted";
	}

	@PostMapping("/logout")
	public String logout(HttpServletResponse response) {

		Cookie cookie = new Cookie("jwt", "");

		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(0);

		response.addCookie(cookie);

		return "redirect:/login?logout";
	}

	private void addPieceFormInformation(Model model) {

		model.addAttribute("genres", Genre.values());

		model.addAttribute("statuses", LearningStatus.values());
	}

	private void addJwtCookie(HttpServletResponse response, String token) {

		Cookie cookie = new Cookie("jwt", token);

		cookie.setHttpOnly(true);
		cookie.setPath("/");
		cookie.setMaxAge(24 * 60 * 60);

		response.addCookie(cookie);
	}

	@GetMapping("/pieces/sheet-music/{id}")
	public ResponseEntity<Resource> openSheetMusic(@PathVariable Long id, Authentication authentication)
			throws IOException {

		PianoPiece pianoPiece = pianoPieceService.findByIdAndUser(id, authentication.getName());

		Resource resource = sheetMusicService.getSheetMusic(pianoPiece);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).header(HttpHeaders.CONTENT_DISPOSITION,
				"inline; filename=\"" + pianoPiece.getSheetMusicFileName() + "\"").body(resource);
	}
}