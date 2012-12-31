
package breakthrough;

import boardgame.Move;


/**
 * Represent a move in the Breakthrough game. This class does
 * no error checking with respect to the validity of moves.
 * @author pkelle
 */
public class BTMove extends Move {
    protected int player;
    protected int orig;
    protected int dest;
    
    public BTMove( int player, int orig, int dest ) 
    throws IllegalArgumentException {
        this.player = player; this.orig = orig; this.dest = dest;
    }
    
    public BTMove( String str ) 
    throws NumberFormatException, IllegalArgumentException{
        fromString( str );
    }
    
    public int getPlayerID() { return player; }
    
    public static String toPrettyString( int player, int orig, int dest ) {
        return new StringBuffer()
            .append( (player == BTBoard.BLACK ? "BLACK " : "WHITE ") )
            .append( (char)('A' + (orig%BTBoard.SIZE)) )
            .append( orig/BTBoard.SIZE + 1 )
            .append( " -> " )
            .append( (char)('A' + (dest%BTBoard.SIZE)) )
            .append( dest/BTBoard.SIZE + 1 )
            .toString();
    }
    
    public String toPrettyString() {
        return toPrettyString( player, orig, dest );
    } 
    
    public static String toTransportable( int player, int orig, int dest ) {
        return new StringBuffer()
            .append( (player == BTBoard.BLACK ? "BLACK " : "WHITE ") )
            .append( orig ).append( ' ' ).append( dest ).toString();
    }
    
    public String toTransportable() { 
        return toTransportable(player, orig, dest); }
    
    public void fromString( String str ) 
    throws IllegalArgumentException, NumberFormatException {
        if( str.startsWith("BLACK") ) player = BTBoard.BLACK;
        else if( str.startsWith("WHITE") ) player = BTBoard.WHITE;
        else throw new IllegalArgumentException( "Colour must be BLACK or WHITE.");
        int idx = str.indexOf(' ', 6);
        orig = Integer.parseInt( str.substring(6, idx).trim() );
	str = str.substring( idx + 1).trim();
	idx = str.indexOf( ' ' );
	if( idx < 0 ) idx = str.length();
        dest = Integer.parseInt( str.substring(0, idx).trim() );
    }
}
