package mygames.sos;

import java.util.List;

public class GameReplayer {

    private final List<Move> moves;
    private int index = 0;

    public GameReplayer(List<Move> moves) {
        this.moves = moves;
    }

    /** Check if one more move exists */
    public boolean hasNext() {
        return index < moves.size();
    }

    /** Return next move in the replay sequence */
    public Move next() {
        return moves.get(index++);
    }

    /** Restart from the beginning */
    public void reset() {
        index = 0;
    }
}
