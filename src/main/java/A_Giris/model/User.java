package A_Giris.model;

import A_Giris.model.GameControls;

import java.util.concurrent.atomic.AtomicBoolean;

public class User {
    private String userName;
    private int score;
    private GameControls gameControls;
    //threadler aynı anda kullanırken senkronize olması açısından
    private AtomicBoolean isActive = new AtomicBoolean(false);

    public User(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public GameControls getGameControls() {
        return gameControls;
    }

    public AtomicBoolean getIsActive() {
        return isActive;
    }

    public void setIsActive(AtomicBoolean isActive) {
        this.isActive = isActive;
    }

    public void setGameControls(GameControls gameControls) {
        this.gameControls = gameControls;
    }
}
