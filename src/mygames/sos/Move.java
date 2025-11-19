package mygames.sos;

/**
 * Simple value object representing a move: row, col and symbol ("S" or "O").
 */
public class Move {
    public final int row;
    public final int col;
    public final String symbol;

    public Move(int row, int col, String symbol) {
        this.row = row;
        this.col = col;
        this.symbol = symbol;
    }
}
