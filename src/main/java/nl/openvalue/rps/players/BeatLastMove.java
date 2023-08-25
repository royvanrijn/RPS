package nl.openvalue.rps.players;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class BeatLastMove implements RpsBot {

    @Override
    public String getName() {
        return "BeaterBot";
    }

    private Move lastMove = null;


    public Move nextMove() {
        if(lastMove == null) {
            return Move.ROCK;
        }
        return lastMove.beatenBy();
    }

    public void opponentPlayed(final Move move) {
        lastMove = move;
    }
}
