package nl.openvalue.rps.players;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class TimBot implements RpsBot {

    private List<Move> opponentHistory = new LinkedList<>();
    private List<Move> myHistory = new LinkedList<>();
    private Strategy currentStrategy = Strategy.RANDOM;
    private Random rand = new Random();
    private Move lastMove = null;

    private static final int MEMORY_SIZE = 100;
    private static final int RECENT_MEMORY_SIZE = 10;

    @Override
    public Move nextMove() {
        identifyStrategy();

        switch (currentStrategy) {
            case COUNTER_BEATER:
                if (lastMove == null) {
                    lastMove = Move.ROCK;
                    return makeMove(lastMove);
                }
                lastMove = lastMove.beatenBy().beatenBy();  // Play the move that beats what BeaterBot expects us to play
                return makeMove(lastMove);

            case DOUBLE_BLUFF:
                if (lastMove == null) {
                    lastMove = Move.ROCK;
                    return makeMove(lastMove);
                }
                Move predictedBeaterbotMove = lastMove.beatenBy();
                lastMove = predictedBeaterbotMove.beatenBy();
                return makeMove(lastMove);

            case ADAPTIVE:
                if (opponentHistory.size() < MEMORY_SIZE) {
                    return makeMove(Move.values()[rand.nextInt(3)]);
                }
                Move predictedMove = predictNextMove();
                return makeMove(predictedMove.beatenBy());

            case RANDOM:
            default:
                return makeMove(Move.values()[rand.nextInt(3)]);
        }
    }

    private Move makeMove(final Move myMove) {
        if (myHistory.size() == MEMORY_SIZE) {
            myHistory.remove(0); // Keep the size to MEMORY_SIZE
        }
        myHistory.add(myMove);

        return myMove;
    }

    private Move predictNextMove() {
        int rockCount = 0, paperCount = 0, scissorsCount = 0;

        for (Move move : opponentHistory) {
            switch (move) {
                case ROCK:
                    rockCount++;
                    break;
                case PAPER:
                    paperCount++;
                    break;
                case SCISSORS:
                    scissorsCount++;
                    break;
            }
        }

        if (rockCount > paperCount && rockCount > scissorsCount) return Move.ROCK;
        if (paperCount > rockCount && paperCount > scissorsCount) return Move.PAPER;
        return Move.SCISSORS; // Default to scissors if counts are even or scissors is the most frequent
    }

    private void identifyStrategy() {
        int size = opponentHistory.size();

        // If the opponent always plays the move that beats our last move, it's the BeaterBot strategy
        int counterBeatCount = 0;
        for (int i = 1; i < size; i++) {
            if (opponentHistory.get(i) == myHistory.get(i - 1).beatenBy()) {
                counterBeatCount++;
            }
        }

        // Double Bluff Strategy Detection (your existing logic):
        int doubleBluffCount = 0;
        for (int i = 1; i < size; i++) {
            if (opponentHistory.get(i) == opponentHistory.get(i - 1).beatenBy()) {
                doubleBluffCount++;
            }
        }

        // Repeating Moves Detection (your existing logic):
        int[] moveCounts = new int[3];
        for (Move move : opponentHistory) {
            moveCounts[move.ordinal()]++;
        }
        int maxCount = Math.max(moveCounts[0], Math.max(moveCounts[1], moveCounts[2]));

        // Strategy Determination:
        if (counterBeatCount > size * 0.6) { // If 60% of the moves are the expected counter beats
            currentStrategy = Strategy.COUNTER_BEATER;
        } else if (doubleBluffCount > size * 0.6) {
            currentStrategy = Strategy.DOUBLE_BLUFF;
        } else if (maxCount > size * 0.6) {
            currentStrategy = Strategy.ADAPTIVE;
        } else {
            currentStrategy = Strategy.ADAPTIVE; // Default to ADAPTIVE if no strong patterns are identified.
        }
    }



    @Override
    public void opponentPlayed(Move move) {
        if (opponentHistory.size() == MEMORY_SIZE) {
            opponentHistory.remove(0); // Keep the size to MEMORY_SIZE
        }
        opponentHistory.add(move);
    }

    private enum Strategy {
        RANDOM,
        DOUBLE_BLUFF,
        ADAPTIVE,
        COUNTER_BEATER  // New strategy added
    }

    @Override
    public String getName() {
        return "TimBot";
    }
}
