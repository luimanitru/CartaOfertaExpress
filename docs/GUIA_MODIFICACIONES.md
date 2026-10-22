# Guía de modificaciones

Formato: **"Si quiero hacer X, toco Y"**. Los puntos marcados con ⚠️ son riesgosos: léelos antes de tocar.

**Antes de cualquier cambio:** crea una rama (`git checkout -b nombre-del-cambio`), compila con `mvn clean package` al terminar y prueba el flujo completo (crear candidato, generar carta, abrir PDF) antes de unir a `main`.

---

## 1. Cambiar el diseño o el texto de la carta

**El diseño y el texto de la carta están en la plantilla de PowerPoint, no en el código.**

1. Abre el `.pptx` de la carpeta `plantillas/` (o crea uno nuevo) y edítalo en PowerPoint.
2. Mantén los marcadores tal cual: `{{NOMBRE}}`, `{{CEDULA}}`, `{{CARGO}}`, `{{SALARIO}}`, `{{ROL}}`, `{{TIPO_CONTRATO}}`, `{{FECHA_INGRESO}}`, `{{CIUDAD}}`, `{{TASA}}`, `{{CELULAR}}`, `{{CORREO}}`.
3. Si es un archivo nuevo, agrégalo desde el botón **Plantillas** de la aplicación y selecciónalo.

⚠️ **Limitaciones del reemplazo** (`PptGeneratorService.reemplazarEnTexto`):
- Solo reemplaza en cuadros de texto de primer nivel. **No reemplaza dentro de tablas ni de formas agrupadas.** Si pones un marcador en una tabla, saldrá sin reemplazar.
- Si un párrafo tiene varios estilos (por ejemplo una palabra en negrita), al reemplazar todo el párrafo toma el estilo del **primer** fragmento y se pierden los otros estilos.
- El marcador debe estar escrito de una sola vez en PowerPoint. Si lo escribes y le cambias el formato a medias, puede quedar partido internamente. Si no se reemplaza, bórralo y escríbelo de nuevo.

## 2. Agregar una variable nueva a la carta (sin campo nuevo)

Ejemplo: una `{{FECHA_CARTA}}` con la fecha de hoy.

1. `PptGeneratorService.construirVariables`: agrega `variables.put("{{FECHA_CARTA}}", ...)`.
2. En la plantilla `.pptx`, escribe `{{FECHA_CARTA}}`.

## 3. Agregar un campo nuevo al candidato

Ejemplo: "Jefe inmediato". Hay que tocar **varias** clases; si olvidas alguna, el campo se guarda mal o no aparece. Orden recomendado:

| # | Archivo | Qué hacer |
|---|---|---|
| 1 | `database/DatabaseManager` | Agrega la columna con `agregarColumnaSiNoExiste("jefe", "TEXT")` en `actualizarEstructura()`. Así las bases de datos que ya existen se actualizan solas |
| 2 | `model/Candidato` | Atributo, getter y setter. Si hay constructor con parámetros, decide si lo ampliás |
| 3 | `repository/CandidatoRepository` | Agrega la columna en `guardar`, `actualizar` y `mapearCandidato` (los tres) |
| 4 | `ui/CandidatoFormView` | Agrega `agregarCampo(...)` y recoge el valor en `guardarCandidato()` |
| 5 | `service/ValidadorCandidato` | Si necesita validación, agrega un método |
| 6 | `ui/MainView` (`configurarTabla`) | Si quieres verlo como columna de la tabla |
| 7 | `service/ExcelImportService` | Si debe poder importarse (agrega el encabezado reconocido) |
| 8 | `service/ExcelExportService` | Si debe aparecer en el Excel exportado |
| 9 | `service/PptGeneratorService.construirVariables` | Si debe poder usarse en la carta (`{{JEFE}}`) |

⚠️ **No cambies el `CREATE TABLE` para agregar columnas.** Ese bloque solo se ejecuta cuando la tabla no existe. Para bases de datos ya creadas, la columna nueva debe entrar por `agregarColumnaSiNoExiste`.

⚠️ Los parámetros de los `INSERT`/`UPDATE` en `CandidatoRepository` van por **posición**. Si agregas una columna, cuida que el orden de los `?` y el de los `setString(...)` coincidan.

