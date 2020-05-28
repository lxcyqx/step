const navSlide = () => {
  const burger = document.querySelector(".burger");
  const nav = document.querySelector(".nav-links");
  const navLinks = document.querySelectorAll(".nav-links li");

  burger.addEventListener("click", () => {
    nav.classList.toggle("nav-active");

    navLinks.forEach((link, index) => {
      navLinks.forEach(link => {
        link.classList.toggle("fade");
      });
    });

    burger.classList.toggle("toggle");
  });
};

navSlide();
