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
	public class TreeNode {
              int x,y;
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
    public int rank(BTBoard board,int myColor){
    int count=0;
    int[][] pieces = new int[2*BTBoard.SIZE][2];
    int next = 0;
    for( int i = 0; i < BTBoard.SIZE; i++ ){
        for( int j = 0; j < BTBoard.SIZE; j++ ){
            if( board.getPieceAt(i,j) == myColor ) {
                pieces[next][0]= i;
                pieces[next][1]=j;
                next++;
                if(i>=count && myColor == BTBoard.WHITE){
                	count=i;
                }
                if(i<=8-count && myColor == BTBoard.BLACK){
                	count=8-i;
                }
            }
 			}       
     }
     
     return count;
    }
    //recursive checking of moves
    public void myMoveCheck(BTBoard clone,int myColor,int ranked,int count){
    	if(count<3){	
    		int[][] pieces = new int[2*BTBoard.SIZE][2];
	 		int[][] temp = new int[2*BTBoard.SIZE][2];
	 		for(int i=0;i< 2*BTBoard.SIZE;i++){
	 			pieces[i][0]=-1;
	 			pieces[i][1]=-1;
	 		
	 			}
    		pieces=this.Position(clone,myColor);
    		for(int i=0;i<2*BTBoard.SIZE;i++){
    			 for( int dir = -1; dir < 2; dir++ ) {
    			 	int x=pieces[i][0];
    			 	int y=pieces[i][1];
	    		 	int i2 = x + (myColor == BTBoard.WHITE ? 1 : -1);
       		 	int j2 = y + dir;
       	 		BTBoard tempboard = (BTBoard) clone; 
	    		 	BTMove m = new BTMove(myColor, tempboard.getCoord(x,y),tempboard.getCoord(i2,j2) );
	    	 		if(tempboard.isLegal( m )){
						tempboard.move(m);
						int ranking=ranked+rank(tempboard,myColor);
						OppMoveCheck(tempboard,3-myColor,ranking,count++);	    	 	
	    	 		}
       		 }
    		}
    	}
    	else
    	{
    		this.maxRank=ranked;
    	}
    }
    public void OppMoveCheck(BTBoard clone,int myColor,int ranked,int count){
		if(count<4){	
    		int[][] pieces = new int[2*BTBoard.SIZE][2];
	 		int[][] temp = new int[2*BTBoard.SIZE][2];
	 		for(int i=0;i< 2*BTBoard.SIZE;i++){
	 			pieces[i][0]=-1;
	 			pieces[i][1]=-1;
	 		
	 			}
    		pieces=this.Position(clone,myColor);
    		for(int i=0;i<2*BTBoard.SIZE;i++){
    			 for( int dir = -1; dir < 2; dir++ ) {
    			 	int x=pieces[i][0];
    			 	int y=pieces[i][1];
	    		 	int i2 = x + (myColor == BTBoard.WHITE ? 1 : -1);
       		 	int j2 = y + dir;
       	 		BTBoard tempboard = (BTBoard) clone; 
	    		 	BTMove m = new BTMove(myColor, tempboard.getCoord(x,y),tempboard.getCoord(i2,j2) );
	    	 		if(tempboard.isLegal( m )){
						tempboard.move(m);
						int ranking=ranked-((rank(tempboard,myColor)));
						myMoveCheck(tempboard,3-myColor,ranking,count++);	    	 	
	    	 		}
       		 }
    		}
    	}
    	
    }
     public Move chooseMove(Board theboard) {
        // Cast the arguments to the objects we want to work with
        BTBoard board = (BTBoard) theboard;
        BTBoard clone = (BTBoard) board;        
        // What color am I ?
        int myColor = this.getColor();
			//System.out.println(myColor);
        int[][] pieces = new int[2*BTBoard.SIZE][2];
        int[][] OPPpieces = new int[2*BTBoard.SIZE][2];
        pieces=this.Position(board,myColor);
			//OPPpieces=this.Position(board,3-myColor);
		
		  int[][] oppieces = new int[2*BTBoard.SIZE][2];
	 		int[][] temp = new int[2*BTBoard.SIZE][2];
	 		for(int i=0;i< 2*BTBoard.SIZE;i++){
	 			oppieces[i][0]=-1;
	 			oppieces[i][1]=-1;
	 		
	 			}
	 		int[][] playing=new int [48][4];
	 		
	 		for(int i=0;i< 48;i++){
	 			playing[i][0]=-1;
	 			playing[i][1]=-1;
	 		
	 			}
    		oppieces=this.Position(clone,myColor);
    		for(int i=0;i<2*BTBoard.SIZE;i++){
    			int count=0;
    			int max=this.maxRank;
    			 for( int dir = -1; dir < 2; dir++ ) {
    			 	int x=oppieces[i][0];
    			 	int y=oppieces[i][1];
	    		 	int i2 = x + (myColor == BTBoard.WHITE ? 1 : -1);
       		 	int j2 = y + dir;
       	 		BTBoard tempboard = (BTBoard) clone; 
	    		 	BTMove m = new BTMove(myColor, tempboard.getCoord(x,y),tempboard.getCoord(i2,j2) );
	    	 		if(tempboard.isLegal( m )){
						tempboard.move(m);
						playing[i+count][0]=dir;
						int ranking=rank(tempboard,myColor);
						OppMoveCheck(tempboard,3-myColor,ranking,1);	
						if(max<this.maxRank){playing[i+count][1]=this.maxRank;}
						else {playing[i+count][1]=0;}    	 	
	    	 		}
	    	 		count++;
       		 }
    		}
    		int movePiece=0;
    		int moveDir=0;
    		int max=0;
    		double mP=0;
    		boolean capture=false;
    		int e=0,f=0,g=0,h=0;
    		int[][] capt=new int[48][5];
    		for(int i=0;i< 48;i++){
	 			capt[i][0]=-1;
	 		
	 			}
    		
    		for( int i = 0; i < 2*BTBoard.SIZE; i++ ) {
	    			int orig1 = oppieces[i][0];
	    			int orig2 = oppieces[i][1];
	    			int counter=0;
	   			 // For each possible move
	    			for( int d = -1; d < 2; d++ ) {
	    				int dest1 = orig1+(myColor == BTBoard.WHITE ? 1 : -1);
						int dest2 = orig2+d;
						// If it can capture
						BTMove moveC = new BTMove( myColor,board.getCoord(orig1,orig2),board.getCoord(dest1,dest2) );
						if( board.isLegal( moveC ) &&
		   			 board.getPieceAt( board.getCoord(dest1,dest2))!=0 )
		   			 {
		   			 	capture=true;
		   			 	capt[i+counter][0]=orig1;
		   			 	capt[i+counter][1]=orig2;
		   			 	capt[i+counter][2]=dest1;
		   			 	capt[i+counter][3]=dest2;
		   			 	capt[i+counter][4]=i;
		   			 }
	    			}
	    			
			}
			if(capture==false){
    		
    			for(int j=0;j<48;j++){
					if(playing[j][1]!=-1 && playing[j][1]>=max ){
						max=playing[j][1];
						moveDir=playing[j][0];
						mP=Math.floor(j/3);
						movePiece=(int) mP;
					}    		
    			}
    			for(int j=0;j<48;j++){
    				System.out.print(playing[j][1]+"\t"+playing[j][1]);
    			}
				int a,b,c,d;
				a=oppieces[movePiece][0];
				b=oppieces[movePiece][1];
				c=a + (myColor == BTBoard.WHITE ? 1 : -1);
				d=b+moveDir;
				BTMove moving = new BTMove( myColor,board.getCoord(a,b),board.getCoord(c,d) );
				return moving;
				}
				else
				{	int min=0;
					if(myColor==BTBoard.WHITE){
						min=8;	
						for(int i=0;i<48;i++){
						if(capt[i][2]<min){
							min=capt[i][2];
							e=capt[i][0];
		   			 	f=capt[i][1];
		   			 	g=capt[i][2];
		   			 	h=capt[i][3];			
							}				
						}
					}
					else
					{
						min=0;
						for(int i=0;i<48;i++){
						if(capt[i][2]>min){
							min=capt[i][2];
							e=capt[i][0];
		   			 	f=capt[i][1];
		   			 	g=capt[i][2];
		   			 	h=capt[i][3];			
							}
						}
					}
					
					
					BTMove moving = new BTMove( myColor,board.getCoord(e,f),board.getCoord(g,h) );
					return moving;
					
				}

	}
    
} 


