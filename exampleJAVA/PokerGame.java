package exampleJAVA;

public class PokerGame {
    public static void main(String[] args) {
        Dealer dealer = new DealerExample();
        Board board = dealer.dealCardsToPlayers();

        System.out.println(board.toString());

        board = dealer.dealFlop(board);
        System.out.println(board.toString());

        board = dealer.dealTurn(board);
        System.out.println(board.toString());

        board = dealer.dealRiver(board);
        System.out.println(board.toString());

        try {
            PokerResult result = dealer.decideWinner(board);
            System.out.println("Result: " + result);
        } catch (InvalidPokerBoardException e) {
            System.err.println("Invalid poker board: " + e.getMessage());
        }
    }
}