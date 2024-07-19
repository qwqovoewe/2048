import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private Card[][] cards;  // 当前卡片的状态
    private String gameFlag; // 游戏状态标志

    public GameState(Card[][] cards, String gameFlag) {
        this.cards = cards;
        this.gameFlag = gameFlag;
    }

    public Card[][] getCards() {
        return cards;
    }

    public void setCards(Card[][] cards) {
        this.cards = cards;
    }

    public String getGameFlag() {
        return gameFlag;
    }

    public void setGameFlag(String gameFlag) {
        this.gameFlag = gameFlag;
    }
}
