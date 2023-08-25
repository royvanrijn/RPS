package nl.openvalue.rps.players;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class RepeatLastMove implements RpsBot {


    @Override
    public String getName() {
        return "RepeaterBot";
    }

    private Move lastMove = null;

    public Move nextMove() {
        if(lastMove == null) {
            return Move.PAPER;
        }
        // Repeat the opponent, he/she is probably smart right?
        return lastMove;
    }

    public void opponentPlayed(final Move move) {
        lastMove = move;
    }
}
