# Descripcion general de cambios implementados en AP4

## Proposito de la actualizacion

En esta cuarta actividad se evoluciono el prototipo del Sistema Integral de Compras y Contrataciones del STJ Chubut para que deje de ser solamente una demostracion de carga de solicitudes y pase a comportarse como un prototipo operacional mas completo. El foco se mantuvo en el caso de uso CU-01, Registrar solicitud de compra, porque es el flujo principal ya definido en el AP4 y permite demostrar los conceptos solicitados sin cambiar el dominio del proyecto.

Los cambios se orientaron a cubrir los puntos academicos de la consigna: patron MVC, persistencia MySQL, consulta y actualizacion de datos, manejo de excepciones, interfaces, clases abstractas, arreglos, `ArrayList` y uso de archivos.

## Arquitectura MVC y separacion de responsabilidades

Se reforzo la organizacion del prototipo bajo el patron Modelo-Vista-Controlador. La vista por consola permanece como interfaz operacional del prototipo, mientras que el controlador concentra las validaciones y coordina las acciones del usuario. El modelo conserva las entidades propias del negocio, como solicitudes, detalles y bienes del catalogo. La persistencia queda separada en el paquete DAO.

Esta separacion tiene un motivo academico y practico: permite demostrar bajo acoplamiento entre la interfaz, la logica de negocio y la base de datos. El controlador depende del contrato `ISolicitudDAO` y no de detalles JDBC concretos. En esta version final, la unica implementacion operativa incluida es MySQL, porque la consigna solicita persistencia directa en base de datos.

## Persistencia, consulta y actualizacion en MySQL

La implementacion MySQL se amplio para cubrir no solo el alta de solicitudes, sino tambien la consulta y actualizacion de registros. El prototipo ahora permite registrar una solicitud con su detalle, listar solicitudes mediante un reporte integral con uniones entre tablas y actualizar el estado de una solicitud existente. Ademas, el alta muestra el identificador generado por MySQL, lo que permite usar ese dato luego para consultar o actualizar la solicitud.

El motivo de este cambio es responder directamente a la consigna del TP4, que solicita establecer conexiones, realizar consultas, actualizar registros y presentar resultados en la interfaz. Para ello se usa JDBC con `Connection`, `PreparedStatement` y `ResultSet`, manteniendo la estructura normalizada de la base de datos definida para el SIC-STJ.

## Manejo de excepciones

Se separaron las excepciones de negocio y las excepciones de persistencia. Las validaciones del dominio, como cantidades invalidas, bienes inexistentes, usuarios solicitantes inexistentes, justificacion obligatoria o adjuntos no permitidos, se informan mediante `ValidacionException`. Los problemas vinculados con MySQL se encapsulan mediante `PersistenciaException`.

Esta decision permite mostrar con claridad el tratamiento de errores pedido en la teoria: no todos los errores tienen la misma causa ni deben resolverse en la misma capa. El usuario recibe mensajes controlados y el programa evita terminar abruptamente ante entradas invalidas o fallas de base de datos.

## Interfaces, clase abstracta y polimorfismo

El prototipo conserva `SolicitudCompra` como clase abstracta y sus especializaciones `SolicitudOrdinaria` y `SolicitudUrgente`. Cada tipo resuelve de forma polimorfica su plazo de SLA mediante `calcularPlazoSLA()`.

Tambien se amplio el contrato `ISolicitudDAO`, que define las operaciones de persistencia sin atar el controlador a detalles internos de JDBC. Esto permite demostrar el uso pertinente de interfaces manteniendo la persistencia real en MySQL como implementacion concreta del prototipo final.

## Arreglos y ArrayList

Para cumplir el uso complementario de estructuras, se incorporo un catalogo de bienes basado en un arreglo fijo `BienCatalogo[]`. Sobre ese arreglo se aplica una busqueda binaria por identificador, lo que permite mostrar una estructura estatica y un algoritmo de busqueda.

En paralelo, la solicitud conserva sus items mediante `ArrayList<DetalleSolicitud>`, porque la cantidad de bienes pedidos es dinamica y depende de la carga realizada por el usuario. Asi se justifica la diferencia entre una estructura fija para datos maestros y una coleccion dinamica para datos transaccionales.

## Archivos, bitacora y adjuntos

Se agrego una bitacora local en archivo para registrar eventos relevantes del prototipo: altas, consultas, actualizaciones, errores de validacion y notificaciones simuladas. Esta bitacora permite evidenciar el uso de `java.io` y representa una version simplificada de las pistas de auditoria mencionadas en el AP4.

Tambien se incorporo una validacion simple de adjuntos. El prototipo permite continuar sin adjunto, pero si el usuario informa una ruta se controla que el archivo exista, tenga una extension permitida y no supere los 10 MB. No se implementa almacenamiento documental completo porque excede el alcance operacional del CU-01.

## Interfaz de consola y menu operativo

El menu se amplio para que el usuario pueda registrar una solicitud de compra, actualizar estados, listar solicitudes, consultar bienes homologados, consultar usuarios solicitantes y revisar la bitacora. La opcion de alta solicita dentro del flujo si la solicitud es ordinaria o urgente, manteniendo el polimorfismo sin duplicar opciones de menu. Las consultas de bienes y usuarios hacen que el prototipo sea mas operacional, porque el operador puede conocer desde la propia interfaz que IDs existen en MySQL antes de cargar una solicitud. La actualizacion de estado se realiza seleccionando un numero de la lista de estados permitidos y confirmando la operacion antes de impactar la base. Las validaciones se prueban dentro del flujo real de registro manual, ingresando valores no validos como un bien inexistente, un usuario inexistente o una cantidad menor o igual a cero. Tambien se corrigio la lectura de opciones para que un valor invalido no se interprete como salida del sistema.

La consola se mantiene porque el AP4 ya la presenta como interfaz operacional suficiente para el prototipo. La implementacion Swing completa queda documentada como evolucion posible, pero no se incorporo para evitar agregar complejidad visual que no cambia el cumplimiento central de la consigna.

## Pendientes tratados como alcance documentado

Algunos puntos mencionados como trabajo futuro en el AP4 se resolvieron de forma acotada y demostrativa. La bitacora se implemento como archivo local, las notificaciones se simulan por consola y la administracion de estados se cubre mediante una actualizacion basica. En cambio, quedan fuera de alcance el workflow completo de autorizacion, el envio real de correos, el hash criptografico, el buzon digital, el portal web y la gestion documental completa.

Esta decision permite que el prototipo sea operacional y evaluable, sin convertirlo en un sistema institucional completo. El resultado final muestra los conceptos teoricos solicitados y mantiene coherencia con el alcance academico del AP4.
