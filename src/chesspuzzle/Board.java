package chesspuzzle;

import java.awt.Rectangle;
import java.util.ArrayList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Created by hry on 22. 11. 2014.
 */
class Tile {

    int x, y;
    int color;
    protected Piece piece;
    Boolean ThreatenedByBlack;
    Boolean ThreatenedByWhite;

    ArrayList<Piece> blackThreats;
    ArrayList<Piece> whiteThreats;
    ArrayList<Tile> possibleVisitors;

    public Tile(int x_, int y_, int color_) {
        this.x = x_;
        this.y = y_;
        this.color = color_;
        piece = null;
        blackThreats = new ArrayList<Piece>();
        whiteThreats = new ArrayList<Piece>();
        possibleVisitors = new ArrayList<Tile>();
    }

    public int getRowName() {
        return x;
    }

    public boolean isThreatenedBy(Piece piece) {
        return blackThreats.contains(piece) || whiteThreats.contains(piece);
    }

    public void addThreat(Piece piece) {
        if (piece.player == Board.WHITE) {
            if (this.piece != null) {
                if (this.piece.getColor() == Board.BLACK) {
                    whiteThreats.add(piece);
                }
            } else {
                whiteThreats.add(piece);
            }
        }
        if (piece.player == Board.BLACK) {
            if (this.piece != null) {
                if (this.piece.getColor() == Board.WHITE) {
                    blackThreats.add(piece);
                }
            } else {
                blackThreats.add(piece);
            }
        }
    }

    public void removeThreat(Piece piece) {
        if (piece.player == Board.WHITE) {
            whiteThreats.remove(piece);
        }
        if (piece.player == Board.BLACK) {
            blackThreats.remove(piece);
        }
    }

    public boolean canMoveHere(Tile tile) {
        return possibleVisitors.contains(tile) || ((blackThreats.contains(tile.getPiece()) || whiteThreats.contains(tile.getPiece())) && this.piece != null);
    }

    public void addPossibleVisitor(Tile tile) {
        possibleVisitors.add(tile);
    }

    public void removePossibleVisitor(Tile tile) {
        possibleVisitors.remove(tile);
    }

    public String getColumnChar() {

        if (y <= 26) {
            return String.valueOf((char) (y + 'a' - 1));
        } else if (y <= 32) {
            return String.valueOf((char) (y + 'A' - 1));
        }
        int value = y;
        String ret = "";
        while (value > 0) {
            if (value <= 26) {
                ret = ret + String.valueOf((char) (value % 26 + 'a' - 1));
            } else if (value <= 32) {
                ret = ret + String.valueOf((char) (value % 26 + 'A' - 1));
            }
            value = value - 26;
        }
        return ret;
    }

    public void resetMoves() {
        this.blackThreats.clear();
        this.whiteThreats.clear();
        this.possibleVisitors.clear();
    }

    public void placePiece(Piece what) {
        this.piece = what;
    }

    public Piece removePiece() {
        Piece ret = piece;
        this.piece = null;
        return ret;
    }

    public Piece getPiece() {
        return piece;
    }

    void draw(GraphicsContext gc, Tile selected, int i, int j) {
        gc.setStroke(Color.BLACK);
        if (this.color == Board.BLACK) {
            gc.setFill(Color.BURLYWOOD);
        }
        if (this.color == Board.WHITE) {
            gc.setFill(Color.BISQUE);
        }
        if (selected.getPiece() != null && this.canMoveHere(selected)) {
            gc.setFill(Color.BROWN);
            gc.setLineWidth(2);
            gc.strokeRect(i, j, 45, 45);
        }

        gc.fillRect(i, j, 45, 45);
        if (this.piece != null) {
            piece.draw(gc, i, j);
        }
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    boolean isThreatened() {
        if (this.piece == null) {
            return !(this.blackThreats.isEmpty() && this.whiteThreats.isEmpty());
        }
        if (this.piece.getColor() == Board.WHITE) {
            return !(this.blackThreats.isEmpty());
        }
        if (this.piece.getColor() == Board.BLACK) {
            // System.out.println(" x " + this.whiteThreats);
            return !(this.whiteThreats.isEmpty());
        }
        return false;
    }

    public String toString() {
        if (this.piece != null) {
            return piece.toString();
        } else {
            return ".";
        }
    }

    ArrayList<Tile> getPossibleVisitors() {
        return this.possibleVisitors;
    }
}

//////////////////////////////////////////////////////////////////////
//                               MOVE                               //                        
//////////////////////////////////////////////////////////////////////
class Move {

    private Tile from, to;

    public Move(Tile from, Tile to) {
        this.from = from;
        this.to = to;
    }

    Tile getFrom() {
        return from;
    }

