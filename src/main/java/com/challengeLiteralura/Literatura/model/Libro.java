package com.challengeLiteralura.Literatura.model;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private Integer numeroDeDescargas;

    @ManyToOne
    private Autor autor;

    public Libro(){}

    public Libro(DatosLibros datosLibros){
        this.titulo = datosLibros.titulo();
        if (datosLibros.idiomas() != null && !datosLibros.idiomas().isEmpty()) {
            this.idioma = datosLibros.idiomas().get(0);
        } else {
            this.idioma = "N/A";
        }
        this.numeroDeDescargas = datosLibros.numeroDeDescargas();
    }

    @Override
    public String toString() {
        return "---------- LIBRO ----------" +
                "\nTitulo: " + titulo +
                "\nIdioma: " + idioma +
                "\nNumero de descargas: " + numeroDeDescargas +
                "\nAutor: " + autor.getNombre() +
                "\n---------------------------\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public Integer getNumeroDeDescargas() {
        return numeroDeDescargas;
    }

    public void setNumeroDeDescargas(Integer numeroDeDescargas) {
        this.numeroDeDescargas = numeroDeDescargas;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }
}
