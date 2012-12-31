import breakthrough.*;
import boardgame.*;
import breakthrough.*;

/** A slightly improved version of the fixed player 
    which DOES NOT play a fixed strategy. It seems to 
    be just good enough that a neural net player can 
    learn something useful from it... */
public class BTHeuristicPlayer extends BTFixedPlayer {
    // Class initialization
    // Give higher probability to 
    // pieces earlier in the sorted list of moves. This makes 
    // the player more "aggressive".
    //
    // This is really hacky coding since we're changing a static
    // field of the superclass. That means any BTFixedPlayer
    // instances will also use these new values after this 
    // class is loaded.
    static {
	// Assign the probability of each piece being chosen
	double sum = 0;
	for( int i = 0; i < PIECE_PROBS.length; i++ ) {
	    sum = sum + Math.exp( -i / 1.0 );
	    PIECE_PROBS[i] = sum;
	}
	// Normalize so that probabilites add to one
	for( int i = 0; i < PIECE_PROBS.length-1;i++)
	    PIECE_PROBS[i] /= sum;
	PIECE_PROBS[PIECE_PROBS.length-1] = 1.0;
    }

    public BTHeuristicPlayer() {
	super();
	// Don't play deterministically
	this.DETERMINISTIC_PLAY = false;
    }
}