package account;

import lombok.Data;

@Data
public class User {
    private String email;
    private String name;
    private String createdAt;
    private String updatedAt;

    public User(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public User() {
    }

    public User(String email, String name, String createdAt, String updatedAt) {
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
