package A_Giris.model;

import A_Giris.model.GameControls;

import java.util.concurrent.atomic.AtomicBoolean;

public class User {
    private String userName;
    private int score;
    private GameControls gameControls;
    //threadler aynı anda kullanırken senkronize olması açısından
    private AtomicBoolean isActive = new AtomicBoolean(false);
    private boolean isWin;
    private int playedCount;
    private int wined;
    private int loosed;

    public User(String userName, int score) {
        this.userName = userName;
        this.score = score;
    }

    public User(String userName, int score, int playedCount, int wined, int loosed) {
        this.userName = userName;
        this.score = score;
        this.playedCount = playedCount;
        this.wined = wined;
        this.loosed = loosed;
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

    public int getPlayedCount() {
        return playedCount;
    }

    public void setPlayedCount(int playedCount) {
        this.playedCount = playedCount;
    }

    public int getWined() {
        return wined;
    }

    public void setWined(int wined) {
        this.wined = wined;
    }

    public int getLoosed() {
        return loosed;
    }

    public void setLoosed(int loosed) {
        this.loosed = loosed;
    }

    public boolean isWin() {
        return isWin;
    }

    public void setWin(boolean win) {
        isWin = win;
    }
}
