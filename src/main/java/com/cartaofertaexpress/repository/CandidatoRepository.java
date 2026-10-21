package com.cartaofertaexpress.repository;

import com.cartaofertaexpress.database.DatabaseManager;
import com.cartaofertaexpress.model.Candidato;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CandidatoRepository {

    // =====================================================
    // FORMATO DE FECHA Y HORA
    // =====================================================

    private static final DateTimeFormatter FORMATO_FECHA_HORA =
            DateTimeFormatter.ofPattern(
                    "dd/MM/yyyy HH:mm:ss"
            );

    // =====================================================
    // GUARDAR
    // =====================================================

    public Candidato guardar(Candidato candidato) {

        String sql = """
                INSERT INTO candidatos (
                    nombre,
                    cedula,
                    cargo,
                    salario,
                    rol,
                    tipo_contrato,
                    fecha_ingreso,
                    ciudad,
                    tasa,
                    celular,
                    correo,
                    activo,
                    resaltado,
                    fecha_creacion,
                    fecha_modificacion,
                    carta_generada,
                    archivo_carta,
                    fecha_carta,
                    plantilla_carta
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        String ahora = LocalDateTime.now().toString();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(
                                sql,
                                Statement.RETURN_GENERATED_KEYS
                        )
        ) {

            statement.setString(1, candidato.getNombre());
            statement.setString(2, candidato.getCedula());
            statement.setString(3, candidato.getCargo());

            if (candidato.getSalario() != null) {
                statement.setBigDecimal(
                        4,
                        candidato.getSalario()
                );
            } else {
                statement.setNull(
                        4,
                        Types.NUMERIC
                );
            }

            statement.setString(5, candidato.getRol());
            statement.setString(6, candidato.getTipoContrato());
            statement.setString(7, candidato.getFechaIngreso());
            statement.setString(8, candidato.getCiudad());
            statement.setString(9, candidato.getTasa());
            statement.setString(10, candidato.getCelular());
            statement.setString(11, candidato.getCorreo());

            statement.setInt(
                    12,
                    candidato.isActivo() ? 1 : 0
            );

            statement.setInt(
                    13,
                    candidato.isResaltado() ? 1 : 0
            );

            statement.setString(14, ahora);
            statement.setString(15, ahora);

            statement.setInt(
                    16,
                    candidato.isCartaGenerada() ? 1 : 0
            );

            statement.setString(
                    17,
                    candidato.getArchivoCarta()
            );

            statement.setString(
                    18,
                    candidato.getFechaCarta()
            );

            statement.setString(
                    19,
                    candidato.getPlantillaCarta()
            );

            statement.executeUpdate();

            try (
                    ResultSet keys =
                            statement.getGeneratedKeys()
            ) {

                if (keys.next()) {

                    candidato.setId(
                            keys.getInt(1)
                    );
                }
            }

            return candidato;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error guardando el candidato: "
                            + e.getMessage(),
                    e
            );
        }
    }

    // =====================================================
    // BUSCAR TODOS
    // =====================================================

    public List<Candidato> buscarTodos() {

        String sql = """
                SELECT *
                FROM candidatos
                WHERE activo = 1
                ORDER BY nombre
                """;

        List<Candidato> candidatos =
                new ArrayList<>();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()
        ) {

            while (resultSet.next()) {

                candidatos.add(
                        mapearCandidato(resultSet)
                );
            }

            return candidatos;

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error consultando los candidatos.",
                    e
            );
        }
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================

    public Candidato buscarPorId(int id) {

        String sql = """
                SELECT *
                FROM candidatos
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapearCandidato(
                            resultSet
                    );
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error buscando el candidato.",
                    e
            );
        }
    }

    // =====================================================
    // BUSCAR POR CÉDULA
    // =====================================================

    public Candidato buscarPorCedula(
            String cedula
    ) {

        String sql = """
                SELECT *
                FROM candidatos
                WHERE cedula = ?
                """;

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    cedula
            );

            try (
                    ResultSet resultSet =
                            statement.executeQuery()
            ) {

                if (resultSet.next()) {

                    return mapearCandidato(
                            resultSet
                    );
                }

                return null;
            }

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error buscando por cédula.",
                    e
            );
        }
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================

    public void actualizar(
            Candidato candidato
    ) {

        String sql = """
                UPDATE candidatos
                SET
                    nombre = ?,
                    cedula = ?,
                    cargo = ?,
                    salario = ?,
                    rol = ?,
                    tipo_contrato = ?,
                    fecha_ingreso = ?,
                    ciudad = ?,
                    tasa = ?,
                    celular = ?,
                    correo = ?,
                    carta_generada = ?,
                    archivo_carta = ?,
                    fecha_carta = ?,
                    plantilla_carta = ?,
                    fecha_modificacion = ?,
                    resaltado = ?
                WHERE id = ?
                """;

        String ahora = LocalDateTime.now().toString();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    candidato.getNombre()
            );

            statement.setString(
                    2,
                    candidato.getCedula()
            );

            statement.setString(
                    3,
                    candidato.getCargo()
            );

            if (candidato.getSalario() != null) {

                statement.setBigDecimal(
                        4,
                        candidato.getSalario()
                );

            } else {

                statement.setNull(
                        4,
                        Types.NUMERIC
                );
            }

            statement.setString(
                    5,
                    candidato.getRol()
            );

            statement.setString(
                    6,
                    candidato.getTipoContrato()
            );

            statement.setString(
                    7,
                    candidato.getFechaIngreso()
            );

            statement.setString(
                    8,
                    candidato.getCiudad()
            );

            statement.setString(
                    9,
                    candidato.getTasa()
            );

            statement.setString(
                    10,
                    candidato.getCelular()
            );

            statement.setString(
                    11,
                    candidato.getCorreo()
            );

            statement.setInt(
                    12,
                    candidato.isCartaGenerada()
                            ? 1
                            : 0
            );

            statement.setString(
                    13,
                    candidato.getArchivoCarta()
            );

            statement.setString(
                    14,
                    candidato.getFechaCarta()
            );

            statement.setString(
                    15,
                    candidato.getPlantillaCarta()
            );

            statement.setString(
                    16,
                    ahora
            );

            statement.setInt(
                    17,
                    candidato.isResaltado()
                            ? 1
                            : 0
            );

            statement.setInt(
                    18,
                    candidato.getId()
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error actualizando el candidato.",
                    e
            );
        }
    }

    // =====================================================
    // CAMBIAR RESALTADO
    // =====================================================

    public void cambiarResaltado(
            int candidatoId,
            boolean resaltado
    ) {

        String sql = """
                UPDATE candidatos
                SET
                    resaltado = ?,
                    fecha_modificacion = ?
                WHERE id = ?
                """;

        String ahora = LocalDateTime.now().toString();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    resaltado ? 1 : 0
            );

            statement.setString(
                    2,
                    ahora
            );

            statement.setInt(
                    3,
                    candidatoId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error cambiando el resaltado del candidato.",
                    e
            );
        }
    }

    // =====================================================
    // REGISTRAR CARTA GENERADA
    // =====================================================

    public void registrarCartaGenerada(
            int candidatoId,
            String archivoCarta,
            String fechaCarta,
            String plantillaCarta
    ) {

        String sql = """
                UPDATE candidatos
                SET
                    carta_generada = 1,
                    archivo_carta = ?,
                    fecha_carta = ?,
                    plantilla_carta = ?,
                    fecha_modificacion = ?
                WHERE id = ?
                """;

        String ahora = LocalDateTime.now().toString();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    archivoCarta
            );

            statement.setString(
                    2,
                    fechaCarta
            );

            statement.setString(
                    3,
                    plantillaCarta
            );

            statement.setString(
                    4,
                    ahora
            );

            statement.setInt(
                    5,
                    candidatoId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error registrando la carta generada.",
                    e
            );
        }
    }

    // =====================================================
    // CAMBIAR ESTADO DEL CORREO
    // =====================================================

    /**
     * Marca si el correo del candidato fue enviado o no.
     *
     * enviado = true:
     *      Guarda la fecha y hora actual.
     *
     * enviado = false:
     *      Elimina la fecha del correo.
     */
    public void cambiarEstadoCorreo(
            int candidatoId,
            boolean enviado
    ) {

        String sql = """
                UPDATE candidatos
                SET
                    fecha_correo = ?,
                    fecha_modificacion = ?
                WHERE id = ?
                """;

        String ahora =
                LocalDateTime.now()
                        .format(FORMATO_FECHA_HORA);

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            if (enviado) {

                statement.setString(
                        1,
                        ahora
                );

            } else {

                statement.setNull(
                        1,
                        Types.VARCHAR
                );
            }

            statement.setString(
                    2,
                    ahora
            );

            statement.setInt(
                    3,
                    candidatoId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error cambiando el estado del correo.",
                    e
            );
        }
    }
