let imagesArray = [
  "pagan.jpg",
  "forest.jpg",
  "woods.jpg",
  "person.jpg",
  "sky_monotype.jpg",
  "mountain_monotype.jpg",
  "face.jpg",
  "plant.JPG",
  "splash_monotype.jpg",
  "workspace.jpg",
  "persistence.jpg",
  "red-bags.jpg",
  "house.JPG",
  "business man.jpg",
  "woman-with-bike.jpg",
  "self-portrait.jpg",
  "sweater.jpg",
  "corn.JPG",
  "scratchboard.jpg"
];

let getLatestOpenedImgIndex;
let frames = document.querySelectorAll(".art-container");
if (frames) {
  frames.forEach(function(frame, index) {
    frame.onclick = function() {
      getLatestOpenedImgIndex = index;
      console.log(getLatestOpenedImgIndex);
      let imageName = frame.children[0].src.split("images/fine-art/")[1];
      let container = document.body;
      let newImageWindow = document.createElement("div");
      container.appendChild(newImageWindow);
      newImageWindow.setAttribute("class", "img-window");
      newImageWindow.setAttribute("onclick", "closeImg()");

      let newImage = document.createElement("img");
      newImageWindow.appendChild(newImage);
      newImage.setAttribute("src", "images/fine-art/" + imageName);
      newImage.setAttribute("id", "current-img");

      newImage.onload = function() {
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
      };
    };
  });
}

/**
 * Change image given direction
 * @param {prev or next direction} changeDir
 */
function changeImg(changeDir) {
  //   console.log(getLatestOpenedImgIndex);
  //close current image
  document.querySelector("#current-img").remove();
  let getImgWindow = document.querySelector(".img-window");
  //add new image
  let newImg = document.createElement("img");
  getImgWindow.appendChild(newImg);

  let newImageIndex;
  if (changeDir === 1) {
    if (getLatestOpenedImgIndex == imagesArray.length - 1) {
      newImageIndex = 0;
    } else {
      newImageIndex = getLatestOpenedImgIndex + 1;
    }
  } else if (changeDir === 0) {
    if (getLatestOpenedImgIndex == 0) {
      newImageIndex = imagesArray.length - 1;
    } else {
      newImageIndex = getLatestOpenedImgIndex - 1;
    }
  }

  newImg.setAttribute("src", "images/fine-art/" + imagesArray[newImageIndex]);
  newImg.setAttribute("id", "current-img");
  getLatestOpenedImgIndex = newImageIndex;
}

function closeImg() {
  document.querySelector(".img-window").remove();
}
