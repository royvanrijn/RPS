package nl.openvalue.rps.players;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

/**
 * Bot based on an order-2 Markov Chain
 * (Fully written by ChatGPT)
 */
public class MarkovBot implements RpsBot {
    private Move lastMove1 = null;
    private Move lastMove2 = null;
    private final Map<MovePair, Map<Move, Integer>> transitions = new HashMap<>();
    private final Random random = new Random();

    public MarkovBot() {
        for (Move move1 : Move.values()) {
            for (Move move2 : Move.values()) {
                MovePair pair = new MovePair(move1, move2);
                transitions.put(pair, new HashMap<>());
                for (Move nextMove : Move.values()) {
                    transitions.get(pair).put(nextMove, 0);
                }
            }
        }
    }

    @Override
    public String getName() {
        return "MarkovBot";
    }

    @Override
    public Move nextMove() {
        if (lastMove1 == null || lastMove2 == null) {
            return randomMove(); // Play random move if we don't have enough history
        }

        Map<Move, Integer> nextMoveCounts = transitions.get(new MovePair(lastMove1, lastMove2));
        Move likelyNextMove = Move.ROCK;
        int maxCount = -1;

        for (Move move : Move.values()) {
            if (nextMoveCounts.get(move) > maxCount) {
                likelyNextMove = move;
                maxCount = nextMoveCounts.get(move);
            }
        }

        return counterMove(likelyNextMove);
    }

    @Override
    public void opponentPlayed(final Move move) {
        if (lastMove1 != null && lastMove2 != null) {
            int count = transitions.get(new MovePair(lastMove1, lastMove2)).get(move);
            transitions.get(new MovePair(lastMove1, lastMove2)).put(move, count + 1);
        }
        lastMove1 = lastMove2;
        lastMove2 = move;
    }

    private Move counterMove(Move move) {
        switch (move) {
            case ROCK:
                return Move.PAPER;
            case PAPER:
                return Move.SCISSORS;
            case SCISSORS:
                return Move.ROCK;
            default:
                return randomMove();
        }
    }

    private Move randomMove() {
        int choice = random.nextInt(3);
        switch (choice) {
            case 0:
                return Move.ROCK;
            case 1:
                return Move.PAPER;
            default:
                return Move.SCISSORS;
        }
    }

    private static class MovePair {
        final Move move1;
        final Move move2;

        MovePair(Move move1, Move move2) {
            this.move1 = move1;
            this.move2 = move2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            MovePair movePair = (MovePair) o;
            return move1 == movePair.move1 && move2 == movePair.move2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(move1, move2);
        }
    }
}
