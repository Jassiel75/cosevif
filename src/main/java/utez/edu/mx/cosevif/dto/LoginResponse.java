package utez.edu.mx.cosevif.dto;

public class LoginResponse {
    private String token;
    private String id;
    private String emailOrPhone;
    private String role;

    public LoginResponse(String token, String id, String emailOrPhone, String role) {
        this.token = token;
        this.id = id;
        this.emailOrPhone = emailOrPhone;
        this.role = role;
    }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmailOrPhone() { return emailOrPhone; }
    public void setEmailOrPhone(String emailOrPhone) { this.emailOrPhone = emailOrPhone; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}