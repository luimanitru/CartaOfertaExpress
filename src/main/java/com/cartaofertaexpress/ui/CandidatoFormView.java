package com.cartaofertaexpress.ui;

import com.cartaofertaexpress.model.Candidato;
import com.cartaofertaexpress.repository.CandidatoRepository;
import com.cartaofertaexpress.service.ConfiguracionService;
import com.cartaofertaexpress.service.ValidadorCandidato;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.math.BigDecimal;

public class CandidatoFormView {

    private final CandidatoRepository repository;

    private final ConfiguracionService configuracionService =
            new ConfiguracionService();

    private final TextField campoNombre =
            new TextField();

    private final TextField campoCedula =
            new TextField();

    private final ComboBox<String> campoTipoContrato =
            new ComboBox<>();

    private final TextField campoRol =
            new TextField();

    private final TextField campoCargo =
            new TextField();

    private final TextField campoFechaIngreso =
            new TextField();

    private final TextField campoSalario =
            new TextField();

    private final TextField campoCiudad =
            new TextField();

    private final TextField campoCelular =
            new TextField();

    private final TextField campoCorreo =
            new TextField();


    // =====================================================
    // CONSTRUCTOR
    // =====================================================

    public CandidatoFormView() {

        repository =
                new CandidatoRepository();
    }


    // =====================================================
    // NUEVO CANDIDATO
    // =====================================================

    public void mostrar(
            Stage ventanaPrincipal,
            Runnable alGuardar) {

        mostrar(
                ventanaPrincipal,
                null,
                alGuardar
        );
    }


    // =====================================================
    // NUEVO / EDITAR CANDIDATO
    // =====================================================

    public void mostrar(
            Stage ventanaPrincipal,
            Candidato candidato,
            Runnable alGuardar) {

        Stage ventana =
                new Stage();

        ventana.initOwner(
                ventanaPrincipal
        );

        ventana.initModality(
                Modality.WINDOW_MODAL
        );


        boolean modoEdicion =
                candidato != null;


        ventana.setTitle(
                modoEdicion
                        ? "Editar candidato"
                        : "Nuevo candidato"
        );


        // =====================================================
        // CONFIGURAR CAMPOS
        // =====================================================

        campoNombre.setPromptText(
                "Nombre completo"
        );

        campoCedula.setPromptText(
                "Número de cédula"
        );


        campoRol.setPromptText(
                "Rol"
        );

        campoCargo.setPromptText(
                "Cargo"
        );

        campoFechaIngreso.setPromptText(
                "DD/MM/AAAA"
        );

        campoSalario.setPromptText(
                "Ej: 3500000"
        );

        campoCiudad.setPromptText(
                "Ciudad"
        );

        campoCelular.setPromptText(
                "Número de celular"
        );

        campoCorreo.setPromptText(
                "correo@ejemplo.com"
        );


        // =====================================================
        // TIPOS DE CONTRATO
        // =====================================================

        campoTipoContrato
                .getItems()
                .clear();

        campoTipoContrato
                .getItems()
                .addAll(
                        configuracionService
                                .obtenerTiposContrato()
                );

        campoTipoContrato.setPromptText(
                "Seleccione..."
        );


        // =====================================================
        // SI ESTAMOS EDITANDO
        // =====================================================

        if (modoEdicion) {

            campoNombre.setText(
                    candidato.getNombre()
            );

            campoCedula.setText(
                    candidato.getCedula()
            );

            campoTipoContrato.setValue(
                    candidato.getTipoContrato()
            );

            campoRol.setText(
                    candidato.getRol()
            );

            campoCargo.setText(
                    candidato.getCargo()
            );

            campoFechaIngreso.setText(
                    candidato.getFechaIngreso()
            );

            if (candidato.getSalario() != null) {

                campoSalario.setText(
                        candidato.getSalario()
                                .toPlainString()
                );
            } else {

                campoSalario.clear();
            }

            campoCiudad.setText(
                    candidato.getCiudad()
            );

            campoCelular.setText(
                    candidato.getCelular()
            );

            campoCorreo.setText(
                    candidato.getCorreo()
            );
        }


        // =====================================================
        // FORMULARIO
        // =====================================================

        GridPane formulario =
                new GridPane();

        formulario.setHgap(
                15
        );

        formulario.setVgap(
                12
        );

        formulario.setPadding(
                new Insets(20)
        );


        agregarCampo(
                formulario,
                0,
                "Nombre:",
                campoNombre
        );

        agregarCampo(
                formulario,
                1,
                "Cédula:",
                campoCedula
        );

        agregarCampo(
                formulario,
                2,
                "Tipo de contrato:",
                campoTipoContrato
        );

        agregarCampo(
                formulario,
                3,
                "Rol:",
                campoRol
        );

        agregarCampo(
                formulario,
                4,
                "Cargo:",
                campoCargo
        );

        agregarCampo(
                formulario,
                5,
                "Fecha de ingreso:",
                campoFechaIngreso
        );

        agregarCampo(
                formulario,
                6,
                "Salario:",
                campoSalario
        );

        agregarCampo(
                formulario,
                7,
                "Ciudad:",
                campoCiudad
        );

        agregarCampo(
                formulario,
                8,
                "Celular:",
                campoCelular
        );

        agregarCampo(
                formulario,
                9,
                "Correo:",
                campoCorreo
        );


        // =====================================================
        // BOTONES
        // =====================================================

        Button botonCancelar =
                new Button(
                        "Cerrar"
                );

        Button botonGuardar =
                new Button(
                        modoEdicion
                                ? "Guardar cambios"
                                : "Guardar"
                );


        // =====================================================
        // CANCELAR
        // =====================================================

        botonCancelar.setOnAction(
                event ->
                        ventana.close()
        );


        // =====================================================
        // GUARDAR
        // =====================================================

        botonGuardar.setOnAction(
                event -> {

                    boolean guardado =
                            guardarCandidato(
                                    ventana,
                                    candidato
                            );

                    if (guardado) {

                        if (alGuardar != null) {

                            alGuardar.run();
                        }

                        ventana.close();
                    }
                }
        );


        // =====================================================
        // BOTONES
        // =====================================================

        HBox botones =
                new HBox(
                        10,
                        botonCancelar,
                        botonGuardar
                );

        botones.setAlignment(
                Pos.CENTER_RIGHT
        );

        botones.setPadding(
                new Insets(
                        0,
                        20,
                        20,
                        20
                )
        );


        // =====================================================
        // CONTENEDOR
        // =====================================================

        VBox root =
                new VBox(
                        formulario,
                        botones
                );


        // =====================================================
        // ESCENA
        // =====================================================

        Scene escena =
                new Scene(
                        root,
                        550,
                        520
                );


        ventana.setScene(
                escena
        );


        // =====================================================
        // ATAJOS DE TECLADO
        // =====================================================

        escena.setOnKeyPressed(
                event -> {

                    // ENTER = GUARDAR

                    if (event.getCode()
                            == KeyCode.ENTER) {

                        botonGuardar.fire();

                        event.consume();
                    }


                    // ESC = CANCELAR

                    if (event.getCode()
                            == KeyCode.ESCAPE) {

                        botonCancelar.fire();

                        event.consume();
                    }
                }
        );


        ventana.showAndWait();
    }


