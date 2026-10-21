package com.cartaofertaexpress.ui;

import com.cartaofertaexpress.model.Candidato;
import com.cartaofertaexpress.repository.CandidatoRepository;
import com.cartaofertaexpress.service.ConfiguracionService;

import com.cartaofertaexpress.service.EmailService;
import java.time.LocalDateTime;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class CorreosView {

    // =====================================================
    // CONSTANTES
    // =====================================================

    private static final String CARPETA_PLANTILLA_HTML =
            "plantilla_html";

    private static final String NOMBRE_PLANTILLA =
            "correo.html";

    private final EmailService emailService;

    // =====================================================
    // COMPONENTES
    // =====================================================

    private final TableView<Candidato> tabla;

    private final CandidatoRepository repository;

    private final ConfiguracionService configuracionService;

    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CorreosView() {

        repository =
                new CandidatoRepository();

        configuracionService =
                new ConfiguracionService();

        emailService =
                new EmailService();

        tabla =
                new TableView<>();
    }

    // =====================================================
    // MOSTRAR
    // =====================================================

    public void mostrar(
            Stage padre,
            MainView mainView
    ) {

        List<Candidato> candidatos =
                mainView.obtenerCandidatosSeleccionados();

        tabla.setItems(
                FXCollections.observableArrayList(
                        candidatos
                )
        );

        // =================================================
        // VENTANA
        // =================================================

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Administrar correos - Carta Oferta EXPRESS"
        );

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.initOwner(
                padre
        );

        // =================================================
        // ENCABEZADO
        // =================================================

        Label titulo =
                new Label(
                        "Administrar correos"
                );

        titulo.setStyle(
                "-fx-font-size: 21px;" +
                        "-fx-font-weight: bold;"
        );

        Label informacion =
                new Label(
                        "Prepara los candidatos seleccionados " +
                                "para el envío de sus cartas de oferta."
                );

        informacion.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #666666;"
        );

        // =================================================
        // PLANTILLA
        // =================================================

        Label etiquetaPlantilla =
                new Label();

        etiquetaPlantilla.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
        );

        actualizarEstadoPlantilla(
                etiquetaPlantilla
        );

        Button botonCargarPlantilla =
                new Button(
                        "Cargar / reemplazar HTML"
                );

        botonCargarPlantilla.setOnAction(
                evento ->
                        cargarPlantillaHtml(
                                etiquetaPlantilla,
                                ventana
                        )
        );

        HBox filaPlantilla =
                new HBox(
                        10,
                        etiquetaPlantilla,
                        botonCargarPlantilla
                );

        filaPlantilla.setAlignment(
                Pos.CENTER_LEFT
        );

        filaPlantilla.setPadding(
                new Insets(8, 10, 8, 10)
        );

        filaPlantilla.setStyle(
                "-fx-background-color: #f5f7fa;" +
                        "-fx-border-color: #d9d9d9;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        VBox encabezado =
                new VBox(
                        6,
                        titulo,
                        informacion,
                        filaPlantilla
                );

        encabezado.setPadding(
                new Insets(
                        12,
                        15,
                        8,
                        15
                )
        );

        // =================================================
        // TABLA
        // =================================================

        configurarTablaPrincipal();

        tabla.setPrefHeight(
                330
        );

        tabla.setMinHeight(
                250
        );

        tabla.setMaxHeight(
                Double.MAX_VALUE
        );

        // =================================================
        // BOTONES
        // =================================================

        Button botonContinuar =
                new Button(
                        "Continuar con el envío"
                );

        botonContinuar.setDefaultButton(
                true
        );

        Button botonCerrar =
                new Button(
                        "Cerrar"
                );

        botonContinuar.setOnAction(
                evento ->
                        validarParaEnvio(
                                ventana
                        )
        );

        botonCerrar.setOnAction(
                evento ->
                        ventana.close()
        );

        HBox botones =
                new HBox(
                        10,
                        botonCerrar,
                        botonContinuar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        botones.setPadding(
                new Insets(
                        8,
                        15,
                        12,
                        15
                )
        );

        // =================================================
        // CONTENEDOR
        // =================================================

        BorderPane principal =
                new BorderPane();

        principal.setTop(
                encabezado
        );

        principal.setCenter(
                tabla
        );

        principal.setBottom(
                botones
        );

        BorderPane.setMargin(
                tabla,
                new Insets(
                        0,
                        15,
                        5,
                        15
                )
        );

        // =================================================
        // ESCENA
        // =================================================

        Scene escena =
                new Scene(
                        principal,
                        900,
                        540
                );

        ventana.setScene(
                escena
        );

        /*
         * Evitamos que la ventana nazca demasiado alta
         * y termine fuera de la pantalla.
         */
        ventana.setMinWidth(
                750
        );

        ventana.setMinHeight(
                450
        );

        ventana.showAndWait();
    }

    // =====================================================
    // CONFIGURAR TABLA PRINCIPAL
    // =====================================================

    private void configurarTablaPrincipal() {

        tabla.getColumns().clear();

        // =================================================
        // NOMBRE
        // =================================================

        TableColumn<Candidato, String> columnaNombre =
                new TableColumn<>(
                        "Nombre"
                );

        columnaNombre.setCellValueFactory(
                celda ->
                        new SimpleStringProperty(
                                obtenerTexto(
                                        celda.getValue().getNombre()
                                )
                        )
        );

        columnaNombre.setPrefWidth(
                300
        );

        // =================================================
        // CORREO
        // =================================================

        TableColumn<Candidato, String> columnaCorreo =
                new TableColumn<>(
                        "Correo"
                );

        columnaCorreo.setCellValueFactory(
                celda ->
                        new SimpleStringProperty(
                                obtenerTexto(
                                        celda.getValue().getCorreo()
                                )
                        )
        );

        columnaCorreo.setPrefWidth(
                270
        );

        // =================================================
        // CARTA
        // =================================================

        TableColumn<Candidato, String> columnaCarta =
                new TableColumn<>(
                        "Carta"
                );

        columnaCarta.setCellValueFactory(
                celda -> {

                    boolean tieneCarta =
                            celda.getValue()
                                    .isCartaGenerada();

                    return new SimpleStringProperty(
                            tieneCarta
                                    ? "✓ Generada"
                                    : "✗ No generada"
                    );
                }
        );

        columnaCarta.setCellFactory(
                columna ->
                        new TableCell<>() {

                            @Override
                            protected void updateItem(
                                    String item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                if (empty
                                        || item == null) {

                                    setText(
                                            null
                                    );

                                    return;
                                }

                                setText(
                                        item
                                );

                                setAlignment(
                                        Pos.CENTER
                                );
                            }
                        }
        );

        columnaCarta.setPrefWidth(
                150
        );

        // =================================================
        // FECHA DE ENVÍO
        // =================================================

        TableColumn<Candidato, String> columnaFecha =
                new TableColumn<>(
                        "Fecha de envío"
                );

        columnaFecha.setCellValueFactory(
                celda -> {

                    String fecha =
                            celda.getValue()
                                    .getFechaCorreo();

                    if (fecha == null
                            || fecha.isBlank()) {

                        fecha = "—";
                    }

                    return new SimpleStringProperty(
                            fecha
                    );
                }
        );

        columnaFecha.setPrefWidth(
                180
        );

        tabla.getColumns().addAll(
                columnaNombre,
                columnaCorreo,
                columnaCarta,
                columnaFecha
        );

        tabla.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tabla.setPlaceholder(
                new Label(
                        "No hay candidatos seleccionados."
                )
        );
    }

    // =====================================================
    // VALIDAR CANDIDATOS
    // =====================================================

    private void validarParaEnvio(
            Stage ventana
    ) {

        List<Candidato> candidatos =
                new ArrayList<>(
                        tabla.getItems()
                );

        if (candidatos.isEmpty()) {

            mostrarMensaje(
                    "No hay candidatos",
                    "Debes seleccionar al menos un candidato."
            );

            return;
        }

        List<Candidato> candidatosAptos =
                new ArrayList<>();

        List<String> errores =
                new ArrayList<>();

        for (Candidato candidato : candidatos) {

            boolean tieneCarta =
                    candidato.isCartaGenerada();

            boolean tieneCorreo =
                    candidato.getCorreo() != null
                            && !candidato.getCorreo().isBlank();

            if (tieneCarta && tieneCorreo) {

                candidatosAptos.add(
                        candidato
                );

            } else {

                String nombre =
                        obtenerTexto(
                                candidato.getNombre()
                        );

                if (!tieneCarta && !tieneCorreo) {

                    errores.add(
                            nombre
                                    + " — no tiene carta generada "
                                    + "ni dirección de correo."
                    );

                } else if (!tieneCarta) {

                    errores.add(
                            nombre
                                    + " — no tiene carta generada."
                    );

                } else {

                    errores.add(
                            nombre
                                    + " — no tiene dirección de correo."
                    );
                }
            }
        }

        // =================================================
        // NINGUNO APTO
        // =================================================

        if (candidatosAptos.isEmpty()) {

            mostrarListaErrores(
                    ventana,
                    errores
            );

            return;
        }

        // =================================================
        // TODOS APTOS
        // =================================================

        if (errores.isEmpty()) {

            abrirPreparacionEnvio(
                    ventana,
                    candidatosAptos
            );

            return;
        }

        // =================================================
        // HAY APTOS Y NO APTOS
        // =================================================

        mostrarAdvertenciaCandidatosNoAptos(
                ventana,
                candidatosAptos,
                errores
        );
    }

    // =====================================================
    // ADVERTENCIA
    // =====================================================

    private void mostrarAdvertenciaCandidatosNoAptos(
            Stage ventanaAnterior,
            List<Candidato> candidatosAptos,
            List<String> errores
    ) {

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Revisar candidatos"
        );

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.initOwner(
                ventanaAnterior
        );

        // =================================================
        // TÍTULO
        // =================================================

        Label titulo =
                new Label(
                        "Algunos candidatos no pueden continuar"
                );

        titulo.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        // =================================================
        // RESUMEN
        // =================================================

        Label resumen =
                new Label(
                        "Aptos: "
                                + candidatosAptos.size()
                                + "    |    No aptos: "
                                + errores.size()
                );

        resumen.setStyle(
                "-fx-font-size: 14px;" +
                        "-fx-font-weight: bold;"
        );

        // =================================================
        // EXPLICACIÓN
        // =================================================

        Label pregunta =
                new Label(
                        "Estos candidatos serán excluidos del envío:"
                );

        pregunta.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
        );

        // =================================================
        // LISTA
        // =================================================

        VBox lista =
                new VBox(
                        7
                );

        lista.setPadding(
                new Insets(8)
        );

        for (String error : errores) {

            Label item =
                    new Label(
                            "• " + error
                    );

            item.setWrapText(
                    true
            );

            item.setStyle(
                    "-fx-font-size: 13px;"
            );

            lista.getChildren().add(
                    item
            );
        }

        ScrollPane scroll =
                new ScrollPane(
                        lista
                );

        scroll.setFitToWidth(
                true
        );

        scroll.setPrefViewportHeight(
                240
        );

        // =================================================
        // CONFIRMACIÓN
        // =================================================

        Label confirmacion =
                new Label(
                        "¿Deseas continuar únicamente con los "
                                + candidatosAptos.size()
                                + " candidatos aptos?"
                );

        confirmacion.setWrapText(
                true
        );

        confirmacion.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-font-weight: bold;"
        );

        // =================================================
        // BOTONES
        // =================================================

        Button botonCancelar =
                new Button(
                        "Cancelar"
                );

        Button botonContinuar =
                new Button(
                        "Continuar con los aptos"
                );

        botonContinuar.setDefaultButton(
                true
        );

        botonContinuar.setOnAction(
                evento -> {

                    ventana.close();

                    abrirPreparacionEnvio(
                            ventanaAnterior,
                            candidatosAptos
                    );
                }
        );

        botonCancelar.setOnAction(
                evento ->
                        ventana.close()
        );

        HBox botones =
                new HBox(
                        10,
                        botonCancelar,
                        botonContinuar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =================================================
        // CONTENIDO
        // =================================================

        VBox contenido =
                new VBox(
                        12,
                        titulo,
                        resumen,
                        pregunta,
                        scroll,
                        confirmacion,
                        botones
                );

        contenido.setPadding(
                new Insets(18)
        );

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );

        Scene escena =
                new Scene(
                        contenido,
                        700,
                        560
                );

        ventana.setScene(
                escena
        );

        ventana.setMinWidth(
                600
        );

        ventana.setMinHeight(
                450
        );

        ventana.showAndWait();
    }

    // =====================================================
    // NINGÚN CANDIDATO APTO
    // =====================================================

    private void mostrarListaErrores(
            Stage ventanaPadre,
            List<String> errores
    ) {

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "No se puede continuar"
        );

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.initOwner(
                ventanaPadre
        );

        Label titulo =
                new Label(
                        "Ningún candidato puede continuar"
                );

        titulo.setStyle(
                "-fx-font-size: 18px;" +
                        "-fx-font-weight: bold;"
        );

        Label informacion =
                new Label(
                        "Todos los candidatos seleccionados "
                                + "presentan algún requisito faltante:"
                );

        VBox lista =
                new VBox(
                        7
                );

        for (String error : errores) {

            Label item =
                    new Label(
                            "• " + error
                    );

            item.setWrapText(
                    true
            );

            lista.getChildren().add(
                    item
            );
        }

        ScrollPane scroll =
                new ScrollPane(
                        lista
                );

        scroll.setFitToWidth(
                true
        );

        scroll.setPrefViewportHeight(
                300
        );

        Button botonCerrar =
                new Button(
                        "Cerrar"
                );

        botonCerrar.setCancelButton(
                true
        );

        botonCerrar.setOnAction(
                evento ->
                        ventana.close()
        );

        HBox botones =
                new HBox(
                        botonCerrar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        VBox contenido =
                new VBox(
                        12,
                        titulo,
                        informacion,
                        scroll,
                        botones
                );

        contenido.setPadding(
                new Insets(20)
        );

        VBox.setVgrow(
                scroll,
                Priority.ALWAYS
        );

        Scene escena =
                new Scene(
                        contenido,
                        650,
                        500
                );

        ventana.setScene(
                escena
        );

        ventana.showAndWait();
    }

    // =====================================================
    // PREPARAR ENVÍO
    // =====================================================

    private void abrirPreparacionEnvio(
            Stage ventanaAnterior,
            List<Candidato> candidatos
    ) {

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Preparar envío de correos"
        );

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.initOwner(
                ventanaAnterior
        );

        // =================================================
        // ENCABEZADO
        // =================================================

        Label titulo =
                new Label(
                        "Preparar envío de correos"
                );

        titulo.setStyle(
                "-fx-font-size: 21px;" +
                        "-fx-font-weight: bold;"
        );

        Label informacion =
                new Label(
                        "Revisa los destinatarios, el asunto y "
                                + "la vista previa antes de enviar."
                );

        informacion.setStyle(
                "-fx-font-size: 13px;" +
                        "-fx-text-fill: #666666;"
        );

        VBox encabezado =
                new VBox(
                        5,
                        titulo,
                        informacion
                );

        // =================================================
        // TABLA
        // =================================================

        Label etiquetaDestinatarios =
                new Label(
                        "Destinatarios"
                );

        etiquetaDestinatarios.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        TableView<Candidato> tablaPreparacion =
                new TableView<>();

        tablaPreparacion.setItems(
                FXCollections.observableArrayList(
                        candidatos
                )
        );

        tablaPreparacion.setPrefHeight(
                190
        );

        tablaPreparacion.setMinHeight(
                160
        );

        tablaPreparacion.setMaxHeight(
                210
        );

        // =================================================
        // NOMBRE
        // =================================================

        TableColumn<Candidato, String> columnaNombre =
                new TableColumn<>(
                        "Nombre"
                );

        columnaNombre.setCellValueFactory(
                celda ->
                        new SimpleStringProperty(
                                obtenerTexto(
                                        celda.getValue().getNombre()
                                )
                        )
        );

        // =================================================
        // CORREO
        // =================================================

        TableColumn<Candidato, String> columnaCorreo =
                new TableColumn<>(
                        "Correo"
                );

        columnaCorreo.setCellValueFactory(
                celda ->
                        new SimpleStringProperty(
                                obtenerTexto(
                                        celda.getValue().getCorreo()
                                )
                        )
        );

        // =================================================
        // ASUNTO
        // =================================================

        TableColumn<Candidato, String> columnaAsunto =
                new TableColumn<>(
                        "Asunto"
                );

        columnaAsunto.setCellValueFactory(
                celda -> {

                    String nombre =
                            obtenerTexto(
                                    celda.getValue()
                                            .getNombre()
                            );

                    return new SimpleStringProperty(
                            "CARTA OFERTA a " + nombre
                    );
                }
        );

        tablaPreparacion.getColumns().addAll(
                columnaNombre,
                columnaCorreo,
                columnaAsunto
        );

        tablaPreparacion.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        tablaPreparacion.setPlaceholder(
                new Label(
                        "No hay destinatarios."
                )
        );

        // =================================================
        // VISTA PREVIA
        // =================================================

        Label etiquetaVistaPrevia =
                new Label(
                        "Vista previa del correo"
                );

        etiquetaVistaPrevia.setStyle(
                "-fx-font-size: 15px;" +
                        "-fx-font-weight: bold;"
        );

        Label etiquetaNombre =
                new Label(
                        "Nombre:"
                );

        Label etiquetaCorreo =
                new Label(
                        "Correo:"
                );

        Label etiquetaAsunto =
                new Label(
                        "Asunto:"
                );

        etiquetaNombre.setStyle(
                "-fx-font-weight: bold;"
        );

        etiquetaCorreo.setStyle(
                "-fx-font-weight: bold;"
        );

        etiquetaAsunto.setStyle(
                "-fx-font-weight: bold;"
        );

        VBox datosCorreo =
                new VBox(
                        4,
                        etiquetaNombre,
                        etiquetaCorreo,
                        etiquetaAsunto
                );

        datosCorreo.setPadding(
                new Insets(
                        7,
                        10,
                        7,
                        10
                )
        );

        datosCorreo.setStyle(
                "-fx-background-color: #f7f7f7;" +
                        "-fx-border-color: #dddddd;" +
                        "-fx-border-radius: 5;" +
                        "-fx-background-radius: 5;"
        );

        // =================================================
        // WEBVIEW
        // =================================================

        WebView webView =
                new WebView();

        WebEngine webEngine =
                webView.getEngine();

        webView.setPrefHeight(
                280
        );

        webView.setMinHeight(
                240
        );

        webView.setMaxHeight(
                300
        );

        // =================================================
        // SELECCIÓN
        // =================================================

        tablaPreparacion
                .getSelectionModel()
                .selectedItemProperty()
                .addListener(
                        (observable,
                         anterior,
                         candidato) -> {

                            if (candidato == null) {

                                return;
                            }

                            actualizarDatosCorreo(
                                    candidato,
                                    etiquetaNombre,
                                    etiquetaCorreo,
                                    etiquetaAsunto
                            );

                            /*
                             * Esperamos a que JavaFX termine
                             * de actualizar la ventana antes
                             * de cargar el HTML.
                             */
                            Platform.runLater(
                                    () ->
                                            cargarVistaPrevia(
                                                    webEngine,
                                                    candidato
                                            )
                            );
                        }
                );

        // =================================================
        // BOTONES
        // =================================================

        Button botonVolver =
                new Button(
                        "Volver"
                );

        botonVolver.setOnAction(
                evento ->
                        ventana.close()
        );

        Button botonEnviar =
                new Button(
                        "Enviar correos"
                );
        botonEnviar.setDisable(true);

        botonEnviar.setOnAction(
                evento ->
                        enviarCorreos(
                                ventana,
                                candidatos
                        )
        );

        HBox botones =
                new HBox(
                        10,
                        botonVolver,
                        botonEnviar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        // =================================================
        // CONTENIDO
        // =================================================

        VBox contenido =
                new VBox(
                        8,
                        encabezado,
                        etiquetaDestinatarios,
                        tablaPreparacion,
                        etiquetaVistaPrevia,
                        datosCorreo,
                        webView,
                        botones
                );

        contenido.setPadding(
                new Insets(
                        12
                )
        );

        /*
         * Ninguno de estos elementos debe crecer
         * indefinidamente.
         */
        VBox.setVgrow(
                tablaPreparacion,
                Priority.NEVER
        );

        VBox.setVgrow(
                webView,
                Priority.NEVER
        );

        // =================================================
        // ESCENA
        // =================================================

        Scene escena =
                new Scene(
                        contenido,
                        1000,
                        700
                );

        ventana.setScene(
                escena
        );

        ventana.setMinWidth(
                850
        );

        ventana.setMinHeight(
                600
        );

        /*
         * Primero mostramos la ventana.
         *
         * Después seleccionamos el primer candidato
         * y cargamos la vista previa.
         *
         * Esto evita que el WebView bloquee la creación
         * inicial de la ventana.
         */
        ventana.show();

        Platform.runLater(
                () -> {

                    if (!candidatos.isEmpty()) {

                        tablaPreparacion
                                .getSelectionModel()
                                .selectFirst();

                    } else {

                        webEngine.loadContent(
                                """
                                <html>
                                    <body style="font-family: Arial; padding: 20px;">
                                        <h3>No hay destinatarios</h3>
                                    </body>
                                </html>
                                """
                        );
                    }
                }
        );

        ventana.setOnCloseRequest(
                evento ->
                        webView.getEngine()
                                .loadContent("")
        );
    }

    // =====================================================
    // CARGAR / REEMPLAZAR PLANTILLA
    // =====================================================

    private void cargarPlantillaHtml(
            Label etiquetaPlantilla,
            Stage ventana
    ) {

        FileChooser selector =
                new FileChooser();

        selector.setTitle(
                "Seleccionar plantilla HTML"
        );

        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivos HTML",
                        "*.html",
                        "*.htm"
                )
        );

        File archivo =
                selector.showOpenDialog(
                        ventana
                );

        if (archivo == null) {

            return;
        }

        try {

            Path carpetaPlantilla =
                    Paths.get(
                            CARPETA_PLANTILLA_HTML
                    );

            Files.createDirectories(
                    carpetaPlantilla
            );

            Path destino =
                    carpetaPlantilla.resolve(
                            NOMBRE_PLANTILLA
                    );

            Files.copy(
                    archivo.toPath(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

            /*
             * Guardamos la ruta interna.
             *
             * La vista previa utilizará preferentemente
             * plantilla_html/correo.html.
             */

            configuracionService.guardarPlantillaCorreo(
                    destino.toFile()
            );

            actualizarEstadoPlantilla(
                    etiquetaPlantilla
            );

            mostrarMensaje(
                    "Plantilla cargada",
                    "La plantilla fue copiada correctamente a "
                            + "la carpeta plantilla_html."
            );

        } catch (Exception e) {

            mostrarMensaje(
                    "Error",
                    "No se pudo cargar la plantilla HTML.\n\n"
                            + e.getMessage()
            );
        }
    }

    // =====================================================
    // ESTADO DE LA PLANTILLA
    // =====================================================

    private void actualizarEstadoPlantilla(
            Label etiqueta
    ) {

        File plantilla =
                obtenerArchivoPlantilla();

        if (plantilla != null
                && plantilla.exists()) {

            etiqueta.setText(
                    "Plantilla activa: ✓ correo.html"
            );

        } else {

            etiqueta.setText(
                    "Plantilla activa: No configurada"
            );
        }
    }

    // =====================================================
    // OBTENER PLANTILLA
    // =====================================================

    private File obtenerArchivoPlantilla() {

        Path rutaInterna =
                Paths.get(
                        CARPETA_PLANTILLA_HTML,
                        NOMBRE_PLANTILLA
                );

        File archivoInterno =
                rutaInterna.toFile();

        /*
         * IMPORTANTE:
         *
         * La nueva lógica busca primero
         * plantilla_html/correo.html.
         */

        if (archivoInterno.exists()
                && archivoInterno.isFile()) {

            return archivoInterno;
        }

        /*
         * Compatibilidad con configuraciones anteriores.
         */

        String rutaConfigurada =
                configuracionService
                        .obtenerPlantillaCorreo();

        if (rutaConfigurada == null
                || rutaConfigurada.isBlank()) {

            return null;
        }

        File archivoConfigurado =
                new File(
                        rutaConfigurada
                );

        if (archivoConfigurado.exists()
                && archivoConfigurado.isFile()) {

            return archivoConfigurado;
        }

        return null;
    }

    // =====================================================
    // CARGAR VISTA PREVIA
    // =====================================================

    private void cargarVistaPrevia(
            WebEngine webEngine,
            Candidato candidato
    ) {

        File archivoPlantilla =
                obtenerArchivoPlantilla();

        if (archivoPlantilla == null
                || !archivoPlantilla.exists()) {

            webEngine.loadContent(
                    """
                    <html>
                        <body style="font-family: Arial; padding: 20px;">
                            <h3>No hay plantilla HTML configurada</h3>
                            <p>
                                Regresa a la ventana anterior y
                                carga una plantilla HTML.
                            </p>
                        </body>
                    </html>
                    """
            );

            return;
        }

        try {

            String html =
                    Files.readString(
                            archivoPlantilla.toPath()
                    );

            html =
                    reemplazarVariables(
                            html,
                            candidato
                    );

            /*
             * loadContent se ejecuta en JavaFX Application Thread.
             * El HTML ya fue preparado antes de llegar aquí.
             */

            webEngine.loadContent(
                    html
            );

        } catch (Exception e) {

            webEngine.loadContent(
                    """
                    <html>
                        <body style="font-family: Arial; padding: 20px;">
                            <h3>Error cargando la plantilla</h3>
                            <p>
                                No fue posible mostrar la vista previa.
                            </p>
                        </body>
                    </html>
                    """
            );

            e.printStackTrace();
        }
    }

    // =====================================================
    // REEMPLAZAR VARIABLES
    // =====================================================

    private String reemplazarVariables(
            String html,
            Candidato candidato
    ) {

        if (html == null) {

            return "";
        }

        if (candidato == null) {

            return html;
        }

        String resultado =
                html;

        String nombre =
                candidato.getNombre() == null
                        ? ""
                        : candidato.getNombre();

        resultado =
                resultado.replace(
                        "{{nombre}}",
                        nombre
                );

        return resultado;
    }

    // =====================================================
    // ACTUALIZAR DATOS
    // =====================================================

    private void actualizarDatosCorreo(
            Candidato candidato,
            Label etiquetaNombre,
            Label etiquetaCorreo,
            Label etiquetaAsunto
    ) {

        String nombre =
                obtenerTexto(
                        candidato.getNombre()
                );

        String correo =
                obtenerTexto(
                        candidato.getCorreo()
                );

        String asunto =
                "CARTA OFERTA a " + nombre;

        etiquetaNombre.setText(
                "Nombre: " + nombre
        );

        etiquetaCorreo.setText(
                "Correo: " + correo
        );

        etiquetaAsunto.setText(
                "Asunto: " + asunto
        );
    }

    // =====================================================
    // TEXTO SEGURO
    // =====================================================

    private String obtenerTexto(
            String texto
    ) {

        if (texto == null) {

            return "";
        }

        return texto;
    }

    // =====================================================
    // MENSAJES
    // =====================================================

    private void mostrarMensaje(
            String titulo,
            String mensaje
    ) {

        Alert alerta =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                titulo
        );

        alerta.setHeaderText(
                null
        );

        alerta.setContentText(
                mensaje
        );

        /*
         * Permitimos que mensajes largos puedan
         * ocupar varias líneas sin quedar cortados.
         */

        Label contenido =
                new Label(
                        mensaje
                );

        contenido.setWrapText(
                true
        );

        contenido.setMaxWidth(
                600
        );

        alerta.getDialogPane()
                .setContent(
                        contenido
                );

        alerta.showAndWait();
    }

    // =====================================================
