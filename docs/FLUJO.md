# Flujo completo de CartaOferta Express

Este documento sigue el recorrido de los datos: desde que se abre la aplicación hasta que el candidato recibe su carta por correo. En cada paso se indica qué clase y qué método participan.

## Resumen en una línea

```
Abrir app -> Registrar/importar candidato -> Elegir plantilla -> Generar PPTX -> Convertir a PDF -> Enviar por correo
```

---

## 1. Inicio de la aplicación

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `Main.main` -> `Main.start` | JavaFX arranca y crea `SplashView` |
| 2 | `SplashView.mostrar` | Muestra la pantalla de bienvenida con animación |
| 3 | `SplashView.iniciarCarga` | En un hilo aparte ejecuta los pasos 4 y 5 |
| 4 | `DatabaseManager.initializeDatabase` | Crea `datos/`, la tabla `candidatos` y aplica migraciones |
| 5 | `new MainView()` y `MainView.inicializarInterfaz` | Construye la pantalla principal |
| 6 | `SplashView.finalizar` | Cierra el splash y abre la ventana principal |

## 2. Registrar candidatos

### Opción A: formulario

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `MainView` (botón de nuevo candidato) | Abre `CandidatoFormView.mostrar` |
| 2 | `CandidatoFormView.guardarCandidato` | Toma los campos del formulario |
| 3 | `ValidadorCandidato.validarCedula`, `validarCelular`, `validarCorreo`, `validarFecha` | Valida y normaliza. Si algo falla, se muestra el mensaje de error |
| 4 | `CandidatoRepository.guardar` (o `actualizar` si se edita) | Escribe la fila en SQLite usando `DatabaseManager.getConnection` |
| 5 | `MainView.cargarCandidatos` | Refresca la tabla |

### Opción B: importar desde Excel

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `MainView.importarExcel` | El usuario elige un `.xlsx` |
| 2 | `ExcelImportService.importar` | Lee la primera hoja, normaliza los encabezados y convierte cada fila en un `Candidato` |
| 3 | Dentro de `importar` | Las filas sin cédula se descartan y se cuentan en `ResultadoImportacion.getCandidatosSinCedula()` |
| 4 | `MainView.importarExcel` + `CandidatoRepository` | Para cada candidato consulta `buscarPorCedula`: si ya existe lo cuenta como duplicado y lo omite; si no, lo guarda con `guardar`. Refresca la tabla y muestra un resumen |

## 3. Elegir la plantilla

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `MainView` (botón "Plantillas") | Abre `PlantillasView.mostrar` |
| 2 | `PlantillasView.agregarPlantilla` -> `PlantillaService.agregarPlantilla` | Copia un `.pptx` a la carpeta `plantillas/` |
| 3 | Selección en la lista | La plantilla elegida se guarda con `ConfiguracionService.guardarPlantilla` |
| 4 | `MainView.actualizarIndicadorPlantilla` | Muestra cuál plantilla está activa |

La plantilla es un PowerPoint normal que contiene marcadores como `{{NOMBRE}}`, `{{CARGO}}` o `{{SALARIO}}` (lista completa en `PptGeneratorService.construirVariables`).

## 4. Generar cartas

Se dispara con el botón **Generar cartas** de `MainView`, sobre los candidatos seleccionados.

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `MainView.generarCartas` | Pide confirmación y verifica que haya plantilla (`ConfiguracionService.obtenerPlantilla`) |
| 2 | `MainView.generarCartas` | Crea la carpeta `cartas_generadas/<dd-MM-yyyy>/` y lanza una `Task` en segundo plano con ventana de progreso y botón de cancelar |
| 3 | Por cada candidato: `PptGeneratorService.generar` | Copia la plantilla y reemplaza las variables por los datos del candidato. Resultado: `Carta_Oferta_<Nombre>.pptx` |
| 4 | Por cada candidato: `PowerPointPdfService.convertir` | Convierte el `.pptx` en `.pdf` con PowerPoint (ver sección siguiente) |
| 5 | Por cada candidato: `CandidatoRepository.registrarCartaGenerada` | Guarda en la base de datos que la carta existe, su archivo, fecha y plantilla usada |
| 6 | `MainView.mostrarResultadoGeneracion` | Resume cuántas se generaron y cuántas se reemplazaron |

### Cómo funciona el reemplazo de variables

