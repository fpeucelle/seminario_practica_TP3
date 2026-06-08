package controlador;

import dao.ISolicitudDAO;
import excepciones.ValidacionException;
import modelo.SolicitudCompra;

public class SolicitudController {
    private final ISolicitudDAO dao;

    public SolicitudController(ISolicitudDAO dao) {
        this.dao = dao;
    }

    public void procesarGuardado(SolicitudCompra solicitud) throws ValidacionException {
        if (solicitud == null) {
            throw new ValidacionException("La solicitud no puede ser nula.");
        }

        if (solicitud.getDetalles().isEmpty()) {
            throw new ValidacionException("No se puede registrar una solicitud vacia. Ingrese items.");
        }

        if (solicitud.calcularMontoTotal() <= 0) {
            throw new ValidacionException("El monto de la solicitud debe ser mayor a 0.");
        }

        solicitud.setEstado("Pendiente de autorizacion");

        boolean exito = dao.registrarSolicitud(solicitud);

        if (exito) {
            System.out.println(">> EXITO: Solicitud persistida correctamente.");
        } else {
            System.out.println(">> AVISO: Solicitud procesada en memoria. Configure MySQL para persistir.");
        }
    }
}
