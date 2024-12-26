package exampleJAVA;

public interface Dealer {
    Board dealCardsToPlayers();
    Board dealFlop(Board board);
    Board dealTurn(Board board);
    Board dealRiver(Board board);
    PokerResult decideWinner(Board board) throws InvalidPokerBoardException;
}