`PptGeneratorService` abre el `.pptx` con Apache POI (`XMLSlideShow`), recorre cada diapositiva y cada forma que contenga texto. En cada párrafo busca los marcadores y los sustituye. El texto resultante queda en el primer fragmento de texto del párrafo y se eliminan los demás fragmentos del mismo párrafo.

### Para qué se usan Jacob y PowerPoint

Apache POI sabe leer y editar `.pptx`, pero **no sabe convertirlos a PDF** con la misma fidelidad que PowerPoint. Por eso `PowerPointPdfService` usa **Jacob**, una librería que permite a Java controlar programas de Windows mediante COM:

1. `new ActiveXComponent("PowerPoint.Application")` abre PowerPoint.
2. `Presentations.Open(ruta, ...)` abre la carta generada.
3. `SaveAs(rutaPdf, 32)` guarda como PDF (32 es el código del formato PDF en PowerPoint).
4. `Close` y `Quit` cierran la presentación y PowerPoint.

Por eso la aplicación **solo funciona en Windows y con PowerPoint instalado**, y por eso necesita `jacob-1.18-x64.dll` en la carpeta indicada por `-Djava.library.path` (la carpeta `native/` en desarrollo, `app\native` en el portable).

## 5. Enviar por correo

Se abre desde `MainView` con `CorreosView.mostrar`.

| Paso | Clase y método | Qué ocurre |
|---|---|---|
| 1 | `CorreosView.validarParaEnvio` | Un candidato es **apto** solo si tiene carta generada y correo. Se muestra el conteo de aptos y no aptos |
| 2 | `CorreosView.mostrarAdvertenciaCandidatosNoAptos` | Permite continuar solo con los aptos |
| 3 | `CorreosView.cargarPlantillaHtml` | El usuario carga el HTML del correo; se guarda como `plantilla_html/correo.html` y su ruta en la configuración |
| 4 | `CorreosView.cargarVistaPrevia` / `reemplazarVariables` | Muestra una vista previa y reemplaza `{{nombre}}` por el nombre del candidato |
| 5 | `CorreosView.enviarCorreos` | Para cada candidato apto, llama a `EmailService.enviarCorreo` |
| 6 | `EmailService.obtenerArchivoPdf` | Localiza el PDF de la carta del candidato |
| 7 | `EmailService.enviarCorreo` | Consigue el token (sección siguiente), arma el mensaje con asunto `CARTA OFERTA a <nombre>`, cuerpo HTML y PDF adjunto, y lo envía |
| 8 | `CandidatoRepository.registrarCorreoEnviado` | Guarda el estado y la fecha del envío |

### Cómo funciona el inicio de sesión con Microsoft

`MicrosoftAuthService.obtenerAccessToken` usa **MSAL4J** con el flujo interactivo de una aplicación pública:

1. Se abre la ventana de inicio de sesión de Microsoft en el navegador (redirección a `http://localhost`).
2. El usuario inicia sesión y acepta el permiso `https://outlook.office.com/SMTP.Send`.
3. Microsoft devuelve un **token de acceso**.
4. `EmailService` lo usa como "contraseña" en una sesión SMTP con autenticación **XOAUTH2** contra `smtp-mail.outlook.com:587`.

La aplicación nunca guarda ni conoce la contraseña del usuario. La autoridad configurada es `login.microsoftonline.com/consumers`, es decir, cuentas personales de Microsoft (Outlook, Hotmail, Live).

## 6. Otras acciones sobre las cartas

| Acción | Clase y método |
|---|---|
| Abrir una carta | `MainView.abrirCarta` (usa `buscarArchivoPDF`) |
| Eliminar una carta | `MainView.eliminarCarta` -> `CandidatoRepository.eliminarRegistroCarta` |
| Abrir la carpeta de cartas | `MainView.abrirCarpeta` |
| Marcar o desmarcar manualmente un correo como enviado | `MainView` (columna de correo) -> `CandidatoRepository.cambiarEstadoCorreo` |
| Exportar la lista a Excel | `MainView.exportarExcel` -> `ExcelExportService.exportar` |

## Dónde queda cada dato

| Dato | Lugar |
|---|---|
| Candidatos y estado de cartas/correos | `datos/carta_oferta.db` (tabla `candidatos`) |
| Configuración (plantilla activa, fuentes, tipos de contrato) | `datos/configuracion.properties` |
| Plantillas de PowerPoint | `plantillas/` |
| Plantilla HTML del correo | `plantilla_html/correo.html` |
| Cartas generadas | `cartas_generadas/<dd-MM-yyyy>/` |
