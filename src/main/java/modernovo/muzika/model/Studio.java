package modernovo.muzika.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studio", schema = "public")
public class Studio {

    protected Studio() {

    }

    public Studio(String name, String address) {
        this.name = name;
        this.address = address;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    private String name;

    public String getName() {
        return name;
    }

    private String address;

    public String getAddress() {
        return address;
    }


}
