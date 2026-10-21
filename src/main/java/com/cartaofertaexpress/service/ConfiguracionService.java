package com.cartaofertaexpress.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ConfiguracionService {

    private static final String CARPETA_DATOS = "datos";
    private static final String CARPETA_PLANTILLA_HTML =
            "plantilla_html";

    private static final String ARCHIVO_PLANTILLA_HTML =
            "correo.html";

    private static final String ARCHIVO_CONFIGURACION =
            "configuracion.properties";

    // ==================================================
    // CLAVES DE CONFIGURACIÓN EXISTENTES
    // ==================================================

    private static final String CLAVE_PLANTILLA =
            "plantillaSeleccionada";

    private static final String CLAVE_TIPOS_CONTRATO =
            "tiposContrato";

    private static final String CLAVE_TAMANO_FUENTE =
            "tamanoFuente";

    private static final String CLAVE_TAMANO_FUENTE_TABLA =
            "tamanoFuenteTabla";

    private static final String CLAVE_FILAS_ALTERNADAS =
            "filasAlternadas";

    // ==================================================
    // CLAVES DE CONFIGURACIÓN DE CORREO
    // ==================================================

    private static final String CLAVE_SERVIDOR_CORREO =
            "servidorCorreo";

    private static final String CLAVE_PUERTO_CORREO =
            "puertoCorreo";

    private static final String CLAVE_CORREO_REMITENTE =
            "correoRemitente";

    private static final String CLAVE_NOMBRE_REMITENTE =
            "nombreRemitente";

    private static final String CLAVE_PLANTILLA_CORREO =
            "plantillaCorreo";

    // ==================================================
    // VALORES POR DEFECTO
    // ==================================================

    private static final int TAMANO_FUENTE_DEFECTO =
            14;

    private static final int TAMANO_FUENTE_TABLA_DEFECTO =
            14;

    private static final List<String> TIPOS_CONTRATO_DEFECTO =
            List.of(
                    "Término indefinido",
                    "Término fijo",
                    "Obra o labor",
                    "Prestación de servicios",
                    "Aprendizaje"
            );

    private static final boolean FILAS_ALTERNADAS_DEFECTO =
            false;

    // ==================================================
    // ARCHIVO
    // ==================================================

    private final File archivoConfiguracion;

    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    public ConfiguracionService() {

        File carpetaDatos =
                new File(
                        CARPETA_DATOS
                );

        if (!carpetaDatos.exists()) {

            carpetaDatos.mkdirs();
        }

        archivoConfiguracion =
                new File(
                        carpetaDatos,
                        ARCHIVO_CONFIGURACION
                );
    }

    // ==================================================
    // PLANTILLA DE CARTA
    // ==================================================

    public void guardarPlantilla(
            File plantilla
    ) {

        Properties propiedades =
                cargarPropiedades();

        if (plantilla != null) {

            propiedades.setProperty(
                    CLAVE_PLANTILLA,
                    plantilla.getAbsolutePath()
            );

        } else {

            propiedades.remove(
                    CLAVE_PLANTILLA
            );
        }

        guardarPropiedades(
                propiedades
        );
    }

    public File obtenerPlantilla() {

        Properties propiedades =
                cargarPropiedades();

        String ruta =
                propiedades.getProperty(
                        CLAVE_PLANTILLA
                );

        if (ruta == null
                || ruta.isBlank()) {

            return null;
        }

        File plantilla =
                new File(
                        ruta
                );

        if (!plantilla.exists()
                || !plantilla.isFile()) {

            return null;
        }

        return plantilla;
    }

    // ==================================================
    // TIPOS DE CONTRATO
    // ==================================================

    public List<String> obtenerTiposContrato() {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                propiedades.getProperty(
                        CLAVE_TIPOS_CONTRATO
                );

        if (valor == null
                || valor.isBlank()) {

            return new ArrayList<>(
                    TIPOS_CONTRATO_DEFECTO
            );
        }

        return new ArrayList<>(
                Arrays.asList(
                        valor.split(
                                "\\|"
                        )
                )
        );
    }

    public void guardarTiposContrato(
            List<String> tipos
    ) {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                String.join(
                        "|",
                        tipos
                );

        propiedades.setProperty(
                CLAVE_TIPOS_CONTRATO,
                valor
        );

        guardarPropiedades(
                propiedades
        );
    }

    // ==================================================
    // TAMAÑO GENERAL DE FUENTE
    // ==================================================

    public int obtenerTamanoFuente() {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                propiedades.getProperty(
                        CLAVE_TAMANO_FUENTE
                );

        try {

            int tamano =
                    Integer.parseInt(
                            valor
                    );

            if (tamano < 10 || tamano > 24) {

                return TAMANO_FUENTE_DEFECTO;
            }

            return tamano;

        } catch (Exception e) {

            return TAMANO_FUENTE_DEFECTO;
        }
    }

    public void guardarTamanoFuente(
            int tamano
    ) {

        if (tamano < 10 || tamano > 24) {

            tamano =
                    TAMANO_FUENTE_DEFECTO;
        }

        Properties propiedades =
                cargarPropiedades();

        propiedades.setProperty(
                CLAVE_TAMANO_FUENTE,
                String.valueOf(
                        tamano
                )
        );

        guardarPropiedades(
                propiedades
        );
    }

    // ==================================================
    // TAMAÑO DE FUENTE DE TABLA
    // ==================================================

    public int obtenerTamanoFuenteTabla() {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                propiedades.getProperty(
                        CLAVE_TAMANO_FUENTE_TABLA
                );

        try {

            int tamano =
                    Integer.parseInt(
                            valor
                    );

            if (tamano < 10 || tamano > 24) {

                return TAMANO_FUENTE_TABLA_DEFECTO;
            }

            return tamano;

        } catch (Exception e) {

            return TAMANO_FUENTE_TABLA_DEFECTO;
        }
    }

    public void guardarTamanoFuenteTabla(
            int tamano
    ) {

        if (tamano < 10 || tamano > 24) {

            tamano =
                    TAMANO_FUENTE_TABLA_DEFECTO;
        }

        Properties propiedades =
                cargarPropiedades();

        propiedades.setProperty(
                CLAVE_TAMANO_FUENTE_TABLA,
                String.valueOf(
                        tamano
                )
        );

        guardarPropiedades(
                propiedades
        );
    }

    // ==================================================
    // FILAS ALTERNADAS DE LA TABLA
    // ==================================================

    public boolean obtenerFilasAlternadas() {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                propiedades.getProperty(
                        CLAVE_FILAS_ALTERNADAS
                );

        if (valor == null
                || valor.isBlank()) {

            return FILAS_ALTERNADAS_DEFECTO;
        }

        return Boolean.parseBoolean(
                valor
        );
    }

    public void guardarFilasAlternadas(
            boolean activadas
    ) {

        Properties propiedades =
                cargarPropiedades();

        propiedades.setProperty(
                CLAVE_FILAS_ALTERNADAS,
                String.valueOf(
                        activadas
                )
        );

        guardarPropiedades(
                propiedades
        );
    }

    // ==================================================
    // CONFIGURACIÓN DE CORREO
    // ==================================================

    // --------------------------------------------------
    // SERVIDOR SMTP
    // --------------------------------------------------

    public String obtenerServidorCorreo() {

        Properties propiedades =
                cargarPropiedades();

        return propiedades.getProperty(
                CLAVE_SERVIDOR_CORREO,
                ""
        );
    }

    public void guardarServidorCorreo(
            String servidor
    ) {

        Properties propiedades =
                cargarPropiedades();

        if (servidor == null
                || servidor.isBlank()) {

            propiedades.remove(
                    CLAVE_SERVIDOR_CORREO
            );

        } else {

            propiedades.setProperty(
                    CLAVE_SERVIDOR_CORREO,
                    servidor.trim()
            );
        }

        guardarPropiedades(
                propiedades
        );
    }

    // --------------------------------------------------
    // PUERTO SMTP
    // --------------------------------------------------

    public int obtenerPuertoCorreo() {

        Properties propiedades =
                cargarPropiedades();

        String valor =
                propiedades.getProperty(
                        CLAVE_PUERTO_CORREO
                );

        try {

            return Integer.parseInt(
                    valor
            );

        } catch (Exception e) {

            return 587;
        }
    }

    public void guardarPuertoCorreo(
            int puerto
    ) {

        Properties propiedades =
                cargarPropiedades();

        propiedades.setProperty(
                CLAVE_PUERTO_CORREO,
                String.valueOf(
                        puerto
                )
        );

        guardarPropiedades(
                propiedades
        );
    }

    // --------------------------------------------------
    // CORREO REMITENTE
    // --------------------------------------------------

    public String obtenerCorreoRemitente() {

        Properties propiedades =
                cargarPropiedades();

        return propiedades.getProperty(
                CLAVE_CORREO_REMITENTE,
                ""
        );
    }

    public void guardarCorreoRemitente(
            String correo
    ) {

        Properties propiedades =
                cargarPropiedades();

        if (correo == null
                || correo.isBlank()) {

            propiedades.remove(
                    CLAVE_CORREO_REMITENTE
            );

        } else {

            propiedades.setProperty(
                    CLAVE_CORREO_REMITENTE,
                    correo.trim()
            );
        }

        guardarPropiedades(
                propiedades
        );
    }

    // --------------------------------------------------
    // NOMBRE DEL REMITENTE
    // --------------------------------------------------

    public String obtenerNombreRemitente() {

        Properties propiedades =
                cargarPropiedades();

        return propiedades.getProperty(
                CLAVE_NOMBRE_REMITENTE,
                ""
        );
    }

    public void guardarNombreRemitente(
            String nombre
    ) {

        Properties propiedades =
                cargarPropiedades();

        if (nombre == null
                || nombre.isBlank()) {

            propiedades.remove(
                    CLAVE_NOMBRE_REMITENTE
            );

        } else {

            propiedades.setProperty(
                    CLAVE_NOMBRE_REMITENTE,
                    nombre.trim()
            );
        }

        guardarPropiedades(
                propiedades
        );
    }

    // --------------------------------------------------
    // PLANTILLA HTML DE CORREO
    // --------------------------------------------------

    public String obtenerPlantillaCorreo() {

        File carpeta =
                new File(
                        CARPETA_PLANTILLA_HTML
                );

        File plantilla =
                new File(
                        carpeta,
                        ARCHIVO_PLANTILLA_HTML
                );

        if (!plantilla.exists()
                || !plantilla.isFile()) {

            return "";
        }

        return plantilla.getAbsolutePath();
    }

    public void guardarPlantillaCorreo(
            File plantilla
    ) {

        if (plantilla == null
                || !plantilla.exists()
                || !plantilla.isFile()) {

            throw new IllegalArgumentException(
                    "La plantilla HTML seleccionada no es válida."
            );
        }

        File carpeta =
                new File(
                        CARPETA_PLANTILLA_HTML
                );

        if (!carpeta.exists()) {

            boolean creada =
                    carpeta.mkdirs();

            if (!creada
                    && !carpeta.exists()) {

                throw new RuntimeException(
                        "No se pudo crear la carpeta de plantillas HTML."
                );
            }
        }

        File destino =
                new File(
                        carpeta,
                        ARCHIVO_PLANTILLA_HTML
                );

        try {

            java.nio.file.Files.copy(
                    plantilla.toPath(),
                    destino.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo copiar la plantilla HTML.",
                    e
            );
        }

        /*
         * Guardamos en configuración la ruta de la copia
         * que realmente utiliza el programa.
         */
        Properties propiedades =
                cargarPropiedades();

        propiedades.setProperty(
                CLAVE_PLANTILLA_CORREO,
                destino.getAbsolutePath()
        );

        guardarPropiedades(
                propiedades
        );
    }

    // ==================================================
    // CARGAR PROPIEDADES
    // ==================================================

    private Properties cargarPropiedades() {

        Properties propiedades =
                new Properties();

        if (!archivoConfiguracion.exists()) {

            return propiedades;
        }

        try (
                FileInputStream entrada =
                        new FileInputStream(
                                archivoConfiguracion
                        )
        ) {

            propiedades.load(
                    entrada
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo leer la configuración.",
                    e
            );
        }

        return propiedades;
    }

    // ==================================================
    // GUARDAR PROPIEDADES
    // ==================================================

    private void guardarPropiedades(
            Properties propiedades
    ) {

        try (
                FileOutputStream salida =
                        new FileOutputStream(
                                archivoConfiguracion
                        )
        ) {

            propiedades.store(
                    salida,
                    "Configuracion Carta Oferta EXPRESS"
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo guardar la configuración.",
                    e
            );
        }
    }
}