(function () {
	"use strict";

	const BACKDROP = `
		<div class="staff-backdrop" aria-hidden="true">
			<div class="staff-lines"></div>
			<div class="clef-watermark">
				<svg viewBox="0 0 80 180" fill="none" xmlns="http://www.w3.org/2000/svg">
					<path d="M42 28c8 6 16 16 16 30 0 22-20 34-20 52 0 10 7 18 16 18 14 0 22-14 22-30"
						stroke="currentColor" stroke-width="6" stroke-linecap="round"/>
					<path d="M38 20c0 70-28 88-28 112 0 16 12 28 28 28 22 0 36-18 36-40"
						stroke="currentColor" stroke-width="6" stroke-linecap="round"/>
					<circle cx="32" cy="154" r="16" fill="currentColor"/>
					<circle cx="48" cy="22" r="8" fill="currentColor"/>
				</svg>
			</div>
		</div>`;

	function header(active) {
		const link = function (id, href, label) {
			return `<a href="${href}" class="${active === id ? "nav-link is-active" : "nav-link"}">${label}</a>`;
		};

		return `
		<header class="app-header">
			<a class="brand" href="pieces.html">
				<span class="brand-mark" aria-hidden="true">
					<svg viewBox="0 0 40 40" xmlns="http://www.w3.org/2000/svg">
						<rect width="40" height="40" rx="10" fill="currentColor"/>
						<rect x="7" y="10" width="5.5" height="20" rx="1" fill="#fbf8f2"/>
						<rect x="13.75" y="10" width="5.5" height="20" rx="1" fill="#fbf8f2"/>
						<rect x="20.5" y="10" width="5.5" height="20" rx="1" fill="#fbf8f2"/>
						<rect x="27.25" y="10" width="5.5" height="20" rx="1" fill="#fbf8f2"/>
						<rect x="11.4" y="10" width="3.4" height="12" rx="0.6" fill="#1a1612"/>
						<rect x="18.15" y="10" width="3.4" height="12" rx="0.6" fill="#1a1612"/>
						<rect x="24.9" y="10" width="3.4" height="12" rx="0.6" fill="#1a1612"/>
					</svg>
				</span>
				<span class="brand-copy">
					<span class="brand-title">Piano Piece Tracker</span>
					<span class="brand-tag">Practice studio</span>
				</span>
			</a>
			<button class="nav-toggle" type="button" aria-expanded="false" aria-controls="siteNav" aria-label="Open menu">
				<span></span><span></span><span></span>
			</button>
			<nav id="siteNav" class="site-nav" aria-label="Main">
				${link("pieces", "pieces.html", "Pieces")}
				${link("dashboard", "dashboard.html", "Dashboard")}
				${link("practice", "practice.html", "Practice")}
				${link("goals", "goals.html", "Goals")}
				${link("history", "history.html", "History")}
				${link("readme", "readme.html", "README")}
			</nav>
			<div class="header-user">
				<p class="user-chip">
					<span class="user-label">Signed in</span>
					<span>studio@example.com</span>
				</p>
				<form method="get" action="index.html">
					<input type="submit" value="Logout" class="logout-button">
				</form>
			</div>
		</header>`;
	}

	window.StudioPartials = {
		mount: function (active) {
			const mount = document.getElementById("studio-shell");
			if (!mount) {
				return;
			}
			mount.innerHTML = BACKDROP + header(active);
		}
	};
})();
