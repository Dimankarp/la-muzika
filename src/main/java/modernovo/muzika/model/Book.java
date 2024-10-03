package modernovo.muzika.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    Long isbn;

    @Basic(optional = false)
    String title;

    LocalDate publicationDate;
}