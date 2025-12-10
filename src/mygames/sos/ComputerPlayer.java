package mygames.sos;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Simple computer opponent.
 *
 * Strategy:
 * 1. Try all empty cells and see if placing "S" or "O" creates an SOS.
 *    If yes, return that move immediately.
 * 2. Otherwise, pick a random empty cell and choose "S" 70% of the time, "O" 30%.
 */
public class ComputerPlayer extends Player {
    private final Random random = new Random();

    public ComputerPlayer(String name) {
        super(name, true);
    }

    @Override
    public Move chooseMove(SOSGame game) {
        String[][] board = game.getGameBoard();
        int n = game.getBoardSize();

        // 1. Look for an immediate SOS move (try "S" then "O" at each empty cell)
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r][c].equals("")) {
                    if (wouldCreateSOS(board, n, r, c, "S")) {
                        return new Move(r, c, "S");
                    }
                    if (wouldCreateSOS(board, n, r, c, "O")) {
                        return new Move(r, c, "O");
                    }
                }
            }
        }

        // 2. No immediate SOS: pick a random empty cell and prefer "S"
        List<int[]> empties = new ArrayList<>();
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                if (board[r][c].equals("")) {
                    empties.add(new int[]{r, c});
                }
            }
        }
        if (empties.isEmpty()) {
            return null; // board is full
        }

        int[] cell = empties.get(random.nextInt(empties.size()));
        String symbol = random.nextDouble() < 0.7 ? "S" : "O";
        return new Move(cell[0], cell[1], symbol);
    }

    // Check if placing symbol at (row,col) would create an SOS anywhere involving that cell
    private boolean wouldCreateSOS(String[][] board, int n, int row, int col, String symbol) {
        java.util.function.BiFunction<Integer, Integer, String> read = (r, c) -> {
            if (r == row && c == col) {
                return symbol;
            }
            return board[r][c];
        };

        // horizontal
        for (int startC = col - 2; startC <= col; startC++) {
            if (startC >= 0 && startC + 2 < n) {
                String a = read.apply(row, startC);
                String b = read.apply(row, startC + 1);
                String c2 = read.apply(row, startC + 2);
                if (a.equals("S") && b.equals("O") && c2.equals("S")) return true;
            }
        }

        // vertical
        for (int startR = row - 2; startR <= row; startR++) {
            if (startR >= 0 && startR + 2 < n) {
                String a = read.apply(startR, col);
                String b = read.apply(startR + 1, col);
                String c2 = read.apply(startR + 2, col);
                if (a.equals("S") && b.equals("O") && c2.equals("S")) return true;
            }
        }

        // diagonal TL -> BR
        for (int k = -2; k <= 0; k++) {
            int r0 = row + k;
            int c0 = col + k;
            if (r0 >= 0 && r0 + 2 < n && c0 >= 0 && c0 + 2 < n) {
                String a = read.apply(r0, c0);
                String b = read.apply(r0 + 1, c0 + 1);
                String c2 = read.apply(r0 + 2, c0 + 2);
                if (a.equals("S") && b.equals("O") && c2.equals("S")) return true;
            }
        }

        // diagonal TR -> BL
        for (int k = -2; k <= 0; k++) {
            int r0 = row + k;
            int c0 = col - k;
            if (r0 >= 0 && r0 + 2 < n && c0 - 2 >= 0 && c0 < n) {
                String a = read.apply(r0, c0);
                String b = read.apply(r0 + 1, c0 - 1);
                String c2 = read.apply(r0 + 2, c0 - 2);
                if (a.equals("S") && b.equals("O") && c2.equals("S")) return true;
            }
        }

        return false;
    }
}