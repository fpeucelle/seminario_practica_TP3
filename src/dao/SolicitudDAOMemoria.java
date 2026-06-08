package dao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import modelo.SolicitudCompra;

public class SolicitudDAOMemoria implements ISolicitudDAO {
    private final ArrayList<SolicitudCompra> solicitudes = new ArrayList<>();

    @Override
    public boolean registrarSolicitud(SolicitudCompra solicitud) {
        solicitudes.add(solicitud);
        return true;
    }

    public List<SolicitudCompra> listarSolicitudes() {
        return Collections.unmodifiableList(solicitudes);
    }
}
