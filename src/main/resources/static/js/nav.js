// Script para el toggle del nav
document.addEventListener('DOMContentLoaded', function() {
	const navToggle = document.querySelector('.nav-toggle');
	const navLinks = document.querySelector('.nav-links');
		            
	if(navToggle && navLinks) {
		navToggle.addEventListener('click', function() {
			navLinks.classList.toggle('nav-open');
		});
	}
});