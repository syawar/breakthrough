	Board Game Client/Server packages
Author: Philipp Keller <pkelle@cs.mcgill.ca>
Last update: September 21, 2005
---------------------------------
Contents: 1-Overview
          2-Compiling
          3-Launching the Server
          4-Launching a Client
          5-Launching the logfile viewer
          6-Human players
          7-Implementing a Player
          8-Implementing a different game
          9-Changes  


1-Overview
----------
This software consists of two packages:
* boardgame: a generic board game client/server package and
* breakthrough: an implementation of the 'BreakThrough' game based on
it.

The boardgame package is intended to allow the implementation of
different board games and AI players with minimal effort. It provides
networking support, log file management, and GUI support. No
familiarity with this code is required to build an AI player, and only
limited knowledge is required to implement an entire game. 

Breakthrough, designed by Dan Troyka, is the winning entry of the 
2001 8x8 Game Design Competition, sponsored by About Board Games, the
Strategy Gaming Society and Abstract Games magazines. This is the
default game launched by the boardgame server and client classes.

To play, two clients (the players) connect to the server via TCP 
sockets. This allows the clients to be run on seperate computers. 
A GUI, which can be run by the server to display an ongoing game or 
manually to examine existing log files is provided but is not required
to run the servers or clients.

THIS SOFTWARE HAS NOT BEEN EXTENSIVELY TESTED. PLEASE REPORT ANY BUGS
ASAP!

This software was built and tested using Sun JDK 1.5.0, but is
source-compatible with Sun JDK 1.4 (I think!).

Files:

ROOT
|-- boardgame
|   |-- Board.java               Abstract game board logic class
|   |-- Move.java                Abstract game move class
|   |-- Player.java              Abstract player class
|   |-- Client.java              Generic client, implements networking
|   |-- Server.java              Generic server, implements networking
|   |-- HumanPlayer.java         Generic remote GUI client
|   |-- ServerGUI.java           Main GUI class
|   `-- BoardPanel.java          Displays and gets input for a board in GUI
|-- breakthrough
|   |-- BTBoard.java             Board implementation, contains game logic
|   |-- BTMove.java              Move implementation, just data storage
|   |-- BTBoardPanel.java        Custom display for GUI
|   |-- BTHumanPlayer.java       Remote GUI client
|   |-- BTFixedPlayer.java       Fixed policy player
|   `-- BTRandomPlayer.java      Random player
|-- image
|   |-- first.png                Icons for GUI
|   |-- last.png
|   |-- next.png
|   `-- prev.png
`-- README.txt                   This file


2-Compiling
-----------
javac boardgame/*.java breakthrough/*.java


3-Launching the Server
----------------------
Ensure that the root directory (containing the 'boardgame' and 'breakthrough'
directories) is in your classpath, e.g. by making it your working directory.

java boardgame.Server [-p port] [-ng] [-q] [-t n] [-b class]
  Where '-p port' sets the port to listen on. (default=8123)
        '-ng' indicates not to show a GUI.
        '-q' indicates not to dump log to console.
        '-t n' sets the timeout. (default=5000)
        '-b class' determines the game to run. (default=breakthrough.BTBoard)
        '-k' indicates to start a new server once a game is running  
  e.g.
    java boardgame.Server -p 8123 -t 5000 -b breakthrough.BTBoard
  launches a server with a GUI and the default parameters.

The server waits for two clients to connect and does not accept any
more connections unless the '-k' flag is passed as an argument.

Closing the GUI window will NOT terminate the server. The server exits
once the game is finished.

Log files are written to the 'log' subdirectory.


4-Launching a Client
--------------------
The server waits for two clients to connect before starting the game. 
The first player is assigned the colour white, and the second is assigned
black.
 
Clients are launched with:

java boardgame.Client [PlayerClass [serverName [serverPort]]]
  Where PlayerClass is the player to be run (default=
						breakthrough.BTRandomPlayer)
        serverName is the server address (default=localhost) and
        serverPort is the port number (default=8123).
  e.g.
    java boardgame.Client breakthrough.BTRandomPlayer localhost 8123
  launches a client with the default parameters.

The game starts as soon as the two clients are connected.

If using the server GUI, it is also possible to launch clients to be run 
on the same machine as the server from the 'Launch' menu. This starts a 
regular client running in a background thread.


5-Launching the logfile Viewer
------------------------------
It is possible to run the server GUI without starting a server to 
examine existing log files. Moves are loaded from the file as though
the game was being played on a server.

java boardgame.ServerGUI [filename] 
  Where filename is an optional log file to open.
e.g.
java boardgame.ServerGUI logs/game00001.log


6-Human players
---------------
ON THE SERVER GUI:
To play against a sofware player or another person, launch the server
with a GUI. The status bar indicates that the server is waiting for a
client connection. Select "Launch Human Player" from the "Launch" menu
to start a fake client which will obtain moves through the GUI. Launch
another client from the command line or form the "Launch" menu.  You
can make moves for the human player by dragging a token to a desired location.

REMOTE HUMAN CLIENT:
Humans can also connect to a remote server by passing the Player class
breakthrough.BTHumanPlayer to the client software. This player obtains
moves from a GUI on its turn. 
i.e.: 
   java boardgame.Client breakthrough.BTHumanPlayer
brings up a GUI to choose moves for the client
.
IMPORTANT: Set a large timeout for the server for remote clients since
the server doesn't know whether it is communicating with a computer 
or human player.


7-Implementing a Player
-----------------------
To implement a new player, extend the boardgame.Player class, and 
pass the new class as the argument to the client software. See the 
file 'Player.java' for further instructions, and 'BTRandomPlayer.java'
for an example. 

To lauch custom players from the GUI, add the class name to the array
ServerGUI.PLAYER_CLASSES in the 'ServerGUI.java' file.


8-Implementing a different game
-------------------------------
To implement a different game, extend the boardgame.Board class and
pass the new class as the argument to the server software. See the
file 'Board.java' for instructions and 'BTBoard.java' for an example.

All the generic code is in the 'boardgame' package, while the
'breakthrough' package contains the Breakthrough-specific code.

To fully implement a new game:
- Implement game rules in a boardgame.Board subclass.
- Implement the panel to display the board as a boardgame.BoardPanel
  subclass. 
- To allow human players, implement the user input methods in the 
  boardgame.BoardPanel subclass.
- To allow servers to be launched from the GUI, add the Board class
name to the array ServerGUI.BOARD_CLASSES in the 'ServerGUI.java'
file.

Only the fist step is needed to run the server without displaying a
board.

The classes of the breakthrough package provide an example.


9-Changes
---------
v0.01 2005/07/12 - Initial version
v0.02 2005/09/12 - Added breakthrough.BTFixedPlayer
                 - Changed breakthrough.BTBoard to recognize absence
                   of pieces as a loss
v0.03 2005/09/16 - Made breakthrough.BTFixedPlayer Java 1.4 compatible
v0.04 2005/09/21 - Added HumanPlayer.java and BTHumanPlayer.java
