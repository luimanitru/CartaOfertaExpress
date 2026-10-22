package com.cartaofertaexpress.ui;
import com.cartaofertaexpress.service.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.concurrent.Task;

import javafx.geometry.Rectangle2D;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;
import com.cartaofertaexpress.model.Candidato;
import com.cartaofertaexpress.repository.CandidatoRepository;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

import java.util.concurrent.atomic.AtomicBoolean;

import java.awt.Desktop;
import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MainView {

    private final CandidatoRepository repository;

    private final ConfiguracionService configuracionService;

    private int tamanoFuenteGeneral;
    private int tamanoFuenteTabla;
    private boolean filasAlternadas;
    private List<String> tiposContrato;

    private final TableView<Candidato> tablaCandidatos;

    private TableColumn<Candidato, String> columnaTipoContrato;

    private File plantillaSeleccionada;

    private final TextField campoBusqueda;

    private Stage stage;

    private BorderPane root;

    private Scene scene;


    public MainView() {

        repository =
                new CandidatoRepository();

        tablaCandidatos =
                new TableView<>();

        campoBusqueda =
                new TextField();

        configuracionService =
                new ConfiguracionService();
    }

    public List<Candidato> obtenerCandidatosSeleccionados(){

        return tablaCandidatos
                .getSelectionModel()
                .getSelectedItems();
    }

    public void cargarConfiguracionInicial() {

        tamanoFuenteGeneral =
                configuracionService.obtenerTamanoFuente();

        tamanoFuenteTabla =
                configuracionService.obtenerTamanoFuenteTabla();

        tiposContrato =
                configuracionService.obtenerTiposContrato();

        filasAlternadas =
                configuracionService.obtenerFilasAlternadas();

        plantillaSeleccionada =
                configuracionService.obtenerPlantilla();
    }
    public void inicializarInterfaz() {

        cargarConfiguracionInicial();

        configurarTabla();
    }
    private record ResultadoGeneracion(
            int generadas,
            int reemplazadas,
            int procesadas
    ) {
    }
    // ==================================================
    // MOSTRAR VENTANA
    // ==================================================

    public void mostrar(Stage stage) {

        this.stage = stage;

        // ==================================================
        // ICONO DE LA VENTANA
        // ==================================================

        Image logoVentana =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/images/logo.png"
                                )
                );

        stage.getIcons().add(
                logoVentana
        );

        plantillaSeleccionada =
                configuracionService.obtenerPlantilla();

        stage.setTitle(
                "Carta Oferta EXPRESS"
        );

        // ==================================================
        // TÍTULO
        // ==================================================

        Label tituloCarta =
                new Label(
                        "Carta Oferta"
                );

        tituloCarta.setStyle("""
                -fx-font-size: 40px;
                -fx-font-weight: bold;
                -fx-font-family: "Georgia";
                -fx-text-fill: #FFFFFF;
                """);

        Label tituloExpress =
                new Label(
                        " EXPRESS"
                );

        tituloExpress.setStyle("""
                -fx-font-size: 40px;
                -fx-font-weight: bold;
                -fx-font-family: "Georgia";
                -fx-text-fill: #F0CEA4;
                """);

        HBox tituloPrincipal =
                new HBox(
                        0,
                        tituloCarta,
                        tituloExpress
                );

        tituloPrincipal.setAlignment(
                Pos.CENTER
        );

        // ==================================================
        // LOGO AL LADO DEL TÍTULO
        // ==================================================

        Image imagenLogoEncabezado =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/images/logo.png"
                                )
                );

        ImageView logoEncabezado =
                new ImageView(
                        imagenLogoEncabezado
                );

        logoEncabezado.setFitWidth(55);
        logoEncabezado.setFitHeight(55);
        logoEncabezado.setPreserveRatio(true);

        // ==================================================
        // LOGO + TÍTULO
        // ==================================================

        HBox tituloConLogo =
                new HBox(
                        10,
                        logoEncabezado,
                        tituloPrincipal
                );

        tituloConLogo.setAlignment(
                Pos.CENTER
        );

        // ==================================================
        // SUBTÍTULO
        // ==================================================

        Label subtitulo =
                new Label(
                        "Gestión de candidatos y generación de cartas de oferta"
                );

        subtitulo.setStyle("""
                -fx-font-size: 20px;
                -fx-font-weight: normal;
                -fx-text-fill: #FFFFFF;
                """);

        subtitulo.setTranslateY(-8);


        // ==================================================
        // ENCABEZADO
        // ==================================================

        VBox encabezado =
                new VBox(
                        6,
                        tituloConLogo,
                        subtitulo
                );

        encabezado.setAlignment(
                Pos.CENTER
        );

        encabezado.setPadding(
                new Insets(
                        15,
                        20,
                        10,
                        20
                )
        );

        // ==================================================
        // IMAGEN DE FONDO
        // ==================================================

        Image imagenEncabezado =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/images/encabezado.jpeg"
                                )
                );

        BackgroundImage fondoEncabezado =
                new BackgroundImage(
                        imagenEncabezado,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.CENTER,
                        new BackgroundSize(
                                100,
                                100,
                                true,
                                true,
                                false,
                                true
                        )
                );

        encabezado.setBackground(
                new Background(
                        fondoEncabezado
                )
        );

        encabezado.setStyle("""
                -fx-border-color: #E5E7EB;
                -fx-border-width: 0 0 1 0;
                """);

        // ==================================================
        // BARRA SUPERIOR
        // ==================================================

        Label etiquetaBuscar =
                new Label(
                        "Buscar candidato:"
                );

        campoBusqueda.setPromptText(
                "Nombre, cédula o cargo..."
        );

        campoBusqueda.setPrefWidth(
                350
        );

        Button botonBuscar =
                new Button(
                        "Buscar"
                );

        botonBuscar.setOnAction(
                event ->
                        buscarCandidatos()
        );

        campoBusqueda.setOnAction(
                event ->
                        buscarCandidatos()
        );

        HBox buscador =
                new HBox(
                        10,
                        etiquetaBuscar,
                        campoBusqueda,
                        botonBuscar
                );

        buscador.setAlignment(
                Pos.CENTER_LEFT
        );

        // ==================================================
        // PLANTILLA
        // ==================================================

        Label etiquetaPlantilla =
                new Label();

        etiquetaPlantilla.setStyle("""
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                -fx-text-fill: #374151;
                """);

        Button botonCambiarPlantilla =
                new Button(
                        "Cambiar"
                );

        botonCambiarPlantilla.setOnAction(
                event -> {

                    PlantillasView plantillas =
                            new PlantillasView();

                    plantillas.mostrar(stage);

                    File seleccionada =
                            plantillas.getPlantillaSeleccionada();

                    if (seleccionada != null) {

                        plantillaSeleccionada =
                                seleccionada;

                        configuracionService.guardarPlantilla(
                                plantillaSeleccionada
                        );

                        actualizarIndicadorPlantilla(
                                etiquetaPlantilla
                        );
                    }
                }
        );
