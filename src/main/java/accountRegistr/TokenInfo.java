package accountRegistr;

import lombok.Data;

@Data
public class TokenInfo {
    private boolean success;
    private User user;
    private String accessToken;
    private String refreshToken;

    public TokenInfo(boolean success, User user, String accessToken, String refreshToken) {
        this.success = success;
        this.user = user;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public TokenInfo() {
    }
}
