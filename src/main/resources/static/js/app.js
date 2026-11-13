(function () {
	"use strict";

	function initNav() {
		const toggle = document.querySelector(".nav-toggle");
		const nav = document.getElementById("siteNav");
		const header = document.querySelector(".app-header");

		if (toggle && nav) {
			toggle.addEventListener("click", function () {
				const open = document.body.classList.toggle("nav-open");
				toggle.setAttribute("aria-expanded", open ? "true" : "false");
				toggle.setAttribute("aria-label", open ? "Close menu" : "Open menu");
			});

			nav.querySelectorAll("a").forEach(function (link) {
				link.addEventListener("click", function () {
					document.body.classList.remove("nav-open");
					toggle.setAttribute("aria-expanded", "false");
					toggle.setAttribute("aria-label", "Open menu");
				});
			});

			document.addEventListener("keydown", function (event) {
				if (event.key === "Escape") {
					document.body.classList.remove("nav-open");
					toggle.setAttribute("aria-expanded", "false");
				}
			});
		}

		if (!header) {
			return;
		}

		const onScroll = function () {
			header.classList.toggle("is-scrolled", window.scrollY > 8);
		};

		onScroll();
		window.addEventListener("scroll", onScroll, { passive: true });
	}

	function initPasswordToggle() {
		document.querySelectorAll("[data-password-toggle]").forEach(function (button) {
			button.addEventListener("click", function () {
				const input = document.getElementById(button.getAttribute("data-password-toggle"));
				if (!input) {
					return;
				}

				const hidden = input.type === "password";
				input.type = hidden ? "text" : "password";
				button.classList.toggle("is-visible", hidden);
				button.setAttribute("aria-label", hidden ? "Hide password" : "Show password");
			});
		});
	}

	function initToasts() {
		document.querySelectorAll(".success-message, .error-message").forEach(function (message) {
			message.classList.add("is-visible");

			if (!message.querySelector(".notice-close")) {
				const close = document.createElement("button");
				close.type = "button";
				close.className = "notice-close";
				close.setAttribute("aria-label", "Dismiss message");
				close.innerHTML = "&times;";
				close.addEventListener("click", function () {
					message.classList.add("is-dismissed");
				});
				message.appendChild(close);
			}
		});
	}

	function initCountUp() {
		const reduceMotion = window.matchMedia("(prefers-reduced-motion: reduce)").matches;

		document.querySelectorAll("[data-count]").forEach(function (el) {
			const end = Number(el.getAttribute("data-count"));
			if (!Number.isFinite(end)) {
				return;
			}

			if (reduceMotion) {
				el.textContent = String(end);
				return;
			}

			const duration = 900;
			const start = performance.now();

			function frame(now) {
				const progress = Math.min((now - start) / duration, 1);
				const eased = 1 - Math.pow(1 - progress, 3);
				el.textContent = String(Math.round(end * eased));
				if (progress < 1) {
					requestAnimationFrame(frame);
				}
			}

			requestAnimationFrame(frame);
		});
	}

	function initFileDrop() {
		const input = document.getElementById("sheetMusicFile");
		const zone = document.querySelector("[data-file-drop]");
		const label = document.querySelector("[data-file-name]");

		if (!input || !zone) {
			return;
		}

		function showName(file) {
			if (!label) {
				return;
			}
			if (file) {
				label.textContent = file.name;
				zone.classList.add("has-file");
			} else {
				label.textContent = "PDF up to 20MB";
				zone.classList.remove("has-file");
			}
		}

		zone.addEventListener("click", function (event) {
			if (event.target !== input) {
				input.click();
			}
		});

		zone.addEventListener("keydown", function (event) {
			if (event.key === "Enter" || event.key === " ") {
				event.preventDefault();
				input.click();
			}
		});

		["dragenter", "dragover"].forEach(function (type) {
			zone.addEventListener(type, function (event) {
				event.preventDefault();
				zone.classList.add("is-dragover");
			});
		});

		["dragleave", "drop"].forEach(function (type) {
			zone.addEventListener(type, function (event) {
				event.preventDefault();
				zone.classList.remove("is-dragover");
			});
		});

		zone.addEventListener("drop", function (event) {
			const files = event.dataTransfer && event.dataTransfer.files;
			if (files && files.length) {
				input.files = files;
				showName(files[0]);
			}
		});

		input.addEventListener("change", function () {
			showName(input.files && input.files[0]);
		});
	}

	function initPieceFilters() {
		const search = document.getElementById("pieceSearch");
		const cards = document.querySelectorAll("[data-piece-card]");
		const empty = document.querySelector("[data-filter-empty]");
		const pills = document.querySelectorAll("[data-status-filter]");

		if (!cards.length) {
			return;
		}

		let status = "all";

		function matches(card, query) {
			const haystack = [
				card.getAttribute("data-title") || "",
				card.getAttribute("data-composer") || "",
				card.getAttribute("data-genre") || ""
			].join(" ").toLowerCase();

			const cardStatus = (card.getAttribute("data-status") || "").toLowerCase();
			const statusOk = status === "all" || cardStatus === status;
			return statusOk && haystack.indexOf(query) !== -1;
		}

		function apply() {
			const query = search ? search.value.trim().toLowerCase() : "";
			let visible = 0;

			cards.forEach(function (card) {
				const show = matches(card, query);
				card.hidden = !show;
				if (show) {
					visible += 1;
				}
			});

			if (empty) {
				empty.hidden = visible !== 0;
			}
		}

		if (search) {
			search.addEventListener("input", apply);
		}

		pills.forEach(function (pill) {
			pill.addEventListener("click", function () {
				status = (pill.getAttribute("data-status-filter") || "all").toLowerCase();
				pills.forEach(function (item) {
					item.classList.toggle("is-active", item === pill);
				});
				apply();
			});
		});
	}

	function initInteractiveForms() {
		document.querySelectorAll("form").forEach(function (form) {
			form.addEventListener("submit", function () {
				form.classList.add("is-submitting");
			});
		});

		document.querySelectorAll(".field-group input, .field-group select, .field-group textarea").forEach(function (field) {
			const group = field.closest(".field-group");
			if (!group) {
				return;
			}

			const sync = function () {
				group.classList.toggle("has-value", Boolean(field.value));
			};

			field.addEventListener("focus", function () {
				group.classList.add("is-focused");
			});
			field.addEventListener("blur", function () {
				group.classList.remove("is-focused");
			});
			field.addEventListener("input", sync);
			field.addEventListener("change", sync);
			sync();
		});
	}

	function initStagger() {
		document.querySelectorAll(".stagger-in > *").forEach(function (child, index) {
			child.style.setProperty("--stagger", String(index));
		});
	}

	document.addEventListener("DOMContentLoaded", function () {
		initNav();
		initPasswordToggle();
		initToasts();
		initCountUp();
		initFileDrop();
		initPieceFilters();
		initInteractiveForms();
		initStagger();
	});
})();
