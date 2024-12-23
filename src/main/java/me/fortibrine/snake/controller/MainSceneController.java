package me.fortibrine.snake.controller;

import com.github.chen0040.rl.learning.qlearn.QAgent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import me.fortibrine.snake.board.Board;
import me.fortibrine.snake.board.MoveResult;
import me.fortibrine.snake.board.Point;
import me.fortibrine.snake.board.Side;

import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainSceneController {

    @FXML
    private BorderPane rootLayout;

    @FXML
    private Canvas canvas;

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    @FXML
    public void initialize() {

        this.canvas.heightProperty().bind(rootLayout.heightProperty());
        this.canvas.widthProperty().bind(rootLayout.widthProperty());

        Board board = new Board();
        board.setup();

        int actionCount = 4;

        QAgent agent = new QAgent(board.getMaxState(), actionCount);

        agent.start(board.getState());

        executor.scheduleAtFixedRate(() -> {
            GraphicsContext context = canvas.getGraphicsContext2D();

            int actionId = switch (board.getSide()) {
                case LEFT -> agent.selectAction(Set.of(1, 2, 3)).getIndex();
                case RIGHT -> agent.selectAction(Set.of(0, 2, 3)).getIndex();
                case UP -> agent.selectAction(Set.of(0, 1, 3)).getIndex();
                case DOWN -> agent.selectAction(Set.of(0, 1, 2)).getIndex();
            };

            board.setSide(Side.fromValue(actionId));

            MoveResult moveResult = board.tick();

            if (moveResult == MoveResult.LOSE) {
                System.out.println("lose");
                agent.update(actionId, board.getState(), -1);
            } else if (moveResult == MoveResult.TAKE_APPLE) {
                System.out.println("apple take");
                agent.update(actionId, board.getState(), board.getApplesTaken());
            } else {
                agent.update(actionId, board.getState(), 0);
            }

            context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

            double cellWidth = canvas.getWidth() / board.getWidth();
            double cellHeight = canvas.getHeight() / board.getHeight();

            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = 0; j < board.getHeight(); j++) {
                    if (board.getSnake().contains(new Point(i + 1, j  + 1))) {
                        context.setFill(Color.WHITE);
                    } else if (board.getApples().contains(new Point(i + 1, j + 1))) {
                        context.setFill(Color.GREEN);
                    } else {
                        context.setFill(Color.BLACK);
                    }
                    context.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                }
            }

        }, 1000, 20, TimeUnit.MILLISECONDS);
    }

}
