/** Fetch quote from server and add to DOM */
function getRandomQuote() {
    fetch('/quotes').then(response => response.text()).then((quote) => {
        document.getElementById('quote-container').innerText = quote;
    })
}

let currPage = 0;

/** Fetch comments from server and add to DOM */
function getComments() {
    maxNumComments = document.getElementById("num-comments").value;
    var prevBtn = document.getElementById("prevBtn")
    if (currPage === 0) {
        prevBtn.disabled = true;
        prevBtn.style.display = "none"
    } else {
        prevBtn.disabled = false;
        prevBtn.style.display = "block"
    }
    fetch('/data?num=' + maxNumComments + "&page=" + currPage).then(response => response.json()).then((comments) => {
        const commentElement = document.getElementById('video-comments-container');
        commentElement.innerHTML = '';
        for (i = 0; i < comments.length; i++) {
            commentElement.appendChild(createCommentElement(comments[i]));
        }
    });
}

/** Create formatting for comment to be inserted */
function createCommentElement(comment) {
    const commentElement = document.createElement('div');
    commentElement.setAttribute("id", "video-comments");
    //add delete button
    const deleteButton = document.createElement("button");
    deleteButton.setAttribute("class", "delete");
    deleteButton.addEventListener('click', () => {
        deleteComment(comment);
        //remove task from DOM
        commentElement.remove();
    })
    deleteButton.innerText = "Delete";
    commentElement.appendChild(deleteButton);
    //add comment text
    const commentText = document.createElement('div');
    commentText.setAttribute("class", "comment-text");
    commentText.innerText = comment.text;
    commentElement.appendChild(commentText);
    //add comment footer
    const commentFooter = document.createElement('div');
    commentFooter.setAttribute("class", "comment-footer");
    const footerInfo = document.createElement('div');
    footerInfo.setAttribute("class", "footer-info");
    //if user did not provide name, set name as anonymous
    if (comment.name.trim() === '') {
        footerInfo.innerText = "Anonymous" + " | " + comment.timestamp;
    } else {
        footerInfo.innerText = comment.name + " | " + comment.timestamp;
    }
    commentFooter.appendChild(footerInfo);
    commentElement.appendChild(commentFooter);
    return commentElement;
}

/** Creates an <li> element containing text */
function createListElement(text) {
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}

/** Deletes all the comments and calls getComments to get comments from API with all comments now deleted*/
function deleteAll() {
    const request = new Request('/delete-data', { method: 'POST' });
    fetch(request).then(() => {
        getComments()
    });
}

/** Delete given comment */
function deleteComment(comment) {
    const params = new URLSearchParams();
    params.append('id', comment.id);
    fetch('/delete-comment', { method: 'POST', body: params });
}

/** Add user's comment */
function addComment() {
    let text = document.getElementById("comment-box").value;
    let name = document.getElementById("name-box").value;
    //if comment input is empty, can't submit
    if (text.trim() === '') return;
    document.getElementById("comment-box").value = '';
    fetch('add-comment?comment-text=' + text + '&name=' + name, { method: 'POST' });

    setTimeout(getComments, 500);
}

function nextPage() {
    currPage++;
    getComments();
}

function prevPage() {
    currPage--;
    getComments();
}

