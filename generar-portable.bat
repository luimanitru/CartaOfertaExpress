@echo off
setlocal

echo ============================================
echo   Generando portable de CartaOfertaExpress
echo ============================================

REM Verificar que exista el jar compilado
if not exist target\carta-oferta-express-1.0-SNAPSHOT.jar (
    echo.
    echo ERROR: No se encontro target\carta-oferta-express-1.0-SNAPSHOT.jar
    echo Compila el proyecto primero desde IntelliJ.
    pause
    exit /b 1
)

REM 1. Limpiar y crear estructura dist
echo.
echo [1/3] Preparando carpeta dist...
if exist dist rmdir /s /q dist
mkdir dist
mkdir dist\lib
mkdir dist\mods
mkdir dist\native

REM 2. Copiar archivos
echo.
echo [2/3] Copiando archivos...
copy target\carta-oferta-express-1.0-SNAPSHOT.jar dist\ >nul

for %%f in (target\lib\*.jar) do (
    echo %%~nxf | findstr /i "javafx" >nul
    if errorlevel 1 (
        copy "%%f" dist\lib\ >nul
    )
)

copy target\lib\javafx-*-win.jar dist\mods\ >nul
copy native\jacob-1.18-x64.dll dist\native\ >nul

REM 3. Generar el ejecutable con jpackage
echo.
echo [3/3] Generando ejecutable con jpackage...
if exist salida rmdir /s /q salida

jpackage --type app-image --name CartaOfertaExpress --input dist --main-jar carta-oferta-express-1.0-SNAPSHOT.jar --main-class com.cartaofertaexpress.Main --icon icono.ico --dest salida --java-options "-Djava.library.path=$APPDIR/native" --java-options "--module-path=$APPDIR/mods" --java-options "--add-modules=javafx.controls,javafx.web" --java-options "--class-path=$APPDIR/carta-oferta-express-1.0-SNAPSHOT.jar;$APPDIR/lib/*"

if errorlevel 1 (
    echo.
    echo ERROR: Fallo jpackage. Revisa los errores arriba.
    pause
    exit /b 1
)

echo.
echo ============================================
echo   LISTO! Tu portable esta en:
echo   salida\CartaOfertaExpress\CartaOfertaExpress.exe
echo ============================================
pause