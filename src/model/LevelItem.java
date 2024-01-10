package model;

public enum LevelItem {
    WALL('#'), EMPTY(' '), EXIT('E'), DRAGON('D');
    LevelItem(char rep){ representation = rep; }
    public final char representation;
}
