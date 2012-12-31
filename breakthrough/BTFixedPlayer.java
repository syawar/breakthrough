package breakthrough;

import boardgame.*;

/** This is a "random" player which is guaranteed to always return the same move
 *  when presented with the same board. A stochastic policy is used, but the 
 *  random number generator is seeded as a function of the board position. This
 *  behavior can be changed by setting DETERMINISTIC_PLAY to false.
 */
public class BTFixedPlayer extends Player {
    
    /** If true, players of this class always choose the same 
     *  move for the same board position. */
    public boolean DETERMINISTIC_PLAY = true;

    /** Probability of making a capture if possible */
    protected static final double CAPTURE_PROB = 0.90;

    /* The ranking according to which pieces are chosen, the i-th piece index
     * in this array corresponds to the i-th element of the PIECE_PROB array.
     * entries may be positive or negative integers.*/
    protected static final int[]    PIECE_ORDER = new int[2*BTBoard.SIZE];;
    
    /* The cumulative probability of playing each piece */
    protected static final double[] PIECE_PROBS = new double[2*BTBoard.SIZE]; ;    

    /* The cumulative probability of moving in each direction: towards center,
       straight and away from center respectively. */
    protected static double DIR_PROBS[] = { .25, .85, 1.0 };

    // Class initialization
    static {
	// Assign the probability of each piece being chosen
	double sum = 0;
	for( int i = 0; i < PIECE_PROBS.length; i++ ) {
	    sum = sum + Math.exp( -i / 3.0 );
	    PIECE_PROBS[i] = sum;
	    PIECE_ORDER[i] = i;
	}
	// Normalize so that probabilites add to one
	for( int i = 0; i < PIECE_PROBS.length-1;i++)
	    PIECE_PROBS[i] /= sum;
	PIECE_PROBS[PIECE_PROBS.length-1] = 1.0;
    }

    /* The random number generator for selecting moves */
    protected final java.util.Random random = new java.util.Random();

    /* Default constructor */
    public BTFixedPlayer() {
	super( "fixed" );
    }

    /* Choose the move to be played. The board must allow a valid move. */
    public Move chooseMove( Board __board ) {
	final BTBoard board = (BTBoard) __board;

	// Select the random number generator seed
	if( DETERMINISTIC_PLAY ) {
	    // Compute the seed for the random number generator as a function
	    // of the board position only. We always get the same stream of 
	    // random numbers for the same board position. Since the order
	    // of the pieces below is well defined, we will always
	    // choose the same move for the same board position.
	    long seed  = 0 ;
	    for( int i = 0; i < BTBoard.SIZE*BTBoard.SIZE; i++ ) 
		seed = (3 * seed + board.getPieceAt(i)) % 947147262401L;
	    random.setSeed(seed);
	}   

	// What colour am I ?
	final int color = board.getTurn();
	// Which way is forward
	final int forward = (color == BTBoard.WHITE ? BTBoard.SIZE : -BTBoard.SIZE );

	// Find all the pieces
	Integer pieces[] = new Integer[2*BTBoard.SIZE];
	int next = 0;
	for( int i = 0; i < BTBoard.SIZE*BTBoard.SIZE; i++ ) {
	    if( board.getPieceAt(i) == color ) {
		pieces[next++] = new Integer(i);
	    }
	}

	if(next == 0) throw new IllegalArgumentException( "No valid moves found!" );
	
	// Sort the pieces by their row, and then distance from the 5th column. This is 
	// a stable sort, so the order of "equal" pieces is well defined by their
	// discovery order above.
	java.util.Arrays.sort( pieces, 0, next, new java.util.Comparator() {
		public int compare( Object o1, Object o2 ) {
		    int i = ((Integer)o1).intValue();
		    int j = ((Integer)o2).intValue();
		    if( board.getRow(i) == board.getRow(j) ) 
			return Math.abs(board.getCol(i)-BTBoard.SIZE/2) -
			    Math.abs(board.getCol(j)-BTBoard.SIZE/2);
		    else
			return (color == BTBoard.BLACK ? 1 : -1 ) *
			    (board.getRow(i) - board.getRow(j));
		}
	    } );

	// For each piece
	for( int i = 0; i < next; i++ ) {
	    int orig = pieces[i].intValue();
	    // For each possible move
	    for( int d = -1; d < 2; d++ ) {
		int dest = orig+forward+d;
		// If it can capture, do so with probability CAPTURE_PROB
		if( board.isLegal( color, orig, dest ) &&
		    board.getPieceAt( dest ) != 0 &&
		    random.nextDouble() < CAPTURE_PROB )
		    return  new BTMove( color, orig, dest );
	    }
	}

	// Select a piece index using the RNG
	double d1 = random.nextDouble();
	int i;
	for( i = 0; i < PIECE_PROBS.length; i++ )
	    if( PIECE_PROBS[i] >= d1 ) break;
	if( i > PIECE_PROBS.length ) i = PIECE_PROBS.length - 1;
	while( i < 0 ) i = next - i;
	int index = PIECE_ORDER[i] % next;
	
	// Select a direction using the RNG
	double d2 = random.nextDouble();
	for( i = 0; i < DIR_PROBS.length; i++ )
	    if( DIR_PROBS[i] >= d2 ) break;
	if( i > DIR_PROBS.length ) i = DIR_PROBS.length;
	int direction = i;

	int idx = index;
	int dir = direction;
	do {
	    do {
		// Compute actual move parameters
		int location = pieces[idx].intValue();
		int dest = location+forward;
		if( board.getCol( location ) <= BTBoard.SIZE/2 )
		    dest += (2-dir) - 1;
		else
		    dest += dir - 1;

		// If the move is legal, return it
		if( board.isLegal( color, location, dest ) )  
		    return new BTMove( color, location, dest );

		// Try the next direction
		dir = (dir + 1) % 3;

	    } while( dir != direction ); // until we get back to the original direction

	    // Try the next piece
	    idx = (idx + 1 ) % next;

	} while( idx != index ); // until we get back to the original piece
	
	// If we get here, there were no valid moves!
	throw new IllegalArgumentException( "No valid moves found!" );
    }
}



