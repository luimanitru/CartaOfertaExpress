package com.cartaofertaexpress.service;

import com.cartaofertaexpress.model.Candidato;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExcelImportService {

    /**
     * Formato final de las fechas.
     *
     * Ejemplo:
     * 25/08/2026
     */
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy");


    // =====================================================
    // CONFIGURACIÓN
    // =====================================================

    private final ConfiguracionService configuracionService;


    // =====================================================
    // RESULTADO DE IMPORTACIÓN
    // =====================================================

    public static class ResultadoImportacion {

        private final List<Candidato> candidatos;
        private final int candidatosSinCedula;

        public ResultadoImportacion(
                List<Candidato> candidatos,
                int candidatosSinCedula
        ) {

            this.candidatos =
                    candidatos;

            this.candidatosSinCedula =
                    candidatosSinCedula;
        }

        public List<Candidato> getCandidatos() {

            return candidatos;
        }

        public int getCandidatosSinCedula() {

            return candidatosSinCedula;
        }
    }


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public ExcelImportService() {

        configuracionService =
                new ConfiguracionService();
    }


    // =====================================================
    // IMPORTAR EXCEL
    // =====================================================

    public ResultadoImportacion importar(
            File archivo
    ) {

        List<Candidato> candidatos =
                new ArrayList<>();

        int candidatosSinCedula = 0;


        try (
                FileInputStream entrada =
                        new FileInputStream(archivo);

                Workbook workbook =
                        new XSSFWorkbook(entrada)
        ) {

            Sheet hoja =
                    workbook.getSheetAt(0);

            /*
             * =================================================
             * TIPOS DE CONTRATO EXISTENTES
             * =================================================
             *
             * Cargamos una sola vez los tipos existentes
             * en configuración.
             *
             * Esta lista será actualizada durante la
             * importación si aparecen tipos nuevos.
             */
            List<String> tiposContrato =
                    new ArrayList<>(
                            configuracionService
                                    .obtenerTiposContrato()
                    );


            // =================================================
            // FILAS
            // =================================================

            java.util.Iterator<Row> filas =
                    hoja.iterator();


            // =================================================
            // VALIDAR ARCHIVO VACÍO
            // =================================================

            if (!filas.hasNext()) {

                throw new RuntimeException(
                        "El archivo Excel está vacío."
                );
            }


            // =================================================
            // ENCABEZADOS
            // =================================================

            Row filaEncabezados =
                    filas.next();


            int columnaNombre = -1;
            int columnaCedula = -1;
            int columnaCargo = -1;
            int columnaSalario = -1;
            int columnaRol = -1;
            int columnaTipoContrato = -1;
            int columnaFechaIngreso = -1;
            int columnaCiudad = -1;
            int columnaTasa = -1;
            int columnaCelular = -1;
            int columnaCorreo = -1;


            // =================================================
            // BUSCAR COLUMNAS
            // =================================================

            for (Cell celda :
                    filaEncabezados
            ) {

                String encabezado =
                        normalizarEncabezado(
                                obtenerTexto(celda)
                        );


                switch (encabezado) {

                    case "nombre":

                        columnaNombre =
                                celda.getColumnIndex();

                        break;


                    case "cedula":

                        columnaCedula =
                                celda.getColumnIndex();

                        break;


                    case "cargo":

                        columnaCargo =
                                celda.getColumnIndex();

                        break;


                    case "salario":

                        columnaSalario =
                                celda.getColumnIndex();

                        break;


                    case "rol":

                        columnaRol =
                                celda.getColumnIndex();

                        break;


                    /*
                     * Aceptamos:
                     *
                     * Tipo contrato
                     * Tipo de contrato
                     * TipoContrato
                     * tipo_contrato
                     */
                    case "tipocontrato":
                    case "tipodecontrato":

                        columnaTipoContrato =
                                celda.getColumnIndex();

                        break;


                    /*
                     * Aceptamos:
                     *
                     * Fecha ingreso
                     * Fecha de ingreso
                     */
                    case "fechaingreso":
                    case "fechadeingreso":

                        columnaFechaIngreso =
                                celda.getColumnIndex();

                        break;


                    case "ciudad":

                        columnaCiudad =
                                celda.getColumnIndex();

                        break;


                    case "tasa":

                        columnaTasa =
                                celda.getColumnIndex();

                        break;


                    case "celular":
                    case "telefono":
                    case "telefonocelular":

                        columnaCelular =
                                celda.getColumnIndex();

                        break;


                    /*
                     * Aceptamos:
                     *
                     * Correo
                     * Email
                     * E-mail
                     * Correo electrónico
                     */
                    case "correo":
                    case "email":
                    case "correoelectronico":

                        columnaCorreo =
                                celda.getColumnIndex();

                        break;
                }
            }


            // =================================================
            // CÉDULA OBLIGATORIA
            // =================================================

            if (columnaCedula == -1) {

                throw new RuntimeException(
                        "El archivo Excel no contiene la columna obligatoria 'CEDULA'."
                );
            }


            // =================================================
            // LEER FILAS
            // =================================================

            while (filas.hasNext()) {

                Row fila =
                        filas.next();


                // =================================================
                // IGNORAR FILAS VACÍAS
                // =================================================

                if (filaVacia(fila)) {

                    continue;
                }


                // =================================================
                // CÉDULA
                // =================================================

                String cedula =
                        obtenerTexto(
                                fila,
                                columnaCedula
                        );


                /*
                 * Las filas sin cédula no se importan.
                 */

                if (cedula.isBlank()) {

                    candidatosSinCedula++;

                    continue;
                }


                /*
                 * La cédula solamente puede contener números.
                 *
                 * Si Excel la trae con espacios,
                 * los eliminamos.
                 */

                cedula =
                        cedula
                                .trim()
                                .replace(
                                        " ",
                                        ""
                                );


                if (!cedula.matches("[0-9.-]+")) {
                    candidatosSinCedula++;
                    continue;
                }


                // =================================================
                // NOMBRE
                // =================================================

                String nombre =
                        ValidadorCandidato.normalizarNombre(
                                obtenerTexto(
                                        fila,
                                        columnaNombre
                                )
                        );


                // =================================================
                // CARGO
                // =================================================

                String cargo =
                        ValidadorCandidato.normalizarTexto(
                                obtenerTexto(
                                        fila,
                                        columnaCargo
                                )
                        );


                // =================================================
                // ROL
                // =================================================

                String rol =
                        ValidadorCandidato.normalizarTexto(
                                obtenerTexto(
                                        fila,
                                        columnaRol
                                )
                        );


                // =================================================
                // TIPO DE CONTRATO
                // =================================================

                String tipoContratoExcel =
                        ValidadorCandidato.normalizarTexto(
                                obtenerTexto(
                                        fila,
                                        columnaTipoContrato
                                )
                        );


                /*
                 * Buscamos si el tipo ya existe en configuración.
                 *
                 * La comparación ignora:
                 *
                 * - mayúsculas
                 * - minúsculas
                 * - tildes
                 * - espacios sobrantes
                 *
                 * Ejemplo:
                 *
                 * "Termino indefinido"
                 *
                 * coincide con:
                 *
                 * "Término indefinido"
                 */
                String tipoContrato =
                        obtenerTipoContratoConfigurado(
                                tipoContratoExcel,
                                tiposContrato
                        );


                // =================================================
                // FECHA DE INGRESO
                // =================================================

                String fechaIngreso =
                        obtenerFecha(
                                fila,
                                columnaFechaIngreso
                        );


                // =================================================
                // CIUDAD
                // =================================================

                String ciudad =
                        ValidadorCandidato.normalizarTexto(
                                obtenerTexto(
                                        fila,
                                        columnaCiudad
                                )
                        );


                // =================================================
                // TASA
                // =================================================

                String tasa =
                        obtenerTexto(
                                fila,
                                columnaTasa
                        );


                // =================================================
                // CELULAR
                // =================================================

                String celular =
                        normalizarCelularExcel(
                                obtenerTexto(
                                        fila,
                                        columnaCelular
                                )
                        );


                // =================================================
                // CORREO
                // =================================================

                String correo =
                        normalizarCorreoExcel(
                                obtenerTexto(
                                        fila,
                                        columnaCorreo
                                )
                        );


                // =================================================
                // SALARIO
                // =================================================

                BigDecimal salario =
                        obtenerSalario(
                                fila,
                                columnaSalario
                        );


                // =================================================
                // CREAR CANDIDATO
                // =================================================

                Candidato candidato =
                        new Candidato(
                                nombre,
                                cedula,
                                cargo,
                                salario,
                                rol,
                                tipoContrato,
                                fechaIngreso,
                                ciudad,
                                tasa,
                                celular,
                                correo
                        );


                candidatos.add(
                        candidato
                );
            }


            // =================================================
            // GUARDAR TIPOS DE CONTRATO ACTUALIZADOS
            // =================================================

            configuracionService
                    .guardarTiposContrato(
                            tiposContrato
                    );


            // =================================================
            // RESULTADO
            // =================================================

            return new ResultadoImportacion(
                    candidatos,
                    candidatosSinCedula
            );


        } catch (RuntimeException e) {

            throw e;

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo leer el archivo Excel: "
                            + e.getMessage(),
                    e
            );
        }
    }


    // =====================================================
    // NORMALIZAR ENCABEZADO
    // =====================================================

    private String normalizarEncabezado(
            String texto
    ) {

        if (texto == null) {

            return "";
        }


        /*
         * Convertimos:
         *
         * "Cédula"
         *
         * en:
         *
         * "cedula"
         */

        String normalizado =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );


        /*
         * Eliminamos las marcas de los caracteres
         * acentuados.
         */

        normalizado =
                normalizado.replaceAll(
                        "\\p{M}",
                        ""
                );


        /*
         * Todo a minúsculas.
         */

        normalizado =
                normalizado.toLowerCase(
                        Locale.ROOT
                );


        /*
         * Quitamos espacios, guiones y guiones bajos.
         *
         * Esto permite aceptar:
         *
         * tipo contrato
         * tipo_contrato
         * tipo-contrato
         * TipoContrato
         */
        normalizado =
                normalizado.replaceAll(
                        "[\\s_\\-]+",
                        ""
                );


        return normalizado.trim();
    }


    // =====================================================
    // TIPO DE CONTRATO
    // =====================================================

    private String obtenerTipoContratoConfigurado(
            String tipoExcel,
            List<String> tiposConfigurados
    ) {

        if (tipoExcel == null
                || tipoExcel.isBlank()) {

            return "";
        }


        String tipoNormalizado =
                normalizarValorComparacion(
                        tipoExcel
                );


        /*
         * Primero buscamos coincidencia
         * con los valores existentes.
         */
        for (String tipoConfigurado :
                tiposConfigurados
        ) {

            if (tipoConfigurado == null
                    || tipoConfigurado.isBlank()) {

                continue;
            }


            String configuradoNormalizado =
                    normalizarValorComparacion(
                            tipoConfigurado
                    );


            if (configuradoNormalizado.equals(
                    tipoNormalizado
            )) {

                /*
                 * IMPORTANTE:
                 *
                 * Devolvemos el valor que ya estaba
                 * guardado en configuración.
                 *
                 * Ejemplo:
                 *
                 * Configuración:
                 * Término indefinido
                 *
                 * Excel:
                 * termino indefinido
                 *
                 * Candidato:
                 * Término indefinido
                 */
                return tipoConfigurado;
            }
        }


        /*
         * No existe.
         *
         * Agregamos el valor del Excel.
         */
        tiposConfigurados.add(
                tipoExcel.trim()
        );


        return tipoExcel.trim();
    }


    // =====================================================
    // NORMALIZAR VALOR PARA COMPARACIÓN
    // =====================================================

    private String normalizarValorComparacion(
            String texto
    ) {

        if (texto == null) {

            return "";
        }


        String normalizado =
                Normalizer.normalize(
                        texto,
                        Normalizer.Form.NFD
                );


        /*
         * Eliminar tildes.
         */
        normalizado =
                normalizado.replaceAll(
                        "\\p{M}",
                        ""
                );


        /*
         * Minúsculas.
         */
        normalizado =
                normalizado.toLowerCase(
                        Locale.ROOT
                );


        /*
         * Normalizar espacios.
         *
         * "Termino    indefinido"
         *
         * queda:
         *
         * "termino indefinido"
         */
        normalizado =
                normalizado.replaceAll(
                        "\\s+",
                        " "
                );


        return normalizado.trim();
    }


    // =====================================================
    // OBTENER TEXTO DE CELDA
    // =====================================================

    private String obtenerTexto(
            Cell celda
    ) {

        if (celda == null) {

            return "";
        }


        DataFormatter formatter =
                new DataFormatter();


        return formatter
                .formatCellValue(celda)
                .trim();
    }


    // =====================================================
    // OBTENER TEXTO DE COLUMNA
    // =====================================================

    private String obtenerTexto(
            Row fila,
            int indiceColumna
    ) {

        if (fila == null
                || indiceColumna < 0) {

            return "";
        }


        return obtenerTexto(
                fila.getCell(
                        indiceColumna
                )
        );
    }


    // =====================================================
    // FECHA
    // =====================================================

    private String obtenerFecha(
            Row fila,
            int indiceColumna
    ) {

        if (fila == null
                || indiceColumna < 0) {

            return "";
        }


        Cell celda =
                fila.getCell(
                        indiceColumna
                );


        if (celda == null) {

            return "";
        }


        // =====================================================
        // FECHA NUMÉRICA DE EXCEL
        // =====================================================

        if (celda.getCellType()
                == CellType.NUMERIC
                && DateUtil.isCellDateFormatted(
                celda
        )) {

            try {

                LocalDate fecha =
                        celda.getLocalDateTimeCellValue()
                                .toLocalDate();


                return fecha.format(
                        FORMATO_FECHA
                );

            } catch (Exception e) {

                return "";
            }
        }


        // =====================================================
        // FECHA COMO TEXTO
        // =====================================================

        String texto =
                obtenerTexto(celda);


        if (texto.isBlank()) {

            return "";
        }


        String[] formatos = {

                "dd/MM/yyyy",
                "dd-MM-yyyy",
                "yyyy-MM-dd",
                "MM/dd/yyyy",
                "M/d/yyyy",
                "d/M/yyyy"
        };


        for (String formato :
                formatos
        ) {

            try {

                DateTimeFormatter formatter =
                        DateTimeFormatter.ofPattern(
                                formato
                        );


                LocalDate fecha =
                        LocalDate.parse(
                                texto,
                                formatter
                        );


                return fecha.format(
                        FORMATO_FECHA
                );

            } catch (Exception ignored) {

                // Continuamos con el siguiente formato.
            }
        }


        return "";
    }


    // =====================================================
    // SALARIO
    // =====================================================

    private BigDecimal obtenerSalario(
            Row fila,
            int indiceColumna
    ) {

        if (fila == null
                || indiceColumna < 0) {

            return null;
        }


        Cell celda =
                fila.getCell(
                        indiceColumna
                );


        if (celda == null) {

            return null;
        }


        try {

            // =================================================
            // CELDA NUMÉRICA
            // =================================================

            if (celda.getCellType()
                    == CellType.NUMERIC) {

                return BigDecimal.valueOf(
                        celda.getNumericCellValue()
                );
            }


            // =================================================
            // CELDA COMO TEXTO
            // =================================================

            String texto =
                    obtenerTexto(celda);


            if (texto.isBlank()) {

                return null;
            }


            texto =
                    texto
                            .replace(
                                    "$",
                                    ""
                            )
                            .replace(
                                    " ",
                                    ""
                            )
                            .replace(
                                    ".",
                                    ""
                            )
                            .replace(
                                    ",",
                                    "."
                            );


            return new BigDecimal(
                    texto
            );


        } catch (Exception e) {

            return null;
        }
    }


    // =====================================================
    // CELULAR
    // =====================================================

    private String normalizarCelularExcel(
            String celular
    ) {

        if (celular == null
                || celular.isBlank()) {

            return "";
        }


        try {

            return ValidadorCandidato
                    .normalizarCelular(
                            celular
                    );

        } catch (IllegalArgumentException e) {

            return "";
        }
    }


    // =====================================================
    // CORREO
    // =====================================================

    private String normalizarCorreoExcel(
            String correo
    ) {

        if (correo == null
                || correo.isBlank()) {

            return "";
        }


        correo =
                correo
                        .trim()
                        .toLowerCase(
                                Locale.ROOT
                        );


        if (correo.matches(
                "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
        )) {

            return correo;
        }


        return "";
    }


    // =====================================================
    // FILA VACÍA
    // =====================================================

    private boolean filaVacia(
            Row fila
    ) {

        if (fila == null) {

            return true;
        }


        for (Cell celda :
                fila
        ) {

            if (!obtenerTexto(celda)
                    .isBlank()) {

                return false;
            }
        }


        return true;
    }
}