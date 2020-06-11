/**
 * Load arrows on image gallery popup.
 * @param {*} document
 * @param {*} container
 */
function loadArrows(document, container) {
  //execute code after image is loaded
  let newNextBtn = document.createElement("img");
  container.appendChild(newNextBtn);
  newNextBtn.setAttribute("src", "images/right-arrow.png");
  newNextBtn.setAttribute("class", "img-btn-next");
  newNextBtn.setAttribute("onclick", "changeImg(1)");
  newNextBtn.style.cssText = "right: 20px";

  let newPrevBtn = document.createElement("img");
  container.appendChild(newPrevBtn);
  newPrevBtn.setAttribute("src", "images/left-arrow.png");
  newPrevBtn.setAttribute("class", "img-btn-prev");
  newPrevBtn.setAttribute("onclick", "changeImg(0)");
  newPrevBtn.style.cssText = "left: 20px";
}
