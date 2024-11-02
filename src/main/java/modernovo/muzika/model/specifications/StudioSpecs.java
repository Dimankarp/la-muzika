package modernovo.muzika.model.specifications;

import jakarta.persistence.criteria.Join;
import modernovo.muzika.model.Album;
import modernovo.muzika.model.Studio;
import modernovo.muzika.model.User;
import org.springframework.data.jpa.domain.Specification;

public class StudioSpecs {

    public static Specification<Studio> nameLike(final String name) {
        return (root, query, builder) ->builder.like(root.get("name"), "%" + name + "%");
    }
    public static Specification<Studio> addressLike(final String address) {
        return (root, query, builder) ->builder.like(root.get("address"), "%" + address + "%");
    }

    public static Specification<Studio> ownerNameEquals(final String ownerName) {
        return (root, query, builder) ->{
            Join<Album, User> bandOwners = root.join("owner");
            return builder.equal(bandOwners.get("username"), ownerName);
        };
    }

}