    Tile getTo() {
        return to;
    }
}

/////////////////////////////////////////////////////////////////////////////
//                                 BOARD                                   //
/////////////////////////////////////////////////////////////////////////////
public class Board {

    public static final int WHITE = -1;
    public static final int BLACK = 1;
    int size;
    private Tile[][] board;
    private ArrayList<Tile> occupiedTiles;
    private Tile selected;
    private boolean whiteCheck;
    private boolean blackCheck;
    private Tile whiteKing;
    private Tile blackKing;

    public void Draw(GraphicsContext gc) {
        for (int i = 0; i < size * 45; i = i + 45) {
            for (int j = 0; j < size * 45; j = j + 45) {
                board[i / 45][j / 45].draw(gc, selected, i, j);
            }
        }
    }

    public String writeTile(int x, int y) {
        return board[x][y].toString();
    }

    boolean blackCheck() {
        return blackCheck;
    }

    public Board(int size) {
        occupiedTiles = new ArrayList<>();
        this.size = size;
        board = new Tile[size][size];
        whiteKing = null;
        blackKing = null;
        whiteCheck = false;
        blackCheck = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + j) % 2 == 0) {
                    board[i][j] = new Tile(i, j, WHITE);
                } else {
                    board[i][j] = new Tile(i, j, BLACK);
                }
            }
        }

        //addPiece(new King(BLACK), 5, 2);
       /* addPiece(new Rook(WHITE), 5, 1);
         addPiece(new Queen(BLACK), 5, 5);
         addPiece(new Pawn(WHITE), 4, 3);
         addPiece(new Knight(WHITE), 3, 3);*/
        this.selected = new Tile(-1, -1, WHITE);
        updateBoard();
    }

    public boolean isWonByWhite() {
        return (this.getSuccessors(BLACK).isEmpty() && blackCheck);
    }

    public Board() {
        this(8);
    }

    public Board clone() {
        Board ret = new Board(size);
        for (Tile current : occupiedTiles) {
            ret.addPiece(current.getPiece(), current.getX(), current.getY());
        }
        ret.selected = this.selected;
        return ret;
    }

    public void resetBoard(int size) {
        this.size = size;
        board = new Tile[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int color;
                if ((i + j) % 2 == 0) {
                    color = WHITE;
                } else {
                    color = BLACK;
                }
                board[i][j] = new Tile(i, j, color);

            }
        }
    }

    public void removePiece(int x, int y) {
        Piece piece = board[x][y].removePiece();
        occupiedTiles.remove(board[x][y]);
        if (piece != null) {
            if (piece.getName() == "king" && piece.getColor() == WHITE) {
                whiteKing = null;
            }
            if (piece.getName() == "king" && piece.getColor() == BLACK) {
                blackKing = null;
            }
        }
    }

    public void updateBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j].resetMoves();
            }
        }
        for (Tile current : occupiedTiles) {
            Piece piece = current.getPiece();
            //System.out.println(piece);
            ArrayList<Direction> moveDirections = piece.getMoveDirections();
            ArrayList<Direction> threatDirections = piece.getThreatDirections();

            //first we update the threats
            while (!threatDirections.isEmpty()) {
                //we go through next layer of tiles
                for (int i = 0; i < threatDirections.size(); i++) {
                    Point p = threatDirections.get(i).nextTile();
                    if (p == null) {
                        threatDirections.remove(i);
                    } else {
                        int x = p.getX() + current.getX();
                        int y = p.getY() + current.getY();
                        //if we run out of board, nothing is to see here
                        if (x < 0 || x >= size || y < 0 || y >= size) {
                            threatDirections.remove(i);
                        } else {
                            //we add threat

                            board[x][y].addThreat(piece);
                            if (board[x][y].getPiece() != null) {
                                board[x][y].addPossibleVisitor(current);
                            }
                            //if we stumble upon a piece and the current one is not skipper
                            if (board[x][y].getPiece() != null && !piece.getSkips()) {
                                //we can't move past the tile we stumbled upon
                                threatDirections.remove(i);
                                //if the piece is the same colour
                                if (board[x][y].getPiece().getColor() == piece.getColor()) {
                                    //we can't threaten it
                                    board[x][y].removeThreat(piece);
                                    board[x][y].removePossibleVisitor(current);
                                }
                            }

                        }
                    }
                }
            }

            //second we update possible moves
            while (!moveDirections.isEmpty()) {
                //we go through next layer of tiles
                for (int i = 0; i < moveDirections.size(); i++) {
                    Point p = moveDirections.get(i).nextTile();
                    if (p == null) {
                        moveDirections.remove(i);
                    } else {
                        int x = p.getX() + current.getX();
                        int y = p.getY() + current.getY();
                        //if we run out of board, nothing is to see here
                        if (x < 0 || x >= size || y < 0 || y >= size) {
                            moveDirections.remove(i);
                        } else {
                            //if we can capture the piece on the spot or there is no piece
                            if (board[x][y].getPiece() == null) {
                                //we can move there
                                board[x][y].addPossibleVisitor(current);
                            }
                            //if we stumble upon a piece and the current one is not skipper
                            if (board[x][y].getPiece() != null && !piece.getSkips()) {
                                //we can't move past it
                                moveDirections.remove(i);
                                //if the piece is the same colour
                                if (board[x][y].getPiece().getColor() == piece.getColor()) {
                                    //we can't move there
                                    board[x][y].removePossibleVisitor(current);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (blackKing != null) {
            blackCheck = blackKing.isThreatened();
            //System.out.println(blackKing.getX() + " " + blackKing.getY() + " " + blackCheck);

        }
        if (whiteKing != null) {
            whiteCheck = whiteKing.isThreatened();
        }
    }

    public void addPiece(Piece what, int x, int y) {
        if (what.getName().equals("king")) {
            if (what.getColor() == WHITE && whiteKing == null) {
                if (board[x][y].getPiece() == null) {
                    whiteKing = board[x][y];
                    board[x][y].placePiece(what);
                    occupiedTiles.add(board[x][y]);
                }
            }
            if (what.getColor() == BLACK && blackKing == null) {
                if (board[x][y].getPiece() == null) {
                    blackKing = board[x][y];
                    board[x][y].placePiece(what);
                    occupiedTiles.add(board[x][y]);
                }
            }
        } else if (board[x][y].getPiece() == null) {
            board[x][y].placePiece(what);
            occupiedTiles.add(board[x][y]);
        }
        updateBoard();
    }
    public boolean hasBothKings(){
        return (blackKing!=null && whiteKing!=null);
    }
    public boolean canMove(int x, int y) {
        if (selected.getPiece() == null) {
            return false;
        } else {
            return board[x][y].canMoveHere(selected);
        }
    }

    public boolean Move(int x, int y) {
        /*Tile[][] save = board.clone();
         ArrayList<Tile> oldPositions=(ArrayList<Tile>) this.occupiedTiles.clone();
         */
        int oldX = selected.getX(), oldY = selected.getY();
        if (canMove(x, y)) {
            Piece piece = selected.removePiece();
            Piece captured = board[x][y].removePiece();
            if (captured != null) {
                occupiedTiles.remove(board[x][y]);
            }
            board[x][y].placePiece(piece);
            occupiedTiles.remove(selected);
            occupiedTiles.add(board[x][y]);
            //System.out.println(captured);
            select(x, y);
            if (piece.getName().equals("king")) {
                if (piece.getColor() == WHITE) {
                    whiteKing = board[x][y];
                }
                if (piece.getColor() == BLACK) {
                    blackKing = board[x][y];
                }
            }
            updateBoard();

            if (piece.getColor() == WHITE) {
                if (whiteCheck) {
                    piece = board[x][y].removePiece();
                    occupiedTiles.remove(board[x][y]);
                    board[oldX][oldY].placePiece(piece);
                    occupiedTiles.add(board[oldX][oldY]);
                    select(oldX, oldY);
                    updateBoard();
                    return false;
                }
            }
            if (piece.getColor() == BLACK) {
                if (blackCheck) {
                    //System.out.println("y " + blackKing.isThreatened());

                    piece = board[x][y].removePiece();
                    occupiedTiles.remove(board[x][y]);
                    board[oldX][oldY].placePiece(piece);
                    occupiedTiles.add(board[oldX][oldY]);
                    select(oldX, oldY);
                    updateBoard();
                    return false;
                }
            }
            return true;
        } else {
            // System.out.println("can't move to "+x+" "+y );
            return false;
        }
    }

    public void select(int x, int y) {
        selected = board[x][y];
        // System.out.println(selected+selected.getPossibleVisitors().toString() );

    }

    public int getSelectedColor() {
        if (selected.getPiece() != null) {
            return selected.getPiece().getColor();
        } else {
            return 0;
        }
    }

    public ArrayList<Board> getSuccessors(int Player) {
        ArrayList<Board> ret = new ArrayList();
        //System.out.println(blackKing.isThreatened());
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Tile current = board[i][j];
                ArrayList<Tile> possibleVisitors = current.getPossibleVisitors();
                for (Tile t : possibleVisitors) {
                    Piece piece = t.getPiece();
                    if (piece.getColor() == Player) {
                        Board newBoard = this.clone();
                        newBoard.select(t.getX(), t.getY());
                        boolean success = newBoard.Move(i, j);
                        if (success) {
                            ret.add(newBoard);
                        }
                    }
                }
            }
        }

        return ret;
    }

}
