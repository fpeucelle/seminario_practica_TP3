package dao;

import modelo.SolicitudCompra;

public interface ISolicitudDAO {
    boolean registrarSolicitud(SolicitudCompra solicitud);
}
