package ca.sheridancollege.khvostiv.tests;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.time.Duration;

import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class PianoPiecePageTests {

	static WebDriver browser;

	@BeforeAll
	static void setUpDriver() {

		String testEmail = System.getenv("TEST_EMAIL");
		String testPassword = System.getenv("TEST_PASSWORD");

		assertNotNull(testEmail, "TEST_EMAIL environment variable is not set");
		assertNotNull(testPassword, "TEST_PASSWORD environment variable is not set");

		browser = new ChromeDriver();
		browser.get("http://localhost:50000/login");

		// Enter the email
		WebElement email = browser.findElement(By.id("email"));
		email.sendKeys(testEmail);

		// Enter the password
		WebElement password = browser.findElement(By.id("password"));
		password.sendKeys(testPassword);

		// Click login
		WebElement loginButton = browser.findElement(By.cssSelector("input[type='submit']"));

		loginButton.click();
	}

	// Test 3: Create a Piano Piece through the UI
	@Test
	public void addPianoPiece() {

		browser.get("http://localhost:50000/pieces");

		WebElement addPianoPieces = browser.findElement(By.id("addPianoPieceLink"));
		addPianoPieces.click();

		WebDriverWait wait = new WebDriverWait(browser, Duration.ofSeconds(10));

		WebElement title = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("title")));

		title.sendKeys("Ballade 3");

		WebElement composer = browser.findElement(By.id("composerName"));
		composer.sendKeys("Chopin");

		// Genre dropdown
		WebElement genre = browser.findElement(By.id("genre"));
		Select genreDropdown = new Select(genre);
		genreDropdown.selectByValue("BALLADE");

		// Difficulty dropdown
		WebElement difficulty = browser.findElement(By.id("difficulty"));
		Select difficultyDropdown = new Select(difficulty);
		difficultyDropdown.selectByValue("Advanced");

		// Learning Status dropdown
		WebElement learningStatus = browser.findElement(By.id("learningStatus"));
		Select learningStatusDropdown = new Select(learningStatus);
		learningStatusDropdown.selectByValue("LEARNING");

		// Rating dropdown
		WebElement rating = browser.findElement(By.id("rating"));
		Select ratingDropdown = new Select(rating);
		ratingDropdown.selectByValue("5");

		// Submit form
		WebElement addButton = browser.findElement(By.cssSelector("input[type='submit'][value='Add Piano Piece']"));

		addButton.click();

		// Verify piece was created successfully
		WebElement successMessage = browser.findElement(By.className("success-message"));

		assertTrue(successMessage.getText().contains("Piano piece was added successfully."));
	}

	@AfterAll
	static void stopDriver() {

		if (browser != null) {
			browser.quit();
		}
	}
}