// ENVIAR CORREOS
// =====================================================

    private void enviarCorreos(
            Stage ventana,
            List<Candidato> candidatos
    ) {

        if (candidatos == null
                || candidatos.isEmpty()) {

            mostrarMensaje(
                    "Sin destinatarios",
                    "No hay candidatos para enviar."
            );

            return;
        }

        // =================================================
        // CONFIRMACIÓN
        // =================================================

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Confirmar envío"
        );

        confirmacion.setHeaderText(
                "¿Deseas enviar los correos?"
        );

        confirmacion.setContentText(
                "Se enviarán "
                        + candidatos.size()
                        + " correo(s)."
        );

        var respuesta =
                confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get()
                != javafx.scene.control.ButtonType.OK) {

            return;
        }

        // =================================================
        // DATOS DEL REMITENTE
        // =================================================

        String usuario =
                obtenerUsuarioCorreo();

        if (usuario == null
                || usuario.isBlank()) {

            mostrarMensaje(
                    "Correo remitente no configurado",
                    "No se encontró el correo del remitente."
            );

            return;
        }

        // =================================================
        // HTML
        // =================================================

        File plantilla =
                obtenerArchivoPlantilla();

        if (plantilla == null
                || !plantilla.exists()) {

            mostrarMensaje(
                    "Plantilla no encontrada",
                    "Debes configurar una plantilla HTML antes "
                            + "de enviar los correos."
            );

            return;
        }

        int enviados = 0;

        List<String> errores =
                new ArrayList<>();

        // =================================================
        // ENVÍO
        // =================================================

        for (Candidato candidato : candidatos) {

            try {

                String destinatario =
                        candidato.getCorreo();

                if (destinatario == null
                        || destinatario.isBlank()) {

                    errores.add(
                            candidato.getNombre()
                                    + " — no tiene correo."
                    );

                    continue;
                }

                // -----------------------------------------
                // HTML PERSONALIZADO
                // -----------------------------------------

                String html =
                        Files.readString(
                                plantilla.toPath()
                        );

                html =
                        reemplazarVariables(
                                html,
                                candidato
                        );

                // -----------------------------------------
                // ASUNTO
                // -----------------------------------------

                String nombre =
                        obtenerTexto(
                                candidato.getNombre()
                        );

                String asunto =
                        "CARTA OFERTA a "
                                + nombre;

                // -----------------------------------------
                // CARTA PDF
                // -----------------------------------------

                File archivoPdf =
                        emailService.obtenerArchivoPdf(
                                candidato.getArchivoCarta()
                        );

                if (archivoPdf == null) {

                    errores.add(
                            nombre
                                    + " — no se encontró "
                                    + "el PDF de la carta."
                    );

                    continue;
                }

                // -----------------------------------------
                // ENVIAR
                // -----------------------------------------

                emailService.enviarCorreo(
                        usuario,
                        destinatario,
                        asunto,
                        html,
                        archivoPdf
                );

                // -----------------------------------------
                // REGISTRAR ENVÍO
                // -----------------------------------------

                repository.registrarCorreoEnviado(
                        candidato.getId()
                );

                candidato.setFechaCorreo(
                        LocalDateTime.now().toString()
                );

                enviados++;

            } catch (Exception e) {

                String nombre =
                        obtenerTexto(
                                candidato.getNombre()
                        );

                errores.add(
                        nombre
                                + " — "
                                + e.getMessage()
                );
            }
        }

        // =================================================
        // RESULTADO
        // =================================================

        StringBuilder resultado =
                new StringBuilder();

        resultado.append(
                "Correos enviados correctamente: "
        );

        resultado.append(
                enviados
        );

        if (!errores.isEmpty()) {

            resultado.append(
                    "\n\nNo enviados:"
            );

            for (String error : errores) {

                resultado.append(
                        "\n• "
                );

                resultado.append(
                        error
                );
            }
        }

        mostrarMensaje(
                "Resultado del envío",
                resultado.toString()
        );

        // =================================================
        // ACTUALIZAR TABLA
        // =================================================

        tabla.refresh();
    }
    private String obtenerUsuarioCorreo() {

        return "luimanitru@hotmail.com";
    }
}