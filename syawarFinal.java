package breakthrough;

import java.util.Random;
import java.lang.*;


import boardgame.Board;
import boardgame.Move;
import boardgame.Player;

public class Syawar extends Player {
   Random rand = new Random();
   public int maxRank=0;
    /** Provide a default public constructor */
   public Syawar() { super("syawar"); }
   
   //tree created to check 
	public class TreeNode {
              int x,y;
              TreeNode parent;
              TreeNode centre;      
              TreeNode left;   
           	  TreeNode right; 
              int checked;
              int ranking; 
           }
   //Find all my pieces
    public int[][] Position(BTBoard board, int myColor){
    int[][] pieces = new int[2*BTBoard.SIZE][2];
    int next = 0;
    for( int i = 0; i < BTBoard.SIZE; i++ )
        for( int j = 0; j < BTBoard.SIZE; j++ )
            if( board.getPieceAt(i,j) == myColor ) {
                pieces[next][0]= i;
                pieces[next][1]=j;
                next++;
            }
	return pieces;
    }
    //evaluates resulting board from moving a certain piece in all available directions
    public TreeNode EvaluatePiece(int x,int y,int myColor){
    	TreeNode checked=
    	if(myColor==BTBoard.WHITE){
    		
    	}
    	else
    	{
    		
    	}
    }
     public Move chooseMove(Board theboard) {
        // Cast the arguments to the objects we want to work with
        BTBoard board = (BTBoard) theboard;
        BTBoard clone = (BTBoard) board;        
        int myColor=this.getColor();
        
} 


