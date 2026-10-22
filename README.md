# CartaOferta Express

Aplicación de escritorio para Windows que genera cartas de oferta en PDF a partir de los datos de los candidatos de un proceso de selección.

**Versión 2.0**

<!-- 
![Pantalla principal](docs/img/captura1.png)
-->

## El problema que resuelve

Preparar cartas de oferta una por una es un trabajo manual, repetitivo y propenso a errores de digitación. CartaOferta Express automatiza el ciclo completo: registra o importa candidatos, llena una plantilla de PowerPoint con sus datos, y exporta cada carta a PDF. Es una aplicación real, usada en un proceso de selección.

## Funcionalidades

- **Gestión de candidatos:** crear, editar, eliminar, buscar y resaltar candidatos, con validación de cédula, celular, correo y fecha de ingreso.
- **Importación y exportación en Excel:** carga masiva desde `.xlsx` (reconoce encabezados con o sin tildes y con variantes como "tipo de contrato" o "TipoContrato") y exportación de la lista.
- **Generación masiva de cartas:** selecciona varios candidatos y genera sus cartas en una sola operación, con barra de progreso y opción de cancelar.
- **Plantillas de PowerPoint:** se administran desde la propia aplicación; las variables `{{NOMBRE}}`, `{{CARGO}}`, `{{SALARIO}}`, etc. se reemplazan automáticamente.
- **Exportación a PDF:** cada carta se convierte a PDF usando PowerPoint.
- **Envío de correos con autenticación Microsoft (OAuth 2.0), implementado pero no en uso:** correo HTML con vista previa y la carta en PDF adjunta, con MSAL4J y Jakarta Mail. Se probado con una cuenta personal de Microsoft y un registro propio en Azure, pero no se llegó a configurar con una cuenta corporativa, por lo que no está en uso en el entorno de trabajo. Ver [Estado del envío de correos](#estado-del-envío-de-correos).
- **Configuración:** tamaño de fuentes, filas alternadas y tipos de contrato editables.
- **Almacenamiento local** en SQLite, con migración automática de la estructura al actualizar la aplicación.

## Tecnologías

| Área | Tecnología |
|---|---|
| Lenguaje | Java 21 |
| Interfaz | JavaFX 21.0.8 (pantallas construidas por código) |
| Construcción | Maven |
| Base de datos | SQLite (`sqlite-jdbc` 3.50.3.0) |
| Excel y PowerPoint | Apache POI 5.4.1 |
| PowerPoint a PDF | Jacob 1.18 (puente COM hacia PowerPoint) |
| Correo (no en uso) | Jakarta Mail 2.0.1 (SMTP con XOAUTH2) |
| Autenticación (no en uso) | MSAL4J 1.17.3 (Microsoft) |
| Empaquetado | `jpackage` (ejecutable portable) |

## Arquitectura

Aplicación organizada en capas dentro del paquete `com.cartaofertaexpress`:

| Capa | Responsabilidad |
|---|---|
| `ui` | Pantallas JavaFX: `SplashView`, `MainView`, `CandidatoFormView`, `PlantillasView`, `CorreosView`, `ConfiguracionView` |
| `service` | Lógica: validación, Excel, generación de PowerPoint, conversión a PDF, correo, autenticación, configuración y plantillas |
| `repository` | Acceso a datos de candidatos (`CandidatoRepository`) |
| `database` | Conexión e inicialización de SQLite (`DatabaseManager`) |
| `model` | Entidad `Candidato` |

Documentación detallada:

- [Arquitectura: qué hace cada clase](docs/ARQUITECTURA.md)
- [Flujo completo: del dato al PDF y al correo](docs/FLUJO.md)
- [Guía de modificaciones: "si quiero cambiar X, toco Y"](docs/GUIA_MODIFICACIONES.md)

## Requisitos

- Windows (64 bits).
- **Microsoft PowerPoint instalado:** la conversión a PDF se hace controlando PowerPoint mediante COM.
- JDK 21 (incluye `jpackage`, necesario solo para generar el portable).
- Maven.

## Compilar y ejecutar

El repositorio no incluye binarios. Antes de ejecutar, prepara esto:

1. **Librería nativa de Jacob.** Coloca `jacob-1.18-x64.dll` en una carpeta `native/` en la raíz del proyecto.
2. **Compilar:**

   ```bash
   mvn clean package
   ```

   Esto genera `target/carta-oferta-express-1.8.jar` y copia las dependencias en `target/lib/`.

3. **Ejecutar en desarrollo:**

   ```bash
   mvn javafx:run
   ```

   El plugin ya está configurado en el `pom.xml` con `-Djava.library.path=${project.basedir}/native`.

### Generar el ejecutable portable

Después de `mvn clean package`, ejecuta:

```
generar-portable.bat
```

El script prepara la carpeta `dist/` (jar, librerías, módulos de JavaFX y la DLL de Jacob) y usa `jpackage` para crear `salida\CartaOfertaExpress\CartaOfertaExpress.exe`.

## Estado del envío de correos

El código de envío está completo (`CorreosView`, `EmailService`, `MicrosoftAuthService`) y funcionó en pruebas con una cuenta personal de Hotmail y una aplicación registrada en Azure. Sin embargo, **no se configuró con una cuenta corporativa** y por eso no se usa en el entorno de trabajo. Para usarlo con otra cuenta haría falta:

- Registrar la aplicación en Azure para el tipo de cuenta que corresponda (la autoridad actual es `consumers`, solo cuentas personales).
- Reemplazar el correo remitente y los datos de la aplicación, que hoy están escritos en el código.

Detalles en [docs/GUIA_MODIFICACIONES.md](docs/GUIA_MODIFICACIONES.md).

## Datos que la aplicación crea al ejecutarse

Todas las rutas son relativas a la carpeta desde donde se ejecuta la aplicación:

| Carpeta | Contenido |
|---|---|
| `datos/` | Base de datos `carta_oferta.db` y `configuracion.properties` |
| `plantillas/` | Plantillas de PowerPoint |
| `plantilla_html/` | Plantilla HTML del correo (`correo.html`) |
| `cartas_generadas/<dd-MM-yyyy>/` | Cartas generadas (`.pptx` y `.pdf`) |

Estas carpetas contienen datos de candidatos y no se suben al repositorio.

## Estructura del proyecto

```
src/main/java/com/cartaofertaexpress/
├── Main.java
├── ui/            # Pantallas JavaFX
├── service/       # Lógica de negocio e integraciones
├── repository/    # Acceso a datos
├── database/      # SQLite
└── model/         # Candidato
src/main/resources/images/
pom.xml
generar-portable.bat
docs/
```

## Capturas

<!-- 
[COMPLETAR: capturas de la pantalla principal, el formulario y el envío de correos, con datos inventados]
-->

## Autor

<!-- [COMPLETAR: nombre y enlace a perfil]-->
