/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesspuzzle;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author hry
 */
public class ChessPuzzle extends Application {

    Client client;
    final javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas();
    Stage primary, newGameStage;

    @Override
    public void start(Stage primaryStage) {

        client = new Sandbox();
        Button btn = new Button();
        Button newBoard = new Button();
        Button newGame = new Button();
        Button undo = new Button();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Text turnsRemaining = new Text();
        // newBoard.setText("New Board");
        newGame.setText("New Game");
        undo.setText("Undo");
        btn.setPrefSize(150, 20);
        //newBoard.setPrefSize(150, 20);
        newGame.setPrefSize(150, 20);
        undo.setPrefSize(150, 20);
        turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
        /* newBoard.setOnAction(new EventHandler<ActionEvent>() {

         @Override
         public void handle(ActionEvent event) {
         client.newBoard();
         redraw(canvas, client);
         }
         });
         */
        newGame.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                newGameSetup setup = new newGameSetup();
                final Stage dialog = new Stage();
                javafx.scene.canvas.Canvas newGameCanvas = new javafx.scene.canvas.Canvas();
                dialog.initOwner(primaryStage);
                BorderPane newGameLayout = new BorderPane();
                HBox PiecesBox = new HBox();
                VBox Right = new VBox();
                dialog.setAlwaysOnTop(true);
                dialog.initModality(Modality.APPLICATION_MODAL);
                Right.setSpacing(3);
                Right.setAlignment(Pos.BASELINE_CENTER);
//right.getChildren().add(); 

                TextField maxTurns = new TextField("3");
                Button confirmButton = new Button("OK");
                Button cancelButton = new Button("Cancel");
                confirmButton.setPrefSize(100, 20);
                cancelButton.setPrefSize(100, 20);

                ToggleButton sb = new ToggleButton("Sandbox");
                ToggleButton sm = new ToggleButton("Seriesmove");
                ToggleGroup gameModesGroup = new ToggleGroup();
                sb.setToggleGroup(gameModesGroup);
                sm.setToggleGroup(gameModesGroup);
                gameModesGroup.selectToggle(sb);

                HBox gameModes = new HBox();
                gameModes.getChildren().add(sb);
                gameModes.getChildren().add(sm);

                ToggleGroup PiecesGroup = new ToggleGroup();
                ToggleButton bp = new ToggleButton("", new ImageView(new Image("/resources/blackPawn.png")));
                ToggleButton wp = new ToggleButton("", new ImageView(new Image("/resources/pawn.png")));
                ToggleButton br = new ToggleButton("", new ImageView(new Image("/resources/blackRook.png")));
                ToggleButton wr = new ToggleButton("", new ImageView(new Image("/resources/rook.png")));
                ToggleButton bk = new ToggleButton("", new ImageView(new Image("/resources/blackKnight.png")));
                ToggleButton wk = new ToggleButton("", new ImageView(new Image("/resources/knight.png")));
                ToggleButton bb = new ToggleButton("", new ImageView(new Image("/resources/blackBishop.png")));
                ToggleButton wb = new ToggleButton("", new ImageView(new Image("/resources/bishop.png")));
                ToggleButton bq = new ToggleButton("", new ImageView(new Image("/resources/blackQueen.png")));
                ToggleButton wq = new ToggleButton("", new ImageView(new Image("/resources/queen.png")));
                ToggleButton bKng = new ToggleButton("", new ImageView(new Image("/resources/blackKing.png")));
                ToggleButton wKng = new ToggleButton("", new ImageView(new Image("/resources/king.png")));

                bp.setToggleGroup(PiecesGroup);
                wp.setToggleGroup(PiecesGroup);
                br.setToggleGroup(PiecesGroup);
                wr.setToggleGroup(PiecesGroup);
                bk.setToggleGroup(PiecesGroup);
                wk.setToggleGroup(PiecesGroup);
                bb.setToggleGroup(PiecesGroup);
                wb.setToggleGroup(PiecesGroup);
                bq.setToggleGroup(PiecesGroup);
                wq.setToggleGroup(PiecesGroup);
                bKng.setToggleGroup(PiecesGroup);
                wKng.setToggleGroup(PiecesGroup);

                VBox blackPieces = new VBox();
                VBox whitePieces = new VBox();
                blackPieces.getChildren().add(bp);
                whitePieces.getChildren().add(wp);
                blackPieces.getChildren().add(br);
                whitePieces.getChildren().add(wr);
                blackPieces.getChildren().add(bk);
                whitePieces.getChildren().add(wk);
                blackPieces.getChildren().add(bb);
                whitePieces.getChildren().add(wb);
                blackPieces.getChildren().add(bq);
                whitePieces.getChildren().add(wq);
                blackPieces.getChildren().add(bKng);
                whitePieces.getChildren().add(wKng);

                newGameCanvas.setOnMousePressed(new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event) {
                        int x = (int) (Math.round(event.getX()) / 45);
                        int y = (int) (Math.round(event.getY()) / 45);
                        if (event.isPrimaryButtonDown()) {

                            if (PiecesGroup.getSelectedToggle() == bp) {
                                setup.addPiece(new Pawn(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wp) {
                                setup.addPiece(new Pawn(Board.WHITE), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == br) {
                                setup.addPiece(new Rook(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wr) {
                                setup.addPiece(new Rook(Board.WHITE), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == bk) {
                                setup.addPiece(new Knight(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wk) {
                                setup.addPiece(new Knight(Board.WHITE), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == bb) {
                                setup.addPiece(new Bishop(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wb) {
                                setup.addPiece(new Bishop(Board.WHITE), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == bq) {
                                setup.addPiece(new Queen(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wq) {
                                setup.addPiece(new Queen(Board.WHITE), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == bKng) {
                                setup.addPiece(new King(Board.BLACK), x, y);
                            }
                            if (PiecesGroup.getSelectedToggle() == wKng) {
                                setup.addPiece(new King(Board.WHITE), x, y);
                            }
                            redraw(newGameCanvas, setup);
                        }
                        if (event.isSecondaryButtonDown()) {
                            setup.removePiece(x, y);
                            redraw(newGameCanvas, setup);
                        }
                    }

                });

                confirmButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        Client newClient = null;

                        if (gameModesGroup.getSelectedToggle() == sb) {
                            newClient = new Sandbox();
                        }
                        if (gameModesGroup.getSelectedToggle() == sm) {
                            newClient = new seriesMove();
                        }
                        if (newClient != null) {
                            setup.FinishBoard(newClient);
                            client = newClient;
                            client.maxTurns = Integer.parseInt(maxTurns.getText());
                            dialog.close();
                            redraw(canvas, client);
                            turnsRemaining.setText("Turns remaining: "+client.getRemainingTurns());
                        }
                    } 
                });

                cancelButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();
                    }
                });

                PiecesBox.getChildren().add(whitePieces);
                PiecesBox.getChildren().add(blackPieces);
                Right.getChildren().add(maxTurns);
                Right.getChildren().add(PiecesBox);
                Right.getChildren().add(confirmButton);
                Right.getChildren().add(cancelButton);
                Group canvasGroup = new Group();
                canvasGroup.getChildren().add(newGameCanvas);
                newGameLayout.setCenter(canvasGroup);
                newGameLayout.setRight(Right);
                newGameLayout.setTop(gameModes);
                redraw(newGameCanvas, setup);
                Scene dialogScene = new Scene(newGameLayout,550,450);
                dialog.setScene(dialogScene);
                dialog.setTitle("New game"); 
                //dialog.sizeToScene();
                dialog.show();

            }
        });
        undo.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                client.undo();
                redraw(canvas, client);
            }
        });

        canvas.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int x = (int) Math.round(event.getX()) / 45;
                int y = (int) Math.round(event.getY()) / 45;

                if (event.isSecondaryButtonDown()) {
                    client.move(x, y);

                   // System.out.println("click on [" + x + "," + y + "]");
                }
                if (event.isPrimaryButtonDown()) {
                    client.Select(x, y);
                }
                //GraphicsContext gc=canvas.getGraphicsContext2D();
                redraw(canvas, client);
                turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
                

            }
        });

        canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                int x = (int) Math.round(event.getX()) / 45;
                int y = (int) Math.round(event.getY()) / 45;
                client.move(x, y);
                //GraphicsContext gc=canvas.getGraphicsContext2D();
                redraw(canvas, client);
                turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
                if (client.gameWon()) {
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    Text text = new Text("You won!");
                    text.setFont(new Font(20)); 
                    Button yay = new Button("Yay!");
                    yay.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dialogStage.close();
                        }
                    });
                    yay.setPrefSize(100, 20); 
                    VBox box=new VBox(text,yay);
                    box.setAlignment(Pos.CENTER);
                    dialogStage.setScene(new Scene(box));

                    dialogStage.show();
                }
            }
        });
        BorderPane layout = new BorderPane();
        VBox rightPane = new VBox();
        Group group = new Group();
        layout.setCenter(group);
        layout.setRight(rightPane);

        rightPane.setPadding(new javafx.geometry.Insets(0, 5, 0, 5));
        rightPane.setSpacing(10);
        rightPane.setAlignment(Pos.CENTER);
        // rightPane.setVgap(4);

        group.getChildren().add(canvas);

        //rightPane.getChildren().add(newBoard);
        rightPane.getChildren().add(turnsRemaining);
        rightPane.getChildren().add(undo);

        rightPane.getChildren().add(newGame);
        layout.setCenter(canvas);

        redraw(canvas, client);
        Scene scene = new Scene(layout);

        primaryStage.setTitle("Chess puzzle interactive");
        primaryStage.setScene(scene);
        primaryStage.show();
        primary = primaryStage;
    }

    /**
     * @param args the command line arguments
     */
    void redraw(javafx.scene.canvas.Canvas canvas, Client client) {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Board board = client.getBoard();
        canvas.setHeight(45 * board.size);
        canvas.setWidth(45 * board.size);
        board.Draw(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }

}
