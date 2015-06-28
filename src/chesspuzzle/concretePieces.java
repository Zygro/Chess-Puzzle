package chesspuzzle;

import java.util.ArrayList;

/**
 * Created by hry on 22. 11. 2014.
 */
class Pawn extends Piece {

    public Pawn(int player){

        super("pawn", player);
        if (player==Board.BLACK){
           /* setMovable(centerX+1, centerY, 1);
            setThreatened(centerX+1,centerY-1,1);
            setThreatened(centerX+1,centerY+1,1);*/
            imagePath="/resources/blackPawn.png";
        }
        if (player==Board.WHITE){
            /*setMovable(centerX-1, centerY, 1);
            setThreatened(centerX-1,centerY-1,1);
            setThreatened(centerX-1,centerY+1,1);*/
            imagePath="/resources/pawn.png";
        }
    }

    @Override
    public boolean isMovable(int dX, int dY) {
        if(dX==0 && dY==1 && player==Board.BLACK) return true;
        if(dX==0 && dY==-1 && player==Board.WHITE) return true;
        return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {

        if (player==Board.BLACK && (dY==1||dY==-1)&&dX==1){
            return true;
        }

        if (player==Board.WHITE && (dY==1||dY==-1)&&dX==-1){
            return true;
        }return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        if (this.player==Board.BLACK)ret.add(new Direction(0,1,1));
        if (this.player==Board.WHITE)ret.add(new Direction(0,-1,1));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        if (this.player==Board.BLACK){
            ret.add(new Direction(-1,1,1));
            ret.add(new Direction(1,1,1));
        }
        if (this.player==Board.WHITE){
            ret.add(new Direction(-1,-1,1));
            ret.add(new Direction(1,-1,1));
        }
        return ret;
    }
    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "p";
        if (this.player==Board.WHITE)return "P";
        return "X";
    }
}

class Rook extends Piece {

    public Rook(int player){

        super("rook", player);
       /* for(int i=0;i<movableTiles.length;i++){
            setThreatened(i,centerY,1);
            setMovable(i, centerY, 1);
        }
        for(int i=0;i<movableTiles[0].length;i++){
            setThreatened(centerX,i,1);
            setMovable(centerX,i,1);
        }*/
        
        if (this.player==Board.BLACK){
            imagePath="/resources/blackRook.png";
        }
        if (this.player==Board.WHITE){
            imagePath="/resources/rook.png";
        }

    }

    @Override
    public boolean isMovable(int dX, int dY) {
        if(dX==0 ||dY==0)return true;
        else return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {
        if (dX==0 || dY==0)return true;
        else return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(0,1));
        ret.add(new Direction(0,-1));
        ret.add(new Direction(-1,0));
        ret.add(new Direction(1,0));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(0,1));
        ret.add(new Direction(0,-1));
        ret.add(new Direction(-1,0));
        ret.add(new Direction(1,0));
        return ret;
    }
    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "r";
        if (this.player==Board.WHITE)return "R";
        return "X";
    }
}

class Bishop extends Piece {

    public Bishop(int player){

        super("bishop", player);
        /*for(int i=0;i<movableTiles.length;i++){
           setThreatened(i,i,1);
            setMovable(i, i, 1);
        }
        for(int i=0;i<movableTiles.length;i++){
            setThreatened(movableTiles.length-i,movableTiles[0].length-i,1);
            setMovable(movableTiles.length - i, movableTiles[0].length - i, 1);
        }*/
        if (this.player==Board.BLACK){
            imagePath="/resources/blackBishop.png";
        }
        if (this.player==Board.WHITE){
            imagePath="/resources/bishop.png";
        }

    }

    @Override
    public boolean isMovable(int dX, int dY) {
        if (Math.abs(dX)==Math.abs(dY))return true;
        else return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {
        if (Math.abs(dX)==Math.abs(dY))return true;
        else return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(1,1));
        ret.add(new Direction(1,-1));
        ret.add(new Direction(-1,1));
        ret.add(new Direction(-1,-1));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(1,1));
        ret.add(new Direction(1,-1));
        ret.add(new Direction(-1,1));
        ret.add(new Direction(-1,-1));
        return ret;
    }

    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "b";
        if (this.player==Board.WHITE)return "B";
        return "X";
    }
}
class Knight extends Piece {

    public Knight(int player){

        super("knight", player);
        /*setMovable(centerX - 1, centerY - 2, 1);
        setMovable(centerX - 1, centerY + 2, 1);
        setMovable(centerX + 1, centerY - 2, 1);
        setMovable(centerX + 1, centerY + 2, 1);

        setMovable(centerX - 2, centerY - 1, 1);
        setMovable(centerX - 2, centerY + 1, 1);
        setMovable(centerX + 2, centerY - 1, 1);
        setMovable(centerX + 2, centerY + 1, 1);

        setThreatened(centerX - 1, centerY - 2, 1);
        setThreatened(centerX - 1, centerY + 2, 1);
        setThreatened(centerX + 1, centerY - 2, 1);
        setThreatened(centerX + 1, centerY + 2, 1);

        setThreatened(centerX - 2, centerY - 1, 1);
        setThreatened(centerX - 2, centerY + 1, 1);
        setThreatened(centerX + 2, centerY - 1, 1);
        setThreatened(centerX + 2, centerY + 1, 1);*/
        skips=true;
        
        if (this.player==Board.BLACK){
            imagePath="/resources/blackKnight.png";
        }
        if (this.player==Board.WHITE){
            imagePath="/resources/knight.png";
        }
    }

