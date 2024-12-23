package me.fortibrine.snake.board;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

@Getter
@Setter
public class Board {

    private int width;
    private int height;

    private final List<Point> snake = new ArrayList<>();
    private final List<Point> apples = new ArrayList<>();

    private Consumer<Void> loseAction = (action) -> {};
    private Side side = Side.RIGHT;

    private int applesTaken = 0;

    public void setup() {
        setWidth(30);
        setHeight(20);
        snake.clear();
        apples.clear();
        snake.add(new Point(4, 4));
        snake.add(new Point(4, 5));

        createApples(30);

    }

    public void lose() {
        applesTaken = 0;
        setup();

        loseAction.accept(null);
    }

    public void createApples(int count) {
        Random random = new Random();

        List<Point> points = new ArrayList<>();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (snake.contains(new Point(i, j))) {
                    continue;
                }

                if (apples.contains(new Point(i, j))) {
                    continue;
                }

                points.add(new Point(i, j));
            }
        }

        for (int i = 0; i < count; i++) {
            Point point = points.get(random.nextInt(points.size()));
            apples.add(point);
        }
    }

    public MoveResult tick() {
        Point head = snake.get(snake.size() - 1);

        Point newHead = switch (side) {
            case LEFT -> new Point(head.getX() - 1, head.getY());
            case RIGHT -> new Point(head.getX() + 1, head.getY());
            case UP -> new Point(head.getX(), head.getY() - 1);
            case DOWN -> new Point(head.getX(), head.getY() + 1);
        };

        if (newHead.getX() > width) {
            newHead = new Point(0, newHead.getY());
        }

        if (newHead.getX() < 0) {
            newHead = new Point(width, newHead.getY());
        }

        if (newHead.getY() > height) {
            newHead = new Point(newHead.getX(), 0);
        }

        if (newHead.getY() < 0) {
            newHead = new Point(newHead.getX(), height);
        }

        if (snake.contains(newHead)) {
            lose();
            return MoveResult.LOSE;
        }

        if (apples.contains(newHead)) {
            apples.remove(newHead);
            applesTaken++;
            createApples(1);

            snake.add(newHead);
            return MoveResult.TAKE_APPLE;
        }

        snake.add(newHead);
        snake.remove(0);

        return MoveResult.NORMAL;
    }

    public int getState() {
        int state = 0;
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                if (snake.get(snake.size() - 1).equals(new Point(i, j))) {
                    state = state * 4 + 3;
                } else if (snake.contains(new Point(i, j))) {
                    state = state * 4 + 2;
                } else if (apples.contains(new Point(i, j))) {
                    state = state * 4 + 1;
                } else {
                    state = state * 4 + 0;
                }
            }
        }
        return state;
    }

    public int getMaxState() {
        int state = 0;
        for (int i = 1; i <= width; i++) {
            for (int j = 1; j <= height; j++) {
                state = state * 4 + 3;
            }
        }
        return state;
    }

}
