# Chessonomics-server

## What is it
Chessonomics-server is a TCP server for chessonomics game. Chessonomics is chess with modified rules.

## How to run it
`./gradlew run`

server will start listening to port 1993

## How to stop it
kill the process (e.g. CTRL+Z)

## Protocol
Server works with lines of UTF-8 text. Every line should contain 
a command and arguments (just like command-line interaction). 

## Available commands

### login [player_name] 
this command should be called first (right after connection). Server responds with 'hello [player_name]'

### find
puts the player into queue of searching players. When opponent is found server responds with 'started [opponent_name] [side]'

### move [move]
make a move. Server will validate the move and resend it to the opponent.
Notice that [move] may me 'null' if player decided to skip the move (make null move).

### spawn [piece type] [square]
spawn new piece. Server will check if player has enough points to perform spawn 