// ==================================================
// BOTÓN EXPORTAR EXCEL
// ==================================================

        Button botonExportarExcel =
                new Button(
                        "📤"
                );

        botonExportarExcel.setStyle("""
        -fx-background-color: #217346;
        -fx-text-fill: white;
        -fx-font-size: 20px;
        -fx-font-weight: bold;
        -fx-background-radius: 6;
        -fx-border-radius: 6;
        -fx-padding: 5 10 5 10;
        """);

        aplicarEstiloBoton3(
                botonExportarExcel
        );

        botonExportarExcel.setOnAction(
                event ->
                        exportarExcel()
        );
        // ==================================================
        // BOTÓN RESALTAR
        // ==================================================

        Button botonResaltar =
                new Button(
                        "🖍"
                );

        botonResaltar.setStyle("""
                -fx-background-color: #F0C84B;
                -fx-text-fill: white;
                -fx-font-size: 22px;
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 4 10 4 10;
                """);

        aplicarEstiloBoton3(botonResaltar);
        botonResaltar.setOnAction(
                event ->
                        cambiarResaltadoSeleccionados()
        );

        //Boton enviar correo
        Button botonCorreos =
                new Button(
                        "📧"
                );
        botonCorreos.setStyle("""
                -fx-background-color: #275BF5;
                -fx-text-fill: white;
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 5 10 5 10;
                """);
        botonCorreos.setOnAction(
                event -> {

                    CorreosView correos =
                            new CorreosView();

                    correos.mostrar(
                            stage,
                            this
                    );
                }
        );
        aplicarEstiloBoton3(botonCorreos);
        // ==================================================
        // BOTÓN CONFIGURACIÓN
        // ==================================================

        Button botonConfiguracion =
                new Button(
                        "⚙"
                );


        botonConfiguracion.setStyle("""
                -fx-background-color: #8B5E3C;
                -fx-text-fill: white;
                -fx-font-size: 20px;
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 5 10 5 10;
                """);

        aplicarEstiloBoton3(botonConfiguracion);

        botonConfiguracion.setOnAction(
                event -> {

                    ConfiguracionView configuracion =
                            new ConfiguracionView();

                    configuracion.mostrar(
                            stage,
                            this
                    );
                }
        );

        // ==================================================
        // INDICADOR PLANTILLA
        // ==================================================

        HBox indicadorPlantilla =
                new HBox(
                        10,
                        etiquetaPlantilla,
                        botonCambiarPlantilla,
                        botonResaltar,
                        botonExportarExcel,
                        // botonCorreos,
                        botonConfiguracion
                );

        indicadorPlantilla.setAlignment(
                Pos.CENTER_RIGHT
        );

        actualizarIndicadorPlantilla(
                etiquetaPlantilla
        );

        // ==================================================
        // BARRA SUPERIOR COMPLETA
        // ==================================================

        Region espacioSuperior =
                new Region();

        HBox.setHgrow(
                espacioSuperior,
                Priority.ALWAYS
        );

        HBox barraSuperior =
                new HBox(
                        10,
                        buscador,
                        espacioSuperior,
                        indicadorPlantilla
                );

        barraSuperior.setAlignment(
                Pos.CENTER_LEFT
        );

        barraSuperior.setPadding(
                new Insets(
                        10,
                        20,
                        10,
                        20
                )
        );

        barraSuperior.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        // ==================================================
        // TABLA
        // ==================================================

        VBox contenedorTabla =
                new VBox(
                        tablaCandidatos
                );

        contenedorTabla.setPadding(
                new Insets(
                        0,
                        20,
                        0,
                        20
                )
        );

        VBox.setVgrow(
                tablaCandidatos,
                Priority.ALWAYS
        );

        // ==================================================
        // BOTONES
        // ==================================================

        Button botonNuevo =
                new Button(
                        "+ Nuevo candidato"
                );

        Button botonEliminar =
                new Button(
                        "Eliminar"
                );

        Button botonImportar =
                new Button(
                        "Importar Excel"
                );

        Button botonPlantillas =
                new Button(
                        "Plantillas"
                );

        Button botonGenerar =
                new Button(
                        "Generar cartas"
                );

        Button botonAbrirCarta =
                new Button(
                        "Abrir carta"
                );

        Button botonEliminarCarta =
                new Button(
                        "Eliminar carta"
                );

        Button botonSeleccionarTodos =
                new Button(
                        "Seleccionar todos"
                );

        Button botonDeseleccionarTodos =
                new Button(
                        "Deseleccionar"
                );

        aplicarEstiloBoton(
                botonEliminar
        );

        aplicarEstiloBoton(
                botonSeleccionarTodos
        );

        aplicarEstiloBoton(
                botonDeseleccionarTodos
        );

        aplicarEstiloBoton(
                botonImportar
        );

        aplicarEstiloBoton(
                botonPlantillas
        );

        aplicarEstiloBoton(
                botonAbrirCarta
        );

        aplicarEstiloBoton(
                botonEliminarCarta
        );

        aplicarEstiloBoton(
                botonBuscar
        );

        aplicarEstiloBoton(
                botonCambiarPlantilla
        );

        aplicarEstiloBoton2(
                botonNuevo
        );

        aplicarEstiloBoton2(
                botonGenerar
        );

        // ==================================================
        // NUEVO
        // ==================================================

        botonNuevo.setOnAction(
                event -> {

                    CandidatoFormView formulario =
                            new CandidatoFormView();

                    formulario.mostrar(
                            stage,
                            this::cargarCandidatos
                    );
                }
        );

        // ==================================================
        // ELIMINAR
        // ==================================================

        botonEliminar.setOnAction(
                event ->
                        eliminarCandidato()
        );

        // ==================================================
        // IMPORTAR
        // ==================================================

        botonImportar.setOnAction(
                event ->
                        importarExcel()
        );

        // ==================================================
        // PLANTILLAS
        // ==================================================

        botonPlantillas.setOnAction(
                event -> {

                    PlantillasView plantillas =
                            new PlantillasView();

                    plantillas.mostrar(stage);

                    File seleccionada =
                            plantillas.getPlantillaSeleccionada();

                    if (seleccionada != null) {

                        plantillaSeleccionada =
                                seleccionada;

                        configuracionService.guardarPlantilla(
                                plantillaSeleccionada
                        );

                        actualizarIndicadorPlantilla(
                                etiquetaPlantilla
                        );
                    }
                }
        );

        // ==================================================
        // GENERAR CARTAS
        // ==================================================

        botonGenerar.setOnAction(
                event ->
                        generarCartas()
        );

        // ==================================================
        // ABRIR CARTA
        // ==================================================

        botonAbrirCarta.setOnAction(
                event ->
                        abrirCarta()
        );

        // ==================================================
        // ELIMINAR CARTA
        // ==================================================

        botonEliminarCarta.setOnAction(
                event ->
                        eliminarCarta()
        );

        // ==================================================
        // SELECCIONAR TODOS
        // ==================================================

        botonSeleccionarTodos.setOnAction(
                event ->
                        tablaCandidatos
                                .getSelectionModel()
                                .selectAll()
        );

        // ==================================================
        // DESELECCIONAR
        // ==================================================

        botonDeseleccionarTodos.setOnAction(
                event ->
                        tablaCandidatos
                                .getSelectionModel()
                                .clearSelection()
        );

        // ==================================================
        // BARRA DE BOTONES
        // ==================================================

        Region espacioIzquierdo =
                new Region();

        HBox.setHgrow(
                espacioIzquierdo,
                Priority.ALWAYS
        );

        Region espacioDerecho =
                new Region();

        HBox.setHgrow(
                espacioDerecho,
                Priority.ALWAYS
        );

        HBox barraBotones =
                new HBox(
                        10,
                        botonNuevo,
                        botonEliminar,
                        botonSeleccionarTodos,
                        botonDeseleccionarTodos,
                        espacioIzquierdo,
                        espacioDerecho,
                        botonImportar,
                        botonPlantillas,
                        botonAbrirCarta,
                        botonEliminarCarta,
                        botonGenerar
                );

        barraBotones.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        barraBotones.setAlignment(
                Pos.CENTER_LEFT
        );

        barraBotones.setPadding(
                new Insets(
                        10,
                        20,
                        10,
                        20
                )
        );

        // ==================================================
        // CONTENEDOR PRINCIPAL
        // ==================================================

        root =
                new BorderPane();

        root.setTop(
                encabezado
        );

        VBox centro =
                new VBox(
                        barraSuperior,
                        contenedorTabla
                );

        centro.setStyle(
                "-fx-background-color: #F8FAFC;"
        );

        VBox.setVgrow(
                contenedorTabla,
                Priority.ALWAYS
        );

        root.setCenter(
                centro
        );

        root.setBottom(
                barraBotones
        );

        // ==================================================
        // CARGAR CANDIDATOS
        // ==================================================

        cargarCandidatos();

        // ==================================================
        // ESCENA
        // ==================================================

        Rectangle2D pantalla =
                Screen.getPrimary()
                        .getVisualBounds();

        double ancho =
                pantalla.getWidth() * 0.95;

        double alto =
                pantalla.getHeight() * 0.95;

        scene =
                new Scene(
                        root,
                        ancho,
                        alto
                );

        scene.getRoot().setStyle(
                "-fx-font-size: "
                        + tamanoFuenteGeneral
                        + "px;"
        );

        stage.setScene(
                scene
        );

        stage.show();
    }

    // ==================================================
    // CONFIGURAR TABLA
    // ==================================================

    private void configurarTabla() {

        // ==================================================
        // NÚMERO
        // ==================================================

        TableColumn<Candidato, Number> numero =
                new TableColumn<>(
                        "N."
                );

        numero.setCellFactory(
                columna -> new TableCell<>() {

                    @Override
                    protected void updateItem(
                            Number item,
                            boolean empty
                    ) {

                        super.updateItem(
                                item,
                                empty
                        );

                        if (empty) {

                            setText(null);

                        } else {

                            setText(
                                    String.valueOf(
                                            getIndex() + 1
                                    )
                            );
                        }
                    }
                }
        );

        numero.setPrefWidth(50);
        numero.setResizable(false);

        numero.setStyle(
                "-fx-alignment: CENTER;"
        );

        tablaCandidatos
                .getSelectionModel()
                .setSelectionMode(
                        SelectionMode.MULTIPLE
                );

        tablaCandidatos.setEditable(true);

        tablaCandidatos.setStyle(
                "-fx-font-size: "
                        + tamanoFuenteTabla
                        + "px;"
        );

        aplicarEstiloFilas();

        // ==================================================
        // NOMBRE
        // ==================================================

        TableColumn<Candidato, String> nombre =
                new TableColumn<>(
                        "Nombre"
                );

        nombre.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getNombre()
                                )
                        )
        );

        nombre.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        nombre.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoNombre =
                            event.getNewValue().trim();

                    if (nuevoNombre.isBlank()) {

                        mostrarMensaje(
                                "Nombre inválido",
                                "El nombre no puede estar vacío."
                        );

                        tablaCandidatos.refresh();

                        return;
                    }

                    candidato.setNombre(
                            ValidadorCandidato.normalizarNombre(
                                    nuevoNombre
                            )
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );

        // ==================================================
        // CÉDULA
        // ==================================================

        TableColumn<Candidato, String> cedula =
                new TableColumn<>(
                        "Cédula"
                );

        cedula.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getCedula()
                                )
                        )
        );

        cedula.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        cedula.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevaCedula =
                            event.getNewValue() == null
                                    ? ""
                                    : event.getNewValue().trim();

                    if (nuevaCedula.isBlank()) {

                        mostrarMensaje(
                                "Cédula inválida",
                                "La cédula no puede estar vacía."
                        );

                        tablaCandidatos.refresh();

                        return;
                    }

                    if (!nuevaCedula.matches("[0-9.-]+")) {

                        mostrarMensaje(
                                "Cédula inválida",
                                "La cédula solamente puede contener números, guiones (-) y puntos (.)."
                        );

                        tablaCandidatos.refresh();

                        return;
                    }

                    Candidato existente =
                            repository.buscarPorCedula(
                                    nuevaCedula
                            );

                    if (existente != null
                            && existente.getId()
                            != candidato.getId()) {

                        mostrarMensaje(
                                "Cédula duplicada",
                                "Ya existe otro candidato registrado "
                                        + "con la cédula:\n\n"
                                        + nuevaCedula
                        );

                        tablaCandidatos.refresh();

                        return;
                    }

                    candidato.setCedula(
                            nuevaCedula
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );

        // ==================================================
        // CARGO
        // ==================================================

        TableColumn<Candidato, String> cargo =
                new TableColumn<>(
                        "Cargo"
                );

        cargo.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getCargo()
                                )
                        )
        );

        cargo.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );


        cargo.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoCargo =
                            event.getNewValue() == null
                                    ? ""
                                    : event.getNewValue().trim();

                    candidato.setCargo(
                            nuevoCargo
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );


        // ==================================================
        // SALARIO
        // ==================================================

        TableColumn<Candidato, String> salario =
                new TableColumn<>(
                        "Salario"
                );

        salario.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                data.getValue().getSalario() == null
                                        ? ""
                                        : String.format(
                                        "%,.0f",
                                        data.getValue().getSalario()
                                ).replace(
                                        ",",
                                        "."
                                )
                        )
        );

        salario.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        salario.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String texto =
                            event.getNewValue()
                                    .trim();

                    if (texto.isBlank()) {

                        candidato.setSalario(null);

                        actualizarCandidato(
                                candidato
                        );

                        return;
                    }

                    try {

                        String valor =
                                texto
                                        .replace("$", "")
                                        .replace(" ", "")
                                        .replace(".", "")
                                        .replace(",", ".");

                        BigDecimal salarioNuevo =
                                new BigDecimal(valor);

                        if (salarioNuevo.compareTo(
                                BigDecimal.ZERO
                        ) < 0) {

                            throw new NumberFormatException();
                        }

                        candidato.setSalario(
                                salarioNuevo
                        );

                        actualizarCandidato(
                                candidato
                        );

                    } catch (Exception e) {

                        mostrarMensaje(
                                "Salario inválido",
                                "Ingresa solamente un valor numérico válido.\n\n"
                                        + "Ejemplo: 3500000"
                        );

                        tablaCandidatos.refresh();
                    }
                }
        );

        // ==================================================
        // ROL
        // ==================================================

        TableColumn<Candidato, String> rol =
                new TableColumn<>(
                        "Rol"
                );

        rol.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getRol()
                                )
                        )
        );

        rol.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        rol.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoRol =
                            event.getNewValue() == null
                                    ? ""
                                    : event.getNewValue().trim();

                    candidato.setRol(
                            nuevoRol
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );

        // ==================================================
        // TIPO DE CONTRATO
        // ==================================================

        columnaTipoContrato =
                new TableColumn<>(
                        "Tipo contrato"
                );

        columnaTipoContrato.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getTipoContrato()
                                )
                        )
        );

        columnaTipoContrato.setCellFactory(
                ComboBoxTableCell.forTableColumn(
                        tiposContrato.toArray(
                                new String[0]
                        )
                )
        );

        columnaTipoContrato.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoTipo =
                            event.getNewValue();

                    if (nuevoTipo == null
                            || nuevoTipo.isBlank()) {

                        tablaCandidatos.refresh();

                        return;
                    }

                    candidato.setTipoContrato(
                            nuevoTipo
                    );

                    actualizarCandidato(
                            candidato
                    );
                }

        );

        // ==================================================
        // FECHA DE INGRESO
        // ==================================================

        TableColumn<Candidato, String> fechaIngreso =
                new TableColumn<>(
                        "Fecha ingreso"
                );

        fechaIngreso.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getFechaIngreso()
                                )
                        )
        );

        fechaIngreso.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        fechaIngreso.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevaFecha =
                            event.getNewValue()
                                    .trim();

                    if (nuevaFecha.isBlank()) {

                        candidato.setFechaIngreso(
                                ""
                        );

                        actualizarCandidato(
                                candidato
                        );

                        return;
                    }

                    try {

                        LocalDate fecha =
                                LocalDate.parse(
                                        nuevaFecha,
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"
                                        )
                                );

                        candidato.setFechaIngreso(
                                fecha.format(
                                        DateTimeFormatter.ofPattern(
                                                "dd/MM/yyyy"
                                        )
                                )
                        );

                        actualizarCandidato(
                                candidato
                        );

                    } catch (Exception e) {

                        mostrarMensaje(
                                "Fecha inválida",
                                "La fecha debe tener el formato:\n\n"
                                        + "dd/MM/yyyy\n\n"
                                        + "Ejemplo: 25/08/2026"
                        );

                        tablaCandidatos.refresh();
                    }
                }
        );

        // ==================================================
        // CIUDAD
        // ==================================================

        TableColumn<Candidato, String> ciudad =
                new TableColumn<>(
                        "Ciudad"
                );

        ciudad.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getCiudad()
                                )
                        )
        );

        ciudad.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );


        ciudad.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevaCiudad =
                            event.getNewValue() == null
                                    ? ""
                                    : event.getNewValue().trim();

                    candidato.setCiudad(
                            nuevaCiudad
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );


        // ==================================================
        // CELULAR
        // ==================================================

        TableColumn<Candidato, String> celular =
                new TableColumn<>(
                        "Celular"
                );

        celular.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getCelular()
                                )
                        )
        );

        celular.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        celular.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoCelular =
                            event.getNewValue()
                                    .trim();

                    try {

                        String celularNormalizado =
                                ValidadorCandidato.normalizarCelular(
                                        nuevoCelular
                                );

                        candidato.setCelular(
                                celularNormalizado
                        );

                        actualizarCandidato(
                                candidato
                        );

                    } catch (IllegalArgumentException e) {

                        mostrarMensaje(
                                "Celular inválido",
                                "El celular ingresado no tiene un formato válido."
                        );

                        tablaCandidatos.refresh();
                    }
                }
        );

        // ==================================================
        // CORREO
        // ==================================================

        TableColumn<Candidato, String> correo =
                new TableColumn<>(
                        "Correo"
                );

        correo.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleStringProperty(
                                textoSeguro(
                                        data.getValue()
                                                .getCorreo()
                                )
                        )
        );

        correo.setCellFactory(
                TextFieldTableCell.forTableColumn()
        );

        correo.setOnEditCommit(
                event -> {

                    Candidato candidato =
                            event.getRowValue();

                    String nuevoCorreo =
                            event.getNewValue()
                                    .trim()
                                    .toLowerCase();

                    if (!nuevoCorreo.isBlank()
                            && !nuevoCorreo.matches(
                            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
                    )) {

                        mostrarMensaje(
                                "Correo inválido",
                                "El correo ingresado no tiene un formato válido."
                        );

                        tablaCandidatos.refresh();

                        return;
                    }

                    candidato.setCorreo(
                            nuevoCorreo
                    );

                    actualizarCandidato(
                            candidato
                    );
                }
        );

        // ==================================================
        // CARTA
        // ==================================================

        TableColumn<Candidato, Boolean> carta =
                new TableColumn<>(
                        "Carta"
                );

        carta.setCellValueFactory(
                data ->
                        new javafx.beans.property
                                .SimpleBooleanProperty(
                                data.getValue()
                                        .isCartaGenerada()
                        ).asObject()
        );

        carta.setCellFactory(
                column ->
                        new TableCell<>() {

                            private final javafx.scene.shape.Circle circulo =
                                    new javafx.scene.shape.Circle(6);

                            {
                                circulo.setStroke(
                                        javafx.scene.paint.Color.GRAY
                                );
                            }

                            @Override
                            protected void updateItem(
                                    Boolean generada,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        generada,
                                        empty
                                );

                                if (empty
                                        || generada == null) {

                                    setGraphic(null);
                                    setText(null);
                                    setContextMenu(null);

                                    return;
                                }

                                if (generada) {

                                    circulo.setFill(
                                            javafx.scene.paint.Color.GREEN
                                    );

                                    setText(
                                            "Generada"
                                    );

                                } else {

                                    circulo.setFill(
                                            javafx.scene.paint.Color.LIGHTGRAY
                                    );

                                    setText(
                                            "No generada"
                                    );
                                }

                                setGraphic(
                                        circulo
                                );

                                // No menú de copiar en Carta.
                                setContextMenu(null);
                            }
                        }
        );

        // ==================================================
        // ENVIADO
        // ==================================================

        TableColumn<Candidato, String> columnaEnviado =
                new TableColumn<>(
                        "Enviado"
                );

        columnaEnviado.setCellValueFactory(
                celda -> {

                    String fechaCorreo =
                            celda.getValue()
                                    .getFechaCorreo();

                    boolean enviado =
                            fechaCorreo != null
                                    && !fechaCorreo.isBlank();

                    return new SimpleStringProperty(
                            enviado
                                    ? "✓ Sí"
                                    : "✗ No"
                    );
                }
        );

        columnaEnviado.setCellFactory(
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

                                    setText(null);
                                    setContextMenu(null);

                                    return;
                                }

                                setText(item);

                                setAlignment(
                                        Pos.CENTER
                                );

                                // ==================================================
                                // MENÚ PARA CAMBIAR ESTADO
                                // ==================================================

                                ContextMenu menu =
                                        new ContextMenu();

                                boolean enviado =
                                        "✓ Sí".equals(item);

                                MenuItem opcionCambiar =
                                        new MenuItem(
                                                enviado
                                                        ? "Marcar como no enviado"
                                                        : "Marcar como enviado"
                                        );

                                opcionCambiar.setOnAction(
                                        evento -> {

                                            Candidato candidato =
                                                    getTableView()
                                                            .getItems()
                                                            .get(getIndex());

                                            if (candidato == null) {

                                                return;
                                            }

                                            String mensaje =
                                                    enviado
                                                            ? "¿Confirmas que deseas marcar el correo de "
                                                            + candidato.getNombre()
                                                            + " como NO enviado?"
                                                            : "¿Confirmas que deseas marcar el correo de "
                                                            + candidato.getNombre()
                                                            + " como enviado?";

                                            Alert confirmacion =
                                                    new Alert(
                                                            Alert.AlertType.CONFIRMATION
                                                    );

                                            confirmacion.setTitle(
                                                    "Confirmar cambio"
                                            );

                                            confirmacion.setHeaderText(
                                                    null
                                            );

                                            confirmacion.setContentText(
                                                    mensaje
                                            );

                                            ButtonType resultado =
                                                    confirmacion.showAndWait()
                                                            .orElse(
                                                                    ButtonType.CANCEL
                                                            );

                                            if (resultado
                                                    != ButtonType.OK) {

                                                return;
                                            }

                                            try {

                                                boolean nuevoEstado =
                                                        !enviado;

                                                repository.cambiarEstadoCorreo(
                                                        candidato.getId(),
                                                        nuevoEstado
                                                );

                                                if (nuevoEstado) {

                                                    candidato.setFechaCorreo(
                                                            LocalDateTime.now()
                                                                    .format(
                                                                            DateTimeFormatter.ofPattern(
                                                                                    "dd/MM/yyyy HH:mm:ss"
                                                                            )
                                                                    )
                                                    );

                                                } else {

                                                    candidato.setFechaCorreo(
                                                            null
                                                    );
                                                }

                                                getTableView().refresh();

                                            } catch (Exception e) {

                                                mostrarMensaje(
                                                        "Error",
                                                        "No se pudo cambiar el estado "
                                                                + "del correo.\n\n"
                                                                + e.getMessage()
                                                );
                                            }
                                        }
                                );

                                menu.getItems().add(
                                        opcionCambiar
                                );

                                setContextMenu(
                                        menu
                                );
                            }
                        }
        );

        columnaEnviado.setPrefWidth(
                100
        );


        // ==================================================
        // AGREGAR COLUMNAS
        // ==================================================

        tablaCandidatos.getColumns()
                .addAll(
                        numero,
                        nombre,
                        cedula,
                        cargo,
                        salario,
                        rol,
                        columnaTipoContrato,
                        fechaIngreso,
                        ciudad,
                        celular,
                        correo,
                        carta,
                        columnaEnviado
                );

        // ==================================================
        // ANCHOS
        // ==================================================

        nombre.setPrefWidth(200);
        cedula.setPrefWidth(105);
        cargo.setPrefWidth(180);
        salario.setPrefWidth(100);
        rol.setPrefWidth(100);
        columnaTipoContrato.setPrefWidth(130);
        fechaIngreso.setPrefWidth(110);
        ciudad.setPrefWidth(110);
        celular.setPrefWidth(115);
        correo.setPrefWidth(220);
        carta.setPrefWidth(120);

        tablaCandidatos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // ==================================================
        // BLOQUEAR MOVIMIENTO DE COLUMNAS
        // ==================================================

        for (TableColumn<Candidato, ?> columna :
                tablaCandidatos.getColumns()) {

            columna.setReorderable(false);
        }


    }

    // =====================================================
