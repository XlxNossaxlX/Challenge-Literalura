package com.challengeLiteralura.Literatura.principal;

import com.challengeLiteralura.Literatura.model.*;
import com.challengeLiteralura.Literatura.repository.AutorRepository;
import com.challengeLiteralura.Literatura.repository.LibroRepository;
import com.challengeLiteralura.Literatura.service.ConsumoAPI;
import com.challengeLiteralura.Literatura.service.ConvierteDatos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private Scanner teclado = new Scanner(System.in);

    @Autowired
    private LibroRepository libroRepository;
    @Autowired
    private AutorRepository autorRepository;

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            String menu = """
                    -------------------------------------------------
                    Selecciona una opción:
                    1 - Buscar libro por título
                    2 - Listar libros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos en un determinado año
                    5 - Listar libros por idioma
                    6 - Lista de descargas de libros
                    7 - Top 10 de libros
                    0 - Salir
                    -------------------------------------------------
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTituloYGuardar();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorAnio();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 6:
                    generarEstadisticas();
                    break;
                case 7:
                    listarTop10Libros();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    return;
                default:
                    System.out.println("Opción inválida.");
                    break;
            }
        }
    }

    private void buscarLibroPorTituloYGuardar() {
        System.out.println("Ingresa el título del libro a buscar:");
        String tituloLibro = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloLibro.replace(" ", "+"));
        Datos datosBusqueda = conversor.obtenerDatos(json, Datos.class);

        Optional<DatosLibros> libroEncontrado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toLowerCase().contains(tituloLibro.toLowerCase()))
                .findFirst();

        if (libroEncontrado.isPresent()) {
            DatosLibros datosLibro = libroEncontrado.get();
            DatosAutor datosAutor = datosLibro.autores().stream().findFirst().orElse(null);

            Autor autor = null;
            if (datosAutor != null) {
                Optional<Autor> autorExistente = autorRepository.findByNombre(datosAutor.nombre());
                if (autorExistente.isPresent()) {
                    autor = autorExistente.get();
                } else {
                    autor = new Autor(datosAutor);
                    autorRepository.save(autor);
                }
            }

            Libro libro = new Libro(datosLibro);
            libro.setAutor(autor);
            libroRepository.save(libro);
            System.out.println("Libro y autor guardados exitosamente.");
        } else {
            System.out.println("Libro no encontrado.");
        }
    }

    private void listarLibrosRegistrados() {
        List<Libro> libros = libroRepository.findAll();
        libros.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> autores = autorRepository.findAll();
        autores.forEach(System.out::println);
    }

    private void listarAutoresVivosPorAnio() {
        System.out.println("Ingresa el año para buscar autores vivos:");
        Integer anio = teclado.nextInt();
        teclado.nextLine();
        List<Autor> autoresVivos = autorRepository.findAutoresVivosEnAnio(anio);
        autoresVivos.forEach(System.out::println);
    }

    private void listarLibrosPorIdioma() {
        System.out.println("Ingresa el idioma (ej. 'es' para español):");
        String idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);
        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
        } else {
            librosPorIdioma.forEach(System.out::println);
        }
    }

    private void generarEstadisticas() {
        List<Libro> libros = libroRepository.findAll();

        DoubleSummaryStatistics estadisticas = libros.stream()
                .collect(Collectors.summarizingDouble(Libro::getNumeroDeDescargas));

        System.out.println("Estadísticas de descargas de libros:");
        System.out.println("Media: " + estadisticas.getAverage());
        System.out.println("Máximo: " + estadisticas.getMax());
        System.out.println("Mínimo: " + estadisticas.getMin());
        System.out.println("Total de libros evaluados: " + estadisticas.getCount());
    }

    private void listarTop10Libros() {
        List<Libro> top10Libros = libroRepository.findAll().stream()
                .sorted(Comparator.comparing(Libro::getNumeroDeDescargas).reversed())
                .limit(10)
                .collect(Collectors.toList());

        System.out.println("Top 10 libros más descargados:");
        top10Libros.forEach(System.out::println);
    }
}