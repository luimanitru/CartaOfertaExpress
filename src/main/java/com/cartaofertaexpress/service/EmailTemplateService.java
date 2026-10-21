package com.cartaofertaexpress.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class EmailTemplateService {


    private final ConfiguracionService configuracionService;


    public EmailTemplateService() {

        configuracionService =
                new ConfiguracionService();
    }


    // ==================================================
    // OBTENER HTML
    // ==================================================

    public String obtenerPlantillaHtml() {


        String ruta =
                configuracionService
                        .obtenerPlantillaCorreo();


        if (ruta == null
                || ruta.isBlank()) {

            throw new RuntimeException(
                    "No existe una plantilla de correo configurada."
            );
        }


        try {


            return Files.readString(
                    Path.of(ruta)
            );


        } catch (IOException e) {


            throw new RuntimeException(
                    "No se pudo leer la plantilla de correo.",
                    e
            );
        }
    }
}