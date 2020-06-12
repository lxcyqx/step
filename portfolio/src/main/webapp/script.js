/**
 * Fetch quote from server and add to DOM.
 */
function getRandomQuote() {
  fetch("/quotes")
    .then(response => response.text())
    .then(quote => {
      document.getElementById("quote-container").innerText = quote;
    });
}

let currPage = 0;
let numCommentsOnPage;
let userEmail;

/**
 * Fetch comments from server and add to DOM.
 */
function getComments() {
  let maxNumComments = document.getElementById("num-comments").value;
  if (maxNumComments === "All") {
    maxNumComments = Number.MAX_VALUE;
  }
  let prevBtn = document.getElementById("prevBtn");

  handleFirstPage();

  fetch(`/data?num=${maxNumComments}&page=${currPage}`)
    .then(response => response.json())
    .then(comments => {
      const commentElement = document.getElementById(
        "video-comments-container"
      );
      commentElement.innerHTML = "";
      numCommentsOnPage = comments.length;

      handleNoComments(maxNumComments);
      handleLastPage(maxNumComments);

      for (i = 0; i < numCommentsOnPage; i++) {
        commentElement.appendChild(createCommentElement(comments[i]));
      }
    });
}

/**
 * If on first page of comments, disable previous button.
 */
function handleFirstPage() {
  if (currPage === 0) {
    prevBtn.disabled = true;
  } else {
    prevBtn.disabled = false;
  }
}

/**
 * Handle scenario if on last page of comments.
 * @param {number} maxNumComments
 */
function handleLastPage(maxNumComments) {
  let nextPage = currPage + 1;
  //check for number of comments on next page
  fetch(`/data?num=${maxNumComments}&page=${nextPage}`).then(response =>
    response.json().then(comments => {
      //if no comments on next page, then current page is last page
      if (comments.length === 0) {
        nextBtn.disabled = true;
      } else {
        nextBtn.disabled = false;
      }
    })
  );
}

/**
 * Handles scenario in which page has no comments, either when there are no  
 * comments in general or when there are no
 * comments on current page due to page size change.
 * @param {number} maxNumComments
 */
function handleNoComments(maxNumComments) {
  //handles case when last page has no comments
  if (numCommentsOnPage === 0) {
    /* Set current page to 0 and fetch comments that would be displayed on   
     * first page. */
    currPage = 0;
    fetch(`/data?num=${maxNumComments}&page=${currPage}`)
      .then(response => response.json())
      .then(comments => {
        //if there aren't any comments at all, add message
        if (comments.length === 0) {
          const commentsContainer = document.getElementById(
            "video-comments-container"
          );
          const noComment = document.createElement("div");
          noComment.setAttribute("class", "no-comment");
          noComment.innerText = "No comments to display.";
          commentsContainer.appendChild(noComment);
        } else {
          //display comments on the first page
          const commentElement = document.getElementById(
            "video-comments-container"
          );
          commentElement.innerHTML = "";
          handleFirstPage();
          handleLastPage(maxNumComments);
          for (i = 0; i < comments.length; i++) {
            commentElement.appendChild(createCommentElement(comments[i]));
          }
        }
      });
  }
}

/**
 * Create formatting for comment to be inserted.
 * @param {object} comment
 */
function createCommentElement(comment) {
  const commentElement = document.createElement("div");
  commentElement.setAttribute("id", "video-comments");
  //add delete button to comments made by
  if (userEmail === comment.email) {
    const deleteButton = document.createElement("button");
    deleteButton.setAttribute("class", "delete");
    deleteButton.addEventListener("click", () => {
      deleteComment(comment);
    });
    deleteButton.innerText = "Delete";
    commentElement.appendChild(deleteButton);
  }
  //add comment text
  const commentText = document.createElement("div");
  commentText.setAttribute("class", "comment-text");
  commentText.innerText = comment.text;
  commentElement.appendChild(commentText);
  //add comment footer
  const commentFooter = document.createElement("div");
  commentFooter.setAttribute("class", "comment-footer");
  const footerInfo = document.createElement("div");
  footerInfo.setAttribute("class", "footer-info");
  if (comment.email) {
    footerInfo.innerText = comment.email.split("@")[0] + " | " + comment.timestamp;
  }
  commentFooter.appendChild(footerInfo);
  commentElement.appendChild(commentFooter);
  return commentElement;
}

/**
 * Deletes all the comments and calls getComments to get comments from API with * all comments now deleted.
 */
function deleteAll() {
  const request = new Request("/delete-data", { method: "POST" });
  fetch(request).then(() => {
    getComments();
  });
}

/**
 * Deletes given comment.
 * @param {object} comment
 */
function deleteComment(comment) {
  const params = new URLSearchParams();
  params.append("id", comment.id);
  params.append("email", comment.email);
  params.append("user", userEmail);
  fetch("/delete-comment", { method: "POST", body: params }).then(() => {
    getComments();
  });
}

/**
 * Adds user's comment.
 */
function addComment() {
  let text = document.getElementById("comment-box").value;
  //if comment input is empty, can't submit
  if (text.trim() === "") return;
  document.getElementById("comment-box").value = "";
  fetch(
    `add-comment?comment-text=${text}&email=${userEmail}`,
    { method: "POST" }
  );

  setTimeout(getComments, 500);
}

/**
 * Increments current page number.
 */
function nextPage() {
  currPage++;
  getComments();
}

/**
 * Decrements current page number.
 */
function prevPage() {
  currPage--;
  getComments();
}

/**
 * Gets user's current login status.
 */
function getLoginStatus() {
  const commentSection = document.getElementById("comment-box");
  const submitButton = document.getElementById("submit");

  fetch("/auth")
    .then(response => response.json())
    .then(userInfo => {
      const loginStatusElement = document.getElementById("login-status");
      loginStatusElement.innerHTML = "";

      //if user is logged in, show comment section
      if (userInfo.isLoggedIn === "true") {
        //allow user to log out
        const logoutElement = document.createElement("a");
        logoutElement.innerHTML = "Log Out";
        logoutElement.href = userInfo.logoutUrl;

        loginStatusElement.appendChild(logoutElement);
        userEmail = userInfo.email;

        commentSection.style.display = "block";
        submitButton.style.display = "block";
      } else {
        //allow user to log in
        const loginElement = document.createElement("a");
        loginElement.innerHTML = "Log In";
        loginElement.href = userInfo.loginUrl;

        const textElement = document.createElement("p");
        textElement.innerHTML = " to add comments";
        textElement.style.display = "inline"

        loginStatusElement.appendChild(loginElement);
        loginStatusElement.appendChild(textElement);

        userEmail = null;
        //hide comment section
        commentSection.style.display = "none";
        submitButton.style.display = "none";
      }
    });

  getComments();
}
