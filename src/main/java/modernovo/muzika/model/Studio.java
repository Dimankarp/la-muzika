package modernovo.muzika.model;

import jakarta.persistence.*;

@Entity
@Table(name = "studio", schema = "public")
public class Studio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;

    private String address;

}
