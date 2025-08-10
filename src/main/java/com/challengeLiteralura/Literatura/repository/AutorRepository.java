package com.challengeLiteralura.Literatura.repository;

import com.challengeLiteralura.Literatura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    @Query("SELECT a FROM Autor a WHERE a.fechaDeNacimiento <= :anio AND (a.fechaDeFallecimiento >= :anio OR a.fechaDeFallecimiento IS NULL)")
    List<Autor> findAutoresVivosEnAnio(Integer anio);
    Optional<Autor> findByNombre(String nombre);
}

