/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesspuzzle;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author hry
 */
class Node {

    Board board;
    int depth;
    Node father;
    int color;
    ArrayList<Node> children;
    boolean isWinnable;
    boolean blackMoves;

    public Board getBoard() {
        return board;
    }

    public Node(Board board, Node father, int depth, int color, boolean blackMoves) {
        this.board = board;
        this.father = father;
        this.depth = depth;
        this.color = color;
        this.blackMoves = blackMoves;
        //System.out.println(color + " " + depth);
        expand();
       // System.out.println(color + " " + depth + " " + isWinnable());
        //isWinnable = computeWinnable();
    }

    public int getDepth() {
        return depth;
    }

    public Node getFather() {
        return father;
    }

    public void expand() {
        children = new ArrayList<Node>();

        if (depth > -1) {
            ArrayList<Board> successors;
            if (depth == 0 && !blackMoves) {
                //successors = board.getSuccessors(Board.BLACK);
                isWinnable = board.isWonByWhite();
            } else {
                successors = board.getSuccessors(color);
                Iterator<Board> it = successors.iterator();
                boolean interrupt = false;
                if (color == Board.BLACK) {
                    isWinnable = true;
                } else {
                    isWinnable = false;
                }
                /*if (depth == 0 && successors.size() == 0 && (color == Board.WHITE || !blackMoves)) {
                 isWinnable = true;
                 } else {*/
                while (it.hasNext() && !interrupt) {
                    if (color == Board.WHITE ) {
                        Node toAdd;
                        if (blackMoves) {
                            toAdd = new Node(it.next(), this, depth - 1, Board.BLACK, blackMoves);
                        } else {
                            toAdd = new Node(it.next(), this, depth - 1, Board.WHITE, blackMoves);
                        }
                        children.add(toAdd);

                        if (toAdd.isWinnable()) {
                            interrupt = true;
                            isWinnable = true;
                        }
                    }
                    if (color == Board.BLACK && !interrupt) {
                        if (board.isWonByWhite()) {
                            isWinnable = true;
                        } else {
                            if (depth == 0) {
                                isWinnable = false;
                            } else {
                                Node toAdd = new Node(it.next(), this, depth - 1, Board.WHITE, blackMoves);
                                children.add(toAdd);
                                if (!toAdd.isWinnable()) {
                                    isWinnable = false;
                                    interrupt = true;
                                }
                            }
                        }

                    }
                    //}
                }

            }
        }
    }

    public boolean computeWinnable() {
        Iterator<Node> it = children.iterator();
        if (color == Board.BLACK && children.isEmpty()) {
            return true;
        }
        if (depth == 0) {
            return false;
        }
        while (this.color == Board.BLACK && it.hasNext()) {
            if (it.next().isWinnable()) {
                return true;
            }
        }
        while (this.color == Board.WHITE && it.hasNext()) {
            if (!it.next().isWinnable()) {
                return false;
            }
        }

        if (this.color == Board.WHITE) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isWinnable() {
        return isWinnable;
    }

}

public class Solver {

    Node start;
    int maxDepth;
    int color;
    boolean blackMoves;

    public Solver(Board start, int maxDepth, int color, boolean blackMoves) {
        this.maxDepth = maxDepth;
        this.color = color;
        this.blackMoves = blackMoves;
        this.start = new Node(start, null, maxDepth, color, blackMoves);
     //   System.out.println(this.start.depth);
    }

    public boolean isWinnable() {
        return start.isWinnable();
    }

    public Board blackTurn() {
        if (isWinnable() && !start.children.isEmpty()) {
            return start.children.get(start.children.size()-1).getBoard().clone();
        } else {
            Iterator<Node> it = start.children.iterator();
            while (it.hasNext()) {
                Node current = it.next();
                if (!current.isWinnable()) {
                    return current.getBoard().clone();
                }
            }
            return start.board;
        }
    }

    public boolean isWon() {
        if (color == Board.BLACK && start.children.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public Board bestWhiteMove() {
        if (!isWinnable()) {
            System.out.println("Shouldn't get here either");
            return null;
        }
        Iterator<Node> it = start.children.iterator();
        while (it.hasNext()) {
            //System.out.println("here");
            Node current = it.next();
            if (current.isWinnable()) {
                return current.getBoard();
            }
        }
        System.out.println("Shouldn't get here");
        return null;
    }
}