    // =====================================================
    // AGREGAR CAMPO
    // =====================================================

    private void agregarCampo(
            GridPane formulario,
            int fila,
            String etiqueta,
            Control campo) {

        Label label =
                new Label(
                        etiqueta
                );

        label.setMinWidth(
                140
        );

        campo.setPrefWidth(
                330
        );


        formulario.add(
                label,
                0,
                fila
        );

        formulario.add(
                campo,
                1,
                fila
        );
    }


    // =====================================================
    // GUARDAR / ACTUALIZAR
    // =====================================================

    private boolean guardarCandidato(
            Stage ventana,
            Candidato candidatoOriginal) {


        // =====================================================
        // VALIDAR NOMBRE
        // =====================================================

        if (campoNombre
                .getText()
                .trim()
                .isEmpty()) {

            mostrarError(
                    ventana,
                    "El nombre es obligatorio."
            );

            return false;
        }


        // =====================================================
        // VALIDAR CÉDULA
        // =====================================================

        if (campoCedula
                .getText()
                .trim()
                .isEmpty()) {

            mostrarError(
                    ventana,
                    "La cédula es obligatoria."
            );

            return false;
        }


        // =====================================================
        // VALIDAR TIPO DE CONTRATO
        // =====================================================

        if (campoTipoContrato.getValue()
                == null
                ||
                campoTipoContrato
                        .getValue()
                        .isBlank()) {

            mostrarError(
                    ventana,
                    "Selecciona el tipo de contrato."
            );

            return false;
        }

        String tipoContrato =
                campoTipoContrato.getValue();


        // =====================================================
        // VALIDAR CARGO
        // =====================================================

        if (campoCargo
                .getText()
                .trim()
                .isEmpty()) {

            mostrarError(
                    ventana,
                    "El cargo es obligatorio."
            );

            return false;
        }


        // =====================================================
        // VALIDAR FECHA
        // =====================================================

        if (campoFechaIngreso
                .getText()
                .trim()
                .isEmpty()) {

            mostrarError(
                    ventana,
                    "La fecha de ingreso es obligatoria."
            );

            return false;
        }


        // =====================================================
        // VARIABLES NORMALIZADAS
        // =====================================================

        String nombre;
        String cedula;
        String cargo;
        String rol;
        String fechaIngreso;
        String ciudad;
        String celular;
        String correo;


        // =====================================================
        // VALIDAR Y NORMALIZAR
        // =====================================================

        try {

            nombre =
                    ValidadorCandidato.normalizarNombre(
                            campoNombre.getText()
                    );

            cedula =
                    ValidadorCandidato.validarCedula(
                            campoCedula.getText()
                    );

            cargo =
                    ValidadorCandidato.normalizarTexto(
                            campoCargo.getText()
                    );

            rol =
                    ValidadorCandidato.normalizarTexto(
                            campoRol.getText()
                    );

            fechaIngreso =
                    ValidadorCandidato.validarFecha(
                            campoFechaIngreso.getText()
                    );

            ciudad =
                    ValidadorCandidato.normalizarTexto(
                            campoCiudad.getText()
                    );

            celular =
                    ValidadorCandidato.validarCelular(
                            campoCelular.getText()
                    );

            correo =
                    ValidadorCandidato.validarCorreo(
                            campoCorreo.getText()
                    );

        } catch (IllegalArgumentException e) {

            mostrarError(
                    ventana,
                    e.getMessage()
            );

            return false;
        }


        // =====================================================
        // SALARIO
        // =====================================================

        BigDecimal salario;

        try {

            String textoSalario =
                    campoSalario
                            .getText()
                            .trim()
                            .replace(".", "")
                            .replace(",", ".");


            if (textoSalario.isBlank()) {

                mostrarError(
                        ventana,
                        "El salario es obligatorio."
                );

                return false;
            }


            salario =
                    new BigDecimal(
                            textoSalario
                    );


            if (salario.compareTo(
                    BigDecimal.ZERO
            ) < 0) {

                mostrarError(
                        ventana,
                        "El salario no puede ser negativo."
                );

                return false;
            }

        } catch (NumberFormatException e) {

            mostrarError(
                    ventana,
                    "El salario no tiene un formato válido."
            );

            return false;
        }


        // =====================================================
        // COMPROBAR CÉDULA
        // =====================================================

        Candidato existente =
                repository.buscarPorCedula(
                        cedula
                );


        if (existente != null) {

            boolean esOtroCandidato =
                    candidatoOriginal == null
                            ||
                            existente.getId()
                                    != candidatoOriginal.getId();


            if (esOtroCandidato) {

                mostrarError(
                        ventana,
                        "Ya existe otro candidato con esa cédula."
                );

                return false;
            }
        }


        // =====================================================
        // NUEVO CANDIDATO
        // =====================================================

        if (candidatoOriginal == null) {

            Candidato candidato =
                    new Candidato(

                            nombre,

                            cedula,

                            cargo,

                            salario,

                            rol,

                            tipoContrato,

                            fechaIngreso,

                            ciudad,

                            null,

                            celular,

                            correo
                    );


            repository.guardar(
                    candidato
            );

            return true;
        }


        // =====================================================
        // EDITAR CANDIDATO
        // =====================================================

        candidatoOriginal.setNombre(
                nombre
        );

        candidatoOriginal.setCedula(
                cedula
        );

        candidatoOriginal.setCargo(
                cargo
        );

        candidatoOriginal.setSalario(
                salario
        );

        candidatoOriginal.setRol(
                rol
        );

        candidatoOriginal.setTipoContrato(
                tipoContrato
        );

        candidatoOriginal.setFechaIngreso(
                fechaIngreso
        );

        candidatoOriginal.setCiudad(
                ciudad
        );

        candidatoOriginal.setCelular(
                celular
        );

        candidatoOriginal.setCorreo(
                correo
        );


        repository.actualizar(
                candidatoOriginal
        );

        return true;
    }


    // =====================================================
    // MOSTRAR ERROR
    // =====================================================

    private void mostrarError(
            Stage ventana,
            String mensaje) {

        Alert alert =
                new Alert(
                        Alert.AlertType.ERROR
                );

        alert.initOwner(
                ventana
        );

        alert.setTitle(
                "Datos inválidos"
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