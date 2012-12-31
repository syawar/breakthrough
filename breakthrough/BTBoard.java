package breakthrough;

import java.util.Arrays;

import boardgame.Board;
import boardgame.BoardPanel;
import boardgame.Move;

/**
 * Breakthrough board representation. The board is represented as a
 * one dimensional array with a coord in the i-th row and j-th column
 * at index <CODE>(i-1) * SIZE + (j-1)</CODE>. 
 * @author pkelle
 */
public class BTBoard extends Board {
    /** Board size */
    public static final int SIZE = 8;
    /** Maximum number of turns before a draw. */
    public static final int MAX_TURNS = 200;
    
    /* Use only the low order two bits to indicate tokens. */
    public static final int EMPTY = 0x00;
    public static final int BLACK = 0x02;
    public static final int WHITE = 0x01;
    
    protected final int data[] = new int[SIZE*SIZE];
    protected int turn = WHITE;
    protected int winner = NOBODY;
    protected int turnCount = 0;
    protected int numPieces[] = {0,16,16};
    
    /** Returns a board in the starting position. */
    public BTBoard() {
        Arrays.fill(data, 0, 2*SIZE, WHITE);
        Arrays.fill(data, SIZE*SIZE-2*SIZE, SIZE*SIZE, BLACK );
    }
    
    /** Returns a deep copy of another board. */
    public BTBoard( BTBoard bb ) {
        System.arraycopy( bb.data, 0, this.data, 0, data.length );
        this.turn = bb.turn;
        this.winner = bb.winner;
        this.turnCount = bb.turnCount;
    }
    
    /** Get the array containing the board data directly */
    public int[] getBoardData() { return  data; }
    
    /** Get the value at a position: return BLACK, WHITE or EMPTY.
     * This used 0-based coordinates. */
    public int getPieceAt( int index ) { return data[index] & (BLACK|WHITE); }
    public int getPieceAt( int row, int col ) { 
        return data[row*SIZE+col]  & (BLACK|WHITE); }
    
    /** Convert between array index and (row,col) pairs */
    public int getCoord( int row, int col ) { return row * SIZE + col; }
    public int getRow( int coord ) { return coord / SIZE; }
    public int getCol( int coord ) { return coord % SIZE; }
    
    /** Return winner colour, DRAW or NOBODY if no winner yet. */ 
    public int getWinner() { return winner; }
    
    /** Set a winner without finishing the game. */
    public void forceWinner( int win ) {
        if( win != WHITE && win != BLACK && win != DRAW )
            throw new IllegalArgumentException( 
                    "Winner must be one of BLACK, WHITE or DRAW.");
        this.winner = win;
    }
    
    /** Return the next player. */
    public int getTurn() { return turn; }
    
    /** Get the number of turns played. */
    public int getTurnsPlayed() { return turnCount; }
    
    /** Verify legality of a move. This depends on the current turn. */
    public boolean isLegal( Move mm ) { 
        BTMove m = (BTMove) mm; return isLegal( m.player, m.orig, m.dest ); }
    
    /** Verify legality of a move. This depends on the current turn. */
    public boolean isLegal( int player, int orig, int dest ) {
        int delta;
        if( player == WHITE ) {
            delta = dest - orig;
        } else if( player == BLACK ) {
            delta = orig - dest;
        } else throw new IllegalArgumentException( 
                "Player colour must be black or white!");
        int horizchange = dest % SIZE - orig % SIZE;
        return !(
                ( horizchange > 1 ) || // No wraparound
                ( horizchange < -1 ) || // No wraparound
                ( delta < SIZE-1 || delta > SIZE + 1 ) || // Possible dests
                ( dest >= SIZE*SIZE || dest < 0 || 
                        orig >= SIZE*SIZE || orig < 0 ) || // Bound check
                ( (data[dest] & player) != 0 ) ||  // Capture opponents only
                ( (data[orig] & player) == 0 ) ||  // Right colour
                ( horizchange == 0 && (data[dest] & (BLACK|WHITE)) > 0) || // Diagonal captures only
                ( player != turn ) ||              // Right turn 
                ( winner != NOBODY )   // Is game over ?
                );  
    }
    
    /** Execute a move. */
    public void move( Move mm ) throws IllegalArgumentException 
    { BTMove m = (BTMove) mm; move( m.player, m.orig, m.dest ); }
    
    /** Execute a move. */
    public void move( int player, int orig, int dest ) 
    throws IllegalArgumentException {
        if( ! isLegal( player, orig, dest ) )
            throw new IllegalArgumentException(
                    "Illegal move: " + BTMove.toTransportable( player ,orig, dest) );
        moveFast( player, orig, dest );
    }
    
    /** Execute a move without checking legality. */
    public void moveFast( int player, int orig, int dest ) {
        int opponent = player ^ (BLACK|WHITE);
	    // Modify only the 2 LSBs
	    int oldDest = data[dest];
        data[orig] = data[orig] & ~(BLACK|WHITE);
        data[dest] = ( data[dest] & ~(BLACK|WHITE) ) | player;
        // Check victory condition
        if( player == WHITE && dest >= SIZE*SIZE - SIZE ) winner = WHITE;
        else if( player == BLACK && dest < SIZE ) winner = BLACK;
        // Check if pieces remain for the opponent
        if( (oldDest & opponent) != 0 && (--numPieces[opponent]) <= 0 )
	        winner = player;
	    // Change turn and increase the count
        turn = turn ^ (BLACK|WHITE);
        turnCount++;
    }
    
    /** Return a copy of this board */
    public Object clone() {
        return new BTBoard( this );
    }

    /** String representation of a board */
    public String toString() {
        StringBuffer b = new StringBuffer();
        b.append( "After " ).append(turnCount ).append(" turns, ");
        if( winner != NOBODY ) {
            if( winner == DRAW ) b.append( "it's a DRAW.");
            else b.append( getNameForID(winner) ).append( " won.");
        } else {
            b.append( getNameForID(turn) );
            b.append( " to play.");
        }
        for( int i=0; i < SIZE*SIZE; i++ ) {
            if( i%SIZE == 0 ) b.append('\n');
            b.append( (data[i] & BLACK) != 0 ? 'X' : 
                ((data[i] & WHITE) != 0 ? 'O' : '.' ) ).append(' ');
        }
        return b.toString();
    }
    
    public String getNameForID( int p ) {
        switch( p ) {
        case BLACK: return "BLACK";
        case WHITE: return "WHITE";
        default: throw new IllegalArgumentException( 
                "Valid player IDs are 1 (BLACK) and 2 (WHITE). Passed " + 
                p + ".");
        }
    }
    
    public int getIDForName( String s ) {
        if( s.equals("BLACK") ) return BLACK;
        if( s.equals("WHITE") ) return WHITE;
        throw new IllegalArgumentException(
                "Valid player IDs are 1 (BLACK) and 2 (WHITE).");
    }

    public int getNumberOfPlayers() { return 2; }
    
    public Move parseMove( String str ) 
    throws NumberFormatException, IllegalArgumentException{
            return new BTMove(str);
    }
   
    /** Return a custom BoardPanel so that we can accept user input */
    public BoardPanel createBoardPanel() { return new BTBoardPanel(); }

} // End class Board


