package unoeste.fipp.bomservico.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "anuncio")
public class Anuncio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "anu_id")
    private Long id;
    private Usuario usuario; // chave estrangeira
    private String titulo;
    private String desc;
    private String diasTrab;
    private String horarioInicioDia;
    private String horarioFimDia;

}
