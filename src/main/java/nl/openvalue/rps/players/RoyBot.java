package nl.openvalue.rps.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

public class RoyBot implements RpsBot {

    private List<Move> allHistory = new ArrayList<>();

    @Override
    public String getName() {
        return "RoyBot";
    }

    @Override
    public Move nextMove() {
        int MARKOV_LENGTH = 8;
        if(allHistory.size() < 32) {
            MARKOV_LENGTH = 6;
        } else if(allHistory.size() < 16) {
            MARKOV_LENGTH = 4;
        } else if(allHistory.size() < 4) {
            Move randomMove = Move.randomMove();
            allHistory.add(randomMove);
            return randomMove;
        }

        Map<String, Integer> transitions = new HashMap<>();

        String moves = "";
        for(int i = 0; i < allHistory.size(); i+=2) {
            moves += allHistory.get(i).ordinal();
            moves += allHistory.get(i+1).ordinal();

            if(moves.length() == MARKOV_LENGTH) {
                // Count this sequence:
                transitions.compute(moves, (k, v) -> (v == null) ? 1 : v+1);
                moves = moves.substring(2);
            }
        }

        int bestCount = 0;
        Move likelyNextMove = Move.randomMove();

        // Predict what we can do / and the opponent will do:
        for(Move moveMy : Move.values()) {
            for(Move moveTheir : Move.values()) {
                Integer count = transitions.get(moves + moveMy.ordinal() + moveTheir.ordinal());
                if (count != null && count > bestCount) {
                    bestCount = count;
                    likelyNextMove = moveTheir;
                }
            }
        }

        // Pick what we think the opponent will do and beat that.
        allHistory.add(likelyNextMove.beatenBy());
        return likelyNextMove.beatenBy();
    }

    @Override
    public void opponentPlayed(final Move move) {
        allHistory.add(move);
    }

}
