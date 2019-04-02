# BlackJack
Rigged BlackJack game used for bachelor thesis.

Used for the bachelor thesis [Analysis of positive auditory feedback in relation to increased risk taking amongst online gamblers ](http://kth.diva-portal.org/smash/record.jsf?pid=diva2%3A1229511&dswid=7910) by Jonathan Öderyd & Carl Svedhag at KTH.

The reason for sharing the code is so the readers of the thesis can run the program to get a sense of the game that was used in the thesis.
The only goal of the program is to fool the players into believing that it was an ordinary game, and not rigged to always give the same cards on the 15 hands that can be played.

The program is written as "code and fix" due to time limits. The code comes with a lot of code repetitions, hard coding, limitations, possible run time errors, deadlocks and lots of other code smell. No errors occured during the experiment in the bachelor thesis as we could observe the users.

I have no intentions of improving the code as the purpose was to only use it for the thesis. 

Easiest way to run the program:
The file PlayGame.java includes the line:
new class new BlackJackBoard("Black Jack", 200, "playerName", true); 

Change arguments to the ones desired and just run the file
start money : change 200 to whatever you like
playerName : determines the name of the textfile where output is stored (.txt is automatically appended)
sound : false only plays sounds when cards are being shuffeled and dealt. "true" will also play sound on winning hands.
