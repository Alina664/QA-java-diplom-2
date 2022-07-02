package account;

import lombok.Data;

@Data
public class User {
    private String email;
    private String name;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }
}
