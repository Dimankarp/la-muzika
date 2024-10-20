package modernovo.muzika.model;


import jakarta.persistence.*;

@Entity
@Table(name = "coordinates", schema = "public")
public class Coordinates {
    protected Coordinates() {

    }

    public Coordinates(Float x, Float y) {
        this.x = x;
        this.y = y;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    private Float x;

    public Float getX() {
        return x;
    }

    private Float y;

    public Float getY() {
        return y;
    }
}
