# Alcance TP4 - SIC-STJ

## Relacion AP4 y L4

El prototipo conserva el dominio del AP4: SIC-STJ y el caso de uso CU-01, Registrar solicitud de compra. La informacion L4 se utiliza como soporte tecnico para reforzar JDBC, MVC, arreglos, `ArrayList`, excepciones verificadas y archivos con `java.io`; no se reemplaza el dominio por ECOViento.

## Evidencia de criterios

- Patron MVC: `MainApp` actua como vista, `SolicitudController` como controlador, `modelo` como dominio y `dao` como persistencia.
- Persistencia MySQL: `SolicitudDAOMySQL.registrarSolicitud` inserta cabecera y detalle en una transaccion.
- Consulta MySQL: `listarSolicitudesResumen` ejecuta un reporte con JOIN, `COUNT` y `SUM`.
- Consultas operativas MySQL: el menu permite listar bienes homologados y usuarios solicitantes para seleccionar IDs validos desde la interfaz.
- Actualizacion MySQL: `actualizarEstado` modifica `solicitudes_compra.estado`.
- Validaciones operativas: el alta valida usuario existente y justificacion obligatoria; la actualizacion de estado se elige por numero y requiere confirmacion.
- Excepciones: `ValidacionException` separa reglas de negocio y `PersistenciaException` separa errores JDBC.
- Interfaces y abstraccion: `ISolicitudDAO`, `INotificador` y `SolicitudCompra`.
- Arreglo + ArrayList: `CatalogoBienes` usa `BienCatalogo[]` y busqueda binaria como evidencia academica complementaria; la operatoria real de bienes consulta MySQL. `SolicitudCompra` usa `ArrayList<DetalleSolicitud>` para items dinamicos.
- Archivos: `BitacoraArchivo` guarda y lee eventos con clases de `java.io`.

## Pendientes dificiles documentados

- Swing completo: no se implementa para evitar romper el alcance AP4; la consola es la vista operacional del prototipo.
- Gestion documental completa: solo se valida el adjunto; no se copian ni almacenan archivos en repositorio documental.
- Notificacion real: se simula por consola porque email/integraciones externas exceden el prototipo.
- Auditoria inmutable/hash: se reemplaza por bitacora local demostrativa.
- Workflow completo CU-02: se implementa actualizacion simple de estado, sin circuito de aprobacion integral.
- Portal web, buzon digital y datos abiertos: quedan fuera del CU-01 operacional.