## 4. Cambiar las validaciones

| Quiero cambiar | Dónde |
|---|---|
| Reglas de cédula, celular, correo o formato de fecha | `ValidadorCandidato` (métodos `validar...` y los patrones `CEDULA`, `SOLO_NUMEROS`, `CORREO`, `FORMATO_FECHA`) |
| Mensajes de error | Los mismos métodos (el texto de `IllegalArgumentException`) |
| Qué campos son obligatorios en el formulario | `CandidatoFormView.guardarCandidato` |
| Qué campos limpia o exige al importar de Excel | `ExcelImportService.importar` y sus métodos `normalizar...` |

⚠️ La validación existe en **dos caminos**: formulario y Excel. Si cambias una regla, revisa ambos.

## 5. Cambiar la plantilla del correo

**El texto y diseño del correo están en un HTML, no en el código.**

1. Edita el HTML (por ejemplo `plantilla_html/correo.html`) o crea uno nuevo.
2. En la ventana de correos, usa la opción de cargar plantilla HTML.
3. La única variable disponible es `{{nombre}}` (en minúscula), definida en `CorreosView.reemplazarVariables`.

| Quiero cambiar | Dónde |
|---|---|
| Asunto del correo (`CARTA OFERTA a ...`) | ⚠️ Está escrito en **tres lugares** de `CorreosView` (incluidos `actualizarDatosCorreo` y `enviarCorreos`). Búscalo con `Ctrl + Shift + F` el texto `CARTA OFERTA a` y cámbialo en todos |
| Agregar otra variable al HTML | `CorreosView.reemplazarVariables` |
| Archivo adjunto | `CorreosView.enviarCorreos` y `EmailService.obtenerArchivoPdf` |

## 6. Cambiar la cuenta o el servidor de correo

> **Estado:** implementado y probado con una cuenta personal de Microsoft, pero no configurado con una cuenta corporativa; no se usa en el entorno de trabajo.

⚠️ **Riesgo alto.** Hoy está repartido y fijo en el código:

| Qué | Dónde |
|---|---|
| Dirección remitente | `CorreosView.obtenerUsuarioCorreo()` (escrita en el código) |
| Servidor y puerto SMTP | `EmailService` (constantes `SERVIDOR_SMTP` y `PUERTO_SMTP`) |
| Identificador de la aplicación de Microsoft, permisos y tipo de cuenta | `MicrosoftAuthService` (`CLIENT_ID`, `AUTHORITY`, `SCOPES`) |

Notas:
- `AUTHORITY` termina en `/consumers`, así que solo acepta cuentas personales de Microsoft. Para cuentas de empresa (Microsoft 365) hay que cambiar eso **y** que la aplicación esté registrada en Azure para ese tipo de cuenta.
- `ConfiguracionService` ya tiene métodos para servidor, puerto y remitente que no se usan. Una mejora natural es conectarlos para no escribir esos valores en el código.

## 7. Cambiar tipos de contrato

- Desde la aplicación: **Configuración > Contratos**.
- Valores por defecto (primera ejecución): `ConfiguracionService`, constante `TIPOS_CONTRATO_DEFECTO`.

## 8. Cambiar la apariencia (fuentes, colores, logo)

| Quiero cambiar | Dónde |
|---|---|
| Tamaño de fuente o filas alternadas | **Configuración > Apariencia** |
| Colores y estilos | Están escritos en cada vista con `setStyle(...)` (estilos JavaFX). No hay archivo CSS: hay que cambiarlos clase por clase |
| Logo e imágenes | `src/main/resources/images/` (`logo.png`, `inicio.jpeg`, `encabezado.jpeg`). Mantén los mismos nombres o cambia las referencias en las vistas |
| Pantalla de bienvenida | `SplashView` |
| Texto de "Acerca de" | `ConfiguracionView.mostrarAcercaDe` |

## 9. Cambiar dónde se guardan las cartas o los datos

⚠️ Las rutas son **relativas** y están repartidas:

