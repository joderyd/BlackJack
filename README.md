# BlackJack
Rigged BlackJack game used for bachelor thesis.

Used for the bachelor thesis [Analysis of positive auditory feedback in relation to increased risk taking amongst online gamblers ](http://kth.diva-portal.org/smash/record.jsf?pid=diva2%3A1229511&dswid=7910) by Jonathan Öderyd & Carl Svedhag at KTH.

The reason for sharing the code is so the readers of the thesis can run the program to get a sense of the game that was used in the thesis.
The only goal of the program is to fool the players into believing that it was an ordinary game, and not rigged to always give the same cards on the 15 hands that can be played.

## DISCLAIMER
The program is written as "code and fix" due to time limits. The code comes with a lot of code repetitions, hard coding, limitations, possible run time errors, deadlocks and lots of other code smell. The latter parts in the file BlackJackBoard.java contains many different classes that extends TimerTask and Thread. I would not recommend attempting to understand it, as it was a "panick solution" for me to solve the timing of all the sounds perfectly (**I had not learned about concurrency and threads at the time**). This was important to make the game appear like a trusted regular online casino game.
No errors occured during the experiment in the bachelor thesis as we observed the users, which was the only requirement of this program.

I have no intentions of improving the code in the future as the only purpose was to only use it for the thesis.


Easiest way to run the program:
The file PlayGame.java includes the line:
new class new BlackJackBoard("Black Jack", 200, "playerName", true); 

Change arguments to the ones desired and just run the file
start money : change 200 to whatever you like 
playerName : determines the name of the textfile where output is stored (.txt is automatically appended)
sound : false only plays sounds when cards are being shuffeled and dealt. "true" will also play s congratulating sound on winning hands. 
(The thesis examined if the congratulating sounds could lead casino players online to bet more money)
