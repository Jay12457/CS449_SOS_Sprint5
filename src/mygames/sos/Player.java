package mygames.sos;

public abstract class Player {
    protected final String name;
    protected final boolean computer;

    public Player(String name, boolean computer) {
        this.name = name;
        this.computer = computer;
    }

    public String getName() {
        return name;
    }

    public boolean isComputer() {
        return computer;
    }

    /**
     * For computer players: return the move the player wants to make.
     * For human players: return null (GUI handles human clicks).
     */
    public abstract Move chooseMove(SOSGame game);
}