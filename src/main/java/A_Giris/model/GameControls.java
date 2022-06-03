package A_Giris.model;

public enum GameControls {
    MOUSE('m'),KEYBOARD('k');

    private char control;

    private GameControls(char control){
        this.control=control;
    }

    public char getControl(){
        return this.control;
    }
}
