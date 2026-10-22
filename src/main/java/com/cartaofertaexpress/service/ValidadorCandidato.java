package com.cartaofertaexpress.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.regex.Pattern;

public class ValidadorCandidato {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private static final Pattern SOLO_NUMEROS =
            Pattern.compile("\\d+");

    private static final Pattern CEDULA =
            Pattern.compile("[0-9.-]+");

    private static final Pattern CORREO =
            Pattern.compile(
                    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
            );


    // =====================================================
    // NOMBRE
    // =====================================================

    public static String normalizarNombre(String texto) {

        if (texto == null || texto.isBlank()) {
            return "";
        }

        String[] palabras =
                texto.trim()
                        .toLowerCase()
                        .replaceAll("\\s+", " ")
                        .split(" ");

        StringBuilder resultado =
                new StringBuilder();

        for (String palabra : palabras) {

            if (palabra.isBlank()) {
                continue;
            }

            if (resultado.length() > 0) {
                resultado.append(" ");
            }

            resultado.append(
                    Character.toUpperCase(
                            palabra.charAt(0)
                    )
            );

            if (palabra.length() > 1) {

                resultado.append(
                        palabra.substring(1)
                );
            }
        }

        return resultado.toString();
    }


    // =====================================================
    // TEXTO GENERAL
    // =====================================================

    public static String normalizarTexto(String texto) {

        if (texto == null || texto.isBlank()) {
            return "";
        }

        texto =
                texto.trim()
                        .replaceAll("\\s+", " ")
                        .toLowerCase();

        return Character.toUpperCase(
                texto.charAt(0)
        ) + texto.substring(1);
    }


    // =====================================================
    // CÉDULA
    // =====================================================

    public static String validarCedula(
            String cedula
    ) {

        if (cedula == null || cedula.isBlank()) {

            throw new IllegalArgumentException(
                    "La cédula es obligatoria."
            );
        }

        cedula =
                cedula.trim();

        if (!CEDULA.matcher(cedula).matches()) {

            throw new IllegalArgumentException(
                    "La cédula solo puede contener números, guiones (-) y puntos (.)."
            );
        }

        return cedula;
    }



    // =====================================================
    // CELULAR
    // =====================================================

    public static String validarCelular(String celular) {

        // El celular es opcional.
        if (celular == null || celular.isBlank()) {
            return "";
        }

        celular = celular.trim();

        if (!SOLO_NUMEROS.matcher(celular).matches()) {
            throw new IllegalArgumentException(
                    "El celular solo puede contener números."
            );
        }

        return celular;
    }


    // =====================================================
    // CORREO
    // =====================================================

    public static String validarCorreo(String correo) {

        // El correo es opcional.
        if (correo == null || correo.isBlank()) {
            return "";
        }

        correo = correo.trim().toLowerCase();

        if (!CORREO.matcher(correo).matches()) {
            throw new IllegalArgumentException(
                    "El correo no tiene un formato válido."
            );
        }

        return correo;
    }



    // =====================================================
    // FECHA
    // =====================================================

    public static String validarFecha(
            String fecha
    ) {

        if (fecha == null || fecha.isBlank()) {

            throw new IllegalArgumentException(
                    "La fecha de ingreso es obligatoria."
            );
        }

        fecha =
                fecha.trim();

        try {

            LocalDate.parse(
                    fecha,
                    FORMATO_FECHA
            );

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    "La fecha debe tener el formato DD/MM/AAAA."
            );
        }

        return fecha;
    }


    // =====================================================
    // SALARIO
    // =====================================================

    public static String normalizarSalario(
            String salario
    ) {

        if (salario == null || salario.isBlank()) {

            throw new IllegalArgumentException(
                    "El salario es obligatorio."
            );
        }

        return salario
                .trim()
                .replace(" ", "");
    }
    public static String normalizarCelular(String celular) {

        if (celular == null) {
            return "";
        }

        String texto = celular.trim();

        if (texto.isEmpty()) {
            return "";
        }

        // Permitimos espacios, guiones y paréntesis al escribir.
        texto = texto
                .replace(" ", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "");

        // Después de limpiar, solamente pueden quedar números.
        if (!texto.matches("\\d+")) {
            throw new IllegalArgumentException(
                    "El celular solo puede contener números."
            );
        }

        return texto;
    }
}