// =====================================================
// REGISTRAR CORREO ENVIADO
// =====================================================

    public void registrarCorreoEnviado(
            int candidatoId
    ) {

        String sql = """
            UPDATE candidatos
            SET
                fecha_correo = ?,
                fecha_modificacion = ?
            WHERE id = ?
            """;

        String ahora =
                LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd/MM/yyyy HH:mm:ss"
                                )
                        );

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    ahora
            );

            statement.setString(
                    2,
                    ahora
            );

            statement.setInt(
                    3,
                    candidatoId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error registrando el correo enviado.",
                    e
            );
        }
    }
    // =====================================================
    // ELIMINAR REGISTRO DE CARTA
    // =====================================================

    public void eliminarRegistroCarta(
            int candidatoId
    ) {

        String sql = """
                UPDATE candidatos
                SET
                    carta_generada = 0,
                    archivo_carta = NULL,
                    fecha_carta = NULL,
                    plantilla_carta = NULL,
                    fecha_modificacion = ?
                WHERE id = ?
                """;

        String ahora = LocalDateTime.now().toString();

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setString(
                    1,
                    ahora
            );

            statement.setInt(
                    2,
                    candidatoId
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error eliminando el registro de la carta.",
                    e
            );
        }
    }

    // =====================================================
    // ELIMINAR CANDIDATO
    // =====================================================

    public void eliminar(int id) {

        String sql = """
                DELETE FROM candidatos
                WHERE id = ?
                """;

        try (
                Connection connection =
                        DatabaseManager.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setInt(
                    1,
                    id
            );

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(
                    "Error eliminando el candidato.",
                    e
            );
        }
    }

    // =====================================================
    // MAPEAR CANDIDATO
    // =====================================================

    private Candidato mapearCandidato(
            ResultSet resultSet
    ) throws SQLException {

        Candidato candidato =
                new Candidato();

        candidato.setId(
                resultSet.getInt("id")
        );

        candidato.setNombre(
                resultSet.getString("nombre")
        );

        candidato.setCedula(
                resultSet.getString("cedula")
        );

        candidato.setCargo(
                resultSet.getString("cargo")
        );

        BigDecimal salario =
                resultSet.getBigDecimal("salario");

        candidato.setSalario(
                salario
        );

        candidato.setRol(
                resultSet.getString("rol")
        );

        candidato.setTipoContrato(
                resultSet.getString("tipo_contrato")
        );

        candidato.setFechaIngreso(
                resultSet.getString("fecha_ingreso")
        );

        candidato.setCiudad(
                resultSet.getString("ciudad")
        );

        candidato.setTasa(
                resultSet.getString("tasa")
        );

        candidato.setCelular(
                resultSet.getString("celular")
        );

        candidato.setCorreo(
                resultSet.getString("correo")
        );

        candidato.setFechaCorreo(
                resultSet.getString("fecha_correo")
        );

        candidato.setActivo(
                resultSet.getInt("activo") == 1
        );

        candidato.setResaltado(
                resultSet.getInt("resaltado") == 1
        );

        candidato.setCartaGenerada(
                resultSet.getInt("carta_generada") == 1
        );

        candidato.setArchivoCarta(
                resultSet.getString("archivo_carta")
        );

        candidato.setFechaCarta(
                resultSet.getString("fecha_carta")
        );

        candidato.setPlantillaCarta(
                resultSet.getString("plantilla_carta")
        );

        return candidato;
    }
}