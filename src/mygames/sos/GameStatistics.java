package mygames.sos;

/**
 * Tracks statistics across multiple completed games.
 * This is independent of per-game SOS counts in SOSGame.
 */
public class GameStatistics {

    private int gamesPlayed;
    private int blueWins;
    private int redWins;
    private int draws;

    public void recordGameResult(int blueSOS, int redSOS) {
        gamesPlayed++;

        if (blueSOS > redSOS) {
            blueWins++;
        } else if (redSOS > blueSOS) {
            redWins++;
        } else {
            draws++;
        }
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getBlueWins() {
        return blueWins;
    }

    public int getRedWins() {
        return redWins;
    }

    public int getDraws() {
        return draws;
    }

    public void reset() {
        gamesPlayed = 0;
        blueWins = 0;
        redWins = 0;
        draws = 0;
    }
}
