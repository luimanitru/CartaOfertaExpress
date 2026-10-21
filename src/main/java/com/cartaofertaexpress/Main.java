package com.cartaofertaexpress;

import javafx.application.Application;
import javafx.stage.Stage;

import com.cartaofertaexpress.ui.SplashView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        SplashView splash =
                new SplashView();

        splash.mostrar(stage);
    }

    public static void main(String[] args) {

        launch(args);
    }
}