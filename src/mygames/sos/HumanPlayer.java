package mygames.sos;

public class HumanPlayer extends Player {
    public HumanPlayer(String name) {
        super(name, false);
    }

    @Override
    public Move chooseMove(SOSGame game) {
        // Human moves are handled by GUI click events
        return null;
    }
}