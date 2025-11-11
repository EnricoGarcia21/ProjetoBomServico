package unoeste.fipp.bomservico.dto;

public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private String login;
    private String nome;
    private int nivel;

    public LoginResponse() {
    }

    public LoginResponse(String token, String login, String nome, int nivel) {
        this.token = token;
        this.login = login;
        this.nome = nome;
        this.nivel = nivel;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
}
