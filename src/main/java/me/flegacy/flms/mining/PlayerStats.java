package me.flegacy.flms.mining;

public class PlayerStats {
    private int fatigue = 0;
    private int haste = 0;
    // TODO add direct mining speed buffs

    public PlayerStats() {}

    public boolean setFatigue(int value) {
        if (value >= 0 && value <= 255)
            fatigue = value;
        else return false;
        return true;
    }

    public boolean setHaste(int value) {
        if (value >= 0 && value <= 255)
            haste = value;
        else return false;
        return true;
    }

    public int getFatigue() {
        return fatigue;
    }

    public int getHaste() {
        return haste;
    }
}