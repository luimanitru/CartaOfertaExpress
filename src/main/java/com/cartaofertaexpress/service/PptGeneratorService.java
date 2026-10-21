package com.cartaofertaexpress.service;

import com.cartaofertaexpress.model.Candidato;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFShape;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.poi.xslf.usermodel.XSLFTextParagraph;
import org.apache.poi.xslf.usermodel.XSLFTextRun;
import org.apache.poi.xslf.usermodel.XSLFTextShape;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class PptGeneratorService {

    private final File carpetaCartas;

    public PptGeneratorService() {

        carpetaCartas =
                new File("cartas_generadas");

        crearCarpetaCartas();
    }

    // =========================================================
    // OBTENER CARPETA PRINCIPAL DE CARTAS
    // =========================================================

    public File getCarpetaCartas() {

        return carpetaCartas;
    }

    // =========================================================
    // CREAR CARPETA
    // =========================================================

    private void crearCarpetaCartas() {

        if (!carpetaCartas.exists()) {

            if (!carpetaCartas.mkdirs()) {

                throw new RuntimeException(
                        "No se pudo crear la carpeta de cartas generadas."
                );
            }
        }
    }

    // =========================================================
    // GENERAR CARTA AUTOMÁTICAMENTE
    // =========================================================

    public File generarCarta(
            File plantilla,
            Candidato candidato
    ) {

        if (plantilla == null || !plantilla.exists()) {

            throw new RuntimeException(
                    "La plantilla PowerPoint no existe."
            );
        }

        if (candidato == null) {

            throw new RuntimeException(
                    "No se recibió un candidato."
            );
        }

        /*
         * Creamos una carpeta para la fecha actual.
         *
         * Ejemplo:
         *
         * cartas_generadas/
         *     22-08-2026/
         */

        String fecha =
                LocalDate.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy"
                                )
                        );

        File carpetaFecha =
                new File(
                        carpetaCartas,
                        fecha
                );

        if (!carpetaFecha.exists()) {

            if (!carpetaFecha.mkdirs()) {

                throw new RuntimeException(
                        "No se pudo crear la carpeta de la fecha."
                );
            }
        }

        /*
         * Nombre:
         *
         * Carta_Oferta_Nombre Completo.pptx
         */

        String nombreArchivo =
                "Carta_Oferta_"
                        + limpiarNombreArchivo(
                        candidato.getNombre()
                )
                        + ".pptx";

        File archivoSalida =
                new File(
                        carpetaFecha,
                        nombreArchivo
                );

        /*
         * Si ya existe una carta con ese nombre,
         * creamos una versión nueva.
         */

        generar(
                plantilla,
                candidato,
                archivoSalida
        );

        return archivoSalida;
    }

    // =========================================================
    // GENERAR
    // =========================================================

    public void generar(
            File plantilla,
            Candidato candidato,
            File archivoSalida
    ) {

        if (plantilla == null || !plantilla.exists()) {

            throw new RuntimeException(
                    "La plantilla PowerPoint no existe."
            );
        }

        if (candidato == null) {

            throw new RuntimeException(
                    "No se recibió un candidato."
            );
        }

        if (archivoSalida == null) {

            throw new RuntimeException(
                    "No se indicó el archivo de salida."
            );
        }

        File carpetaSalida =
                archivoSalida.getParentFile();

        if (carpetaSalida != null
                && !carpetaSalida.exists()) {

            if (!carpetaSalida.mkdirs()) {

                throw new RuntimeException(
                        "No se pudo crear la carpeta de salida."
                );
            }
        }

        Map<String, String> variables =
                construirVariables(candidato);

        try (
                FileInputStream entrada =
                        new FileInputStream(plantilla);

                XMLSlideShow presentacion =
                        new XMLSlideShow(entrada)
        ) {

            /*
             * Recorremos TODAS las diapositivas.
             *
             * No importa si son 3, 10, 15 o 30.
             */

            for (XSLFSlide diapositiva :
                    presentacion.getSlides()) {

                reemplazarEnDiapositiva(
                        diapositiva,
                        variables
                );
            }

            try (
                    FileOutputStream salida =
                            new FileOutputStream(
                                    archivoSalida
                            )
            ) {

                presentacion.write(salida);
            }

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo generar la carta PowerPoint: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =========================================================
    // VARIABLES
    // =========================================================

    private Map<String, String> construirVariables(
            Candidato candidato
    ) {

        Map<String, String> variables =
                new HashMap<>();

        variables.put(
                "{{NOMBRE}}",
                texto(candidato.getNombre())
        );

        variables.put(
                "{{CEDULA}}",
                texto(candidato.getCedula())
        );

        variables.put(
                "{{CARGO}}",
                texto(candidato.getCargo())
        );

        variables.put(
                "{{SALARIO}}",
                formatearSalario(
                        candidato.getSalario()
                )
        );

        variables.put(
                "{{ROL}}",
                texto(candidato.getRol())
        );

        variables.put(
                "{{TIPO_CONTRATO}}",
                texto(candidato.getTipoContrato())
        );

        variables.put(
                "{{FECHA_INGRESO}}",
                texto(candidato.getFechaIngreso())
        );

        variables.put(
                "{{CIUDAD}}",
                texto(candidato.getCiudad())
        );

        variables.put(
                "{{TASA}}",
                texto(candidato.getTasa())
        );

        variables.put(
                "{{CELULAR}}",
                texto(candidato.getCelular())
        );

        variables.put(
                "{{CORREO}}",
                texto(candidato.getCorreo())
        );

        return variables;
    }

    // =========================================================
    // REEMPLAZAR EN DIAPOSITIVA
    // =========================================================

    private void reemplazarEnDiapositiva(
            XSLFSlide diapositiva,
            Map<String, String> variables
    ) {

        for (XSLFShape shape :
                diapositiva.getShapes()) {

            reemplazarEnShape(
                    shape,
                    variables
            );
        }
    }

    // =========================================================
    // REEMPLAZAR EN SHAPE
    // =========================================================

    private void reemplazarEnShape(
            XSLFShape shape,
            Map<String, String> variables
    ) {

        if (shape instanceof XSLFTextShape textShape) {

            reemplazarEnTexto(
                    textShape,
                    variables
            );
        }
    }

    // =========================================================
    // REEMPLAZAR TEXTO
    // =========================================================

    private void reemplazarEnTexto(
            XSLFTextShape textShape,
            Map<String, String> variables
    ) {

        for (XSLFTextParagraph paragraph :
                textShape.getTextParagraphs()) {

            String textoCompleto =
                    paragraph.getText();

            if (textoCompleto == null
                    || textoCompleto.isEmpty()) {

                continue;
            }

            String textoNuevo =
                    textoCompleto;

            for (
                    Map.Entry<String, String> variable :
                    variables.entrySet()
            ) {

                textoNuevo =
                        textoNuevo.replace(
                                variable.getKey(),
                                variable.getValue()
                        );
            }

            if (textoNuevo.equals(textoCompleto)) {
                continue;
            }

            /*
             * Para evitar destruir innecesariamente
             * la estructura del párrafo, trabajamos
             * con el primer run.
             */

            if (!paragraph.getTextRuns().isEmpty()) {

                XSLFTextRun primerRun =
                        paragraph.getTextRuns()
                                .get(0);

                primerRun.setText(
                        textoNuevo
                );

                /*
                 * Eliminamos los runs restantes
                 * porque ahora el texto completo
                 * está en el primero.
                 */

                for (
                        int i =
                        paragraph.getTextRuns()
                                .size() - 1;
                        i >= 1;
                        i--
                ) {

                    paragraph.removeTextRun(
                            paragraph.getTextRuns()
                                    .get(i)
                    );
                }
            }
        }
    }

    // =========================================================
    // TEXTO SEGURO
    // =========================================================

    private String texto(String valor) {

        if (valor == null) {
            return "";
        }

        return valor;
    }

    // =========================================================
    // SALARIO
    // =========================================================

    private String formatearSalario(
            BigDecimal salario
    ) {

        if (salario == null) {
            return "";
        }

        return String.format(
                "%,.0f",
                salario
        ).replace(
                ",",
                "."
        );
    }

    // =========================================================
    // LIMPIAR NOMBRE DE ARCHIVO
    // =========================================================

    private String limpiarNombreArchivo(
            String nombre
    ) {

        if (nombre == null
                || nombre.isBlank()) {

            return "Sin_Nombre";
        }

        String resultado =
                nombre.trim();

        /*
         * Caracteres que Windows no permite
         * dentro de nombres de archivos.
         */

        resultado =
                resultado.replaceAll(
                        "[\\\\/:*?\"<>|]",
                        "_"
                );

        /*
         * Evitamos saltos de línea y espacios
         * innecesarios.
         */

        resultado =
                resultado.replaceAll(
                        "\\s+",
                        " "
                );

        return resultado;
    }

    // =========================================================
    // EVITAR ARCHIVOS DUPLICADOS
    // =========================================================

    private File obtenerArchivoDisponible(
            File archivoOriginal
    ) {

        if (!archivoOriginal.exists()) {

            return archivoOriginal;
        }

        String nombre =
                archivoOriginal.getName();

        String nombreBase =
                nombre.substring(
                        0,
                        nombre.length() - 5
                );

        File carpeta =
                archivoOriginal.getParentFile();

        int contador = 2;

        File nuevoArchivo;

        do {

            nuevoArchivo =
                    new File(
                            carpeta,
                            nombreBase
                                    + " ("
                                    + contador
                                    + ").pptx"
                    );

            contador++;

        } while (nuevoArchivo.exists());

        return nuevoArchivo;
    }
}