package nl.openvalue.rps.players;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class BeatMostPlayed implements RpsBot {

    private int rockPlayed = 0;
    private int scissorPlayed = 0;
    private int paperPlayed = 0;

    @Override
    public String getName() {
        return "PopularBot";
    }

    public Move nextMove() {

        if(rockPlayed > scissorPlayed && rockPlayed > paperPlayed) {
            return Move.PAPER;
        }
        if(scissorPlayed > paperPlayed) {
            return Move.ROCK;
        }
        return Move.SCISSORS;
    }

    public void opponentPlayed(final Move move) {

        switch (move) {
            case ROCK -> rockPlayed++;
            case PAPER -> paperPlayed++;
            case SCISSORS -> scissorPlayed++;
        }
    }
}