// OBTENER TEXTO DE LA CELDA
// =====================================================

    private String obtenerTextoCelda(
            Candidato candidato,
            int columna
    ) {

        switch (columna) {

            case 1:
                return candidato.getNombre();

            case 2:
                return candidato.getCedula();

            case 3:
                return candidato.getCargo();

            case 4:
                return candidato.getSalario() == null
                        ? ""
                        : String.format(
                        "%,.0f",
                        candidato.getSalario()
                ).replace(
                        ",",
                        "."
                );

            case 5:
                return candidato.getRol();

            case 6:
                return candidato.getTipoContrato();

            case 7:
                return candidato.getFechaIngreso();

            case 8:
                return candidato.getCiudad();

            case 9:
                return candidato.getCelular();

            case 10:
                return candidato.getCorreo();

            default:
                return null;
        }
    }

    // ==================================================
// MENÚ COPIAR PARA CELDAS
// ==================================================

    private void agregarMenuCopiar(
            TableColumn<Candidato, String> columna
    ) {

        columna.setCellFactory(
                tablaColumna ->
                        new TableCell<Candidato, String>() {

                            @Override
                            protected void updateItem(
                                    String item,
                                    boolean empty
                            ) {

                                super.updateItem(
                                        item,
                                        empty
                                );

                                // IMPORTANTE:
                                // Cada vez que JavaFX reutiliza esta celda,
                                // eliminamos cualquier menú anterior.
                                setContextMenu(null);

                                if (empty) {

                                    setText(null);

                                    return;
                                }

                                setText(
                                        item == null
                                                ? ""
                                                : item
                                );

                                // ==================================================
                                // CREAR MENÚ
                                // ==================================================

                                ContextMenu menu =
                                        new ContextMenu();

                                MenuItem copiar =
                                        new MenuItem(
                                                "Copiar"
                                        );

                                copiar.setOnAction(
                                        event -> {

                                            int filaIndex =
                                                    getIndex();

                                            if (filaIndex < 0) {
                                                return;
                                            }

                                            Candidato candidato =
                                                    getTableView()
                                                            .getItems()
                                                            .get(filaIndex);

                                            if (candidato == null) {
                                                return;
                                            }

                                            int indiceColumna =
                                                    getTableView()
                                                            .getColumns()
                                                            .indexOf(
                                                                    getTableColumn()
                                                            );

                                            if (indiceColumna < 1
                                                    || indiceColumna > 10) {

                                                return;
                                            }

                                            String texto =
                                                    obtenerTextoCelda(
                                                            candidato,
                                                            indiceColumna
                                                    );

                                            if (texto == null) {
                                                texto = "";
                                            }

                                            Clipboard clipboard =
                                                    Clipboard.getSystemClipboard();

                                            ClipboardContent contenido =
                                                    new ClipboardContent();

                                            contenido.putString(
                                                    texto
                                            );

                                            clipboard.setContent(
                                                    contenido
                                            );

                                            // Cerramos inmediatamente el menú
                                            menu.hide();
                                        }
                                );

                                menu.getItems().add(
                                        copiar
                                );

                                // ==================================================
                                // CLIC DERECHO
                                // ==================================================

                                setOnContextMenuRequested(
                                        event -> {

                                            menu.show(
                                                    this,
                                                    event.getScreenX(),
                                                    event.getScreenY()
                                            );

                                            event.consume();
                                        }
                                );
                            }
                        }
        );
    }

    // ==================================================
    // CAMBIAR RESALTADO DE SELECCIONADOS
    // ==================================================

    private void cambiarResaltadoSeleccionados() {

        ObservableList<Candidato> seleccionados =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItems();

        if (seleccionados.isEmpty()) {

            mostrarMensaje(
                    "Resaltar candidato",
                    "Selecciona una o más filas primero."
            );

            return;
        }

        /*
         * Si todas las filas seleccionadas están resaltadas,
         * quitamos el resaltado de todas.
         *
         * Si al menos una no está resaltada,
         * resaltamos todas.
         */
        boolean todasResaltadas = true;

        for (Candidato candidato :
                seleccionados) {

            if (!candidato.isResaltado()) {

                todasResaltadas = false;

                break;
            }
        }

        boolean nuevoEstado =
                !todasResaltadas;

        try {

            for (Candidato candidato :
                    seleccionados) {

                candidato.setResaltado(
                        nuevoEstado
                );

                repository.cambiarResaltado(
                        candidato.getId(),
                        nuevoEstado
                );
            }

            tablaCandidatos.refresh();
            System.out.println(">>> PUNTO 1");

        } catch (Exception e) {

            mostrarError(
                    "Error cambiando resaltado",
                    e.getMessage()
            );

            cargarCandidatos();
        }
    }

    // ==================================================
    // INDICADOR DE PLANTILLA
    // ==================================================

    private void actualizarIndicadorPlantilla(
            Label etiquetaPlantilla
    ) {

        if (plantillaSeleccionada == null) {

            etiquetaPlantilla.setText(
                    "❗ No hay una plantilla seleccionada."
            );

            etiquetaPlantilla.setStyle("""
                    -fx-text-fill: #991B1B;
                    -fx-font-weight: bold;
                    """);

            return;
        }

        etiquetaPlantilla.setText(
                "📄 Plantilla activa: "
                        + plantillaSeleccionada.getName()
        );

        etiquetaPlantilla.setStyle("""
                -fx-text-fill: #374151;
                -fx-font-weight: bold;
                -fx-font-size: 14px;
                """);
    }

    // ==================================================
    // CARGAR CANDIDATOS
    // ==================================================

    private void cargarCandidatos() {

        List<Candidato> candidatos =
                repository.buscarTodos();

        tablaCandidatos.getItems()
                .setAll(
                        candidatos
                );

        tablaCandidatos.refresh();
    }

    // ==================================================
    // BUSCAR
    // ==================================================

    private void buscarCandidatos() {

        String texto =
                campoBusqueda
                        .getText()
                        .trim()
                        .toLowerCase();

        if (texto.isEmpty()) {

            cargarCandidatos();

            return;
        }

        List<Candidato> candidatos =
                repository.buscarTodos()
                        .stream()
                        .filter(candidato -> {

                            String nombre =
                                    textoSeguro(
                                            candidato.getNombre()
                                    ).toLowerCase();

                            String cedula =
                                    textoSeguro(
                                            candidato.getCedula()
                                    ).toLowerCase();

                            String cargo =
                                    textoSeguro(
                                            candidato.getCargo()
                                    ).toLowerCase();

                            return nombre.contains(texto)
                                    || cedula.contains(texto)
                                    || cargo.contains(texto);
                        })
                        .toList();

        tablaCandidatos.getItems()
                .setAll(
                        candidatos
                );

        tablaCandidatos.refresh();
    }

    // ==================================================
    // EDITAR CANDIDATO
    // ==================================================

    private void editarCandidato() {

        Candidato seleccionado =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItem();

        if (seleccionado == null) {

            mostrarMensaje(
                    "Editar candidato",
                    "Selecciona primero un candidato."
            );

            return;
        }

        CandidatoFormView formulario =
                new CandidatoFormView();

        formulario.mostrar(
                stage,
                seleccionado,
                this::cargarCandidatos
        );
    }

    // ==================================================
    // ELIMINAR CANDIDATO
    // ==================================================

    private void eliminarCandidato() {

        List<Candidato> seleccionados =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItems();

        if (seleccionados.isEmpty()) {

            mostrarMensaje(
                    "Eliminar candidatos",
                    "Selecciona al menos un candidato."
            );

            return;
        }

        String mensaje;

        if (seleccionados.size() == 1) {

            Candidato candidato =
                    seleccionados.get(0);

            boolean tieneCarta =
                    candidato.isCartaGenerada()
                            && candidato.getArchivoCarta() != null
                            && !candidato.getArchivoCarta().isBlank();

            mensaje =
                    "¿Deseas eliminar a:\n\n"
                            + candidato.getNombre()
                            + "\n"
                            + candidato.getCedula()
                            + "?\n\n";

            if (tieneCarta) {

                mensaje +=
                        "También se eliminarán los archivos "
                                + "PowerPoint y PDF de su carta.";

            } else {

                mensaje +=
                        "Este candidato no tiene una carta asociada.";
            }

        } else {

            int candidatosConCarta = 0;

            for (Candidato candidato :
                    seleccionados) {

                boolean tieneCarta =
                        candidato.isCartaGenerada()
                                && candidato.getArchivoCarta() != null
                                && !candidato.getArchivoCarta().isBlank();

                if (tieneCarta) {

                    candidatosConCarta++;
                }
            }

            mensaje =
                    "¿Deseas eliminar los "
                            + seleccionados.size()
                            + " candidatos seleccionados?\n\n";

            if (candidatosConCarta == 0) {

                mensaje +=
                        "Ninguno de los candidatos tiene "
                                + "una carta asociada.";

            } else if (candidatosConCarta == 1) {

                mensaje +=
                        "1 candidato tiene una carta asociada "
                                + "cuyos archivos PowerPoint y PDF "
                                + "también serán eliminados.";

            } else {

                mensaje +=
                        candidatosConCarta
                                + " candidatos tienen cartas asociadas "
                                + "cuyos archivos PowerPoint y PDF "
                                + "también serán eliminados.";
            }
        }

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar candidatos"
        );

        confirmacion.setHeaderText(
                null
        );

        confirmacion.setContentText(
                mensaje
        );

        var respuesta =
                confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {

            return;
        }

        try {

            for (Candidato candidato :
                    seleccionados) {

                if (candidato.getArchivoCarta() != null
                        && !candidato.getArchivoCarta().isBlank()) {

                    File archivoPpt =
                            new File(
                                    candidato.getArchivoCarta()
                            );

                    // ==================================================
                    // OBTENER PDF ASOCIADO
                    // ==================================================

                    File archivoPdf =
                            new File(
                                    archivoPpt.getAbsolutePath()
                                            .replaceFirst(
                                                    "(?i)\\.pptx$",
                                                    ".pdf"
                                            )
                            );

                    // ==================================================
                    // ELIMINAR PPT
                    // ==================================================

                    if (archivoPpt.exists()) {

                        if (!archivoPpt.delete()) {

                            throw new RuntimeException(
                                    "No se pudo eliminar el PowerPoint de:\n"
                                            + candidato.getNombre()
                            );
                        }
                    }

                    // ==================================================
                    // ELIMINAR PDF
                    // ==================================================

                    if (archivoPdf.exists()) {

                        if (!archivoPdf.delete()) {

                            throw new RuntimeException(
                                    "No se pudo eliminar el PDF de:\n"
                                            + candidato.getNombre()
                            );
                        }
                    }

                    // ==================================================
                    // ELIMINAR CARPETA DE FECHA SI QUEDÓ VACÍA
                    // ==================================================

                    File carpetaPadre =
                            archivoPpt.getParentFile();

                    if (carpetaPadre != null
                            && carpetaPadre.isDirectory()) {

                        File[] archivosRestantes =
                                carpetaPadre.listFiles();

                        if (archivosRestantes != null
                                && archivosRestantes.length == 0) {

                            if (!carpetaPadre.delete()) {

                                mostrarMensaje(
                                        "Carpeta vacía",
                                        "Quedó la carpeta \""
                                                + carpetaPadre.getName()
                                                + "\" vacía pero no se pudo eliminar."
                                );
                            }
                        }
                    }
                }

                // ==================================================
                // ELIMINAR CANDIDATO DE LA BASE DE DATOS
                // ==================================================

                repository.eliminar(
                        candidato.getId()
                );
            }

            cargarCandidatos();

            mostrarMensaje(
                    "Eliminación completada",
                    "Los candidatos y sus cartas asociadas "
                            + "fueron eliminados correctamente."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error eliminando candidatos",
                    e.getMessage()
            );
        }
    }

    // ==================================================
    // IMPORTAR EXCEL
    // ==================================================

    private void importarExcel() {

        FileChooser fileChooser =
                new FileChooser();

        fileChooser.setTitle(
                "Seleccionar archivo Excel"
        );

        fileChooser.getExtensionFilters()
                .add(
                        new FileChooser.ExtensionFilter(
                                "Archivos Excel",
                                "*.xlsx"
                        )
                );

        File archivo =
                fileChooser.showOpenDialog(stage);

        if (archivo == null) {

            return;
        }

        try {

            ExcelImportService servicio =
                    new ExcelImportService();

            ExcelImportService.ResultadoImportacion resultado =
                    servicio.importar(archivo);
            actualizarTiposContrato();

            List<Candidato> candidatos =
                    resultado.getCandidatos();

            int candidatosSinCedula =
                    resultado.getCandidatosSinCedula();

            if (candidatosSinCedula > 0) {

                Alert advertencia =
                        new Alert(
                                Alert.AlertType.CONFIRMATION
                        );

                advertencia.setTitle(
                        "Candidatos sin cédula"
                );

                advertencia.setHeaderText(
                        "Se encontraron "
                                + candidatosSinCedula
                                + " candidato(s) sin cédula."
                );

                advertencia.setContentText(
                        "Estos candidatos no serán importados.\n\n"
                                + "Los candidatos que sí tienen cédula "
                                + "pueden ser importados normalmente.\n\n"
                                + "¿Deseas continuar?"
                );

                var respuesta =
                        advertencia.showAndWait();

                if (respuesta.isEmpty()
                        || respuesta.get()
                        != ButtonType.OK) {

                    return;
                }
            }

            int importados = 0;
            int duplicados = 0;

            for (Candidato candidato :
                    candidatos) {

                Candidato existente =
                        repository.buscarPorCedula(
                                candidato.getCedula()
                        );

                if (existente != null) {

                    duplicados++;

                    continue;
                }

                repository.guardar(
                        candidato
                );

                importados++;
            }

            cargarCandidatos();

            StringBuilder mensaje =
                    new StringBuilder();

            mensaje.append(
                    "Importación completada.\n\n"
            );

            mensaje.append(
                    "Candidatos importados: "
            );

            mensaje.append(
                    importados
            );

            if (duplicados > 0) {

                mensaje.append(
                        "\nCandidatos ya existentes: "
                );

                mensaje.append(
                        duplicados
                );
            }

            if (candidatosSinCedula > 0) {

                mensaje.append(
                        "\nCandidatos sin cédula: "
                );

                mensaje.append(
                        candidatosSinCedula
                );
            }

            mostrarMensaje(
                    "Importación completada",
                    mensaje.toString()
            );

        } catch (Exception e) {

            mostrarError(
                    "Error importando Excel",
                    e.getMessage()
            );
        }
    }

    // ==================================================
    // GENERAR CARTAS
    // ==================================================
    // ==================================================
