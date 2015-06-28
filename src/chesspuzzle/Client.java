/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesspuzzle;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hry
 */
class newGameSetup extends Client {

    public newGameSetup() {
        super();
    }

    public newGameSetup(Client client) {
        board = client.getBoard();
    }

    public void addPiece(Piece piece, int wX, int wY) {
        board.addPiece(piece, wX, wY);
        log.add(board.clone());
    }

    public void reset() {
        board = new Board();
    }

    public void edit(Client client) {
        board = client.start.clone();
    }

    @Override
    public void move(int wX, int wY) {
    }

    @Override
    public void undo() {
        board = log.removeLast();
        
    }

    public void FinishBoard(Client client) {
        client.board = this.board.clone();
        client.start = this.board.clone();
        client.createSolver();
    }

    @Override
    public void bestWhiteMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

class Sandbox extends Client {

    public Sandbox() {
        super();
        maxTurns = Integer.MAX_VALUE;

    }

    @Override
    public String getRemainingTurns() {
        return "N/A";
    }

    @Override
    public void move(int wX, int wY) {
        Board toLog = board.clone();

        boolean success = board.Move(wX, wY);
        if (success) {
            turnCount++;
            log.add(toLog);
        }
        // System.out.println(log.size()); 
    }

    @Override
    public void undo() {
        if (!log.isEmpty()) {
            turnCount--;
            board = log.removeLast();
            createSolver();
        }
    }

    @Override
    public void bestWhiteMove() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

class seriesMove extends Client {

    @Override
    public void move(int wX, int wY) {
        Board toLog = board.clone();
        if (board.getSelectedColor() == Board.WHITE && turnCount < maxTurns && !playerWon) {
            boolean success = board.Move(wX, wY);
            if (success) {
                turnCount++;
                if (board.getSuccessors(Board.BLACK).isEmpty()) {
                    playerWon = true;
                }
                log.add(toLog);
            }
        }
    }

    @Override
    public void undo() {

        if (!log.isEmpty()) {
            playerWon = false;
            turnCount--;
            board = log.removeLast();
            createSolver();
        }
    }

    @Override
    public void bestWhiteMove() {
        board = solver.bestWhiteMove();
    }

    @Override
    public void createSolver() {
        solver = new Solver(board, maxTurns, Board.WHITE, false);
    }

    @Override
    public void autoTurn() {
        board = solver.bestWhiteMove();
        solverLog.add(board.clone());
        solver = new Solver(board, maxTurns - turnCount - solverLog.size(), Board.WHITE, false);
    }

    @Override
    public void undoAutoTurn() {
        if (!solverLog.isEmpty()) {
            board = solverLog.removeLast();       
            solver = new Solver(board, maxTurns - turnCount - solverLog.size(), Board.WHITE, false);

        }
    }

}

class directMate extends Client {

    @Override
    public void move(int wX, int wY) {
        Board toLog = board.clone();
        if (board.getSelectedColor() == Board.WHITE && turnCount < maxTurns && !playerWon) {
            boolean success = board.Move(wX, wY);
            if (success) {
                turnCount++;
                solver = new Solver(board.clone(), 2 * (maxTurns - turnCount) + 1, Board.BLACK, true);
                if (solver.isWon()) {
                    playerWon = true;
                }
                board = solver.blackTurn();
                log.add(toLog);
                log.add(board.clone());
                solver = new Solver(board.clone(), 2 * (maxTurns - turnCount), Board.WHITE, true);
            }
        }
    }

    @Override
    public void undo() {
        if (!log.isEmpty()) {
            playerWon = false;
            turnCount--;
            log.removeLast();
            board = log.removeLast();
            createSolver();
        }
    }

    @Override
    public void bestWhiteMove() {
        board = solver.bestWhiteMove();
    }

    @Override
    public void createSolver() {
        solver = new Solver(board, 2 * maxTurns, Board.WHITE, true);
    }

    @Override 
    public void autoTurn() {

        solverLog.add(board.clone());
        board = solver.bestWhiteMove();
        solver = new Solver(board.clone(), 2 * maxTurns - 2 * turnCount - solverLog.size(), Board.BLACK, true);
        board = solver.blackTurn();
        solverLog.add(board.clone());
        solver = new Solver(board.clone(), 2 * maxTurns - 2 * turnCount - solverLog.size(), Board.WHITE, true);
       // System.out.println(solverLog.size());
    }

