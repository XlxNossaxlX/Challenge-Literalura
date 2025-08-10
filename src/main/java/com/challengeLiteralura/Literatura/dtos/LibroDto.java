package com.challengeLiteralura.Literatura.dtos;

public record LibroDto(
        Long id,
        String titulo,
        String idioma,
        Integer numeroDeDescargas
) {}