// GENERAR CARTAS
// ==================================================
    private void generarCartas() {

        List<Candidato> seleccionados =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItems();

        if (seleccionados.isEmpty()) {

            mostrarMensaje(
                    "Generar cartas",
                    "Selecciona al menos un candidato."
            );

            return;
        }

        if (plantillaSeleccionada == null) {

            mostrarMensaje(
                    "Generar cartas",
                    "Primero debes seleccionar una plantilla."
            );

            return;
        }

        File plantilla =
                plantillaSeleccionada;

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Generar cartas"
        );

        confirmacion.setHeaderText(
                "¿Generar las cartas seleccionadas?"
        );

        confirmacion.setContentText(
                "Plantilla:\n"
                        + plantilla.getName()
                        + "\n\n"
                        + "Candidatos: "
                        + seleccionados.size()
        );

        var respuesta =
                confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {

            return;
        }

        String fecha =
                LocalDate.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy"
                                )
                        );

        File carpetaSalida =
                new File(
                        "cartas_generadas",
                        fecha
                );

        if (!carpetaSalida.exists()
                && !carpetaSalida.mkdirs()) {

            mostrarError(
                    "Error",
                    "No se pudo crear la carpeta de cartas."
            );

            return;
        }

        PptGeneratorService generador =
                new PptGeneratorService();

        PowerPointPdfService conversorPdf =
                new PowerPointPdfService();

        // ==================================================
        // CONTROL DE CANCELACIÓN
        // ==================================================

        AtomicBoolean cancelarGeneracion =
                new AtomicBoolean(false);

        // ==================================================
        // CONTADOR
        // ==================================================

        AtomicInteger procesadasActuales =
                new AtomicInteger(0);

        // ==================================================
        // TASK
        // ==================================================

        Task<ResultadoGeneracion> task =
                new Task<>() {

                    @Override
                    protected ResultadoGeneracion call()
                            throws Exception {

                        int generadas = 0;
                        int reemplazadas = 0;

                        int total =
                                seleccionados.size();

                        for (Candidato candidato :
                                seleccionados) {

                            // ==================================================
                            // COMPROBAR CANCELACIÓN
                            // ==================================================

                            if (cancelarGeneracion.get()) {
                                break;
                            }

                            updateProgress(
                                    procesadasActuales.get(),
                                    total
                            );

                            updateMessage(
                                    "Generando carta de:\n"
                                            + candidato.getNombre()
                            );

                            // ==================================================
                            // NOMBRE DEL PPT
                            // ==================================================

                            String nombreArchivo =
                                    construirNombreArchivo(
                                            candidato
                                    );

                            File archivoPpt =
                                    new File(
                                            carpetaSalida,
                                            nombreArchivo
                                    );

                            boolean yaExistia =
                                    archivoPpt.exists();

                            try {

                                // ==================================================
                                // GENERAR PPT
                                // ==================================================

                                updateMessage(
                                        "Generando PowerPoint de:\n"
                                                + candidato.getNombre()
                                );

                                generador.generar(
                                        plantilla,
                                        candidato,
                                        archivoPpt
                                );

                                // ==================================================
                                // NOMBRE DEL PDF
                                // ==================================================

                                String nombrePdf =
                                        archivoPpt.getName()
                                                .replaceFirst(
                                                        "(?i)\\.pptx$",
                                                        ".pdf"
                                                );

                                File archivoPdf =
                                        new File(
                                                carpetaSalida,
                                                nombrePdf
                                        );

                                // ==================================================
                                // GENERAR PDF
                                // ==================================================

                                updateMessage(
                                        "Generando PDF de:\n"
                                                + candidato.getNombre()
                                );

                                conversorPdf.convertir(
                                        archivoPpt,
                                        archivoPdf
                                );

                                // ==================================================
                                // REGISTRAR CARTA
                                // ==================================================

                                String fechaCarta =
                                        LocalDate.now()
                                                .format(
                                                        DateTimeFormatter.ofPattern(
                                                                "dd-MM-yyyy"
                                                        )
                                                );

                                repository.registrarCartaGenerada(
                                        candidato.getId(),
                                        archivoPpt.getAbsolutePath(),
                                        fechaCarta,
                                        plantilla.getAbsolutePath()
                                );

                                // ==================================================
                                // ACTUALIZAR CANDIDATO
                                // ==================================================

                                candidato.setCartaGenerada(
                                        true
                                );

                                candidato.setArchivoCarta(
                                        archivoPpt.getAbsolutePath()
                                );

                                candidato.setFechaCarta(
                                        fechaCarta
                                );

                                candidato.setPlantillaCarta(
                                        plantilla.getAbsolutePath()
                                );

                                // ==================================================
                                // CONTAR
                                // ==================================================

                                if (yaExistia) {

                                    reemplazadas++;

                                } else {

                                    generadas++;
                                }

                                int procesadas =
                                        procesadasActuales.incrementAndGet();

                                updateProgress(
                                        procesadas,
                                        total
                                );

                                updateMessage(
                                        "Carta generada:\n"
                                                + candidato.getNombre()
                                                + "\n\n"
                                                + procesadas
                                                + " de "
                                                + total
                                );

                                // ==================================================
                                // COMPROBAR CANCELACIÓN
                                // ==================================================

                                if (cancelarGeneracion.get()) {
                                    break;
                                }

                            } catch (Exception e) {

                                throw new RuntimeException(
                                        "No se pudo completar la carta de:\n"
                                                + candidato.getNombre()
                                                + "\n\n"
                                                + e.getMessage(),
                                        e
                                );
                            }
                        }

                        return new ResultadoGeneracion(
                                generadas,
                                reemplazadas,
                                procesadasActuales.get()
                        );
                    }
                };

        // ==================================================
        // VENTANA DE PROGRESO
        // ==================================================

        Stage ventanaProgreso =
                crearVentanaProgreso(
                        task,
                        seleccionados.size(),
                        cancelarGeneracion
                );

        // ==================================================
        // TERMINÓ CORRECTAMENTE
        // ==================================================

        task.setOnSucceeded(event -> {

            ventanaProgreso.close();
            tablaCandidatos.setDisable(false);

            ResultadoGeneracion resultado =
                    task.getValue();

            tablaCandidatos.refresh();

            if (cancelarGeneracion.get()) {

                mostrarMensaje(
                        "Generación cancelada",
                        "La generación fue cancelada.\n\n"
                                + "Se generaron "
                                + resultado.procesadas()
                                + " de "
                                + seleccionados.size()
                                + " carta(s).\n\n"
                                + "Las cartas que ya fueron generadas "
                                + "se conservarán."
                );

            } else {

                mostrarResultadoGeneracion(
                        resultado,
                        carpetaSalida
                );
            }
        });

        // ==================================================
        // ERROR
        // ==================================================

        task.setOnFailed(event -> {

            ventanaProgreso.close();
            tablaCandidatos.setDisable(false);

            Throwable error =
                    task.getException();

            mostrarError(
                    "Error generando cartas",
                    error != null
                            ? error.getMessage()
                            : "Ocurrió un error desconocido."
            );

            tablaCandidatos.refresh();
        });

        // ==================================================
        // INICIAR
        // ==================================================
        tablaCandidatos.setDisable(true);
        ventanaProgreso.show();

        Thread hilo =
                new Thread(task);

        hilo.setDaemon(true);

        hilo.start();
    }

    private Stage crearVentanaProgreso(
            Task<?> task,
            int total,
            AtomicBoolean cancelarGeneracion
    ) {

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Generando cartas..."
        );

        ventana.setResizable(false);
        ventana.setAlwaysOnTop(true);

        Label mensaje =
                new Label(
                        "Preparando generación..."
                );

        mensaje.setWrapText(true);

        mensaje.setStyle(
                "-fx-font-size: 14px; -fx-font-weight: bold;"
        );
        Label contador =
                new Label(
                        "0 de " + total
                );

        contador.setStyle(
                "-fx-font-size: 14px;"
        );

        javafx.scene.control.ProgressBar progreso =
                new javafx.scene.control.ProgressBar(0);

        progreso.setPrefWidth(400);
        progreso.setPrefHeight(30);

        Button cancelar =
                new Button(
                        "Cancelar"
                );

        cancelar.setOnAction(event -> {

            if (cancelarGeneracion.get()) {
                return;
            }

            cancelarGeneracion.set(true);

            cancelar.setDisable(true);

            mensaje.setText(
                    "Cancelando...\n"
                            + "La carta que está en proceso "
                            + "terminará antes de detenerse."
            );
        });

        task.messageProperty()
                .addListener(
                        (observable, anterior, nuevo) -> {

                            if (nuevo != null
                                    && !cancelarGeneracion.get()) {

                                mensaje.setText(
                                        nuevo
                                );
                            }
                        }
                );

        task.progressProperty()
                .addListener(
                        (observable, anterior, nuevo) -> {

                            if (!cancelarGeneracion.get()) {

                                int procesadas =
                                        (int) Math.round(
                                                nuevo.doubleValue()
                                                        * total
                                        );

                                contador.setText(
                                        procesadas
                                                + " de "
                                                + total
                                );
                            }
                        }
                );

        progreso.progressProperty()
                .bind(
                        task.progressProperty()
                );

        VBox contenido =
                new VBox(
                        5,
                        mensaje,
                        contador,
                        progreso,
                        cancelar
                );

        contenido.setPadding(
                new Insets(5)
        );

        contenido.setAlignment(
                Pos.CENTER
        );

        Scene escena =
                new Scene(
                        contenido,
                        450,
                        200
                );

        ventana.setScene(
                escena
        );

        ventana.setOnCloseRequest(event -> {

            event.consume();

            cancelar.fire();
        });

        return ventana;
    }
    // ==================================================
    // ABRIR CARTA
    // ==================================================

    private void abrirCarta() {

        List<Candidato> seleccionados =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItems();

        if (seleccionados.isEmpty()) {

            mostrarMensaje(
                    "Abrir carta",
                    "Selecciona primero un candidato."
            );

            return;
        }

        if (seleccionados.size() > 1) {

            mostrarMensaje(
                    "Abrir carta",
                    "Selecciona solamente un candidato para abrir su carta."
            );

            return;
        }

        Candidato candidato =
                seleccionados.get(0);

        // ==================================================
        // VERIFICAR QUE TENGA CARTA GENERADA
        // ==================================================

        if (!candidato.isCartaGenerada()
                || candidato.getArchivoCarta() == null
                || candidato.getArchivoCarta().isBlank()) {

            mostrarMensaje(
                    "Abrir carta",
                    "Este candidato todavía no tiene una carta generada."
            );

            return;
        }

        // ==================================================
        // PREGUNTAR QUÉ FORMATO ABRIR
        // ==================================================

        Alert opciones =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        opciones.setTitle(
                "Abrir carta"
        );

        opciones.setHeaderText(
                "¿Qué archivo deseas abrir?"
        );

        opciones.setContentText(
                candidato.getNombre()
        );

        ButtonType botonPowerPoint =
                new ButtonType(
                        "PowerPoint"
                );

        ButtonType botonPDF =
                new ButtonType(
                        "PDF"
                );

        ButtonType botonCancelar =
                new ButtonType(
                        "Cancelar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        opciones.getButtonTypes().setAll(
                botonPowerPoint,
                botonPDF,
                botonCancelar
        );

        ButtonType resultado =
                opciones.showAndWait()
                        .orElse(
                                botonCancelar
                        );

        if (resultado == botonCancelar) {

            return;
        }

        // ==================================================
        // ARCHIVO POWERPOINT
        // ==================================================

        File archivoPPT =
                new File(
                        candidato.getArchivoCarta()
                );

        // ==================================================
        // ARCHIVO PDF
        // ==================================================

        File archivoPDF =
                buscarArchivoPDF(
                        archivoPPT
                );

        File archivoAbrir;

        if (resultado == botonPowerPoint) {

            archivoAbrir =
                    archivoPPT;

        } else {

            archivoAbrir =
                    archivoPDF;
        }

        // ==================================================
        // VERIFICAR EXISTENCIA
        // ==================================================

        if (archivoAbrir == null
                || !archivoAbrir.exists()) {

            String tipo =
                    resultado == botonPowerPoint
                            ? "PowerPoint"
                            : "PDF";

            mostrarError(
                    "Archivo no encontrado",
                    "El archivo " + tipo
                            + " de este candidato no existe.\n\n"
                            + (
                            archivoAbrir == null
                                    ? "No se pudo determinar la ubicación del archivo."
                                    : archivoAbrir.getAbsolutePath()
                    )
            );

            return;
        }

        // ==================================================
        // ABRIR ARCHIVO
        // ==================================================

        try {

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop()
                        .open(archivoAbrir);

            } else {

                mostrarError(
                        "Abrir carta",
                        "El sistema no permite abrir archivos automáticamente."
                );
            }

        } catch (Exception e) {

            mostrarError(
                    "Error abriendo carta",
                    e.getMessage()
            );
        }
    }
    private File buscarArchivoPDF(
            File archivoPPT
    ) {

        String nombre =
                archivoPPT.getName();

        int posicionExtension =
                nombre.lastIndexOf(".");

        if (posicionExtension == -1) {

            return null;
        }

        String nombrePDF =
                nombre.substring(
                        0,
                        posicionExtension
                )
                        + ".pdf";

        return new File(
                archivoPPT.getParent(),
                nombrePDF
        );
    }

    // ==================================================
    // ELIMINAR CARTA
    // ==================================================

    private void eliminarCarta() {

        List<Candidato> seleccionados =
                tablaCandidatos
                        .getSelectionModel()
                        .getSelectedItems();

        if (seleccionados.isEmpty()) {

            mostrarMensaje(
                    "Eliminar carta",
                    "Selecciona al menos un candidato."
            );

            return;
        }

        List<Candidato> conCarta =
                seleccionados.stream()
                        .filter(candidato ->
                                candidato.isCartaGenerada()
                                        && candidato.getArchivoCarta() != null
                                        && !candidato.getArchivoCarta().isBlank()
                        )
                        .toList();

        if (conCarta.isEmpty()) {

            mostrarMensaje(
                    "Eliminar carta",
                    "Ninguno de los candidatos seleccionados tiene una carta generada."
            );

            return;
        }

        String mensaje;

        if (conCarta.size() == 1) {

            mensaje =
                    "¿Deseas eliminar la carta de:\n\n"
                            + conCarta.get(0).getNombre()
                            + "?\n\n"
                            + "Se eliminarán los archivos "
                            + "PowerPoint y PDF.";

        } else {

            mensaje =
                    "¿Deseas eliminar las cartas de los "
                            + conCarta.size()
                            + " candidatos que tienen carta generada?\n\n"
                            + "Se eliminarán los archivos "
                            + "PowerPoint y PDF.\n\n"
                            + "Los candidatos sin carta no serán afectados.";
        }

        Alert confirmacion =
                new Alert(
                        Alert.AlertType.CONFIRMATION
                );

        confirmacion.setTitle(
                "Eliminar cartas"
        );

        confirmacion.setHeaderText(
                null
        );

        confirmacion.setContentText(
                mensaje
        );

        var respuesta =
                confirmacion.showAndWait();

        if (respuesta.isEmpty()
                || respuesta.get() != ButtonType.OK) {

            return;
        }

        try {

            int eliminadas = 0;

            for (Candidato candidato :
                    conCarta) {

                if (candidato.getArchivoCarta() != null
                        && !candidato.getArchivoCarta().isBlank()) {

                    File archivoPpt =
                            new File(
                                    candidato.getArchivoCarta()
                            );

                    // ==================================================
                    // OBTENER PDF ASOCIADO
                    // ==================================================

                    File archivoPdf =
                            new File(
                                    archivoPpt.getAbsolutePath()
                                            .replaceFirst(
                                                    "(?i)\\.pptx$",
                                                    ".pdf"
                                            )
                            );

                    // ==================================================
                    // ELIMINAR PPT
                    // ==================================================

                    if (archivoPpt.exists()) {

                        if (!archivoPpt.delete()) {

                            throw new RuntimeException(
                                    "No se pudo eliminar el PowerPoint de:\n"
                                            + candidato.getNombre()
                            );
                        }
                    }

                    // ==================================================
                    // ELIMINAR PDF
                    // ==================================================

                    if (archivoPdf.exists()) {

                        if (!archivoPdf.delete()) {

                            throw new RuntimeException(
                                    "No se pudo eliminar el PDF de:\n"
                                            + candidato.getNombre()
                            );
                        }
                    }

                    // ==================================================
                    // ELIMINAR CARPETA DE FECHA SI QUEDÓ VACÍA
                    // ==================================================

                    File carpetaPadre =
                            archivoPpt.getParentFile();

                    if (carpetaPadre != null
                            && carpetaPadre.isDirectory()) {

                        File[] archivosRestantes =
                                carpetaPadre.listFiles();

                        if (archivosRestantes != null
                                && archivosRestantes.length == 0) {

                            if (!carpetaPadre.delete()) {

                                mostrarMensaje(
                                        "Carpeta vacía",
                                        "Quedó la carpeta \""
                                                + carpetaPadre.getName()
                                                + "\" vacía pero no se pudo eliminar."
                                );
                            }
                        }
                    }
                }

                // ==================================================
                // ELIMINAR REGISTRO DE CARTA
                // ==================================================

                repository.eliminarRegistroCarta(
                        candidato.getId()
                );

                candidato.setCartaGenerada(
                        false
                );

                candidato.setArchivoCarta(
                        null
                );

                candidato.setFechaCarta(
                        null
                );

                candidato.setPlantillaCarta(
                        null
                );

                eliminadas++;
            }

            tablaCandidatos.refresh();

            mostrarMensaje(
                    "Cartas eliminadas",
                    "Se eliminaron correctamente "
                            + eliminadas
                            + " carta(s)."
            );

        } catch (Exception e) {

            mostrarError(
                    "Error eliminando cartas",
                    e.getMessage()
            );
        }
    }

    // ==================================================
    // NOMBRE DEL ARCHIVO
    // ==================================================

    private String construirNombreArchivo(
            Candidato candidato
    ) {

        String nombre =
                textoSeguro(
                        candidato.getNombre()
                ).trim();

        nombre =
                nombre.replace(
                        "\\",
                        ""
                );

        nombre =
                nombre.replace(
                        "/",
                        ""
                );

        nombre =
                nombre.replace(
                        ":",
                        ""
                );

        nombre =
                nombre.replace(
                        "*",
                        ""
                );

        nombre =
                nombre.replace(
                        "?",
                        ""
                );

        nombre =
                nombre.replace(
                        "\"",
                        ""
                );

        nombre =
                nombre.replace(
                        "<",
                        ""
                );

        nombre =
                nombre.replace(
                        ">",
                        ""
                );

        nombre =
                nombre.replace(
                        "|",
                        ""
                );

        if (nombre.isBlank()) {

            nombre = "Sin_Nombre";
        }

        nombre =
                nombre.replaceAll(
                        "\\s+",
                        "_"
                );

        return "Carta_Oferta_"
                + nombre
                + ".pptx";
    }

    // ==================================================
    // ABRIR CARPETA
    // ==================================================

    private void abrirCarpeta(
            File carpeta
    ) {

        try {

            if (Desktop.isDesktopSupported()) {

                Desktop.getDesktop()
                        .open(carpeta);

            } else {

                mostrarMensaje(
                        "Abrir carpeta",
                        "El sistema no permite abrir el Explorador automáticamente."
                );
            }

        } catch (Exception e) {

            mostrarError(
                    "Error",
                    "No se pudo abrir la carpeta."
            );
        }
    }

    // ==================================================
    // TEXTO SEGURO
    // ==================================================

    private String textoSeguro(
            String texto
    ) {

        return texto == null
                ? ""
                : texto;
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

    // ==================================================
    // ERROR
    // ==================================================

    private void mostrarError(
            String titulo,
            String mensaje
    ) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
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

    // ==================================================
    // ACTUALIZAR CANDIDATO
    // ==================================================

    private void actualizarCandidato(
            Candidato candidato
    ) {

        try {

            repository.actualizar(
                    candidato
            );

            tablaCandidatos.refresh();

        } catch (Exception e) {

            mostrarError(
                    "Error actualizando candidato",
                    e.getMessage()
            );

            cargarCandidatos();
        }
    }

    // ==================================================
    // APLICAR CONFIGURACIÓN
    // ==================================================

    public void aplicarConfiguracion() {

        tamanoFuenteGeneral =
                configuracionService.obtenerTamanoFuente();

        tamanoFuenteTabla =
                configuracionService.obtenerTamanoFuenteTabla();

        tiposContrato =
                configuracionService.obtenerTiposContrato();

        filasAlternadas =
                configuracionService.obtenerFilasAlternadas();

        if (stage != null
                && stage.getScene() != null) {

            stage.getScene()
                    .getRoot()
                    .setStyle(
                            "-fx-font-size: "
                                    + tamanoFuenteGeneral
                                    + "px;"
                    );
        }

        tablaCandidatos.setStyle(
                "-fx-font-size: "
                        + tamanoFuenteTabla
                        + "px;"
        );

        actualizarTiposContrato();

        aplicarEstiloFilas();

        tablaCandidatos.refresh();
    }

    // ==================================================
    // ACTUALIZAR TIPOS DE CONTRATO
    // ==================================================

    private void actualizarTiposContrato() {

        if (columnaTipoContrato == null) {

            return;
        }

        tiposContrato =
                configuracionService.obtenerTiposContrato();

        columnaTipoContrato.setCellFactory(
                ComboBoxTableCell.forTableColumn(
                        tiposContrato.toArray(
                                new String[0]
                        )
                )
        );
    }

    // ==================================================
    // ESTILO DE FILAS
    // ==================================================

    private void aplicarEstiloFilas() {

        tablaCandidatos.setRowFactory(
                tabla -> {

                    TableRow<Candidato> fila =
                            new TableRow<>();

                    // ==================================================
                    // ACTUALIZAR COLOR DE FILA
                    // ==================================================

                    fila.itemProperty().addListener(
                            (obs, anterior, actual) ->
                                    actualizarColorFila(fila)
                    );

                    fila.indexProperty().addListener(
                            (obs, anterior, actual) ->
                                    actualizarColorFila(fila)
                    );

                    fila.selectedProperty().addListener(
                            (obs, anterior, actual) ->
                                    actualizarColorFila(fila)
                    );

                    // ==================================================
                    // COPIAR CON CLIC DERECHO
                    // ==================================================

                    fila.setOnContextMenuRequested(
                            event -> {

                                if (fila.isEmpty()) {

                                    return;
                                }

                                javafx.geometry.Point2D punto =
                                        fila.screenToLocal(
                                                event.getScreenX(),
                                                event.getScreenY()
                                        );

                                double x =
                                        punto.getX();

                                int columnaIndice = -1;

                                double acumulado = 0;

                                for (int i = 0;
                                     i < tabla.getColumns().size();
                                     i++) {

                                    TableColumn<Candidato, ?> columna =
                                            tabla.getColumns().get(i);

                                    double ancho =
                                            columna.getWidth();

                                    if (x >= acumulado
                                            && x <= acumulado + ancho) {

                                        columnaIndice = i;

                                        break;
                                    }

                                    acumulado += ancho;
                                }

                                // ==================================================
                                // NO SE ENCONTRÓ LA COLUMNA
                                // ==================================================

                                if (columnaIndice < 0) {

                                    return;
                                }

                                // ==================================================
                                // NO COPIAR CARTA NI ENVIADO
                                // ==================================================

                                String nombreColumna =
                                        tabla.getColumns()
                                                .get(columnaIndice)
                                                .getText();

                                if ("Carta".equals(nombreColumna)
                                        || "Enviado".equals(nombreColumna)) {

                                    return;
                                }

                                Candidato candidato =
                                        fila.getItem();

                                if (candidato == null) {

                                    return;
                                }

                                // ==================================================
                                // OBTENER TEXTO
                                // ==================================================

                                String texto =
                                        obtenerTextoCelda(
                                                candidato,
                                                columnaIndice
                                        );

                                if (texto == null) {

                                    return;
                                }

                                // ==================================================
                                // MENÚ COPIAR
                                // ==================================================

                                ContextMenu menu =
                                        new ContextMenu();

                                MenuItem copiar =
                                        new MenuItem(
                                                "Copiar"
                                        );

                                copiar.setOnAction(
                                        copiarEvent -> {

                                            Clipboard clipboard =
                                                    Clipboard.getSystemClipboard();

                                            ClipboardContent contenido =
                                                    new ClipboardContent();

                                            contenido.putString(
                                                    texto
                                            );

                                            clipboard.setContent(
                                                    contenido
                                            );
                                        }
                                );

                                menu.getItems().add(
                                        copiar
                                );

                                menu.show(
                                        fila,
                                        event.getScreenX(),
                                        event.getScreenY()
                                );

                                event.consume();
                            }
                    );

                    // ==================================================
                    // APLICAR COLOR INICIAL
                    // ==================================================

                    actualizarColorFila(
                            fila
                    );

                    return fila;
                }
        );
    }

    // ==================================================
    // COLOR DE FILA
    // ==================================================

    private void actualizarColorFila(
            TableRow<Candidato> fila
    ) {

        if (fila.isEmpty()) {

            fila.setStyle("");

            return;
        }

        Candidato candidato =
                fila.getItem();

        // ==================================================
        // SELECCIONADA
        // ==================================================

        if (fila.isSelected()) {

            /*
             * No sobrescribimos el estilo de selección
             * de JavaFX.
             *
             * Esto permite que el usuario vea claramente
             * cuáles filas están seleccionadas.
             */
            fila.setStyle("");

            return;
        }

        // ==================================================
        // RESALTADA
        // ==================================================

        if (candidato != null
                && candidato.isResaltado()) {

            fila.setStyle("""
                    -fx-background-color: #FFF3A3;
                    -fx-border-color: #E6B800;
                    -fx-border-width: 0 0 1 0;
                    """);

            return;
        }

        // ==================================================
        // FILAS ALTERNADAS
        // ==================================================

        if (!filasAlternadas) {

            fila.setStyle("");

            return;
        }

        if (fila.getIndex() % 2 == 0) {

            fila.setStyle(
                    "-fx-background-color: #FFFFFF;"
            );

        } else {

            fila.setStyle(
                    "-fx-background-color: #B3EBF2;"
            );
        }
    }

    // ==================================================
    // ESTILO FIJO DE BOTONES
    // ==================================================

    private void aplicarEstiloBoton(
            Button boton
    ) {

        boton.setStyle("""
                -fx-background-color: #4A9DAE;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 8 14 8 14;
                """);

        boton.setOnMouseEntered(
                event ->
                        boton.setStyle("""
                                -fx-background-color: #77CBDA;
                                -fx-text-fill: white;
                                -fx-font-weight: bold;
                                -fx-background-radius: 6;
                                -fx-border-radius: 6;
                                -fx-padding: 8 14 8 14;
                                """)
        );

        boton.setOnMouseExited(
                event ->
                        boton.setStyle("""
                                -fx-background-color: #4A9DAE;
                                -fx-text-fill: white;
                                -fx-font-weight: bold;
                                -fx-background-radius: 6;
                                -fx-border-radius: 6;
                                -fx-padding: 8 14 8 14;
                                """)
        );
    }
    private void aplicarEstiloBoton2(
            Button boton
    ) {
        boton.setStyle("""
                -fx-background-color: #8B5E3C;
                -fx-text-fill: white;
                -fx-font-weight: bold;
                -fx-background-radius: 6;
                -fx-border-radius: 6;
                -fx-padding: 8 14 8 14;
                """);
        boton.setOnMouseEntered(
                event ->
                        boton.setStyle("""
                                -fx-background-color: #550000;
                                -fx-text-fill: white;
                                -fx-font-weight: bold;
                                -fx-background-radius: 6;
                                -fx-border-radius: 6;
                                -fx-padding: 8 14 8 14;
                                """)
        );
        boton.setOnMouseExited(
                event ->
                        boton.setStyle("""
                                -fx-background-color: #8B5E3C;
                                -fx-text-fill: white;
                                -fx-font-weight: bold;
                                -fx-background-radius: 6;
                                -fx-border-radius: 6;
                                -fx-padding: 8 14 8 14;
                                """)
        );
    }

    private void aplicarEstiloBoton3(
            Button boton ){

            String estiloOriginal =
                    boton.getStyle();

            boton.setOnMouseEntered(event -> {

                boton.setStyle(
                        estiloOriginal
                                + "; -fx-effect: dropshadow("
                                + "gaussian, black, 4, 0.6, 0, 0"
                                + ");"
                );
            });

            boton.setOnMouseExited(event -> {

                boton.setStyle(
                        estiloOriginal
                );
            });
    }

    private Stage crearVentanaProgreso(
            Task<?> task,
            int total
    ) {

        Stage ventana =
                new Stage();

        ventana.setTitle(
                "Generando cartas"
        );

        ventana.initModality(
                javafx.stage.Modality.APPLICATION_MODAL
        );

        ventana.setResizable(
                false
        );

        Label titulo =
                new Label(
                        "Generando cartas de oferta..."
                );

        titulo.setStyle(
                "-fx-font-size: 18px; "
                        + "-fx-font-weight: bold;"
        );

        Label mensaje =
                new Label();

        mensaje.textProperty()
                .bind(
                        task.messageProperty()
                );

        mensaje.setWrapText(
                true
        );

        ProgressBar progreso =
                new ProgressBar();

        progreso.setPrefWidth(
                400
        );

        progreso.progressProperty()
                .bind(
                        task.progressProperty()
                );

        Label contador =
                new Label();

        contador.textProperty()
                .bind(
                        javafx.beans.binding.Bindings.format(
                                "%.0f%%",
                                task.progressProperty()
                                        .multiply(100)
                        )
                );

        Button cancelar =
                new Button(
                        "Cancelar"
                );

        cancelar.setPrefWidth(
                100
        );

        cancelar.setOnAction(event -> {

            Alert confirmacion =
                    new Alert(
                            Alert.AlertType.CONFIRMATION
                    );

            confirmacion.setTitle(
                    "Cancelar generación"
            );

            confirmacion.setHeaderText(
                    "¿Cancelar la generación?"
            );

            confirmacion.setContentText(
                    "Las cartas que ya fueron generadas "
                            + "se conservarán."
            );

            var respuesta =
                    confirmacion.showAndWait();

            if (respuesta.isPresent()
                    && respuesta.get()
                    == ButtonType.OK) {

                cancelar.setDisable(
                        true
                );

                cancelar.setText(
                        "Cancelando..."
                );

                task.cancel();
            }
        });

        VBox contenido =
                new VBox(
                        15,
                        titulo,
                        mensaje,
                        progreso,
                        contador,
                        cancelar
                );

        contenido.setPadding(
                new Insets(25)
        );

        contenido.setAlignment(
                Pos.CENTER
        );

        Scene escena =
                new Scene(
                        contenido,
                        500,
                        230
                );

        ventana.setScene(
                escena
        );

        return ventana;
    }
    private void mostrarResultadoGeneracion(
            ResultadoGeneracion resultado,
            File carpetaSalida
    ) {

        StringBuilder mensaje =
                new StringBuilder();

        mensaje.append(
                "Proceso terminado correctamente.\n\n"
        );

        mensaje.append(
                "Cartas nuevas: "
        );

        mensaje.append(
                resultado.generadas()
        );

        if (resultado.reemplazadas() > 0) {

            mensaje.append(
                    "\nCartas reemplazadas: "
            );

            mensaje.append(
                    resultado.reemplazadas()
            );
        }

        mensaje.append(
                "\n\nCarpeta:\n"
        );

        mensaje.append(
                carpetaSalida.getAbsolutePath()
        );

        Alert resultadoAlert =
                new Alert(
                        Alert.AlertType.INFORMATION
                );

        resultadoAlert.setTitle(
                "Cartas generadas"
        );

        resultadoAlert.setHeaderText(
                "Generación completada"
        );

        resultadoAlert.setContentText(
                mensaje.toString()
        );

        ButtonType abrirCarpeta =
                new ButtonType(
                        "Abrir carpeta"
                );

        ButtonType cerrar =
                new ButtonType(
                        "Cerrar",
                        ButtonBar.ButtonData.CANCEL_CLOSE
                );

        resultadoAlert.getButtonTypes()
                .setAll(
                        abrirCarpeta,
                        cerrar
                );

        resultadoAlert.showAndWait()
                .ifPresent(boton -> {

                    if (boton == abrirCarpeta) {

                        abrirCarpeta(
                                carpetaSalida
                        );
                    }
                });
    }
// ==================================================
// EXPORTAR EXCEL
// ==================================================

    private void exportarExcel() {

        List<Candidato> candidatos =
                tablaCandidatos.getItems();

        if (candidatos.isEmpty()) {

            mostrarMensaje(
                    "Exportar Excel",
                    "No hay candidatos para exportar."
            );

            return;
        }

        FileChooser selector =
                new FileChooser();

        selector.setTitle(
                "Guardar base de datos en Excel"
        );

        selector.setInitialFileName(
                "candidatos.xlsx"
        );

        selector.getExtensionFilters().add(
                new FileChooser.ExtensionFilter(
                        "Archivo Excel (*.xlsx)",
                        "*.xlsx"
                )
        );

        File archivoSeleccionado =
                selector.showSaveDialog(stage);

        if (archivoSeleccionado == null) {

            return;
        }

        // ==================================================
        // ARCHIVO DEFINITIVO
        // ==================================================

        final File archivo;

        if (archivoSeleccionado.getName()
                .toLowerCase()
                .endsWith(".xlsx")) {

            archivo =
                    archivoSeleccionado;

        } else {

            archivo =
                    new File(
                            archivoSeleccionado.getParentFile(),
                            archivoSeleccionado.getName()
                                    + ".xlsx"
                    );
        }

        try {

            ExcelExportService exportador =
                    new ExcelExportService();

            exportador.exportar(
                    candidatos,
                    archivo
            );

            // ==================================================
            // VENTANA DE RESULTADO
            // ==================================================

            Alert resultado =
                    new Alert(
                            Alert.AlertType.INFORMATION
                    );

            resultado.setTitle(
                    "Exportación completada"
            );

            resultado.setHeaderText(
                    "Excel exportado correctamente"
            );

            resultado.setContentText(
                    "Candidatos exportados: "
                            + candidatos.size()
                            + "\n\n"
                            + "Archivo:\n"
                            + archivo.getAbsolutePath()
            );

            ButtonType abrirCarpeta =
                    new ButtonType(
                            "Abrir carpeta"
                    );

            ButtonType cerrar =
                    new ButtonType(
                            "Cerrar",
                            ButtonBar.ButtonData.CANCEL_CLOSE
                    );

            resultado.getButtonTypes()
                    .setAll(
                            abrirCarpeta,
                            cerrar
                    );

            resultado.showAndWait()
                    .ifPresent(boton -> {

                        if (boton == abrirCarpeta) {

                            abrirCarpeta(
                                    archivo.getParentFile()
                            );
                        }
                    });

        } catch (Exception e) {

            mostrarError(
                    "Error exportando Excel",
                    e.getMessage()
            );
        }
    }

}