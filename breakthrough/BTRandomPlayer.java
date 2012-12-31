package breakthrough;

import java.util.Random;

import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

/**
 *A random BreakThrough player.
 */
public class BTRandomPlayer extends Player {

    Random rand = new Random();
    
    /** Provide a default public constructor */
    public BTRandomPlayer() { super("dummy"); }
    
    /** Implement a very stupid way of picking moves */
    public Move chooseMove(Board theboard) {
        // Cast the arguments to the objects we want to work with
        BTBoard board = (BTBoard) theboard;
                
        // What color am I ?
        int myColor = this.getColor();
        
        // Find all my pieces
        int[][] pieces = new int[2*BTBoard.SIZE][2];
        int next = 0;
        for( int i = 0; i < BTBoard.SIZE; i++ )
            for( int j = 0; j < BTBoard.SIZE; j++ )
                if( board.getPieceAt(i,j) == myColor ) {
                    pieces[next][0]= i;
                    pieces[next][1]=j;
                    next++;
                }
                
        // Try to find a random valid move a few times, 
        // or give up an play an illegal move
        int idx, dir;
        int i1, j1, i2, j2;
        for( int k = 0; true; k++ ) {
            // Pick a piece at random   
            idx = rand.nextInt(next);
            // Pick a direction at random (left, right, forward)
            dir = rand.nextInt(3) - 1;
            // Play if the move is valid or we've tried enough times 
            i1 = pieces[idx][0]; 
            j1 = pieces[idx][1];
            i2 = i1 + (myColor == BTBoard.WHITE ? 1 : -1);
            j2 = j1 + dir;
            BTMove m = new BTMove( myColor, 
                    board.getCoord(i1,j1), 
                    board.getCoord(i2,j2) );
            if( k >= 50 || board.isLegal( m ) ) {
                System.out.println( (k >= 50 ? "I give up, " : "") +
                        "I play " + m.toPrettyString() );
                return  m;
            }
        }
    }
    
} // End class
