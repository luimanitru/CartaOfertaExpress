package com.cartaofertaexpress.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlantillaService {

    private final File carpetaPlantillas;

    public PlantillaService() {

        carpetaPlantillas =
                new File(
                        System.getProperty("user.dir"),
                        "plantillas"
                );

        crearCarpeta();
    }

    // =====================================================
    // CREAR CARPETA
    // =====================================================

    private void crearCarpeta() {

        if (!carpetaPlantillas.exists()) {

            if (!carpetaPlantillas.mkdirs()) {

                throw new RuntimeException(
                        "No se pudo crear la carpeta de plantillas."
                );
            }
        }
    }

    // =====================================================
    // OBTENER CARPETA
    // =====================================================

    public File getCarpetaPlantillas() {

        return carpetaPlantillas;
    }

    // =====================================================
    // LISTAR PLANTILLAS
    // =====================================================

    public List<File> listarPlantillas() {

        crearCarpeta();

        File[] archivos =
                carpetaPlantillas.listFiles(
                        archivo ->
                                archivo.isFile()
                                        && archivo.getName()
                                        .toLowerCase()
                                        .endsWith(".pptx")
                );

        if (archivos == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(
                Arrays.asList(archivos)
        );
    }

    // =====================================================
    // AGREGAR PLANTILLA
    // =====================================================

    public File agregarPlantilla(
            File archivoOriginal
    ) {

        if (archivoOriginal == null
                || !archivoOriginal.exists()) {

            throw new RuntimeException(
                    "El archivo seleccionado no existe."
            );
        }

        if (!archivoOriginal.getName()
                .toLowerCase()
                .endsWith(".pptx")) {

            throw new RuntimeException(
                    "La plantilla debe ser un archivo .pptx."
            );
        }

        String nombre =
                archivoOriginal.getName();

        File destino =
                new File(
                        carpetaPlantillas,
                        nombre
                );

        /*
         * Si ya existe una plantilla con el mismo
         * nombre, creamos una copia con número.
         */
        int contador = 1;

        String nombreBase =
                nombre.substring(
                        0,
                        nombre.length() - 5
                );

        while (destino.exists()) {

            destino =
                    new File(
                            carpetaPlantillas,
                            nombreBase
                                    + " ("
                                    + contador
                                    + ").pptx"
                    );

            contador++;
        }

        try {

            Files.copy(
                    archivoOriginal.toPath(),
                    destino.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return destino;

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo copiar la plantilla: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =====================================================
    // ELIMINAR PLANTILLA
    // =====================================================

    public void eliminarPlantilla(
            File plantilla
    ) {

        if (plantilla == null
                || !plantilla.exists()) {

            return;
        }

        try {

            Files.delete(
                    plantilla.toPath()
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo eliminar la plantilla: "
                            + e.getMessage(),
                    e
            );
        }
    }
}