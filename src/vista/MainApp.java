package vista;

import controlador.SolicitudController;
import dao.ISolicitudDAO;
import dao.SolicitudDAOMemoria;
import dao.SolicitudDAOMySQL;
import excepciones.ValidacionException;
import java.util.Scanner;
import modelo.BienCatalogo;
import modelo.DetalleSolicitud;
import modelo.SolicitudCompra;
import modelo.SolicitudOrdinaria;
import modelo.SolicitudUrgente;

public class MainApp {
    public static void main(String[] args) {
        boolean usarMySQL = contieneArgumento(args, "--mysql");

        System.out.println("=== SIC-STJ: Sistema Integral de Compras ===");
        System.out.println("--- Prototipo Operacional - Modulo de Carga ---");

        ISolicitudDAO dao = usarMySQL ? new SolicitudDAOMySQL() : new SolicitudDAOMemoria();
        SolicitudController controlador = new SolicitudController(dao);

        try (Scanner scanner = new Scanner(System.in)) {
            if (contieneArgumento(args, "--probar-error")) {
                ejecutarRegistro(controlador, scanner, new SolicitudOrdinaria(), true);
                return;
            }

            if (contieneArgumento(args, "--urgente")) {
                ejecutarRegistro(controlador, scanner, new SolicitudUrgente(), false);
                return;
            }

            mostrarMenu(controlador, scanner);
        } catch (ValidacionException e) {
            System.err.println("Error de Validacion (Regla de Negocio): " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error Critico del Sistema: " + e.getMessage());
        } finally {
            System.out.println("--- Fin de la ejecucion del Prototipo ---");
        }
    }

    private static void mostrarMenu(SolicitudController controlador, Scanner scanner) throws ValidacionException {
        int opcion;

        do {
            System.out.println();
            System.out.println("Menu de seleccion");
            System.out.println("1. Registrar solicitud ordinaria");
            System.out.println("2. Registrar solicitud urgente");
            System.out.println("3. Probar validacion con cantidad invalida");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero(scanner, 0);

            switch (opcion) {
                case 1:
                    ejecutarRegistro(controlador, scanner, new SolicitudOrdinaria(), false);
                    break;
                case 2:
                    ejecutarRegistro(controlador, scanner, new SolicitudUrgente(), false);
                    break;
                case 3:
                    ejecutarRegistro(controlador, scanner, new SolicitudOrdinaria(), true);
                    break;
                case 0:
                    System.out.println("Saliendo del prototipo.");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void ejecutarRegistro(
            SolicitudController controlador,
            Scanner scanner,
            SolicitudCompra nuevaSolicitud,
            boolean probarError) throws ValidacionException {
        BienCatalogo resmas = new BienCatalogo(1, "Resmas Papel A4 80gr", 6500.00);
        BienCatalogo toner = new BienCatalogo(2, "Toner alternativo Brother", 25000.00);

        nuevaSolicitud.setIdUsuario(leerIdUsuario(scanner));

        System.out.println("Agregando items a la solicitud...");
        nuevaSolicitud.agregarDetalle(new DetalleSolicitud(resmas, 50));
        nuevaSolicitud.agregarDetalle(new DetalleSolicitud(toner, 4));

        if (probarError) {
            nuevaSolicitud.agregarDetalle(new DetalleSolicitud(toner, -5));
        }

        imprimirResumen(nuevaSolicitud);

        System.out.println();
        System.out.println("Presione ENTER para confirmar y registrar la solicitud...");
        scanner.nextLine();

        controlador.procesarGuardado(nuevaSolicitud);
        System.out.println("Estado final: " + nuevaSolicitud.getEstado());
    }

    private static int leerIdUsuario(Scanner scanner) {
        System.out.print("Ingrese ID de usuario solicitante [2]: ");
        return leerEntero(scanner, 2);
    }

    private static int leerEntero(Scanner scanner, int valorPorDefecto) {
        String entrada = scanner.nextLine().trim();

        if (entrada.isEmpty()) {
            return valorPorDefecto;
        }

        try {
            return Integer.parseInt(entrada);
        } catch (NumberFormatException e) {
            System.out.println("Valor invalido. Se usara " + valorPorDefecto + ".");
            return valorPorDefecto;
        }
    }

    private static void imprimirResumen(SolicitudCompra solicitud) {
        System.out.println();
        System.out.println("Resumen de Items:");

        for (DetalleSolicitud detalle : solicitud.getDetalles()) {
            System.out.println("- " + detalle.getResumen());
        }

        System.out.println();
        System.out.println("Monto Total Estimado: $" + solicitud.calcularMontoTotal());
        System.out.println("SLA de Resolucion: " + solicitud.calcularPlazoSLA() + " dias.");
    }

    private static boolean contieneArgumento(String[] args, String valor) {
        for (String arg : args) {
            if (valor.equalsIgnoreCase(arg)) {
                return true;
            }
        }

        return false;
    }
}
