package com.alura.literalura.model;

import jakarta.persistence.*;


@Entity
@Table(name = "libros")
public class Libro {
    //Mapeo de datos tabla Libros

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String idioma;

    private int numeroDescargas;

    @ManyToOne
    @JoinColumn(name = "escritor_id")
    private Escritor escritor;

    // Getters y Setters de Libro

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

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }

    public Escritor getEscritor() {
        return escritor;
    }

    public void setEscritor(Escritor escritor) {
        this.escritor = escritor;
    }

    @Override
    public String toString() {
        return "LIBRO -----\n" +
                "Título: " + titulo + "\n" +
                "Escritor: " + (escritor != null ? escritor.getNombre() : "Desconocido") + "\n" +
                "Idioma: " + idioma + "\n" +
                "Número de descargas: " + numeroDescargas + "\n" +
                "***************";
    }
}
