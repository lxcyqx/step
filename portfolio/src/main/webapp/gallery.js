let galleryImages = document.querySelectorAll(".gallery-img");
let getLatestOpenedImg;
//get current width of window inside browser
let windowWidth = window.innerWidth;

function init() {
    //check if there are gallery images
    if (galleryImages) {
        //for each image and take in index of image in the array
        galleryImages.forEach(function(image, index) {
            //get full resolution image
            image.onclick = function() {
            //get css style of selected image
            let getElementsCss = window.getComputedStyle(image);
            //get background image property of the image from css style list
            let getFullImageURL = getElementsCss.getPropertyValue("background-image");
            //get last part of background image url, the image name
            let getImageURLPos = getFullImageURL.split(
                "/images/photography/thumbnails/"
            );
            //get second part of the split URL and remove the ")
            let setNewImageURL = getImageURLPos[1].replace('")', "");

            getLatestOpenedImg = index + 1;

            let container = document.body;
            let newImageWindow = document.createElement("div");
            //apply div to body of website
            container.appendChild(newImageWindow);
            newImageWindow.setAttribute("class", "img-window");
            newImageWindow.setAttribute("onclick", "closeImg()");

            let newImage = document.createElement("img");
            newImageWindow.appendChild(newImage);
            newImage.setAttribute("src", "images/photography/" + setNewImageURL);
            newImage.setAttribute("id", "current-img");

            //execute code after image is loaded
            newImage.onload = function() {
                loadArrows(document, container);
            };
            };
        });
    }
}

function closeImg() {
  document.querySelector(".img-window").remove();
}

/**
 * Change image given direction
 * @param {prev or next direction} changeDir
 */
function changeImg(changeDir) {
  //close current image
  document.querySelector("#current-img").remove();
  let getImgWindow = document.querySelector(".img-window");
  //add new image
  let newImg = document.createElement("img");
  getImgWindow.appendChild(newImg);

  let calcNewImg;
  if (changeDir === 1) {
    calcNewImg = getLatestOpenedImg + 1;
    //if current image is the last image
    if (calcNewImg > galleryImages.length) {
      calcNewImg = 1;
    }
  } else if (changeDir === 0) {
    calcNewImg = getLatestOpenedImg - 1;
    if (calcNewImg < 1) {
      calcNewImg = galleryImages.length;
    }
  }

  newImg.setAttribute("src", "images/photography/img" + calcNewImg + ".jpg");
  newImg.setAttribute("id", "current-img");
  getLatestOpenedImg = calcNewImg;
}

init(); 