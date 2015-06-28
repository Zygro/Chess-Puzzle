/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesspuzzle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 *
 * @author hry
 */
public class ChessPuzzle extends Application {

    Client client;
    final javafx.scene.canvas.Canvas canvas = new javafx.scene.canvas.Canvas();
    Stage primary, newGameDialog, solverStage;
    boolean wasWon = false;

    @Override
    public void start(Stage primaryStage) {

        client = new Sandbox();
        Button btn = new Button();
        Button newBoard = new Button();
        Button newGame = new Button();
        Button undo = new Button();
        Button save = new Button();
        Button load = new Button();
        Button solve = new Button();
        Button edit = new Button();
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Text turnsRemaining = new Text();
        // newBoard.setText("New Board");
        newGame.setText("New Game");
        undo.setText("Undo");
        save.setText("Save");
        load.setText("Load");
        solve.setText("Solve");
        edit.setText("Edit");
        btn.setPrefSize(150, 20);
        solve.setPrefSize(150, 20);
        edit.setPrefSize(150, 20);

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
        solve.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                final Stage dialog = new Stage();
                javafx.scene.canvas.Canvas solverCanvas = new javafx.scene.canvas.Canvas();
                dialog.initOwner(primaryStage);
                BorderPane solverLayout = new BorderPane();
                VBox right = new VBox();
                right.setPadding(
                        new javafx.geometry.Insets(0, 5, 0, 5));
               // dialog.setAlwaysOnTop(true);
                dialog.initModality(Modality.APPLICATION_MODAL);
                right.setSpacing(10);
                right.setAlignment(Pos.CENTER);
                Button closeButton = new Button("Close");
                Button nextTurn = new Button("Next");
                Button previousTurn = new Button("Previous");
                previousTurn.setPrefSize(150, 20);
                nextTurn.setPrefSize(150, 20);
                dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {

                    @Override
                    public void handle(WindowEvent event) {
                        closeButton.fire();
                    }
                });
                redraw(solverCanvas, client);

                nextTurn.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        if (!client.isWinnable()) {
                            Stage dialogStage = new Stage();
                            dialogStage.initModality(Modality.WINDOW_MODAL);
                            dialog.setAlwaysOnTop(true);
                            Text text = new Text("Can't win!");
                            text.setFont(new Font(20));
                            Button ok = new Button("OK");
                            ok.setOnAction(new EventHandler<ActionEvent>() {

                                @Override
                                public void handle(ActionEvent event) {
                                    dialogStage.close();
                                }
                            });
                            ok.setPrefSize(100, 20);
                            VBox box = new VBox(text, ok); 
                            box.setPadding(new javafx.geometry.Insets(0,5,0,5));
                            box.setSpacing(10);
                            box.setAlignment(Pos.CENTER); 
                            dialogStage.setScene(new Scene(box));

                            dialogStage.show();
                        } else {
                            client.autoTurn();
                            redraw(solverCanvas, client);
                        }

                    }
                });
                previousTurn.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        client.undoAutoTurn();
                        redraw(solverCanvas, client);
                    }
                });
                closeButton.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        dialog.close();
                        client.revertSolver();
                    }
                });

                right.getChildren().add(nextTurn);
                right.getChildren().add(previousTurn);
                right.getChildren().add(closeButton);
                solverLayout.setRight(right);
                solverLayout.setCenter(solverCanvas);
                Scene dialogScene = new Scene(solverLayout, 550, 450);
                dialog.setScene(dialogScene);
                dialog.setTitle("Solver");
                //dialog.sizeToScene();
                dialog.show();

            }
        });
        newGameDialog = new Stage();
        javafx.scene.canvas.Canvas newGameCanvas = new javafx.scene.canvas.Canvas();
        newGameDialog.initOwner(primaryStage);
        BorderPane newGameLayout = new BorderPane();
        HBox PiecesBox = new HBox();
        VBox Right = new VBox();
        newGameDialog.setAlwaysOnTop(true);
        newGameDialog.initModality(Modality.APPLICATION_MODAL);
        Right.setSpacing(3);
        Right.setAlignment(Pos.BASELINE_CENTER);
