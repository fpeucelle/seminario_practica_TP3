package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import modelo.DetalleSolicitud;
import modelo.SolicitudCompra;

public class SolicitudDAOMySQL implements ISolicitudDAO {
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/sic_stj_db";
    private static final String USER = "root";
    private static final String PASSWORD = "Root";

    @Override
    public boolean registrarSolicitud(SolicitudCompra solicitud) {
        String queryCabecera = "INSERT INTO solicitudes_compra "
                + "(id_usuario, fecha_solicitud, estado, justificacion) "
                + "VALUES (?, CURDATE(), ?, ?)";
        String queryDetalle = "INSERT INTO detalles_solicitud "
                + "(id_solicitud, id_bien, cantidad, precio_estimado) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(URL, USER, PASSWORD)) {
            con.setAutoCommit(false);

            try (PreparedStatement pstCabecera = con.prepareStatement(queryCabecera, Statement.RETURN_GENERATED_KEYS)) {
                pstCabecera.setInt(1, solicitud.getIdUsuario());
                pstCabecera.setString(2, solicitud.getEstado());
                pstCabecera.setString(3, "Justificacion automatica prototipo Java");

                int filasAfectadas = pstCabecera.executeUpdate();
                if (filasAfectadas == 0) {
                    con.rollback();
                    return false;
                }

                int idSolicitud = obtenerIdSolicitudGenerado(pstCabecera);
                solicitud.setIdSolicitud(idSolicitud);

                try (PreparedStatement pstDetalle = con.prepareStatement(queryDetalle)) {
                    for (DetalleSolicitud detalle : solicitud.getDetalles()) {
                        pstDetalle.setInt(1, idSolicitud);
                        pstDetalle.setInt(2, detalle.getBien().getIdBien());
                        pstDetalle.setInt(3, detalle.getCantidad());
                        pstDetalle.setDouble(4, detalle.getBien().getPrecioReferencia());
                        pstDetalle.addBatch();
                    }

                    pstDetalle.executeBatch();
                }

                con.commit();
                return true;
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            System.err.println("Error de Base de Datos: " + e.getMessage());
            return false;
        }
    }

    private int obtenerIdSolicitudGenerado(PreparedStatement pst) throws SQLException {
        try (ResultSet rs = pst.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("No se pudo obtener el ID generado para la solicitud.");
    }
}
