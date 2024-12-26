package exampleJAVA;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Deck {
    private final Set<String> cardSet;
    private final List<Card> cards;

    public Deck() {
        cardSet = new HashSet<>();
        cards = new ArrayList<>();
        String[] suits = {"H", "D", "C", "S"};
        String[] ranks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A"};

        for (String suit : suits) {
            for (String rank : ranks) {
                String cardIdentifier = rank + suit;
                if (cardSet.add(cardIdentifier)) {
                    cards.add(new Card(suit, rank));
                }
            }
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() throws InvalidPokerBoardException {
        if (cards.isEmpty()) {
            throw new InvalidPokerBoardException("В колоде нет карт");
        }
        Card card = cards.remove(cards.size() - 1);
        cardSet.remove(card.toString());
        return card;
    }
}