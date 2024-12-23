package me.fortibrine.snake.board;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Side {
    LEFT(0),
    RIGHT(1),
    UP(2),
    DOWN(3);

    private final int value;

    public static Side fromValue(int value) {
        for (Side side : values()) {
            if (side.value == value) {
                return side;
            }
        }
        return Side.LEFT;
    }
}
