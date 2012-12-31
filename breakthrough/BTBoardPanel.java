package breakthrough;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.FontMetrics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import boardgame.BoardPanel;

/**
 * A board panel for dispaly and input for the BreakThrough game.
 */
public class BTBoardPanel extends BoardPanel 
implements MouseListener, MouseMotionListener, ComponentListener {
    // Some constants affecting display
    static final Color BGCOLOR = new Color(130,130,0);
    static final Color COLOR1 = new Color(230,230,160);
    static final Color COLOR2 = new Color(190,190,130);
    static final Color BCOLOR = new Color(50,50,50);
    static final Color WCOLOR = new Color(250,250,250);
    static final Color TCOLOR = new Color(210,210,145);
    static final Color LINECOLOR = Color.RED;
    static final int BORDER = 16;

    // Board location of piece playing 
    Point orig = null; // Bad naming: x == i, y==j for board coords...
    Point dragStart = null, dragEnd = null;
    BoardPanelListener list = null; // Who needs a move input ?
    private int w, h, wp, hp, xoff, yoff;
    
    public BTBoardPanel() {
        this.addMouseListener( this );
        this.addMouseMotionListener( this );
        this.addComponentListener( this );
    }
    
    protected void requestMove( BoardPanelListener l ) {
        list = l;
    }
    
    protected void cancelMoveRequest() {
        list = null; 
    }

    public void mousePressed(MouseEvent arg0) {
        BTBoard board = (BTBoard) getCurrentBoard();
        Point p = getSquare( arg0.getX(), arg0.getY() );
        if( list != null && p != null &&
            board.getPieceAt(p.x, p.y) == board.getTurn() ) {
            orig = p;
            dragStart = getCenterCoord( orig.x, orig.y );
        }
    }

    public void mouseDragged(MouseEvent arg0) {
        if( list != null && dragStart != null ) {
            dragEnd = new Point( arg0.getX(), arg0.getY() );
            repaint();
        }
    }

    public void mouseReleased(MouseEvent arg0) {
        BTBoard board = (BTBoard) getCurrentBoard();
        Point p = getSquare( arg0.getX(), arg0.getY() );
        if( list != null && orig != null && p != null &&
                board.isLegal( board.getTurn(),
                        board.getCoord(orig.x, orig.y),
                        board.getCoord(p.x, p.y) ) ) {
            list.moveEntered( new BTMove( board.getTurn(),
                    board.getCoord(orig.x, orig.y),
                    board.getCoord(p.x, p.y) ) ); 
            cancelMoveRequest();
        }
        // Request dirty area to be redrawn
        orig = null; dragStart = null; dragEnd = null;
        repaint();
    }
    
    /** Paint the board to the offscreen buffer. This does the painting
     * of the actual board, but not the pieces being moved by the user.*/
    public void drawBoard( Graphics g ) {
	BTBoard bd = (BTBoard) getCurrentBoard();
        Rectangle clip = g.getClipBounds();
        int w = (clip.width - BORDER*2)/ BTBoard.SIZE;
        int h = (clip.height - BORDER*2)/ BTBoard.SIZE;
        if( w < h ) h = w; else w = h; // Make square aspect ratio
        int wp = w*8/10, hp = h*8/10; 
        int xoff = (clip.width - BTBoard.SIZE * w) / 2;
        int yoff = (clip.height - BTBoard.SIZE * h) / 2;
        g.setColor(BGCOLOR);
        g.fillRect(clip.x,clip.y,clip.width,clip.height);
        FontMetrics fm = g.getFontMetrics();
        int x1 = xoff - BORDER, x2 = xoff + w * BTBoard.SIZE;
        int y1 = yoff - BORDER, y2 = yoff + w * BTBoard.SIZE;
        g.setColor(TCOLOR);
        for( int i = 0; i < BTBoard.SIZE; i++ ) {
            String s = Integer.toString(i+1);
            String t = Character.toString((char) ('A' + i));
            Rectangle r = fm.getStringBounds(s, g).getBounds();
            Rectangle q = fm.getStringBounds(t, g).getBounds();
            int o = (BORDER - r.width) / 2;
            int p = (h-r.height) / 2 + yoff;
            g.drawString(s,x1+o,h*i+p+r.height-1);
            g.drawString(s,x2+o,h*i+p+r.height-1);
            int m = (BORDER - q.height) / 2;
            int n = (w-q.width) / 2 + xoff;
            g.drawString(t,w*i+n,y1+m+q.height-1);
            g.drawString(t,w*i+n,y2+m+q.height-1);
        }
        for( int i = 0; i < BTBoard.SIZE; i++ ) {
            int y = yoff + i * h, yp = y + h/10;
            nextSquare: for( int j = 0; j < BTBoard.SIZE; j++ ) {
                int x = xoff + j * w, xp = x + w/10;
                g.setColor( (i + j) % 2 == 0 ? COLOR1 : COLOR2 );
                g.fillRect( x,y,w,h );
                switch( bd.getPieceAt(i,j) ) {
                  case BTBoard.BLACK: g.setColor( BCOLOR ); break;
                  case BTBoard.WHITE: g.setColor( WCOLOR ); break;
                  default: continue nextSquare;
                }
                g.fillOval(xp,yp,wp,hp);
            } }
    }

    /** We use the double-buffering provided by the superclass, but draw
     *  the "transient" elements in the paint() method. */
    public void paint( Graphics g ) {
        // Paint the board as usual, this will used the offscreen buffer
        super.paint(g);
        if( dragEnd != null ) {
	    // Paint the dragged piece's origin
            int x = orig.y * w + xoff, y = orig.x * h + yoff; 
            g.setColor( (orig.x + orig.y) % 2 == 0 ? 
                    COLOR1 : COLOR2 );
            g.fillRect( x,y,w,h );
            // Paint the drag-line
            g.setColor( LINECOLOR );
            g.drawLine(dragStart.x, dragStart.y, dragEnd.x, dragEnd.y);
            // Paint the piece itself
            int xp = dragEnd.x - wp/2, yp = dragEnd.y - hp/2;
            g.setColor( getCurrentBoard().getTurn() == BTBoard.WHITE ? 
                    WCOLOR :BCOLOR );
            g.fillOval(xp,yp,wp,hp);
        }
    }
    
    public void componentResized(ComponentEvent arg0) {
        w = (this.getWidth() - BORDER*2) / BTBoard.SIZE;
        h = (this.getHeight() - BORDER*2) / BTBoard.SIZE;
        if( w < h ) h = w; else w = h; // Make square aspect ratio
        wp = w*8/10; hp = h*8/10; 
        xoff = (this.getWidth() - BTBoard.SIZE * w) / 2;
        yoff = (this.getHeight() - BTBoard.SIZE * h) / 2;
    }

    private Point getSquare( int x, int y) {
        // This is a bit ugly, since we swap coord order
        return new Point( (y-yoff) / w, (x-xoff) / w);
    }
    
    private Point getCenterCoord( int i, int j ) {
        // This is a bit ugly, since we swap coord order
        return new Point( orig.y * w + xoff + w/2, orig.x * h + yoff + h/2 );
    }
    
    /* Don't use these interface methods */
    public void mouseClicked(MouseEvent arg0) {}
    public void mouseEntered(MouseEvent arg0) {}
    public void mouseExited(MouseEvent arg0) {}
    public void mouseMoved(MouseEvent arg0) {}
    public void componentMoved(ComponentEvent arg0) {}
    public void componentShown(ComponentEvent arg0) {}
    public void componentHidden(ComponentEvent arg0) {}
}
