package exampleJAVA;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DealerExample implements Dealer {
    private final Deck deck;
    private final List<Player> players;
    private final List<Card> communityCards;

    public DealerExample() {
        this.deck = new Deck();
        this.players = new ArrayList<>();
        this.players.add(new Player("PlayerOne"));
        this.players.add(new Player("PlayerTwo"));
        this.communityCards = new ArrayList<>();
    }

    @Override
    public Board dealCardsToPlayers() {
        for (Player player : players) {
            try {
                player.addCard(deck.drawCard());
                player.addCard(deck.drawCard());
            } catch (InvalidPokerBoardException e) {
                throw new RuntimeException("Failed to draw cards", e);
            }
        }
        return new Board(players.get(0).getHand().toString(), players.get(1).getHand().toString(), null, null, null);
    }

    @Override
    public Board dealFlop(Board board) {
        List<Card> flopCards = new ArrayList<>();
        try {
            for (int i = 0; i < 3; i++) {
                flopCards.add(deck.drawCard());
            }
        } catch (InvalidPokerBoardException e) {
            throw new RuntimeException("Failed to draw flop cards", e);
        }
        communityCards.addAll(flopCards);
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), flopCards.toString(), null, null);
    }

    @Override
    public Board dealTurn(Board board) {
        Card turnCard;
        try {
            turnCard = deck.drawCard();
        } catch (InvalidPokerBoardException e) {
            throw new RuntimeException("Failed to draw turn card", e);
        }
        communityCards.add(turnCard);
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), turnCard.toString(), null);
    }

    @Override
    public Board dealRiver(Board board) {
        Card riverCard;
        try {
            riverCard = deck.drawCard();
        } catch (InvalidPokerBoardException e) {
            throw new RuntimeException("Failed to draw river card", e);
        }
        communityCards.add(riverCard);
        return new Board(board.getPlayerOne(), board.getPlayerTwo(), board.getFlop(), board.getTurn(), riverCard.toString());
    }

    @Override
    public PokerResult decideWinner(Board board) throws InvalidPokerBoardException {
        List<Player> winners = new ArrayList<>();
        int highestRank = -1;
        List<String> highestHand = new ArrayList<>();

        for (Player player : players) {
            List<Card> fullHand = new ArrayList<>(player.getHand());
            fullHand.addAll(communityCards);
            List<String> handRanks = evaluateHand(fullHand);
            int handRank = Integer.parseInt(handRanks.get(0));

            if (handRank > highestRank) {
                winners.clear();
                winners.add(player);
                highestRank = handRank;
                highestHand = handRanks;
            } else if (handRank == highestRank) {
                if (compareHands(handRanks.subList(1, handRanks.size()), highestHand.subList(1, highestHand.size())) > 0) {
                    winners.clear();
                    winners.add(player);
                    highestHand = handRanks;
                } else if (compareHands(handRanks.subList(1, handRanks.size()), highestHand.subList(1, highestHand.size())) == 0) {
                    winners.add(player);
                }
            }
        }

        if (winners.size() == 1) {
            return winners.get(0).getName().equals("PlayerOne") ? PokerResult.PLAYER_ONE_WIN : PokerResult.PLAYER_TWO_WIN;
        } else {
            return PokerResult.DRAW;
        }
    }

    private int compareHands(List<String> hand1, List<String> hand2) {
        List<String> rankOrder = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
        for (int i = 0; i < hand1.size(); i++) {
            int rankValue1 = rankOrder.indexOf(hand1.get(i));
            int rankValue2 = rankOrder.indexOf(hand2.get(i));
            if (rankValue1 != rankValue2) {
                return Integer.compare(rankValue1, rankValue2);
            }
        }
        return 0;
    }

    private List<String> evaluateHand(List<Card> hand) {
        List<String> result = new ArrayList<>();
        if (isRoyalFlush(hand)) {
            result.add("10"); // Royal Flush
        } else if (isStraightFlush(hand)) {
            result.add("9"); // Straight Flush
        } else if (isFourOfAKind(hand)) {
            result.add("8"); // Four of a Kind
        } else if (isFullHouse(hand)) {
            result.add("7"); // Full House
        } else if (isFlush(hand)) {
            result.add("6"); // Flush
        } else if (isStraight(hand)) {
            result.add("5"); // Straight
        } else if (isThreeOfAKind(hand)) {
            result.add("4"); // Three of a Kind
        } else if (isTwoPair(hand)) {
            result.add("3"); // Two Pair
        } else if (isPair(hand)) {
            result.add("2"); // Pair
        } else {
            result.add("1"); // High Card
        }

        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        ranks.sort((a, b) -> getRankValue(b) - getRankValue(a));
        result.addAll(ranks);
        return result;
    }

    private boolean isRoyalFlush(List<Card> hand) {
        return isStraightFlush(hand) && hand.stream().anyMatch(card -> card.getRank().equals("A"));
    }

    private boolean isStraightFlush(List<Card> hand) {
        return isFlush(hand) && isStraight(hand);
    }

    private boolean isFourOfAKind(List<Card> hand) {
        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        for (String rank : ranks) {
            if (Collections.frequency(ranks, rank) == 4) {
                return true;
            }
        }
        return false;
    }

    private boolean isFullHouse(List<Card> hand) {
        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        boolean hasThree = false;
        boolean hasTwo = false;
        for (String rank : ranks) {
            if (Collections.frequency(ranks, rank) == 3) {
                hasThree = true;
            } else if (Collections.frequency(ranks, rank) == 2) {
                hasTwo = true;
            }
        }
        return hasThree && hasTwo;
    }

    private boolean isFlush(List<Card> hand) {
        List<String> suits = new ArrayList<>();
        for (Card card : hand) {
            suits.add(card.getSuit());
        }
        for (String suit : suits) {
            if (Collections.frequency(suits, suit) >= 5) {
                return true;
            }
        }
        return false;
    }

    private boolean isStraight(List<Card> hand) {
        List<String> rankOrder = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
        List<Integer> rankIndices = new ArrayList<>();
        for (Card card : hand) {
            rankIndices.add(rankOrder.indexOf(card.getRank()));
        }
        Collections.sort(rankIndices);

        for (int i = 0; i <= rankIndices.size() - 5; i++) {
            boolean isStraight = true;
            for (int j = 0; j < 4; j++) {
                if (rankIndices.get(i + j + 1) - rankIndices.get(i + j) != 1) {
                    isStraight = false;
                    break;
                }
            }
            if (isStraight) {
                return true;
            }
        }
        return false;
    }

    private boolean isThreeOfAKind(List<Card> hand) {
        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        for (String rank : ranks) {
            if (Collections.frequency(ranks, rank) == 3) {
                return true;
            }
        }
        return false;
    }

    private boolean isTwoPair(List<Card> hand) {
        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        int pairCount = 0;
        List<String> checkedRanks = new ArrayList<>();
        for (String rank : ranks) {
            if (Collections.frequency(ranks, rank) == 2 && !checkedRanks.contains(rank)) {
                pairCount++;
                checkedRanks.add(rank);
            }
        }
        return pairCount == 2;
    }

    private boolean isPair(List<Card> hand) {
        List<String> ranks = new ArrayList<>();
        for (Card card : hand) {
            ranks.add(card.getRank());
        }
        for (String rank : ranks) {
            if (Collections.frequency(ranks, rank) == 2) {
                return true;
            }
        }
        return false;
    }

    private int getRankValue(String rank) {
        List<String> rankOrder = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
        return rankOrder.indexOf(rank);
    }
}