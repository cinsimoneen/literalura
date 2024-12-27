package com.alura.literalura.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoLibrosDTO {

    //Mapeo de la lista de datos de los libros consultados
    @JsonProperty("results")
    private List<LibroDTO> libros;

    // getters y setters
    public List<LibroDTO> getLibros() {
        return libros;
    }

    public void setLibros(List<LibroDTO> libros) {
        this.libros = libros;
    }
}