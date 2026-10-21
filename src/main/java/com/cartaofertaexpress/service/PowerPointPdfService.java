package com.cartaofertaexpress.service;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

import java.io.File;

public class PowerPointPdfService {

    private static final int FORMATO_PDF = 32;

    public void convertir(
            File archivoPptx,
            File archivoPdf
    ) {

        if (archivoPptx == null
                || !archivoPptx.exists()) {

            throw new IllegalArgumentException(
                    "El archivo PowerPoint no existe."
            );
        }

        if (archivoPdf == null) {

            throw new IllegalArgumentException(
                    "No se indicó el archivo PDF de destino."
            );
        }

        ActiveXComponent powerpoint = null;
        Dispatch presentacion = null;

        try {

            // ==================================================
            // ABRIR POWERPOINT
            // ==================================================

            powerpoint =
                    new ActiveXComponent(
                            "PowerPoint.Application"
                    );


            // ==================================================
            // OBTENER PRESENTACIONES
            // ==================================================

            Dispatch presentaciones =
                    powerpoint
                            .getProperty(
                                    "Presentations"
                            )
                            .toDispatch();

            // ==================================================
            // ABRIR PPTX
            // ==================================================

            presentacion =
                    Dispatch.call(
                            presentaciones,
                            "Open",
                            archivoPptx.getAbsolutePath(),
                            false,
                            true,
                            false
                    ).toDispatch();

            // ==================================================
            // EXPORTAR A PDF
            // ==================================================

            Dispatch.call(
                    presentacion,
                    "SaveAs",
                    archivoPdf.getAbsolutePath(),
                    FORMATO_PDF
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo convertir el PowerPoint a PDF: "
                            + e.getMessage(),
                    e
            );

        } finally {

            // ==================================================
            // CERRAR PRESENTACIÓN
            // ==================================================

            if (presentacion != null) {

                try {

                    Dispatch.call(
                            presentacion,
                            "Close"
                    );

                } catch (Exception ignored) {
                }
            }

            // ==================================================
            // CERRAR POWERPOINT
            // ==================================================

            if (powerpoint != null) {

                try {

                    powerpoint.invoke(
                            "Quit"
                    );

                } catch (Exception ignored) {
                }
            }
        }
    }
}