package com.challengeLiteralura.Literatura.dtos;

public record AutorDto(
        Long id,
        String nombre,
        Integer fechaDeNacimiento,
        Integer fechaDeFallecimiento
) {}
