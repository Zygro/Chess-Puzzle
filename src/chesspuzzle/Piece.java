package chesspuzzle;

import java.awt.Image;
import java.util.ArrayList;
import javafx.scene.canvas.GraphicsContext;

/**
 * Created by hry on 22. 11. 2014.
 */

class Direction{
    private int count;
    private int x,y,dX,dY;
    private int max;
    public Direction(int dX,int dY,int x, int y,int max){
        this.dX=dX;
        this.dY=dY;
        this.x=x;
        this.y=y;
        this.max=max;
        this.count=0;
    }
    public Direction(int dX,int dY){
        this(dX,dY,0,0,Integer.MAX_VALUE);
    }
    public Direction(int dX,int dY,int max){
        this(dX, dY, 0, 0, max);
    }

    public Point nextTile(){
        x=x+dX;
        y=y+dY;
        count++;
        if(count<=max)
        return new Point(x,y);
        else return null;
    }
    
}


class Point{
    private int x,y;
    public Point (int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}
public abstract class Piece {
    
    protected String imagePath;
    protected String name;
    protected int player;
   /* protected int[][]threatenedTiles;
    protected int[][] movableTiles;
    protected int centerX,centerY;*/
    protected boolean skips;
    /*protected void setThreatened(int x, int y,int value){
        threatenedTiles[centerX+x][centerY+y]=value;
    }*
    protected void setMovable(int x, int y,int value){
        movableTiles[centerX+x][centerY+y]=value;
    */

    public Piece (String name,int player){
        this.name=name;
        this.player=player;
        /*threatenedTiles=new int[17][17];
        movableTiles =new int[17][17];
        for (int i=0;i<17;i++){
            for (int j = 0;j < 17; j++){
                threatenedTiles[i][j]=0;
                movableTiles[i][j]=0;
            }
        }
        threatenedTiles[centerY][centerX]=-1;
        movableTiles[centerX][centerY]=-1;*/
        skips=false;
    }/*
    public int[][] getThreatenedTiles(){
        return threatenedTiles;
    }
    public int[][] getMovableTiles(){
        return movableTiles;
    }*/
    public abstract boolean isMovable(int dX,int dY);
    public abstract boolean isThreatened(int dX,int dY);
    public boolean getSkips(){return skips;}
    public abstract ArrayList<Direction> getMoveDirections();
    public abstract ArrayList<Direction> getThreatDirections();
    public abstract String toString();
    public void draw(GraphicsContext gc, int x, int y){
        gc.drawImage(new javafx.scene.image.Image(imagePath), x, y); 
        
    }
    public int getColor(){
        return player;
    }

    String getName() {
    return name;
    }

}
