package model;

public class Position {
    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }    
    
    public Position translate(Direction d){
        return new Position(x + d.x, y + d.y);
    }
    
    public boolean isNeibour(Position p){
        return Math.abs(x - p.x) <= 1 && Math.abs(y - p.y) <= 1;
    }
}


