package modernovo.muzika.model.specifications;

import jakarta.persistence.criteria.Join;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.User;
import org.springframework.data.jpa.domain.Specification;

public class AlbumSpecs {

    public static Specification<Album> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Album> ownerNameEquals(final String ownerName) {
        return (root, query, builder) ->{
            Join<Album, User> bandOwners = root.join("owner");
            return builder.equal(bandOwners.get("username"), ownerName);
        };
    }

}
