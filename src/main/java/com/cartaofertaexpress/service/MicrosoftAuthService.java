package com.cartaofertaexpress.service;

import com.microsoft.aad.msal4j.*;

import java.net.URI;
import java.util.Collections;
import java.util.Set;

public class MicrosoftAuthService {

    private static final String CLIENT_ID =
            "66747e74-c580-429e-bb6f-fc7c6e974f65";

    private static final String AUTHORITY =
            "https://login.microsoftonline.com/consumers";

    private static final String REDIRECT_URI =
            "http://localhost";

    private static final Set<String> SCOPES =
            Collections.singleton(
                    "https://outlook.office.com/SMTP.Send"
            );

    public String obtenerAccessToken() {

        try {

            PublicClientApplication aplicacion =
                    PublicClientApplication
                            .builder(CLIENT_ID)
                            .authority(AUTHORITY)
                            .build();

            InteractiveRequestParameters parametros =
                    InteractiveRequestParameters
                            .builder(
                                    new URI(REDIRECT_URI)
                            )
                            .scopes(SCOPES)
                            .build();

            IAuthenticationResult resultado =
                    aplicacion
                            .acquireToken(parametros)
                            .join();

            System.out.println(
                    "Usuario del token: "
                            + resultado.account().username()
            );

            System.out.println(
                    "Scopes concedidos: "
                            + resultado.scopes()
            );

            return resultado.accessToken();

        } catch (Exception e) {

            throw new RuntimeException(
                    "No se pudo autenticar con Microsoft: "
                            + e.getMessage(),
                    e
            );
        }
    }
}