//right.getChildren().add(); 

        TextField maxTurns = new TextField("2");
        Button confirmButton = new Button("OK");
        Button cancelButton = new Button("Cancel");
        confirmButton.setPrefSize(100, 20);
        cancelButton.setPrefSize(100, 20);

        ToggleButton sb = new ToggleButton("Sandbox");
        ToggleButton sm = new ToggleButton("Seriesmove");
        ToggleButton dm = new ToggleButton("Directmate");
        ToggleGroup gameModesGroup = new ToggleGroup();
        sb.setToggleGroup(gameModesGroup);
        sm.setToggleGroup(gameModesGroup);
        dm.setToggleGroup(gameModesGroup);
        gameModesGroup.selectToggle(sb);

        HBox gameModes = new HBox();
        gameModes.getChildren().add(sb);
        gameModes.getChildren().add(sm);
        gameModes.getChildren().add(dm);

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

        newGameSetup setup = new newGameSetup();
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
                if (setup.isViable()) {
                    if (gameModesGroup.getSelectedToggle() == sb) {
                        newClient = new Sandbox();
                    }
                    if (gameModesGroup.getSelectedToggle() == sm) {
                        newClient = new seriesMove();
                    }
                    if (gameModesGroup.getSelectedToggle() == dm) {
                        newClient = new directMate();
                    }
                    if (newClient != null) {
                        newClient.maxTurns = Integer.parseInt(maxTurns.getText());
                        setup.FinishBoard(newClient);
                        client = newClient;
                        newGameDialog.close();
                        redraw(canvas, client);
                        turnsRemaining.setText("Turns remaining: " + client.getRemainingTurns());
                        if (client instanceof Sandbox) {
                            solve.setDisable(true);
                        } else {
                            solve.setDisable(false);
                        }
                    }
                } else {
                    Stage dialogStage = new Stage();
                    dialogStage.initModality(Modality.WINDOW_MODAL);
                    dialogStage.setAlwaysOnTop(true);
                    Text text = new Text("Board requires both kings!");
                    text.setFont(new Font(14));
                    Button ok = new Button("OK");
                    ok.setOnAction(new EventHandler<ActionEvent>() {

                        @Override
                        public void handle(ActionEvent event) {
                            dialogStage.close();
                        }
                    });
                    ok.setPrefSize(100, 20);
                    VBox box = new VBox(text, ok);
                    box.setPadding(
                            new javafx.geometry.Insets(5, 5, 5, 5));
                    box.setSpacing(10);
                    box.setAlignment(Pos.CENTER);
                    dialogStage.setScene(new Scene(box));

                    dialogStage.show();
                }
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                client.revertSolver();
                newGameDialog.close();
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
        Scene dialogScene = new Scene(newGameLayout, 550, 450);
        newGameDialog.setScene(dialogScene);
        newGameDialog.setTitle("New game");
        newGame.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event
                    ) {
                        setup.reset();
                        newGameDialog.show();
                        redraw(newGameCanvas, setup);
                    }
                }
        );
        edit.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                setup.edit(client);
                maxTurns.setText(Integer.toString(client.getMaxMoves()));
                newGameDialog.show();
                if (client instanceof Sandbox) {
                    gameModesGroup.selectToggle(sb);
                }
                if (client instanceof directMate) {
                    gameModesGroup.selectToggle(dm);
                }
                if (client instanceof seriesMove) {
                    gameModesGroup.selectToggle(sm);
                }
                redraw(newGameCanvas, setup);
            }
        });
        undo.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event
                    ) {
                        client.undo();
                        redraw(canvas, client);
                        turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
                        wasWon = false;
                    }
                }
        );
        save.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event
                    ) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save game");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Chess Save File(*.csv)", "*.csv"));

                        File file = fileChooser.showSaveDialog(primaryStage);
                        if (!file.getAbsolutePath().contains(".")) {
                            file = new File(file.getAbsolutePath() + ".csv");
                        }
                        if (file != null) {
                            String fileName = file.getAbsolutePath();

                            /*  String[] parts = fileName.split(".");
                             /* if (parts.length == 1) {
                             fileName = fileName + ".csv";
                             file = new File(fileName);
                             }
                             if (parts.length > 1 && !parts[1].equals("csv")) {*/
                            /*  Stage dialog = new Stage();
                             dialog.initStyle(StageStyle.UTILITY);
                             Scene scene = new Scene(new Group(new Text(25, 25, "Wrong Extension")));
                             dialog.setScene(scene);
                             dialog.show();*/
                            //}
                            client.Save(fileName);
                        }
                    }
                }
        );

        load.setOnAction(
                new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event
                    ) {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Open Resource File");
                        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Chess Save File(*.csv)", "*.csv"));
                        File file = fileChooser.showOpenDialog(primaryStage);
                        if (file != null) {
                            
                            Client newClient = null;
                            try {solve.setDisable(false); 
                                Scanner sc = new Scanner(file);
                                String gameType = sc.nextLine();
                                switch (gameType) {
                                    case "seriesmove":
                                        newClient = new seriesMove();
                                        break;
                                    case "sandbox":
                                        newClient = new Sandbox();
                                        solve.setDisable(true);
                                        break; 
                                    case "directmate":
                                        newClient = new directMate();
                                        break;
                                }
                            } catch (FileNotFoundException ex) {
                                System.out.println("could not find file " + file.getName());
                            }
                            newClient.Load(file.getAbsolutePath());
                            client = newClient;
                            redraw(canvas, client);
                            turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
                        }
                    }
                }
        );
        canvas.setOnMousePressed(
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event
                    ) {
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
                }
        );

        canvas.setOnMouseReleased(
                new EventHandler<MouseEvent>() {

                    @Override
                    public void handle(MouseEvent event
                    ) {
                        int x = (int) Math.round(event.getX()) / 45;
                        int y = (int) Math.round(event.getY()) / 45;
                        client.move(x, y);
                        //GraphicsContext gc=canvas.getGraphicsContext2D();
                        redraw(canvas, client);
                        turnsRemaining.setText("Remaining turns: " + client.getRemainingTurns());
                        if (client.gameWon() && wasWon == false) {
                            wasWon = true;
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
                            VBox box = new VBox(text, yay);
                            box.setAlignment(Pos.CENTER);
                            dialogStage.setScene(new Scene(box));

                            dialogStage.show();
                        }
                    }
                }
        );
        BorderPane layout = new BorderPane();
        VBox rightPane = new VBox();
        Group group = new Group();

        layout.setCenter(group);

        layout.setRight(rightPane);

        rightPane.setPadding(
                new javafx.geometry.Insets(0, 5, 0, 5));
        rightPane.setSpacing(
                10);
        rightPane.setAlignment(Pos.CENTER);
        // rightPane.setVgap(4);

        group.getChildren()
                .add(canvas);

        //rightPane.getChildren().add(newBoard);
        rightPane.getChildren()
                .add(turnsRemaining);
        rightPane.getChildren()
                .add(undo);

        rightPane.getChildren()
                .add(edit);

        rightPane.getChildren()
                .add(newGame);
        rightPane.getChildren()
                .add(solve);
        rightPane.getChildren()
                .add(save);
        rightPane.getChildren()
                .add(load);
        layout.setCenter(canvas);

        redraw(canvas, client);
        Scene scene = new Scene(layout);

        primaryStage.setTitle(
                "Chess puzzle interactive");
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
