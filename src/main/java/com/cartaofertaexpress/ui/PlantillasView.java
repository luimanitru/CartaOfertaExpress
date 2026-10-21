package com.cartaofertaexpress.ui;

import com.cartaofertaexpress.service.PlantillaService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.Desktop;
import java.io.File;
import java.util.List;

public class PlantillasView {

    private final PlantillaService servicio;

    private final ListView<File> listaPlantillas;

    private File plantillaSeleccionada;

    public PlantillasView() {

        servicio =
                new PlantillaService();

        listaPlantillas =
                new ListView<>();

        configurarLista();

        cargarPlantillas();
    }

    // =====================================================
    // MOSTRAR
    // =====================================================

    public void mostrar(Stage ventanaPrincipal) {

        Stage ventana =
                new Stage();

        ventana.initOwner(
                ventanaPrincipal
        );

        ventana.initModality(
                Modality.WINDOW_MODAL
        );

        ventana.setTitle(
                "Plantillas de cartas"
        );

        // =================================================
        // TÍTULO
        // =================================================

        Label titulo =
                new Label(
                        "Plantillas de cartas"
                );

        titulo.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                """);

        Label subtitulo =
                new Label(
                        "Selecciona la plantilla que deseas utilizar para generar las cartas."
                );

        VBox encabezado =
                new VBox(
                        6,
                        titulo,
                        subtitulo
                );

        encabezado.setPadding(
                new Insets(
                        20,
                        20,
                        10,
                        20
                )
        );

        // =================================================
        // LISTA
        // =================================================

        listaPlantillas.setPlaceholder(
                new Label(
                        "No hay plantillas cargadas."
                )
        );

        VBox contenedorLista =
                new VBox(
                        listaPlantillas
                );

        contenedorLista.setPadding(
                new Insets(
                        10,
                        20,
                        10,
                        20
                )
        );

        VBox.setVgrow(
                listaPlantillas,
                javafx.scene.layout.Priority.ALWAYS
        );

        // =================================================
        // BOTONES
        // =================================================

        Button botonAgregar =
                new Button(
                        "➕ Agregar plantilla"
                );

        Button botonEliminar =
                new Button(
                        "🗑 Eliminar"
                );

        Button botonAbrirCarpeta =
                new Button(
                        "📁 Abrir carpeta"
                );

        Button botonSeleccionar =
                new Button(
                        "✓ Seleccionar"
                );

        Button botonCerrar =
                new Button(
                        "Cerrar"
                );

        // =================================================
        // AGREGAR
        // =================================================

        botonAgregar.setOnAction(
                event ->
                        agregarPlantilla()
        );

        // =================================================
        // ELIMINAR
        // =================================================

        botonEliminar.setOnAction(
                event ->
                        eliminarPlantilla()
        );

        // =================================================
        // ABRIR CARPETA
        // =================================================

        botonAbrirCarpeta.setOnAction(
                event ->
                        abrirCarpeta()
        );

        // =================================================
        // SELECCIONAR
        // =================================================

        botonSeleccionar.setOnAction(
                event -> {

                    File seleccionada =
                            listaPlantillas
                                    .getSelectionModel()
                                    .getSelectedItem();

                    if (seleccionada == null) {

                        mostrarMensaje(
                                Alert.AlertType.WARNING,
                                "Seleccionar plantilla",
                                "Selecciona primero una plantilla."
                        );

                        return;
                    }

                    plantillaSeleccionada =
                            seleccionada;

                    mostrarMensaje(
                            Alert.AlertType.INFORMATION,
                            "Plantilla seleccionada",
                            "La plantilla seleccionada es:\n\n"
                                    + seleccionada.getName()
                    );
                }
        );

        // =================================================
        // CERRAR
        // =================================================

        botonCerrar.setOnAction(
                event ->
                        ventana.close()
        );

        // =================================================
        // BARRA DE BOTONES
        // =================================================

        HBox botones =
                new HBox(
                        10,
                        botonAgregar,
                        botonEliminar,
                        botonAbrirCarpeta,
                        botonSeleccionar,
                        botonCerrar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        botones.setPadding(
                new Insets(
                        10,
                        20,
                        20,
                        20
                )
        );

        // =================================================
        // CONTENEDOR
        // =================================================

        BorderPane root =
                new BorderPane();

        root.setTop(
                encabezado
        );

        root.setCenter(
                contenedorLista
        );

        root.setBottom(
                botones
        );

        // =================================================
        // ESCENA
        // =================================================

        Scene escena =
                new Scene(
                        root,
                        700,
                        500
                );

        ventana.setScene(
                escena
        );

        ventana.showAndWait();
    }

    // =====================================================
    // CONFIGURAR LISTA
    // =====================================================

    private void configurarLista() {

        listaPlantillas.setCellFactory(
                lista -> new ListCell<>() {

                    @Override
                    protected void updateItem(
                            File archivo,
                            boolean empty
                    ) {

                        super.updateItem(
                                archivo,
                                empty
                        );

                        if (empty
                                || archivo == null) {

                            setText(null);

                            return;
                        }

                        setText(
                                archivo.getName()
                        );
                    }
                }
        );

        /*
         * Doble clic = seleccionar.
         */

        listaPlantillas.setOnMouseClicked(
                event -> {

                    if (event.getClickCount() == 2) {

                        File seleccionada =
                                listaPlantillas
                                        .getSelectionModel()
                                        .getSelectedItem();

                        if (seleccionada != null) {

                            plantillaSeleccionada =
                                    seleccionada;
                        }
                    }
                }
        );
    }

    // =====================================================
    // CARGAR PLANTILLAS
    // =====================================================

    private void cargarPlantillas() {

        List<File> plantillas =
                servicio.listarPlantillas();

        listaPlantillas
                .getItems()
                .setAll(
                        plantillas
                );
    }

    // =====================================================
    // AGREGAR PLANTILLA
    // =====================================================

    private void agregarPlantilla() {

        FileChooser selector =
                new FileChooser();

        selector.setTitle(
                "Seleccionar plantilla PowerPoint"
        );

        selector.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Plantillas PowerPoint",
                                "*.pptx"
                        )
                );

        File archivo =
                selector.showOpenDialog(
                        listaPlantillas
                                .getScene()
                                .getWindow()
                );

        if (archivo == null) {
            return;
        }

        try {

            File copia =
                    servicio.agregarPlantilla(
                            archivo
                    );

            cargarPlantillas();

            listaPlantillas
                    .getSelectionModel()
                    .select(copia);

            plantillaSeleccionada =
                    copia;

            mostrarMensaje(
                    Alert.AlertType.INFORMATION,
                    "Plantilla agregada",
                    "La plantilla fue agregada correctamente."
            );

        } catch (Exception e) {

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error",
                    e.getMessage()
            );
        }
    }

    // =====================================================
    // ELIMINAR PLANTILLA
    // =====================================================

    private void eliminarPlantilla() {

        File seleccionada =
                listaPlantillas
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionada == null) {

            mostrarMensaje(
                    Alert.AlertType.WARNING,
                    "Eliminar plantilla",
                    "Selecciona primero una plantilla."
            );

            return;
        }

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar plantilla"
        );

        confirmacion.setHeaderText(
                "¿Eliminar esta plantilla?"
        );

        confirmacion.setContentText(
                seleccionada.getName()
        );

        confirmacion.showAndWait()
                .ifPresent(respuesta -> {

                    if (respuesta ==
                            ButtonType.OK) {

                        try {

                            servicio.eliminarPlantilla(
                                    seleccionada
                            );

                            if (seleccionada.equals(
                                    plantillaSeleccionada
                            )) {

                                plantillaSeleccionada =
                                        null;
                            }

                            cargarPlantillas();

                        } catch (Exception e) {

                            mostrarMensaje(
                                    Alert.AlertType.ERROR,
                                    "Error",
                                    e.getMessage()
                            );
                        }
                    }
                });
    }

    // =====================================================
    // ABRIR CARPETA
    // =====================================================

    private void abrirCarpeta() {

        File carpeta =
                servicio.getCarpetaPlantillas();

        try {

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop()
                        .open(carpeta);

            } else {

                mostrarMensaje(
                        Alert.AlertType.WARNING,
                        "Abrir carpeta",
                        "El sistema no permite abrir el Explorador automáticamente."
                );
            }

        } catch (Exception e) {

            mostrarMensaje(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo abrir la carpeta."
            );
        }
    }

    // =====================================================
    // MENSAJE
    // =====================================================

    private void mostrarMensaje(
            Alert.AlertType tipo,
            String titulo,
            String mensaje
    ) {

        Alert alert =
                new Alert(tipo);

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

    // =====================================================
    // OBTENER PLANTILLA SELECCIONADA
    // =====================================================

    public File getPlantillaSeleccionada() {

        return plantillaSeleccionada;
    }
}