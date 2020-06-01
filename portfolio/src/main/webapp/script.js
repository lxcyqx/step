function getRandomQuote(){
    fetch('/data').then(response => response.text()).then((quote) => {
        document.getElementById('quote-container').innerText = quote;
    })
}