package modernovo.muzika.model;

public class UserDTO{

    public UserDTO(String name) {
        this.name = name;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