    @Override
    public boolean isMovable(int dX, int dY) {
       /* if (movableTiles[centerX+dX][centerY+dY]==1)return true;*/
            return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {
       /* if (movableTiles[centerX+dX][centerY+dY]==1)return true;*/
        return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(-1,-2,1));
        ret.add(new Direction(-1,2,1));
        ret.add(new Direction(1,-2,1));
        ret.add(new Direction(1,2,1));

        ret.add(new Direction(-2,-1,1));
        ret.add(new Direction(-2,1,1));
        ret.add(new Direction(2,-1,1));
        ret.add(new Direction(2,1,1));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction> ret=new ArrayList<Direction>();
        ret.add(new Direction(-1,-2,1));
        ret.add(new Direction(-1,2,1));
        ret.add(new Direction(1,-2,1));
        ret.add(new Direction(1,2,1));

        ret.add(new Direction(-2,-1,1));
        ret.add(new Direction(-2,1,1));
        ret.add(new Direction(2,-1,1));
        ret.add(new Direction(2,1,1));
        return ret;
    }
    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "h";
        if (this.player==Board.WHITE)return "H";
        return "X";
    }
}
class Queen extends Piece {

    public Queen(int player){

        super("queen", player);
        /*for(int i=0;i<movableTiles.length;i++){
            setThreatened(i,i,1);
            setMovable(i, i, 1);
        }
        for(int i=0;i<movableTiles.length;i++){
          /*  setThreatened(movableTiles.length-i,movableTiles[0].length-i,1);
            setMovable(movableTiles.length - i, movableTiles[0].length - i, 1);
        }
        for(int i=0;i<movableTiles.length;i++){
            setThreatened(i,centerY,1);
            setMovable(i, centerY, 1);
        }
        for(int i=0;i<movableTiles[0].length;i++){
            setThreatened(centerX,i,1);
            setMovable(centerX,i,1);
        }*/
        if (this.player==Board.BLACK){
            imagePath="/resources/blackQueen.png";
        }
        if (this.player==Board.WHITE){
            imagePath="/resources/queen.png";
            }
    }

    @Override
    public boolean isMovable(int dX, int dY) {
        if (Math.abs(dX)==Math.abs(dY)) return true;
        if (dX==0||dY==0)return true;
        return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {
        if (Math.abs(dX)==Math.abs(dY)) return true;
        if (dX==0||dY==0)return true;
        return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction>ret=new ArrayList<Direction>();
        ret.add(new Direction(0,1));
        ret.add(new Direction(0,-1));
        ret.add(new Direction(1,0));
        ret.add(new Direction(-1,0));

        ret.add(new Direction(-1,-1));
        ret.add(new Direction(-1,1));
        ret.add(new Direction(1,-1));
        ret.add(new Direction(1,1));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction>ret=new ArrayList<Direction>();
        ret.add(new Direction(0,1));
        ret.add(new Direction(0,-1));
        ret.add(new Direction(1,0));
        ret.add(new Direction(-1,0));

        ret.add(new Direction(-1,-1));
        ret.add(new Direction(-1,1));
        ret.add(new Direction(1,-1));
        ret.add(new Direction(1,1));
        return ret;
    }
    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "q";
        if (this.player==Board.WHITE)return "Q";
        return "X";
    }
}
class King extends Piece{

    public King(int player){
        super("king",player);
       /* setMovable(centerX - 1, centerY, 1);
        setMovable(centerX + 1, centerY, 1);
        setMovable(centerX, centerY + 1, 1);
        setMovable(centerX,centerY-1,1);

        setThreatened(centerX - 1, centerY, 1);
        setThreatened(centerX + 1, centerY, 1);
        setThreatened(centerX, centerY + 1, 1);
        setThreatened(centerX, centerY - 1, 1);*/
        if (this.player==Board.BLACK){
            imagePath="/resources/blackKing.png";
        }
        if (this.player==Board.WHITE){
            imagePath="/resources/king.png";
        }
    }
    @Override
    public boolean isMovable(int dX, int dY) {
        if (Math.abs(dX)==1 && dY==0) return true;
        if (Math.abs(dY)==1 && dX==0)return true;
        return false;
    }

    @Override
    public boolean isThreatened(int dX, int dY) {
        if (Math.abs(dX)==1 && dY==0) return true;
        if (Math.abs(dY)==1 && dX==0)return true;
        return false;
    }

    @Override
    public ArrayList<Direction> getMoveDirections() {
        ArrayList<Direction>ret=new ArrayList<Direction>();

        ret.add(new Direction(0,1,1));
        ret.add(new Direction(0,-1,1));
        ret.add(new Direction(1,0,1));
        ret.add(new Direction(-1,0,1));
        ret.add(new Direction(-1,1,1));
        ret.add(new Direction(-1,-1,1));
        ret.add(new Direction(1,1,1));
        ret.add(new Direction(1,-1,1));
        return ret;
    }

    @Override
    public ArrayList<Direction> getThreatDirections() {
        ArrayList<Direction>ret=new ArrayList<Direction>();

        ret.add(new Direction(0,1,1));
        ret.add(new Direction(0,-1,1));
        ret.add(new Direction(1,0,1));
        ret.add(new Direction(-1,0,1));
        return ret;
    }
    @Override
    public String toString() {
        if (this.player==Board.BLACK)return "k";
        if (this.player==Board.WHITE)return "K";
        return "X";
    }
}