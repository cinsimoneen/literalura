package com.alura.literalura.principal;

import com.alura.literalura.dto.EscritorDTO;
import com.alura.literalura.dto.LibroDTO;
import com.alura.literalura.dto.InfoLibrosDTO;
import com.alura.literalura.model.Escritor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.service.EscritorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

@Component
public class Menu {

    @Autowired
    private LibroService libroService;

    @Autowired
    private EscritorService EscritorService;

    @Autowired
    private ConsumoAPI consumoAPI;

    @Autowired
    private ConvierteDatos convierteDatos;

    private static final String BASE_URL = "https://gutendex.com/books/";

    public void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("***** CATALOGO DE LIBROS *****");
            System.out.println("1 - Buscar libro por título");
            System.out.println("2 - Listar libros registrados");
            System.out.println("3 - Listar Escritores registrados");
            System.out.println("4 - Listar Escritores vivos en un año");
            System.out.println("5 - Listar libros por idioma");
            System.out.println("0 - Salir");
            System.out.print("Por favor seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine();

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String titulo = scanner.nextLine();
                    try {
                        String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8);
                        String json = consumoAPI.obtenerDatos(BASE_URL + "?search=" + encodedTitulo);
                        InfoLibrosDTO respuestaLibrosDTO = convierteDatos.obtenerDatos(json, InfoLibrosDTO.class);
                        List<LibroDTO> librosDTO = respuestaLibrosDTO.getLibros();
                        if (librosDTO.isEmpty()) {
                            System.out.println("Libro no encontrado en la API");
                        } else {
                            boolean libroRegistrado = false;
                            for (LibroDTO libroDTO : librosDTO) {
                                if (libroDTO.getTitulo().equalsIgnoreCase(titulo)) {
                                    Optional<Libro> libroExistente = libroService.obtenerLibroPorTitulo(titulo);
                                    if (libroExistente.isPresent()) {
                                        System.out.println("Detalle: Clave (titulo)=(" + titulo + ") ya existe");
                                        System.out.println("Libro ya registrado");
                                        libroRegistrado = true;
                                        break;
                                    } else {
                                        Libro libro = new Libro();
                                        libro.setTitulo(libroDTO.getTitulo());
                                        libro.setIdioma(libroDTO.getIdiomas().get(0));
                                        libro.setNumeroDescargas(libroDTO.getNumeroDescargas());

                                        // Buscar o crear el Escritor
                                        EscritorDTO primerEscritorDTO = libroDTO.getEscritores().get(0);
                                        Escritor Escritor = EscritorService.obtenerEscritorPorNombre(primerEscritorDTO.getNombre())
                                                .orElseGet(() -> {
                                                    Escritor nuevoEscritor = new Escritor();
                                                    nuevoEscritor.setNombre(primerEscritorDTO.getNombre());
                                                    nuevoEscritor.setAnoNacimiento(primerEscritorDTO.getAnoNacimiento());
                                                    nuevoEscritor.setAnoFallecimiento(primerEscritorDTO.getAnoFallecimiento());
                                                    return EscritorService.crearEscritor(nuevoEscritor);
                                                });

                                        // Asociar el Escritor al Libro
                                        libro.setEscritor(Escritor);

                                        // Guardar el libro en la base de datos
                                        libroService.crearLibro(libro);
                                        System.out.println("Libro registrado: " + libro.getTitulo());
                                        mostrarDetallesLibro(libroDTO);
                                        libroRegistrado = true;
                                        break;
                                    }
                                }
                            }
                            if (!libroRegistrado) {
                                System.out.println("No se encontró un libro con el título '" + titulo + "' en la API");
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Error al obtener datos de la API: " + e.getMessage());
                    }
                    break;
                case 2:
                    libroService.listarLibros().forEach(libro -> {
                        System.out.println("**** LIBRO ****");
                        System.out.println("Título: " + libro.getTitulo());
                        System.out.println("Escritor: " + (libro.getEscritor() != null ? libro.getEscritor().getNombre() : "Desconocido"));
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                    });
                    break;
                case 3:
                    EscritorService.listarEscritores().forEach(Escritor -> {
                        System.out.println("**** ESCRITOR ****");
                        System.out.println("Escritor: " + Escritor.getNombre());
                        System.out.println("Fecha de nacimiento: " + Escritor.getAnoNacimiento());
                        System.out.println("Fecha de fallecimiento: " + (Escritor.getAnoFallecimiento() != null ? Escritor.getAnoFallecimiento() : "Desconocido"));
                        String libros = Escritor.getLibros().stream()
                                .map(Libro::getTitulo)
                                .collect(Collectors.joining(", "));
                        System.out.println("Libros: [ " + libros + " ]");
                    });
                    break;
                case 4:
                    System.out.print("Ingrese el año de Escritor(es) vivos que desea buscar: ");
                    int ano = scanner.nextInt();
                    scanner.nextLine();
                    List<Escritor> EscritoresVivos = EscritorService.listarEscritoresVivosEnAno(ano);
                    if (EscritoresVivos.isEmpty()) {
                        System.out.println("No se encontraron Escritores vivos en el año " + ano);
                    } else {
                        EscritoresVivos.forEach(Escritor -> {
                            System.out.println("**** ESCRITOR ****");
                            System.out.println("Escritor: " + Escritor.getNombre());
                            System.out.println("Fecha de nacimiento: " + Escritor.getAnoNacimiento());
                            System.out.println("Fecha de fallecimiento: " + (Escritor.getAnoFallecimiento() != null ? Escritor.getAnoFallecimiento() : "Desconocido"));
                            System.out.println("Libros: " + Escritor.getLibros().size());
                        });
                    }
                    break;
                case 5:
                    System.out.println("Ingrese el idioma:");
                    System.out.println("es");
                    System.out.println("en");
                    System.out.println("fr");
                    System.out.println("pt");
                    String idioma = scanner.nextLine();
                    if ("es".equalsIgnoreCase(idioma) || "en".equalsIgnoreCase(idioma) || "fr".equalsIgnoreCase(idioma) || "pt".equalsIgnoreCase(idioma)) {
                        libroService.listarLibrosPorIdioma(idioma).forEach(libro -> {
                            System.out.println("****LIBRO****");
                            System.out.println("Título: " + libro.getTitulo());
                            System.out.println("Escritor: " + (libro.getEscritor() != null ? libro.getEscritor().getNombre() : "Desconocido"));
                            System.out.println("Idioma: " + libro.getIdioma());
                            System.out.println("Número de descargas: " + libro.getNumeroDescargas());
                        });
                    } else {
                        System.out.println("Idioma no válido. Intente de nuevo.");
                    }
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcion != 0);

        scanner.close();
    }

    private void mostrarDetallesLibro(LibroDTO libroDTO) {
        System.out.println("**** LIBRO ****");
        System.out.println("Título: " + libroDTO.getTitulo());
        System.out.println("Escritor: " + (libroDTO.getEscritores().isEmpty() ? "Desconocido" : libroDTO.getEscritores().get(0).getNombre()));
        System.out.println("Idioma: " + libroDTO.getIdiomas().get(0));
        System.out.println("Número de descargas: " + libroDTO.getNumeroDescargas());
    }
}
