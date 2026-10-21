package com.cartaofertaexpress.ui;

import com.cartaofertaexpress.database.DatabaseManager;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

public class SplashView {

    // =====================================================
    // CONFIGURACIÓN
    // =====================================================

    private static final long TIEMPO_MINIMO = 3000;

    // =====================================================
    // STAGES
    // =====================================================

    // Ventana exclusiva del splash
    private Stage stageSplash;

    // Ventana principal de la aplicación
    private Stage stagePrincipal;

    // =====================================================
    // MAIN VIEW
    // =====================================================

    private MainView mainView;


    // =====================================================
    // MOSTRAR SPLASH
    // =====================================================

    public void mostrar(Stage stagePrincipal) {

        this.stagePrincipal =
                stagePrincipal;

        // =================================================
        // CREAR STAGE DEL SPLASH
        // =================================================

        stageSplash =
                new Stage();

        stageSplash.initStyle(
                StageStyle.UNDECORATED
        );

        // =================================================
        // IMAGEN
        // =================================================

        Image imagen =
                new Image(
                        getClass()
                                .getResourceAsStream(
                                        "/images/inicio.jpeg"
                                )
                );

        ImageView imagenView =
                new ImageView(
                        imagen
                );

        imagenView.setFitWidth(
                800
        );

        imagenView.setPreserveRatio(
                true
        );

        // =================================================
        // CONTENEDOR
        // =================================================

        StackPane raiz =
                new StackPane(
                        imagenView
                );

        raiz.setStyle(
                "-fx-background-color: white;"
        );

        // =================================================
        // ESCENA
        // =================================================

        Scene escena =
                new Scene(
                        raiz
                );

        escena.setFill(
                Color.WHITE
        );

        stageSplash.setScene(
                escena
        );

        stageSplash.setOpacity(
                1.0
        );

        stageSplash.sizeToScene();

        stageSplash.centerOnScreen();

        stageSplash.show();

        // =================================================
        // FADE DE ENTRADA
        // =================================================

        FadeTransition entrada =
                new FadeTransition(
                        Duration.millis(
                                700
                        ),
                        raiz
                );

        entrada.setFromValue(
                0
        );

        entrada.setToValue(
                1
        );

        entrada.play();

        // =================================================
        // INICIAR CARGA
        // =================================================

        iniciarCarga();
    }


    // =====================================================
    // INICIAR CARGA
    // =====================================================

    private void iniciarCarga() {

        long inicio =
                System.currentTimeMillis();

        Thread hiloCarga =
                new Thread(
                        () -> {

                            try {

                                // =================================
                                // INICIALIZAR BASE DE DATOS
                                // =================================

                                DatabaseManager
                                        .initializeDatabase();


                                // =================================
                                // CREAR MAIN VIEW
                                // =================================

                                mainView =
                                        new MainView();


                                // =================================
                                // INICIALIZAR INTERFAZ
                                // =================================

                                mainView
                                        .inicializarInterfaz();


                                // =================================
                                // ASEGURAR TIEMPO MÍNIMO
                                // =================================

                                long transcurrido =
                                        System.currentTimeMillis()
                                                - inicio;

                                long restante =
                                        TIEMPO_MINIMO
                                                - transcurrido;

                                if (
                                        restante > 0
                                ) {

                                    Thread.sleep(
                                            restante
                                    );
                                }


                                // =================================
                                // FINALIZAR EN JAVAFX
                                // =================================

                                Platform.runLater(
                                        this::finalizar
                                );


                            } catch (
                                    InterruptedException e
                            ) {

                                Thread
                                        .currentThread()
                                        .interrupt();


                            } catch (
                                    Exception e
                            ) {

                                e.printStackTrace();

                                Platform.runLater(
                                        () -> {

                                            if (
                                                    stageSplash != null
                                            ) {

                                                stageSplash.close();
                                            }

                                            if (
                                                    stagePrincipal != null
                                            ) {

                                                stagePrincipal.show();

                                                stagePrincipal
                                                        .setOpacity(
                                                                1.0
                                                        );

                                                stagePrincipal
                                                        .centerOnScreen();

                                                stagePrincipal
                                                        .toFront();

                                                stagePrincipal
                                                        .requestFocus();
                                            }
                                        }
                                );
                            }
                        }
                );

        hiloCarga.setDaemon(
                true
        );

        hiloCarga.start();
    }


    // =====================================================
    // FINALIZAR SPLASH
    // =====================================================

    private void finalizar() {

        // =================================
        // CERRAR SPLASH
        // =================================

        if (
                stageSplash != null
        ) {

            stageSplash.close();
        }


        // =================================
        // MOSTRAR APLICACIÓN PRINCIPAL
        // =================================

        if (
                mainView != null
                        && stagePrincipal != null
        ) {

            mainView.mostrar(
                    stagePrincipal
            );

            // =================================
            // ASEGURAR VISIBILIDAD
            // =================================

            stagePrincipal.setOpacity(
                    1.0
            );

            stagePrincipal.centerOnScreen();

            stagePrincipal.toFront();

            stagePrincipal.requestFocus();
        }
    }
}