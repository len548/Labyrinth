package persistence;

import java.util.Objects;

public class HighScore {
    public final String name;
    public final int score;
    
    public HighScore(String name, int score){
        this.name = name;
        this.score = score;
    }
    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + Objects.hashCode(this.name);
        hash = 89 * hash + this.score;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HighScore other = (HighScore) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return this.score == other.score;
    }   

    @Override
    public String toString() {
        return name + "-" + score ;
    }
    
    
}
