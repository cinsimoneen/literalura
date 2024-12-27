package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

// Mapeo del JSON
@JsonIgnoreProperties(ignoreUnknown = true)
public class LibroDTO {

    @JsonProperty("id")
    private int id;

    @JsonProperty("title")
    private String titulo;

    @JsonProperty("authors")
    private List<EscritorDTO> escritores;

    @JsonProperty("languages")
    private List<String> idiomas;

    @JsonProperty("download_count")
    private int numeroDescargas;

    // Getters y Setters de LibroDTO

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<EscritorDTO> getEscritores() {
        return escritores;
    }

    public void setEscritores(List<EscritorDTO> escritores) {
        this.escritores = escritores;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public int getNumeroDescargas() {
        return numeroDescargas;
    }

    public void setNumeroDescargas(int numeroDescargas) {
        this.numeroDescargas = numeroDescargas;
    }
}