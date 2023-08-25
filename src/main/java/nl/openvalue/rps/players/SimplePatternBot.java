package nl.openvalue.rps.players;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class SimplePatternBot implements RpsBot {

    static int movePtr = 0;
    Move[] moves = new Move[] {
            Move.ROCK, Move.PAPER, Move.PAPER, Move.SCISSORS, Move.SCISSORS, Move.SCISSORS, Move.ROCK, Move.ROCK, Move.PAPER
    };

    @Override
    public String getName() {
        return "PatternBot";
    }

    @Override
    public Move nextMove() {
        if(movePtr >= moves.length) {
            movePtr = 0;
        }
        return moves[movePtr++];
    }

    @Override
    public void opponentPlayed(final Move move) {
        // Don't care
    }
}
