package com.alura.literalura.service;

import com.alura.literalura.model.Escritor;
import com.alura.literalura.repository.EscritorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EscritorService {
    
    @Autowired
    private EscritorRepository EscritorRepository;
    
    public List<Escritor> listarEscritores() {
        return EscritorRepository.findAllConLibros();
    }

    public List<Escritor> listarEscritoresVivosEnAno(int ano) {
        return EscritorRepository.findEscritoresVivosEnAnoConLibros(ano);
    }

    public Escritor crearEscritor(Escritor Escritor) {
        return EscritorRepository.save(Escritor);
    }


    public Optional<Escritor> obtenerEscritorPorId(Long id) {
        return EscritorRepository.findById(id);
    }

    public Optional<Escritor> obtenerEscritorPorNombre(String nombre) {
        return EscritorRepository.findByNombre(nombre);
    }


    public Escritor actualizarEscritor(Long id, Escritor EscritorDetalles) {
        Escritor Escritor = EscritorRepository.findById(id).orElseThrow(() -> new RuntimeException("Escritor no encontrado"));
        Escritor.setNombre(EscritorDetalles.getNombre());
        Escritor.setAnoNacimiento(EscritorDetalles.getAnoNacimiento());
        Escritor.setAnoFallecimiento(EscritorDetalles.getAnoFallecimiento());
        return EscritorRepository.save(Escritor);
    }


    public void eliminarEscritor(Long id) {
        EscritorRepository.deleteById(id);
    }
}
