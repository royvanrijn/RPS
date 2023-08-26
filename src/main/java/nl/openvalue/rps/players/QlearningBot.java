package nl.openvalue.rps.players;

import java.util.Random;

import nl.openvalue.rps.Move;
import nl.openvalue.rps.RpsBot;

/**
 * Self-learning bot with a simple Q-learning implementation
 */
public class QlearningBot implements RpsBot {

    private double[] qTable = new double[] {7.284508066549827, 7.483679110656648, 8.242982754275982}; // states x actions
    private double alpha = 0.1; // learning rate
    private double gamma = 0.95; // discount factor
    private double epsilon = 0.5; // exploration rate


    public int selectAction() {
        // epsilon-greedy policy
        Random rand = new Random();
        if (rand.nextDouble() < epsilon) {
            return rand.nextInt(3);
        } else {
            int bestAction = 0;
            for (int i = 1; i < 3; i++) {
                if (qTable[i] > qTable[bestAction]) {
                    bestAction = i;
                }
            }
            return bestAction;
        }
    }

    public void updateQTable(int action, int reward) {
        double bestNextQ = qTable[0];
        for (int i = 1; i < 3; i++) {
            if (qTable[i] > bestNextQ) {
                bestNextQ = qTable[i];
            }
        }

        qTable[action] = qTable[action] +
                alpha * (reward + gamma * bestNextQ - qTable[action]);
    }

    @Override
    public String getName() {
        return "Q-learning Bot";
    }

    private Move lastPlayed;
    @Override
    public Move nextMove() {
        return lastPlayed = Move.values()[selectAction()];
    }

    @Override
    public void opponentPlayed(final Move move) {
        int reward = 0;
        if(lastPlayed.beats() == move) {
            reward = 1;
        } else if(lastPlayed.beatenBy() == move) {
            reward = -1;
        }
        updateQTable(lastPlayed.ordinal(), reward);
    }
}
