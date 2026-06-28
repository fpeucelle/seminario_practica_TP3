# SIC-STJ - Prototipo Operacional TP4

Prototipo Java para el caso de uso **CU-01: Registrar solicitud de compra** del Sistema Integral de Compras y Contrataciones del STJ Chubut. La implementacion conserva el enfoque del AP4: alcance incremental, arquitectura MVC, persistencia MySQL y una interfaz de consola suficiente para demostrar comportamiento operacional.

## Estructura

- `src/modelo`: entidades, clase abstracta `SolicitudCompra`, polimorfismo, `ArrayList<DetalleSolicitud>` y arreglo fijo demostrativo de catalogo con busqueda binaria.
- `src/vista`: `MainApp`, interfaz de consola con menu operacional.
- `src/controlador`: validaciones, coordinacion del flujo y separacion MVC.
- `src/dao`: contrato `ISolicitudDAO` y DAO MySQL desacoplado de la vista/controlador.
- `src/servicio`: bitacora por archivo, validacion de adjuntos y notificacion simulada.
- `src/excepciones`: excepciones verificadas de validacion y persistencia.
- `sql/schema.sql`: estructura MySQL normalizada.
- `sql/datos_y_operaciones_dml.sql`: datos de prueba, SELECT, UPDATE y DELETE.
- `docs/alcance_tp4.md`: mapeo de criterios TP4 y pendientes documentados.

## Criterios TP4 cubiertos

- MVC: vista por consola, controlador de solicitudes, modelo de dominio y DAO desacoplado.
- MySQL/JDBC: `DriverManager`, `Connection`, `PreparedStatement`, `ResultSet`, `setReadOnly`, `isClosed`, `isValid`, `setCatalog` y `getWarnings`.
- Persistencia: alta de solicitud y detalles.
- Consulta: reporte integral con JOIN y monto total.
- Actualizacion: cambio de estado de una solicitud.
- Validaciones operativas: usuario solicitante existente, justificacion obligatoria y estado seleccionado por numero.
- Excepciones: `ValidacionException` para reglas de negocio y `PersistenciaException` para MySQL/JDBC.
- Interfaces y abstraccion: `ISolicitudDAO`, `INotificador` y `SolicitudCompra` abstracta.
- Arreglos y `ArrayList`: arreglo `BienCatalogo[]` como evidencia academica de estructura fija y busqueda binaria, y `ArrayList` para items dinamicos.
- Archivos: bitacora local en `data/bitacora_solicitudes.csv` con `java.io`.
- Adjuntos: validacion opcional de existencia, extension segura y limite de 10 MB.

## Ejecutar

Compilar:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java).FullName
```

Ejecutar usando MySQL:

```powershell
java -cp "out;mysql-connector-j.jar" vista.MainApp
```

Configurar MySQL por variables de entorno, si no se desean usar los valores por defecto:

```powershell
$env:SIC_DB_URL="jdbc:mysql://127.0.0.1:3306/sic_stj_db"
$env:SIC_DB_USER="root"
$env:SIC_DB_PASSWORD="Root"
```

El menu del prototipo permite:

```text
1. Registrar solicitud de compra
2. Actualizar estado de solicitud
3. Listar solicitudes
4. Listar bienes del catalogo
5. Listar usuarios solicitantes
6. Ver bitacora de auditoria
0. Salir
```

En el menu principal la opcion visible de salida es `0`. Como ayuda general, el sistema informa que se puede ingresar `X` para cancelar una operacion en curso y volver al menu principal sin persistir cambios.

Al registrar una solicitud, el sistema solicita el tipo dentro del flujo:

```text
Tipo de solicitud:
1. Ordinaria
2. Urgente
Seleccione tipo de solicitud:
```

La diferencia entre ordinaria y urgente se resuelve mediante polimorfismo: cada subclase calcula su propio SLA.

Las opciones `4` y `5` permiten consultar desde MySQL los IDs disponibles antes de registrar una solicitud. Ademas, durante el alta se puede ingresar `0` en el campo de usuario o bien para ver el listado correspondiente y luego continuar con la carga.

La seleccion operativa de bienes usa MySQL. El arreglo `CatalogoBienes` queda en el modelo como demostracion academica complementaria de arreglos y busqueda binaria, sin reemplazar la persistencia solicitada.

Durante el alta se solicita una justificacion manual y, al persistir correctamente, se muestra el ID generado por MySQL. Antes de registrar la solicitud se pide confirmacion `S/N`: `S` confirma la transaccion y `N` vuelve al menu sin persistir. Para actualizar estados, el sistema permite ingresar `0` para ver las solicitudes disponibles y tambien `0` para ver los estados permitidos; luego solicita elegir un estado por numero y pide confirmacion `S/N` antes de aplicar el cambio.

## Probar validaciones

Las validaciones se prueban dentro del flujo real de registro, sin opciones especiales de menu.

Para probar un bien inexistente:

1. Elegir `1. Registrar solicitud de compra`.
2. Seleccionar tipo de solicitud ordinaria o urgente.
3. En `Ingrese ID de bien homologado (0 para ver catalogo):`, ingresar `0` si se desea consultar el catalogo.
4. Luego ingresar `999`.
5. El sistema debe mostrar un error controlado indicando que el bien no existe en el catalogo homologado.

Para probar una cantidad invalida:

1. Elegir `1. Registrar solicitud de compra`.
2. Seleccionar tipo de solicitud ordinaria o urgente.
3. Ingresar `0` en el campo de bien para ver el catalogo y luego elegir un ID valido.
4. En `Ingrese cantidad:`, ingresar `0` o `-5`.
5. El sistema debe mostrar un error controlado indicando que la cantidad debe ser mayor a 0.

Para probar un usuario inexistente:

1. Elegir `1. Registrar solicitud de compra`.
2. Seleccionar tipo de solicitud ordinaria o urgente.
3. En `Ingrese ID de usuario solicitante (0 para ver usuarios):`, ingresar `0` si se desea consultar usuarios.
4. Luego ingresar un ID inexistente, por ejemplo `999`.
5. El sistema debe mostrar un error controlado indicando que el usuario solicitante no existe y volver a pedir el ID.

Para probar un adjunto invalido:

1. Registrar una solicitud.
2. En `Ruta de adjunto opcional [sin adjunto]:`, ingresar la ruta de un archivo con extension no permitida, por ejemplo un `.jar` o `.exe`.
3. El sistema debe rechazar el adjunto y registrar el evento en la bitacora.

## Base de datos

1. Ejecutar `sql/schema.sql`.
2. Ejecutar `sql/datos_y_operaciones_dml.sql`.
3. Iniciar el prototipo con el conector MySQL en el classpath.

El DAO respeta el modelo normalizado:

```sql
detalles_solicitud(id_detalle, id_solicitud, id_bien, cantidad, precio_estimado)
```

La descripcion del bien no se duplica en el detalle; se consulta desde `bienes_catalogo`.

## Alcance documentado

El AP4 menciona como trabajo futuro Swing, adjuntos completos, bitacora, notificaciones, estados y expansion a otros casos de uso. Para esta version final del prototipo:

- Swing completo no se implementa; se mantiene consola porque el propio AP4 la declara como interfaz operacional equivalente mediante `MainApp`.
- Adjuntos se validan, pero no se almacenan documentos completos.
- Notificaciones se simulan por consola y se registran en bitacora.
- Estados se actualizan de forma basica; no se implementa el workflow completo CU-02.
- Hash criptografico, buzon digital y portal web quedan fuera del alcance operacional de CU-01.
