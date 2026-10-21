package com.cartaofertaexpress.ui;

import com.cartaofertaexpress.service.ConfiguracionService;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionView {

    private final ConfiguracionService configuracionService;

    private final ListView<String> listaTiposContrato;

    private final Spinner<Integer> spinnerFuenteGeneral;
    private final Spinner<Integer> spinnerFuenteTabla;

    private VBox contenidoDerecho;

    private CheckBox filasAlternadas;

    private Button botonApariencia;
    private Button botonContratos;
    private Button botonAcercaDe;
    private Button botonSeleccionado;

    private BorderPane root;
    private BorderPane centro;
    private VBox menu;
    private HBox barraInferior;

    private VBox encabezado;

    private Scene escena;

    /*
     * Referencia al MainView.
     *
     * Se utiliza para que, después de guardar,
     * la aplicación principal pueda actualizar
     * el tamaño de fuente.
     */
    private MainView mainView;

    // ==================================================
    // COLORES BASE FIJOS
    // ==================================================

    private static final String COLOR_FONDO =
            "#F8FAFC";

    private static final String COLOR_CABECERA =
            "#EEF2F7";

    private static final String COLOR_TEXTO =
            "#1F2937";

    private static final String COLOR_SECUNDARIO =
            "#6B7280";

    private static final String COLOR_BORDE =
            "#E5E7EB";

    private static final String COLOR_SELECCION =
            "#E8F1EC";

    private static final String COLOR_HOVER =
            "#F3F6F4";

    private static final String COLOR_PRINCIPAL =
            "#7FAF9B";

    // ==================================================
    // CONSTRUCTOR
    // ==================================================

    public ConfiguracionView() {

        configuracionService =
                new ConfiguracionService();

        listaTiposContrato =
                new ListView<>();

        /*
         * Fuente general.
         *
         * El valor se obtiene desde configuracion.properties.
         */
        spinnerFuenteGeneral =
                new Spinner<>(
                        10,
                        24,
                        configuracionService
                                .obtenerTamanoFuente()
                );

        /*
         * Fuente de tabla.
         *
         * El valor se obtiene desde configuracion.properties.
         */
        spinnerFuenteTabla =
                new Spinner<>(
                        10,
                        24,
                        configuracionService
                                .obtenerTamanoFuenteTabla()
                );

        spinnerFuenteGeneral.setEditable(
                false
        );

        spinnerFuenteTabla.setEditable(
                false
        );
    }

    // ==================================================
    // MOSTRAR
    // ==================================================

    public void mostrar(
            Stage padre,
            MainView mainView
    ) {

        this.mainView =
                mainView;

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Configuración - Carta Oferta EXPRESS"
        );

        ventana.initModality(
                Modality.APPLICATION_MODAL
        );

        ventana.initOwner(
                padre
        );

        // ==================================================
        // ENCABEZADO
        // ==================================================

        Label titulo =
                new Label(
                        "⚙  Configuración"
                );

        /*
         * El encabezado tiene su propio tamaño.
         *
         * NO será afectado por el tamaño de fuente
         * general configurado por el usuario.
         */
        titulo.setStyle("""
                -fx-font-size: 30px;
                -fx-font-weight: bold;
                -fx-font-family: "Georgia";
                -fx-text-fill: #1F2937;
                """);

        titulo.setAlignment(
                Pos.CENTER
        );

        Label subtitulo =
                new Label(
                        "Personaliza Carta Oferta EXPRESS"
                );

        subtitulo.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: normal;
                -fx-text-fill: #6B7280;
                """);

        subtitulo.setAlignment(
                Pos.CENTER
        );

        encabezado =
                new VBox(
                        5,
                        titulo,
                        subtitulo
                );

        encabezado.setAlignment(
                Pos.CENTER
        );

        encabezado.setPadding(
                new Insets(
                        22,
                        20,
                        22,
                        20
                )
        );

        encabezado.setStyle("""
                -fx-background-color:
                    linear-gradient(
                        to bottom,
                        #F8FAFC,
                        #EEF2F7
                    );
                -fx-border-color: #E5E7EB;
                -fx-border-width: 0 0 1 0;
                """);

        // ==================================================
        // MENÚ
        // ==================================================

        botonApariencia =
                crearBotonMenu(
                        "🎨",
                        "Apariencia",
                        "Tamaño de fuentes"
                );

        botonContratos =
                crearBotonMenu(
                        "📋",
                        "Contratos",
                        "Tipos de contrato"
                );

        botonAcercaDe =
                crearBotonMenu(
                        "ⓘ",
                        "Acerca de...",
                        "Información de la aplicación"
                );

        menu =
                new VBox(
                        8,
                        botonApariencia,
                        botonContratos,
                        botonAcercaDe
                );

        menu.setPadding(
                new Insets(
                        20,
                        12,
                        20,
                        12
                )
        );

        menu.setPrefWidth(
                210
        );

        menu.setStyle("""
                -fx-background-color: #F8FAFC;
                -fx-border-color: #E5E7EB;
                -fx-border-width: 0 1 0 0;
                """);

        // ==================================================
        // CONTENIDO DERECHO
        // ==================================================

        contenidoDerecho =
                new VBox();

        contenidoDerecho.setPadding(
                new Insets(
                        30,
                        35,
                        30,
                        35
                )
        );

        contenidoDerecho.setSpacing(
                15
        );

        contenidoDerecho.setStyle("""
                -fx-background-color: #F8FAFC;
                """);

        VBox.setVgrow(
                contenidoDerecho,
                Priority.ALWAYS
        );

        // ==================================================
        // ACCIONES MENÚ
        // ==================================================

        botonApariencia.setOnAction(
                event ->
                        mostrarApariencia()
        );

        botonContratos.setOnAction(
                event ->
                        mostrarContratos()
        );

        botonAcercaDe.setOnAction(
                event ->
                        mostrarAcercaDe()
        );

        // ==================================================
        // BOTONES INFERIORES
        // ==================================================

        Button botonCancelar =
                new Button(
                        "Cancelar"
                );

        Button botonGuardar =
                new Button(
                        "Guardar cambios"
                );

        botonCancelar.setPrefHeight(
                35
        );

        botonGuardar.setPrefHeight(
                35
        );

        botonCancelar.setStyle("""
                -fx-background-color: #F3F4F6;
                -fx-text-fill: #374151;
                -fx-background-radius: 7;
                -fx-padding: 8 18;
                """);

        botonGuardar.setStyle("""
                -fx-background-color: #7FAF9B;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 7;
                -fx-padding: 8 18;
                """);

        botonGuardar.setOnAction(
                event -> {

                    guardarConfiguracion();

                    /*
                     * Avisamos al MainView para que lea
                     * nuevamente la configuración.
                     */
                    if (mainView != null) {

                        mainView.aplicarConfiguracion();
                    }

                    mostrarMensaje(
                            "Configuración guardada",
                            "Los cambios fueron guardados correctamente."
                    );
                }
        );

        botonCancelar.setOnAction(
                event ->
                        ventana.close()
        );

        Region espacio =
                new Region();

        HBox.setHgrow(
                espacio,
                Priority.ALWAYS
        );

        barraInferior =
                new HBox(
                        10,
                        espacio,
                        botonCancelar,
                        botonGuardar
                );

        barraInferior.setAlignment(
                Pos.CENTER_RIGHT
        );

        barraInferior.setPadding(
                new Insets(
                        12,
                        20,
                        15,
                        20
                )
        );

        barraInferior.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #F8FAFC;
                -fx-border-width: 1 0 0 0;
                """);

        // ==================================================
        // PANEL CENTRAL
        // ==================================================

        centro =
                new BorderPane();

        centro.setLeft(
                menu
        );

        centro.setCenter(
                contenidoDerecho
        );

        // ==================================================
        // ROOT
        // ==================================================

        root =
                new BorderPane();

        root.setTop(
                encabezado
        );

        root.setCenter(
                centro
        );

        root.setBottom(
                barraInferior
        );

        // ==================================================
        // ESCENA
        // ==================================================

        escena =
                new Scene(
                        root,
                        900,
                        650
                );

        ventana.setScene(
                escena
        );

        // ==================================================
        // MOSTRAR APARIENCIA INICIALMENTE
        // ==================================================

        mostrarApariencia();

        ventana.showAndWait();
    }

    // ==================================================
    // BOTÓN MENÚ
    // ==================================================

    private Button crearBotonMenu(
            String icono,
            String titulo,
            String descripcion
    ) {

        Label iconoLabel =
                new Label(
                        icono
                );

        iconoLabel.setStyle("""
                -fx-font-size: 21px;
                """);

        Label tituloLabel =
                new Label(
                        titulo
                );

        tituloLabel.setStyle("""
                -fx-font-size: 14px;
                -fx-font-weight: bold;
                -fx-text-fill: #1F2937;
                """);

        Label descripcionLabel =
                new Label(
                        descripcion
                );

        descripcionLabel.setStyle("""
                -fx-font-size: 10px;
                -fx-text-fill: #6B7280;
                """);

        VBox textos =
                new VBox(
                        2,
                        tituloLabel,
                        descripcionLabel
                );

        HBox contenido =
                new HBox(
                        12,
                        iconoLabel,
                        textos
                );

        contenido.setAlignment(
                Pos.CENTER_LEFT
        );

        Button boton =
                new Button();

        boton.setGraphic(
                contenido
        );

        boton.setMaxWidth(
                Double.MAX_VALUE
        );

        boton.setPrefHeight(
                62
        );

        boton.setAlignment(
                Pos.CENTER_LEFT
        );

        boton.setStyle("""
                -fx-background-color: transparent;
                -fx-background-radius: 8;
                -fx-padding: 8 10;
                """);

        boton.setOnMouseEntered(
                event -> {

                    if (boton != botonSeleccionado) {

                        boton.setStyle("""
                                -fx-background-color: #F3F6F4;
                                -fx-background-radius: 8;
                                -fx-padding: 8 10;
                                """);
                    }
                }
        );

        boton.setOnMouseExited(
                event -> {

                    if (boton != botonSeleccionado) {

                        boton.setStyle("""
                                -fx-background-color: transparent;
                                -fx-background-radius: 8;
                                -fx-padding: 8 10;
                                """);
                    }
                }
        );

        return boton;
    }

    // ==================================================
    // APARIENCIA
    // ==================================================

    private void mostrarApariencia() {

        seleccionarMenu(
                botonApariencia
        );

        VBox titulo =
                crearTitulo(
                        "🎨 Apariencia",
                        "Personaliza el tamaño de fuente y la apariencia de la tabla."
                );

        VBox tarjeta =
                crearTarjeta();

        spinnerFuenteGeneral.setPrefWidth(
                90
        );

        spinnerFuenteGeneral.setMinWidth(
                90
        );

        spinnerFuenteTabla.setPrefWidth(
                90
        );

        spinnerFuenteTabla.setMinWidth(
                90
        );

        HBox filaGeneral =
                crearFila(
                        "Tamaño de fuente general",
                        "Afecta botones, textos, campos y controles de la interfaz, excepto el encabezado principal.",
                        spinnerFuenteGeneral
                );

        HBox filaTabla =
                crearFila(
                        "Tamaño de fuente de la tabla",
                        "Controla exclusivamente el texto mostrado dentro de la tabla.",
                        spinnerFuenteTabla
                );

        filasAlternadas =
                new CheckBox(
                        "Mostrar filas alternadas"
                );

        filasAlternadas.setSelected(
                configuracionService
                        .obtenerFilasAlternadas()
        );

        VBox opcionFilas =
                crearOpcion(
                        filasAlternadas,
                        "Alterna suavemente el color de las filas para facilitar la lectura de los candidatos."
                );

        tarjeta.getChildren().addAll(
                filaGeneral,
                filaTabla,
                opcionFilas
        );

        contenidoDerecho.getChildren()
                .setAll(
                        titulo,
                        tarjeta
                );

        aplicarFuenteGeneralConfiguracion();
    }

    // ==================================================
    // APLICAR FUENTE GENERAL EN CONFIGURACIÓN
    // ==================================================

    private void aplicarFuenteGeneralConfiguracion() {

        if (centro == null) {
            return;
        }

        int tamano =
                spinnerFuenteGeneral.getValue();

        centro.setStyle(
                "-fx-font-size: " + tamano + "px;"
        );
    }

    // ==================================================
    // CONTRATOS
    // ==================================================

    private void mostrarContratos() {

        seleccionarMenu(
                botonContratos
        );

        VBox titulo =
                crearTitulo(
                        "📋 Tipos de contrato",
                        "Administra las opciones disponibles para tus candidatos."
                );

        cargarTiposContrato();

        listaTiposContrato.setPrefHeight(
                300
        );

        listaTiposContrato.setStyle("""
                -fx-background-color: white;
                -fx-border-color: #E5E7EB;
                -fx-border-radius: 8;
                -fx-background-radius: 8;
                """);

        Button agregar =
                new Button(
                        "＋ Agregar tipo"
                );

        Button editar =
                new Button(
                        "✏ Editar"
                );

        Button eliminar =
                new Button(
                        "🗑 Eliminar"
                );

        aplicarEstiloBotonPrincipal(
                agregar
        );

        editar.setStyle("""
                -fx-background-color: #F3F4F6;
                -fx-text-fill: #374151;
                -fx-background-radius: 7;
                """);

        eliminar.setStyle("""
                -fx-background-color: #F3F4F6;
                -fx-text-fill: #374151;
                -fx-background-radius: 7;
                """);

        agregar.setOnAction(
                event ->
                        agregarTipoContrato()
        );

        editar.setOnAction(
                event ->
                        editarTipoContrato()
        );

        eliminar.setOnAction(
                event ->
                        eliminarTipoContrato()
        );

        HBox botones =
                new HBox(
                        8,
                        agregar,
                        editar,
                        eliminar
                );

        VBox tarjeta =
                crearTarjeta();

        tarjeta.getChildren().addAll(
                listaTiposContrato,
                botones
        );

        contenidoDerecho.getChildren()
                .setAll(
                        titulo,
                        tarjeta
                );

        aplicarFuenteGeneralConfiguracion();
    }

    // ==================================================
    // ACERCA DE
    // ==================================================

    private void mostrarAcercaDe() {

        seleccionarMenu(
                botonAcercaDe
        );

        VBox titulo =
                crearTitulo(
                        "ⓘ Acerca de Carta Oferta EXPRESS",
                        "Información sobre la aplicación."
                );

        VBox tarjeta =
                crearTarjeta();

        Label nombre =
                new Label(
                        "Carta Oferta EXPRESS"
                );

        nombre.setStyle("""
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-text-fill: #1F2937;
                """);

        Label version =
                new Label(
                        "Versión 1.8"
                );

        version.setStyle("""
                -fx-font-size: 10px;
                -fx-text-fill: #6B7280;
                """);

        Label descripcion =
                new Label(
                        "Aplicación para la gestión y generación "
                                + "de cartas de oferta laboral."
                );

        descripcion.setWrapText(
                true
        );

        descripcion.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #374151;
                """);

        Label copyright =
                new Label(
                        "© 2026 Luis Manuel Niño Trujillo. Todos los derechos reservados."
                );

        copyright.setStyle("""
                -fx-font-size: 12px;
                -fx-text-fill: #4A4A4A;
                """);

        Label derechos =
                new Label(
                        "Contacto: luimanitru@gmail.com. Desarrollo de aplicaciones personalizadas."
                );

        derechos.setStyle("""
                -fx-font-size: 12px;
                -fx-text-fill: #4A4A4A;
                """);

        VBox informacion =
                new VBox(
                        8,
                        nombre,
                        version,
                        descripcion,
                        new Separator(),
                        copyright,
                        derechos
                );

        tarjeta.getChildren().add(
                informacion
        );

        contenidoDerecho.getChildren()
                .setAll(
                        titulo,
                        tarjeta
                );

        aplicarFuenteGeneralConfiguracion();
    }

    // ==================================================
    // TÍTULO DE SECCIÓN
    // ==================================================

    private VBox crearTitulo(
            String titulo,
            String descripcion
    ) {

        Label labelTitulo =
                new Label(
                        titulo
                );

        labelTitulo.setStyle("""
                -fx-font-size: 24px;
                -fx-font-weight: bold;
                -fx-text-fill: #1F2937;
                """);

        Label labelDescripcion =
                new Label(
                        descripcion
                );

        labelDescripcion.setStyle("""
                -fx-font-size: 13px;
                -fx-text-fill: #6B7280;
                """);

        labelDescripcion.setWrapText(
                true
        );

        VBox caja =
                new VBox(
                        5,
                        labelTitulo,
                        labelDescripcion
                );

        caja.setPadding(
                new Insets(
                        0,
                        0,
                        15,
                        0
                )
        );

        return caja;
    }

    // ==================================================
    // TARJETA
    // ==================================================

    private VBox crearTarjeta() {

        VBox tarjeta =
                new VBox(
                        15
                );

        tarjeta.setPadding(
                new Insets(
                        20
                )
        );

        tarjeta.setStyle("""
                -fx-background-color: white;
                -fx-background-radius: 10;
                -fx-border-color: #E5E7EB;
                -fx-border-radius: 10;
                """);

        return tarjeta;
    }

    // ==================================================
    // FILA
    // ==================================================

    private HBox crearFila(
            String titulo,
            String descripcion,
            Control control
    ) {

        Label tituloLabel =
                new Label(
                        titulo
                );

        tituloLabel.setStyle("""
                -fx-font-weight: bold;
                -fx-text-fill: #374151;
                """);

        Label descripcionLabel =
                new Label(
                        descripcion
                );

        descripcionLabel.setWrapText(
                true
        );

        descripcionLabel.setStyle("""
                -fx-font-size: 11px;
                -fx-text-fill: #9CA3AF;
                """);

        VBox textos =
                new VBox(
                        3,
                        tituloLabel,
                        descripcionLabel
                );

        Region espacio =
                new Region();

        HBox.setHgrow(
                espacio,
                Priority.ALWAYS
        );

        HBox fila =
                new HBox(
                        15,
                        textos,
                        espacio,
                        control
                );

        fila.setAlignment(
                Pos.CENTER_LEFT
        );

        return fila;
    }

    // ==================================================
    // OPCIÓN
    // ==================================================

    private VBox crearOpcion(
            CheckBox check,
            String descripcion
    ) {

        Label label =
                new Label(
                        descripcion
                );

        label.setStyle("""
                -fx-font-size: 11px;
                -fx-text-fill: #9CA3AF;
                """);

        return new VBox(
                3,
                check,
                label
        );
    }

    // ==================================================
    // ESTILO BOTÓN PRINCIPAL
    // ==================================================

    private void aplicarEstiloBotonPrincipal(
            Button boton
    ) {

        boton.setStyle("""
                -fx-background-color: #7FAF9B;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 7;
                -fx-padding: 8 18;
                """);
    }

    // ==================================================
    // SELECCIONAR MENÚ
    // ==================================================

    private void seleccionarMenu(
            Button seleccionado
    ) {

        if (seleccionado == null) {
            return;
        }

        botonSeleccionado =
                seleccionado;

        Button[] botones = {
                botonApariencia,
                botonContratos,
                botonAcercaDe
        };

        for (Button boton : botones) {

            if (boton == seleccionado) {

                boton.setStyle("""
                        -fx-background-color: #E8F1EC;
                        -fx-background-radius: 8;
                        -fx-padding: 8 10;
                        """);

            } else {

                boton.setStyle("""
                        -fx-background-color: transparent;
                        -fx-background-radius: 8;
                        -fx-padding: 8 10;
                        """);
            }
        }
    }

    // ==================================================
    // CONTRATOS
    // ==================================================

    private void cargarTiposContrato() {

        listaTiposContrato
                .getItems()
                .setAll(
                        configuracionService
                                .obtenerTiposContrato()
                );
    }

    private void agregarTipoContrato() {

        TextInputDialog dialog =
                new TextInputDialog();

        dialog.setTitle(
                "Nuevo tipo de contrato"
        );

        dialog.setHeaderText(
                "Agregar tipo de contrato"
        );

        dialog.setContentText(
                "Nombre:"
        );

        dialog.showAndWait()
                .ifPresent(
                        texto -> {

                            String tipo =
                                    texto.trim();

                            if (tipo.isBlank()) {
                                return;
                            }

                            if (listaTiposContrato
                                    .getItems()
                                    .contains(tipo)) {

                                mostrarMensaje(
                                        "Tipo duplicado",
                                        "Ese tipo de contrato ya existe."
                                );

                                return;
                            }

                            listaTiposContrato
                                    .getItems()
                                    .add(
                                            tipo
                                    );
                        }
                );
    }

    private void editarTipoContrato() {

        String seleccionado =
                listaTiposContrato
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarMensaje(
                    "Editar tipo",
                    "Selecciona primero un tipo de contrato."
            );

            return;
        }

        TextInputDialog dialog =
                new TextInputDialog(
                        seleccionado
                );

        dialog.setTitle(
                "Editar tipo de contrato"
        );

        dialog.setHeaderText(
                "Modificar tipo de contrato"
        );

        dialog.setContentText(
                "Nombre:"
        );

        dialog.showAndWait()
                .ifPresent(
                        texto -> {

                            String nuevo =
                                    texto.trim();

                            if (nuevo.isBlank()) {
                                return;
                            }

                            if (!nuevo.equals(
                                    seleccionado
                            )
                                    && listaTiposContrato
                                    .getItems()
                                    .contains(
                                            nuevo
                                    )) {

                                mostrarMensaje(
                                        "Tipo duplicado",
                                        "Ese tipo de contrato ya existe."
                                );

                                return;
                            }

                            int indice =
                                    listaTiposContrato
                                            .getItems()
                                            .indexOf(
                                                    seleccionado
                                            );

                            listaTiposContrato
                                    .getItems()
                                    .set(
                                            indice,
                                            nuevo
                                    );
                        }
                );
    }

    private void eliminarTipoContrato() {

        String seleccionado =
                listaTiposContrato
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarMensaje(
                    "Eliminar tipo",
                    "Selecciona primero un tipo de contrato."
            );

            return;
        }

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar tipo de contrato"
        );

        confirmacion.setHeaderText(
                "¿Eliminar este tipo?"
        );

        confirmacion.setContentText(
                seleccionado
        );

        confirmacion.showAndWait()
                .ifPresent(
                        respuesta -> {

                            if (respuesta ==
                                    ButtonType.OK) {

                                listaTiposContrato
                                        .getItems()
                                        .remove(
                                                seleccionado
                                        );
                            }
                        }
                );
    }

    // ==================================================
    // GUARDAR CONFIGURACIÓN
    // ==================================================

    private void guardarConfiguracion() {

        List<String> tipos =
                new ArrayList<>(
                        listaTiposContrato
                                .getItems()
                );

        // ==================================================
        // TIPOS DE CONTRATO
        // ==================================================

        configuracionService
                .guardarTiposContrato(
                        tipos
                );

        // ==================================================
        // FUENTE GENERAL
        // ==================================================

        configuracionService
                .guardarTamanoFuente(
                        spinnerFuenteGeneral
                                .getValue()
                );

        // ==================================================
        // FUENTE DE TABLA
        // ==================================================

        configuracionService
                .guardarTamanoFuenteTabla(
                        spinnerFuenteTabla
                                .getValue()
                );

        // ==================================================
        // FILAS ALTERNADAS
        // ==================================================

        configuracionService
                .guardarFilasAlternadas(
                        filasAlternadas.isSelected()
                );
    }

    // ==================================================
    // MENSAJE
    // ==================================================

    private void mostrarMensaje(
            String titulo,
            String mensaje
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        alert.setTitle(
                titulo
        );

        alert.setHeaderText(
                null
        );

        alert.setContentText(
                mensaje
        );

        alert.showAndWait();
    }
}