package nl.openvalue.rps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.openvalue.rps.players.BeatLastMove;
import nl.openvalue.rps.players.BeatMostPlayed;
import nl.openvalue.rps.players.RepeatLastMove;
import nl.openvalue.rps.players.ScissorBot;

public class Arena {

    public static void main(String[] args) {
        new Arena().battle();
    }

    public void battle() {

        List<Bot> bots = new ArrayList<>();

        bots.add(new Bot(BeatLastMove.class));
        bots.add(new Bot(RepeatLastMove.class));
        bots.add(new Bot(BeatMostPlayed.class));
        bots.add(new Bot(ScissorBot.class));
        // TODO: Add your own bot here... !


        /**
         * Play round-robin:
         */
        for (int p1 = 0; p1 < bots.size() - 1; p1++) {
            for (int p2 = p1 + 1; p2 < bots.size(); p2++) {

                RpsBot player1 = bots.get(p1).newInstance();
                RpsBot player2 = bots.get(p2).newInstance();

                long p1Score = 0;
                long p2Score = 0;

                System.out.println("(" + player1.getName() + ") vs (" + player2.getName() + ")");
                //Match-up:
                for (int round = 0; round < 1000; round++) {

                    // Get player moves:
                    Move m1 = player1.nextMove();
                    Move m2 = player2.nextMove();

                    p1Score += m1.scoreAgainst(m2);
                    p2Score += m2.scoreAgainst(m1);

//                    System.out.println(m1 + "\t" + m2);

                    // Tell what the opponent played:
                    player1.opponentPlayed(m2);
                    player2.opponentPlayed(m1);

                }
                System.out.println(player1.getName() + " scored:\t" + p1Score);
                System.out.println(player2.getName() + " scored:\t" + p2Score);
                System.out.println();

                bots.get(p1).updateScore(p1Score);
                bots.get(p2).updateScore(p2Score);
            }
        }


        Collections.sort(bots);

        System.out.println(" =====Final scores =====");
        // Print result:
        for (Bot bot : bots) {
            String label = bot.newInstance().getName();
            String padding = " ".repeat(25-label.length());
            System.out.println(label + padding + bot.totalScore);
        }
    }

    class Bot implements Comparable<Bot> {

        private Class<? extends RpsBot> clazz;
        private long totalScore;

        Bot(Class<? extends RpsBot> clazz) {
            this.clazz = clazz;
        }

        void updateScore(long pointsToAdd) {
            totalScore += pointsToAdd;
        }

        RpsBot newInstance() {
            try {
                return clazz.newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int compareTo(final Bot o) {
            return Long.compare(o.totalScore, totalScore);
        }
    }
}
