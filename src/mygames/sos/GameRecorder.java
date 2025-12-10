package mygames.sos;

import java.io.*;
import java.util.*;

public class GameRecorder {

    private final File file;
    private BufferedWriter writer;

    public GameRecorder(String filename) {
        this.file = new File(filename);
    }

    /** Start writing moves into the file */
    public void start() throws IOException {
        writer = new BufferedWriter(new FileWriter(file));
    }

    /** Record a single move */
    public void recordMove(int row, int col, String symbol) throws IOException {
        if (writer != null) {
            writer.write(row + "," + col + "," + symbol);
            writer.newLine();
            writer.flush();
        }
    }

    /** Stop writing */
    public void stop() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }

    /** Read all moves from the file for replay */
    public static List<Move> load(String filename) throws IOException {
        List<Move> moves = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            int r = Integer.parseInt(parts[0]);
            int c = Integer.parseInt(parts[1]);
            String s = parts[2];
            moves.add(new Move(r, c, s));
        }

        reader.close();
        return moves;
    }
}
