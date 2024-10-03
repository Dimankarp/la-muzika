package modernovo.muzika.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

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

    @NotNull
    private Float x;

    public Float getX() {
        return x;
    }

    @NotNull
    private Float y;

    public Float getY() {
        return y;
    }
}
