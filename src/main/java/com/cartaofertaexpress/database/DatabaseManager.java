package com.cartaofertaexpress.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String DATABASE_FOLDER = "datos";
    private static final String DATABASE_FILE = "carta_oferta.db";

    private static final String DATABASE_URL =
            "jdbc:sqlite:" + DATABASE_FOLDER + File.separator + DATABASE_FILE;

    /**
     * Inicializa la base de datos.
     */
    public static void initializeDatabase() {
        createDatabaseFolder();
        createTables();
        actualizarEstructura();
    }

    /**
     * Crea la carpeta donde estará la base de datos.
     */
    private static void createDatabaseFolder() {

        File folder =
                new File(DATABASE_FOLDER);

        if (!folder.exists()) {

            boolean created =
                    folder.mkdirs();

            if (created) {

                System.out.println(
                        "Carpeta 'datos' creada."
                );
            }
        }
    }

    /**
     * Crea las tablas necesarias.
     */
    private static void createTables() {

        String sql = """
                CREATE TABLE IF NOT EXISTS candidatos (
                    
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    
                    nombre TEXT NOT NULL,
                    cedula TEXT NOT NULL UNIQUE,
                    cargo TEXT NOT NULL,
                    salario REAL,
                    rol TEXT,
                    tipo_contrato TEXT,
                    fecha_ingreso TEXT,
                    ciudad TEXT,
                    tasa TEXT,
                    celular TEXT,
                    correo TEXT,
                    
                    activo INTEGER NOT NULL DEFAULT 1,
                    resaltado INTEGER NOT NULL DEFAULT 0,
                    
                    fecha_creacion TEXT NOT NULL,
                    fecha_modificacion TEXT NOT NULL
                );
                """;

        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            statement.execute(sql);

            System.out.println(
                    "Base de datos inicializada correctamente."
            );

            System.out.println(
                    "Tabla 'candidatos' lista."
            );

        } catch (SQLException e) {

            System.err.println(
                    "Error inicializando la base de datos:"
            );

            e.printStackTrace();
        }
    }

    /**
     * Actualiza la estructura de la base de datos
     * para versiones anteriores del programa.
     */
    private static void actualizarEstructura() {
        System.out.println(
                ">>> Ejecutando actualización de estructura"
        );
        try (
                Connection connection =
                        getConnection();

                Statement statement =
                        connection.createStatement()
        ) {

            agregarColumnaSiNoExiste(
                    statement,
                    "carta_generada",
                    "INTEGER NOT NULL DEFAULT 0"
            );

            agregarColumnaSiNoExiste(
                    statement,
                    "archivo_carta",
                    "TEXT"
            );

            agregarColumnaSiNoExiste(
                    statement,
                    "fecha_carta",
                    "TEXT"
            );

            agregarColumnaSiNoExiste(
                    statement,
                    "plantilla_carta",
                    "TEXT"
            );

            agregarColumnaSiNoExiste(
                    statement,
                    "fecha_correo",
                    "TEXT"
            );

            agregarColumnaSiNoExiste(
                    statement,
                    "resaltado",
                    "INTEGER NOT NULL DEFAULT 0"
            );



            System.out.println(
                    "Estructura de cartas actualizada correctamente."
            );

        } catch (SQLException e) {

            System.err.println(
                    "Error actualizando la estructura de la base de datos:"
            );

            e.printStackTrace();
        }
    }

    /**
     * Agrega una columna solamente si todavía no existe.
     */
    private static void agregarColumnaSiNoExiste(
            Statement statement,
            String nombreColumna,
            String definicion
    ) throws SQLException {

        try {

            statement.execute(
                    "ALTER TABLE candidatos ADD COLUMN "
                            + nombreColumna
                            + " "
                            + definicion
            );

            System.out.println(
                    "Columna agregada: "
                            + nombreColumna
            );

        } catch (SQLException e) {

            /*
             * SQLite lanza un error si la columna
             * ya existe.
             *
             * En ese caso simplemente continuamos.
             */
            if (!e.getMessage()
                    .toLowerCase()
                    .contains("duplicate column")) {

                throw e;
            }
        }
    }

    /**
     * Obtiene una conexión a SQLite.
     */
    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                DATABASE_URL
        );
    }
}