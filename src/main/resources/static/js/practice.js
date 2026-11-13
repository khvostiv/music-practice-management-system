/**
 * 
 */
let timerInterval;
let elapsedSeconds = 0;
let practiceStarted = false;

function startPractice() {

	const pianoPiece =
		document.getElementById("pianoPieceId");

	if (pianoPiece.value === "") {
		alert("Please select a piano piece.");
		return;
	}

	practiceStarted = true;
	elapsedSeconds = 0;

	document.getElementById("startedAt").value =
		formatDateTime(new Date());

	document.getElementById("timer").textContent =
		"00:00:00";

	document.getElementById("startButton").disabled =
		true;

	document.getElementById("stopButton").disabled =
		false;

	pianoPiece.disabled = true;

	timerInterval = setInterval(function() {

		elapsedSeconds++;
		displayTime();

	}, 1000);
}

function displayTime() {

	const hours =
		Math.floor(elapsedSeconds / 3600);

	const minutes =
		Math.floor((elapsedSeconds % 3600) / 60);

	const seconds =
		elapsedSeconds % 60;

	document.getElementById("timer").textContent =
		String(hours).padStart(2, "0")
		+ ":"
		+ String(minutes).padStart(2, "0")
		+ ":"
		+ String(seconds).padStart(2, "0");
}

function stopPractice() {

	if (!practiceStarted) {
		alert("Please start the practice timer first.");
		return false;
	}

	clearInterval(timerInterval);

	document.getElementById("endedAt").value =
		formatDateTime(new Date());

	document.getElementById("pianoPieceId").disabled =
		false;

	return true;
}

function formatDateTime(date) {

	const year = date.getFullYear();

	const month =
		String(date.getMonth() + 1).padStart(2, "0");

	const day =
		String(date.getDate()).padStart(2, "0");

	const hours =
		String(date.getHours()).padStart(2, "0");

	const minutes =
		String(date.getMinutes()).padStart(2, "0");

	const seconds =
		String(date.getSeconds()).padStart(2, "0");

	return year
		+ "-"
		+ month
		+ "-"
		+ day
		+ "T"
		+ hours
		+ ":"
		+ minutes
		+ ":"
		+ seconds;
}