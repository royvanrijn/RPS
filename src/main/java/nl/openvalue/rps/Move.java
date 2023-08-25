package nl.openvalue.rps;

public enum Move {

    ROCK, PAPER, SCISSORS;

    public Move beats() {
        return Move.values()[(this.ordinal()+2)%3];
    }

    public Move beatenBy() {
        return Move.values()[(this.ordinal()+1)%3];
    }

    public int scoreAgainst(final Move opponentMove) {
        if(this.beats() == opponentMove) {
            return 3;
        } else if(this.beatenBy() == opponentMove) {
            return 0;
        }
        return 1;
    }
}