| Carpeta | Dónde se define |
|---|---|
| `datos/` y `carta_oferta.db` | `DatabaseManager` y `ConfiguracionService` |
| `cartas_generadas/` | `PptGeneratorService` y `MainView.generarCartas` (dos lugares) |
| `plantillas/` | `PlantillaService` |
| `plantilla_html/` | `ConfiguracionService` y `CorreosView` |

Si cambias una ruta, cámbiala en todos los lugares de la tabla. Las rutas de cartas **ya generadas** quedan guardadas en la base de datos y no se actualizan solas.

## 10. Cambiar qué columnas importa o exporta el Excel

- Importar: `ExcelImportService.importar` (el `switch` de encabezados normalizados). Para aceptar otro nombre de columna, agrega otro `case`.
- Exportar: `ExcelExportService.exportar`.

## 11. Empaquetar la aplicación (generar-portable.bat)

Pasos:

1. `mvn clean package` (genera `target/carta-oferta-express-1.8.jar` y `target/lib/`).
2. Verifica que exista `native/jacob-1.18-x64.dll`.
3. Ejecuta `generar-portable.bat`.
4. Resultado: `salida\CartaOfertaExpress\CartaOfertaExpress.exe`.

Qué hace el script: borra y recrea `dist/`, copia el jar y las librerías (separando los `javafx-*-win.jar` en `dist/mods/`), copia la DLL y llama a `jpackage` con el icono `icono.ico`.

⚠️ Puntos delicados:
- **Si cambias la versión de la aplicación** (`<version>` en el `pom.xml`) o de una dependencia, el nombre del jar cambia y el `.bat` falla, porque tiene `carta-oferta-express-1.8.jar` escrito varias veces y `jacob-1.18-x64.dll`. Actualiza esos nombres.
- Si agregas una dependencia de JavaFX nueva (por ejemplo `javafx-fxml`), hay que agregarla también en `--add-modules` dentro del `jpackage`.
- Si agregas una librería con DLL propia, hay que copiarla al portable.
- `jpackage` requiere JDK 21 en el `PATH`.
- No subas `dist/` ni `salida/` a GitHub (ya están en el `.gitignore`).

## 12. Cambiar la versión de la aplicación (por ejemplo de 1.8 a 2.0)

La versión está en **cuatro lugares** y deben coincidir:

| # | Archivo | Qué cambiar |
|---|---|---|
| 1 | `ConfiguracionView.mostrarAcercaDe` | El texto "Versión 1.8" que ve el usuario (busca `1.8` con `Ctrl + Shift + F`) |
| 2 | `pom.xml` | `<version>` del proyecto (la que está debajo de `<artifactId>carta-oferta-express</artifactId>`, **no** las de las dependencias) |
| 3 | `generar-portable.bat` | El nombre del jar (`carta-oferta-express-1.8.jar`), que aparece varias veces. Opcional: `--app-version` en el comando `jpackage` |
| 4 | `README.md` y `docs/` | El nombre del jar y la línea "Versión" |

Después: recarga Maven en IntelliJ, ejecuta `mvn clean package`, comprueba que se generó `target/carta-oferta-express-<versión>.jar` y prueba `generar-portable.bat`. Para marcar la versión en GitHub: `git tag v2.0` y `git push --tags`.

## 13. Actualizar la versión de la base de datos sin perder datos

- Nunca borres `datos/carta_oferta.db` para "arreglar" un cambio de estructura: perderías los candidatos.
- Usa siempre `agregarColumnaSiNoExiste`. SQLite no permite quitar ni cambiar columnas fácilmente con `ALTER TABLE`; si necesitas eso, haz una copia de seguridad del `.db` antes.

---

## Lista de comprobación antes de unir cambios a `main`

- [ ] Compila con `mvn clean package` sin errores.
- [ ] Se puede crear, editar y eliminar un candidato.
- [ ] Importar un Excel de prueba funciona.
- [ ] Generar una carta produce el `.pptx` y el `.pdf`, y el PDF se ve bien.
- [ ] El envío de correo funciona con una cuenta de prueba (a tu propio correo).
- [ ] Si hubo cambios en la base de datos, probaste con una base **ya existente** (no solo con una nueva).
- [ ] No hay datos reales, contraseñas ni archivos de `datos/` o `cartas_generadas/` en `git status`.
