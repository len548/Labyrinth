package model;

public enum Direction {
    DOWN(0, 1), LEFT(-1, 0), UP(0, -1), RIGHT(1, 0), 
    UP_LEFT(-1, -1), UP_RIGHT(1, -1), DOWN_LEFT(-1, 1), DOWN_RIGHT(1, 1);
    
    Direction(int x, int y){
        this.x = x;
        this.y = y;
    }
    public final int x, y;
}
