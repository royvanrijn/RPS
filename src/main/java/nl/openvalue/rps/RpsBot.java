package nl.openvalue.rps;

public interface RpsBot {

    /**
     * Returns the name of this scary opponent
     * @return
     */
    String getName();

    /**
     * First this method is called, you lock in your move.
     * @return
     */
    Move nextMove();

    /**
     * Next this method is called, where you learn what the opponent did.
     * @param move
     */
    void opponentPlayed(Move move);
}
