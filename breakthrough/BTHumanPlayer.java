package breakthrough;
import boardgame.*;

/** Human Breakthrough client with GUI.
 *
 *  To run, set a large timeout on the server so the 
 *  human player has time to enter amove.
 */
public class BTHumanPlayer extends HumanPlayer {
    public BTHumanPlayer() { super( new BTBoard() ); } 
} // End class BTHumanPlayer

