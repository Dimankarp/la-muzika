package modernovo.muzika.model.specifications;

import jakarta.persistence.criteria.Join;
import modernovo.muzika.model.MusicBand;
import modernovo.muzika.model.User;
import org.springframework.data.jpa.domain.Specification;

public class BandSpecs {

    public static Specification<MusicBand> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<MusicBand> descriptionLike(final String desc) {
        return (root, query, builder) ->builder.like(root.get("description"), "%" + desc + "%");
    }

    public static Specification<MusicBand> ownerNameEquals(final String ownerName) {
        return (root, query, builder) ->{
            Join<MusicBand, User> bandOwners = root.join("owner");
            return builder.equal(bandOwners.get("username"), ownerName);
        };
    }

}