    @Override
    public void undoAutoTurn() {
        if (!solverLog.isEmpty()) {
            board = solverLog.removeLast();
            if ((solverLog.size() % 2) != 0) {
                board = solverLog.removeLast();
            }
            solver = new Solver(board.clone(), 2 * maxTurns - 2 * turnCount - solverLog.size(), Board.WHITE, true);
        }
        //System.out.println(solverLog.size());
    }

}

public abstract class Client {

    protected Solver solver;
    protected LinkedList<Board> log, solverLog;
    protected Board board;
    protected int turnCount;
    protected int maxTurns;
    protected boolean playerWon;
    protected Board start;

    public String getRemainingTurns() {
        return Integer.toString(maxTurns - turnCount);
    }

    public Client() {
        start = new Board();
        board = new Board();
        turnCount = 0;
        playerWon = false;
        log = new LinkedList<>();
        solverLog = new LinkedList<>();

    }

    public void newBoard(int size) {
        board = new Board(size);
    }

    public void setMaxTurns(int max) {
        maxTurns = max;
    }

    public abstract void bestWhiteMove();

    public boolean gameWon() {
        return playerWon;
    }

    public void newBoard() {
        board = new Board();
    }

    public Board getBoard() {
        return board;
    }

    public void Select(int x, int y) {
        board.select(x, y);
    }

    public void removePiece(int x, int y) {
        board.removePiece(x, y);
    }

    public abstract void move(int wX, int wY);

    public abstract void undo();

    public void Save(String filename) {
        String[] parts = filename.split(".");
        if (parts.length == 1) {
            filename = filename + ".csv";
        }
        File file = new File(filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.out.println("could not create file " + filename);
            }
        }
        int size = board.size;
        try {
            PrintWriter pw = new PrintWriter(file);
            if (this instanceof seriesMove) {
                pw.println("seriesmove");
            }
            if (this instanceof Sandbox) {
                pw.println("sandbox");
            }
            if (this instanceof directMate) {
                pw.println("directmate");
            }

            pw.println(size + " " + maxTurns + " " + turnCount);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    pw.print(board.writeTile(i, j));

                }
                pw.println();
            }
            pw.flush();
            pw.close();
        } catch (FileNotFoundException ex) {
        }
    }

    public void Quicksave() {
        Save("Quicksave.csv");
    }

    public void Quickload() {
        Load("Quicksave.csv");
    }

    public void Load(String filename) {
        File file = new File(filename);
        try {
            Scanner sc = new Scanner(file);
            sc.nextLine();
            int size = sc.nextInt();
            maxTurns = sc.nextInt();
            turnCount = sc.nextInt();
            sc.nextLine();
            board = new Board(size);
            for (int i = 0; i < size; i++) {
                String line = sc.nextLine();
                for (int j = 0; j < size; j++) {
                    char c = line.charAt(j);
                    switch (c) {
                        case 'p':
                            board.addPiece(new Pawn(Board.BLACK), i, j);
                            break;
                        case 'P':
                            board.addPiece(new Pawn(Board.WHITE), i, j);
                            break;
                        case 'r':
                            board.addPiece(new Rook(Board.BLACK), i, j);
                            break;
                        case 'R':
                            board.addPiece(new Rook(Board.WHITE), i, j);
                            break;
                        case 'h':
                            board.addPiece(new Knight(Board.BLACK), i, j);
                            break;
                        case 'H':
                            board.addPiece(new Knight(Board.WHITE), i, j);
                            break;
                        case 'b':
                            board.addPiece(new Bishop(Board.BLACK), i, j);
                            break;
                        case 'B':
                            board.addPiece(new Bishop(Board.WHITE), i, j);
                            break;
                        case 'q':
                            board.addPiece(new Queen(Board.BLACK), i, j);
                            break;
                        case 'Q':
                            board.addPiece(new Queen(Board.WHITE), i, j);
                            break;
                        case 'k':
                            board.addPiece(new King(Board.BLACK), i, j);
                            break;
                        case 'K':
                            board.addPiece(new King(Board.WHITE), i, j);
                            break;
                    }
                }
            }
            sc.close();
            createSolver();

        } catch (FileNotFoundException ex) {
            System.out.println("could not find file " + filename);
        }
    }

    void createSolver() {

    }

    public void autoTurn() {

    }

    public void undoAutoTurn() {

    }
    public boolean isViable(){
        return board.hasBothKings();
    }

    public boolean isWinnable() {
        if (!(this instanceof Sandbox)) {
            return solver.isWinnable();
        } else {
            return true;
        }
    }

    public int getMaxMoves() {
        return maxTurns;
    }

    void revertSolver() {
        if (!solverLog.isEmpty()) {
            board = solverLog.get(0);
            solverLog.clear();
            createSolver();
        }
    }
}
