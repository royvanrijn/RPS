package nl.openvalue.rps.players;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class ScissorBot implements RpsBot {


    @Override
    public String getName() {
        return "ScissorBot";
    }

    @Override
    public Move nextMove() {
        // Don't play with scissors.
        return Move.SCISSORS;
    }

    @Override
    public void opponentPlayed(final Move move) {
        // Don't care, always 1/3 chance to win.
    }
}
