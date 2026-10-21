package com.cartaofertaexpress.service;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class EmailService {

    // =====================================================
    // CONFIGURACIÓN
    // =====================================================

    private static final String SERVIDOR_SMTP =
            "smtp-mail.outlook.com";

    private static final String PUERTO_SMTP =
            "587";

    // =====================================================
    // ENVIAR CORREO
    // =====================================================

    public void enviarCorreo(
            String usuario,
            String destinatario,
            String asunto,
            String html,
            File archivoAdjunto
    ) {

        if (usuario == null || usuario.isBlank()) {

            throw new IllegalArgumentException(
                    "No se indicó el correo remitente."
            );
        }

        if (destinatario == null || destinatario.isBlank()) {

            throw new IllegalArgumentException(
                    "No se indicó el destinatario."
            );
        }

        try {

            // =================================================
            // 1. OBTENER TOKEN
            // =================================================

            MicrosoftAuthService authService =
                    new MicrosoftAuthService();

            String accessToken =
                    authService.obtenerAccessToken();

            System.out.println(
                    "Autenticación OAuth exitosa."
            );

            // =================================================
            // 2. CONFIGURAR SMTP
            // =================================================

            Properties propiedades =
                    new Properties();

            propiedades.put(
                    "mail.smtp.host",
                    SERVIDOR_SMTP
            );

            propiedades.put(
                    "mail.smtp.port",
                    PUERTO_SMTP
            );

            propiedades.put(
                    "mail.smtp.auth",
                    "true"
            );

            propiedades.put(
                    "mail.smtp.starttls.enable",
                    "true"
            );

            propiedades.put(
                    "mail.smtp.auth.mechanisms",
                    "XOAUTH2"
            );

            propiedades.put(
                    "mail.smtp.auth.login.disable",
                    "true"
            );

            propiedades.put(
                    "mail.smtp.auth.plain.disable",
                    "true"
            );

            // =================================================
            // 3. SESIÓN
            // =================================================

            Session sesion =
                    Session.getInstance(
                            propiedades,
                            new jakarta.mail.Authenticator() {

                                @Override
                                protected PasswordAuthentication
                                getPasswordAuthentication() {

                                    return new PasswordAuthentication(
                                            usuario,
                                            accessToken
                                    );
                                }
                            }
                    );

            // =================================================
            // 4. MENSAJE
            // =================================================

            MimeMessage mensaje =
                    new MimeMessage(sesion);

            mensaje.setFrom(
                    new InternetAddress(usuario)
            );

            mensaje.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(
                            destinatario
                    )
            );

            mensaje.setSubject(
                    asunto,
                    "UTF-8"
            );

            // =================================================
            // 5. CUERPO HTML
            // =================================================

            MimeBodyPart cuerpo =
                    new MimeBodyPart();

            cuerpo.setContent(
                    html,
                    "text/html; charset=UTF-8"
            );

            Multipart multipart =
                    new MimeMultipart();

            multipart.addBodyPart(
                    cuerpo
            );

            // =================================================
            // 6. ADJUNTO
            // =================================================

            if (archivoAdjunto != null) {

                if (!archivoAdjunto.exists()) {

                    throw new IllegalArgumentException(
                            "El archivo adjunto no existe:\n"
                                    + archivoAdjunto.getAbsolutePath()
                    );
                }

                MimeBodyPart adjunto =
                        new MimeBodyPart();

                DataSource fuente =
                        new FileDataSource(
                                archivoAdjunto
                        );

                adjunto.setDataHandler(
                        new DataHandler(fuente)
                );

                adjunto.setFileName(
                        archivoAdjunto.getName()
                );

                multipart.addBodyPart(
                        adjunto
                );
            }

            mensaje.setContent(
                    multipart
            );

            // =================================================
            // 7. ENVIAR
            // =================================================

            System.out.println(
                    "Enviando correo a: "
                            + destinatario
            );

            Transport.send(
                    mensaje
            );

            System.out.println(
                    "Correo enviado correctamente a: "
                            + destinatario
            );

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo enviar el correo a "
                            + destinatario
                            + ": "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =====================================================
    // BUSCAR PDF DE LA CARTA
    // =====================================================

    public File obtenerArchivoPdf(
            String archivoCarta
    ) {

        if (archivoCarta == null
                || archivoCarta.isBlank()) {

            return null;
        }

        /*
         * Primero probamos exactamente la ruta guardada.
         */

        File archivoOriginal =
                new File(
                        archivoCarta
                );

        if (archivoOriginal.exists()
                && archivoOriginal.isFile()) {

            if (archivoOriginal.getName()
                    .toLowerCase()
                    .endsWith(".pdf")) {

                return archivoOriginal;
            }
        }

        /*
         * Si la base de datos todavía tiene registrada
         * la ruta/nombre PPT, buscamos el PDF equivalente.
         *
         * Ejemplo:
         *
         * carta_Juan.pptx
         *
         * se convierte en:
         *
         * carta_Juan.pdf
         */

        String nombre =
                archivoOriginal.getName();

        String nombreSinExtension =
                nombre;

        int punto =
                nombre.lastIndexOf('.');

        if (punto > 0) {

            nombreSinExtension =
                    nombre.substring(
                            0,
                            punto
                    );
        }

        Path carpeta =
                archivoOriginal.toPath()
                        .getParent();

        if (carpeta == null) {

            carpeta =
                    Paths.get(".");
        }

        Path rutaPdf =
                carpeta.resolve(
                        nombreSinExtension
                                + ".pdf"
                );

        File archivoPdf =
                rutaPdf.toFile();

        if (archivoPdf.exists()
                && archivoPdf.isFile()) {

            return archivoPdf;
        }

        return null;
    }
}