(function () {
	"use strict";

	const params = new URLSearchParams(window.location.search);

	document.querySelectorAll(".success-message[data-param]").forEach(function (el) {
		if (params.has(el.getAttribute("data-param"))) {
			el.hidden = false;
			el.classList.add("is-visible");
		} else {
			el.hidden = true;
		}
	});

	document.querySelectorAll("form").forEach(function (form) {
		form.addEventListener("submit", function (event) {
			if (form.id === "practiceForm") {
				const ok = typeof stopPractice === "function" ? stopPractice() : false;
				event.preventDefault();
				if (ok) {
					window.location.href = "practice.html?created=1";
				}
				return;
			}

			event.preventDefault();

			if (form.querySelector(".logout-button")) {
				window.location.href = "index.html";
				return;
			}

			const submit = form.querySelector("input[type='submit']");
			const value = submit ? submit.value : "";

			if (value === "Login" || value === "Register") {
				window.location.href = "pieces.html";
				return;
			}
			if (value === "Add Piano Piece" || value === "Save Changes") {
				window.location.href = "pieces.html?created=1";
				return;
			}
			if (value === "Create Goal") {
				window.location.href = "goals.html?created=1";
				return;
			}
			if (value === "Complete") {
				window.location.href = "goals.html?completed=1";
				return;
			}
			if (value === "Delete") {
				return;
			}
		});
	});
})();
