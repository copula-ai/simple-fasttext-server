# simple-fasttext-server

You send a REST GET with some text, and get a classification for it.  
Work In Progress.

## Installation

1. install [leiningen](https://leiningen.org/)
2. clone this repo
3. change directory to the included directory `fasttext`.
4. place your trained fasttext model here ― it should be named `classifier.bin`.
5. git clone https://github.com/facebookresearch/fastText.

## Usage

to start the server:

```
lein run [port]
```

to get a response make an http `get` with the single query parameter `text`. E.g. with curl this would be:
```
curl -G http://localhost:3001/predict --data-urlencode "text=classify me"
```
