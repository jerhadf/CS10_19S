/**
 * Simple Object to handle the swapping of sub-images
 * @author Dartmouth CS 10, Fall 2012
 * @author Winter 2014, rewritten for BufferedImage
 * @author Spring 2015, from array to ArrayList, refactored GUI
 *
 */
public class PieceSwap {
    int r1;
    int c1;
    int r2;
    int c2;

    public PieceSwap(int r1, int c1, int r2, int c2) {
        this.r1 = r1;
        this.c1 = c1;
        this.r2 = r2;
        this.c2 = c2;
    }

    public PieceSwap(int[] coords){
        this.r1 = coords[0];
        this.c1 = coords[1];
        this.r2 = coords[2];
        this.c2 = coords[3];
    }

}
