package unoeste.fipp.bomservico.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@Table(name = "interesse")
public class Interesse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "int_id")
    private Long id;

    @Column(name = "int_nome")
    private String nome;

    @Column(name = "int_fone")
    private String fone;

    @Column(name = "int_email")
    private String email;

    @Column(name = "int_mensagem", columnDefinition = "TEXT")
    private String mensagem;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "anu_id")
    @JsonIgnoreProperties({"interesseList", "fotoList", "usuario"})
    private Anuncio anuncio;

    public Interesse() {
    }

    public Interesse(Long id, String nome, String fone, String email, String mensagem, Anuncio anuncio) {
        this.id = id;
        this.nome = nome;
        this.fone = fone;
        this.email = email;
        this.mensagem = mensagem;
        this.anuncio = anuncio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }
}
