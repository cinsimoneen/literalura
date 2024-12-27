package com.alura.literalura.repository;

import com.alura.literalura.model.Escritor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface EscritorRepository extends JpaRepository<Escritor, Long> {


    Optional<Escritor> findByNombre(String nombre);

    @Query("SELECT a FROM Escritor a LEFT JOIN FETCH a.libros WHERE (a.anoFallecimiento IS " +
            "NULL OR a.anoFallecimiento > :ano) AND a.anoNacimiento <= :ano")
    List<Escritor> findEscritoresVivosEnAnoConLibros(@Param("ano") int ano);


    @Query("SELECT a FROM Escritor a LEFT JOIN FETCH a.libros")
    List<Escritor> findAllConLibros();
}

