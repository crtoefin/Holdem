package exampleJAVA;
import java.util.ArrayList;
import java.util.List;

public class Player {
    final private String name;
    final private List<Card> hand;

    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public List<Card> getHand() {
        return hand;
    }

    public String toString() {
        StringBuilder handString = new StringBuilder();
        for (Card card : hand) {
            handString.append(card).append(" ");
        }
        return name + "=" + handString.toString().trim();
    }
}