package exampleJAVA;

public class Board {
    private final String playerOne;
    private final String playerTwo;
    private final String flop;
    private final String turn;
    private final String river;

    public String getPlayerOne() {
        return playerOne;
    }

    public String getPlayerTwo() {
        return playerTwo;
    }

    public String getFlop() {
        return flop;
    }

    public String getTurn() {
        return turn;
    }

    public String getRiver() {
        return river;
    }

    public Board(String playerOne, String playerTwo, String flop, String turn, String river) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.flop = flop;
        this.turn = turn;
        this.river = river;
    }

    public String toString() {
        return "Board{" +
                "playerOne='" + playerOne + '\'' +
                ", playerTwo='" + playerTwo + '\'' +
                ", flop='" + flop + '\'' +
                ", turn='" + turn + '\'' +
                ", river='" + river + '\'' +
                '}';
    }
}
