package A_Giris.model;

import A_Giris.model.GameControls;

public class User {
    private String userName;
    private int score;
    private GameControls gameControls;
    private boolean isActive;

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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setGameControls(GameControls gameControls) {
        this.gameControls = gameControls;
    }
}
