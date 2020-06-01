/** Fetch quote from server and add to DOM */
function getRandomQuote(){
    fetch('/quotes').then(response => response.text()).then((quote) => {
        document.getElementById('quote-container').innerText = quote;
    })
}

/** Fetch comments from server and add to DOM */
function getComments(){
    fetch('/data').then(response => response.json()).then((messages) => {
        const commentElement = document.getElementById('video-comments');
        commentElement.innerHTML = '';
        commentElement.appendChild(createListElement(messages[0]));
        commentElement.appendChild(createListElement(messages[1]));
        commentElement.appendChild(createListElement(messages[2]));
    });
}

/** Creates an <li> element containing text */
function createListElement(text){
    const liElement = document.createElement('li');
    liElement.innerText = text;
    return liElement;
}