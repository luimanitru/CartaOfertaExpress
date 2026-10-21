package com.cartaofertaexpress.service;

import com.cartaofertaexpress.model.Candidato;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ExcelExportService {

    // =====================================================
// EXPORTAR CANDIDATOS
// =====================================================

    public void exportar(
            List<Candidato> candidatos,
            File archivo
    ) {

        if (candidatos == null) {

            throw new RuntimeException(
                    "No se recibió la lista de candidatos."
            );
        }

        if (archivo == null) {

            throw new RuntimeException(
                    "No se indicó el archivo de destino."
            );
        }

        File carpeta =
                archivo.getParentFile();

        if (carpeta != null
                && !carpeta.exists()
                && !carpeta.mkdirs()) {

            throw new RuntimeException(
                    "No se pudo crear la carpeta de destino."
            );
        }

        try (
                Workbook workbook =
                        new XSSFWorkbook();

                FileOutputStream salida =
                        new FileOutputStream(
                                archivo
                        )
        ) {

            Sheet hoja =
                    workbook.createSheet(
                            "Candidatos"
                    );

            // =================================================
            // ESTILO DEL ENCABEZADO
            // =================================================

            CellStyle estiloEncabezado =
                    workbook.createCellStyle();

            Font fuenteEncabezado =
                    workbook.createFont();

            fuenteEncabezado.setBold(
                    true
            );

            estiloEncabezado.setFont(
                    fuenteEncabezado
            );

            estiloEncabezado.setAlignment(
                    HorizontalAlignment.CENTER
            );

            estiloEncabezado.setVerticalAlignment(
                    VerticalAlignment.CENTER
            );

            estiloEncabezado.setFillForegroundColor(
                    IndexedColors.GREY_25_PERCENT.getIndex()
            );

            estiloEncabezado.setFillPattern(
                    FillPatternType.SOLID_FOREGROUND
            );

            // =================================================
            // ESTILO DE SALARIO
            // =================================================

            CellStyle estiloSalario =
                    workbook.createCellStyle();

            estiloSalario.setDataFormat(
                    workbook.createDataFormat()
                            .getFormat(
                                    "#,##0"
                            )
            );

            // =================================================
            // ENCABEZADOS
            // =================================================

            String[] encabezados = {

                    "Nombre",
                    "Cédula",
                    "Cargo",
                    "Salario",
                    "Rol",
                    "Tipo de contrato",
                    "Fecha de ingreso",
                    "Ciudad",
                    "Tasa",
                    "Celular",
                    "Correo"
            };

            Row filaEncabezados =
                    hoja.createRow(0);

            for (
                    int i = 0;
                    i < encabezados.length;
                    i++
            ) {

                Cell celda =
                        filaEncabezados.createCell(i);

                celda.setCellValue(
                        encabezados[i]
                );

                celda.setCellStyle(
                        estiloEncabezado
                );
            }

            // =================================================
            // DATOS
            // =================================================

            int filaActual = 1;

            for (Candidato candidato :
                    candidatos) {

                Row fila =
                        hoja.createRow(
                                filaActual++
                        );

                // Nombre
                fila.createCell(0)
                        .setCellValue(
                                texto(
                                        candidato.getNombre()
                                )
                        );

                // Cédula
                fila.createCell(1)
                        .setCellValue(
                                texto(
                                        candidato.getCedula()
                                )
                        );

                // Cargo
                fila.createCell(2)
                        .setCellValue(
                                texto(
                                        candidato.getCargo()
                                )
                        );

                // Salario
                Cell salario =
                        fila.createCell(3);

                if (candidato.getSalario() != null) {

                    salario.setCellValue(
                            candidato
                                    .getSalario()
                                    .doubleValue()
                    );

                    salario.setCellStyle(
                            estiloSalario
                    );
                }

                // Rol
                fila.createCell(4)
                        .setCellValue(
                                texto(
                                        candidato.getRol()
                                )
                        );

                // Tipo de contrato
                fila.createCell(5)
                        .setCellValue(
                                texto(
                                        candidato
                                                .getTipoContrato()
                                )
                        );

                // Fecha de ingreso
                fila.createCell(6)
                        .setCellValue(
                                texto(
                                        candidato
                                                .getFechaIngreso()
                                )
                        );

                // Ciudad
                fila.createCell(7)
                        .setCellValue(
                                texto(
                                        candidato.getCiudad()
                                )
                        );

                // Tasa
                fila.createCell(8)
                        .setCellValue(
                                texto(
                                        candidato.getTasa()
                                )
                        );

                // Celular
                fila.createCell(9)
                        .setCellValue(
                                texto(
                                        candidato.getCelular()
                                )
                        );

                // Correo
                fila.createCell(10)
                        .setCellValue(
                                texto(
                                        candidato.getCorreo()
                                )
                        );
            }

            // =================================================
            // FILTRO
            // =================================================

            if (!candidatos.isEmpty()) {

                hoja.setAutoFilter(
                        new org.apache.poi.ss.util.CellRangeAddress(
                                0,
                                candidatos.size(),
                                0,
                                encabezados.length - 1
                        )
                );
            }

            // =================================================
            // CONGELAR ENCABEZADO
            // =================================================

            hoja.createFreezePane(
                    0,
                    1
            );

            // =================================================
            // ANCHO DE COLUMNAS
            // =================================================

            for (
                    int i = 0;
                    i < encabezados.length;
                    i++
            ) {

                hoja.autoSizeColumn(i);

                int ancho =
                        hoja.getColumnWidth(i);

                int maximo =
                        10000;

                if (ancho > maximo) {

                    hoja.setColumnWidth(
                            i,
                            maximo
                    );
                }
            }

            // =================================================
            // GUARDAR
            // =================================================

            workbook.write(
                    salida
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo exportar la base de datos a Excel: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =====================================================
    // TEXTO SEGURO
    // =====================================================

    private String texto(
            String valor
    ) {

        if (valor == null) {
            return "";
        }

        return valor;
    }
}