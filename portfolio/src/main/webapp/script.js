/** Fetch quote from server and add to DOM */
function getRandomQuote(){
    fetch('/quotes').then(response => response.text()).then((quote) => {
        document.getElementById('quote-container').innerText = quote;
    })
}

/** Fetch comments from server and add to DOM */
function getComments(){
    maxNumComments = document.getElementById("num-comments").value;
    fetch('/data?num='+ maxNumComments).then(response => response.json()).then((messages) => {
        const commentElement = document.getElementById('video-comments-container');
        commentElement.innerHTML = '';
        for (i = 0; i < messages.length; i++){
            commentElement.appendChild(createCommentElement(messages[i]));
        }
    });
}

/** Creates formatting for comment to be inserted */
function createCommentElement(text){
    const commentElement = document.createElement('div');
    commentElement.setAttribute("id", "video-comments");
    const commentText = document.createElement('div');
    commentText.setAttribute("class", "comment-text");
    commentText.innerText = text;
    commentElement.appendChild(commentText);
    return commentElement;
}

/** Creates an <li> element containing text */
function createListElement(text){
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}

/** Deletes all the comments and calls getComments to get comments from API with all comments now deleted*/
function deleteAll(){
    const request = new Request('/delete-data', {method: 'POST'});
    fetch(request).then(() => {
        getComments()
    });   
}