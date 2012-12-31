import breakthrough.*;
import boardgame.*;
import java.io.*;
import java.util.*;
/*Saqib Khalil Yawar
	260268411
*/
/** This player uses a neural net with a single output
 *  unit to determine whether boards are likely to win.
 *
 *  It picks the board resulting in the highest activation
 *  for the single output neuron after one move. It does
 *  no search. 
 *
 *  See NetTrainer.java for training the network.
 */
public class BTNeuralNetPlayer extends Player {
    /* File from which weights are loaded when playing. */
    static final String NETWORK_FILE = "network.txt";

    /* Number of features we use. */
    static final int NUM_FEATURES = 9;
    
    /* Temporary work space */
    private double theFeatures[] = new double[NUM_FEATURES];

    /* The network used to evaluate boards */
    private NeuralNet theNet;
    protected NeuralNet getNeuralNet() { return theNet; }
    
    /** Default constructor: tries to load weights for the perceptron 
       from a file.*/
    public BTNeuralNetPlayer() {
	super( "neural" );
	try {
	    theNet = new NeuralNet( NETWORK_FILE );
	} catch (Exception e) {
	    System.err.println( "Oh dear, I can't read the '" + 
				NETWORK_FILE + 
				"' file! Using a random network. " + 
				"All hope is lost!!!" );
	    // Use a random neural net with no hidden layer
	    int[] struct = { NUM_FEATURES,  1 };
	    theNet = new NeuralNet( struct );
	} 
    }
    
    /** Create a player with the specified network structure and 
	random weights. */
    protected BTNeuralNetPlayer( int structure[] ) {
	super( "neural" );
	theNet = new NeuralNet( structure );
    }

    /* Chooses the move leading to the board with the highest value
       according to the network. We are not doing any search here. */
    public Move chooseMove( Board __board ) {
	final BTBoard board = (BTBoard) __board;
	final int color = board.getTurn();
	final int forward = 
	    (color == BTBoard.WHITE ? BTBoard.SIZE : -BTBoard.SIZE );
	// The current best move
	BTMove myMove = null;
	double bestValue = Double.NEGATIVE_INFINITY;
	// For each position
	for( int i = 0; i < (BTBoard.SIZE*BTBoard.SIZE); i++ ) {
	    // If my piece is there
	    if( board.getPieceAt(i) == color ) {
		// For each direction
		for( int step = forward-1; step < forward+2; step++ ) {
		    if( board.isLegal( color, i, i+step ) ) {
			// How good is the next board
			BTBoard nextBoard = new BTBoard( board );
			nextBoard.moveFast( color, i, i+step );
			double[] features = featurize( nextBoard, color );
			// Look at the first output value of the net 
			// (there should only be one)
			double netOutput = theNet.query( features )[0];
			// If this is the best move so far, remember it
			if( netOutput > bestValue ) {
			    myMove = new BTMove( color, i, i+step);
			    bestValue = netOutput;
			}			
		    }
		}
		    
	    }
	}       
	return myMove;	
    }
    
    /** Computes some features of the board:
     *   0 - A random number, just for the fun of it. 
     *       The net should learn to ignore this.
     *   1 - 1 if this is a winning board, 0 otherwise.
     *       This is a perfect indicator for final boards.
     *   2 - 1 if our foremost piece moved further than 
     *       the opponent, 0 otherwise
     *   3 - Sum squared distances moved by my pieces, 
     *       divided by 200
     *   4 - Same as 3, for the opponent
     *   5 - Number of my pieces left
     *   6 - Number of opponent pieces left
     *   7 - Maximum distance moved by any of my pieces, divided by 7
     *   8 - Maximum distance moved by any opponent piece, divided by 7
     *  These are very basic features, and should be 
     *  improved upon.
     */
    public double[] featurize( BTBoard board, int color ) {
	Arrays.fill( theFeatures, 0 );
	// The first feature is always -1
	theFeatures[0]= 0;	
	for(int i=0;i<8;i++){
		if(board.getPieceAt(1,i)== 3-color)
		{
			theFeatures[0]=-0.01;
		}
	}
	// Then check if we won
	theFeatures[1] = (board.getWinner() == color ? 1 : 0 );
	// Then check if we lost
	theFeatures[2] = (board.getWinner() == 
			  (color^(BTBoard.BLACK|BTBoard.WHITE)) ? 1 : 0);
	// For now, assume we are white; swap values later if not
	for( int i = 0; i < BTBoard.SIZE*BTBoard.SIZE; i++ ) {
	    int piece = board.getPieceAt(i);
	    if( piece == BTBoard.WHITE ) {
		int r = board.getRow(i); 
		int c = board.getCol(i);
		theFeatures[3] += r * r;
		theFeatures[5] += 1;
		if( r > theFeatures[7] ) theFeatures[7] = r;
	    } else if( piece == BTBoard.BLACK ) {
		int r = BTBoard.SIZE - board.getRow(i); 
		theFeatures[4] += r * r;
		theFeatures[6] += 1;
		if( r > theFeatures[8] ) theFeatures[8] = r;
	    }
	}
	theFeatures[3] /= 200;
	theFeatures[4] /= 200;
	theFeatures[7] /= 7;
	theFeatures[8] /= 7;
	theFeatures[2] = theFeatures[7] > theFeatures[8] ? 1 : 0;
	// If we're BLACK, we need to swap some of the features
	if( color != BTBoard.WHITE ) {
	    double tmp = theFeatures[3];
	    theFeatures[3] = theFeatures[4];
	    theFeatures[4] = tmp;
	    tmp = theFeatures[5];
	    theFeatures[5] = theFeatures[6];
	    theFeatures[6] = tmp;	    
	    tmp = theFeatures[7];
	    theFeatures[7] = theFeatures[8];
	    theFeatures[8] = tmp;	    
	    theFeatures[2] = 1 - theFeatures[2];
	}
	return theFeatures;
    }

    /* Dump the feature values to the console */
    public void printFeatures() {
	for( int i = 0; i < NUM_FEATURES; i++ ) {
	    System.out.print( theFeatures[i] );
	    System.out.print( '\t' );
	}
	System.out.print( '\n' );
    }

}


