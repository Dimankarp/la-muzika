package modernovo.muzika.model;


import jakarta.persistence.*;

@Embeddable
public class Coordinates {
    protected Coordinates() {

    }

    public Coordinates(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    private Float x;


    private Float y;

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }
}
