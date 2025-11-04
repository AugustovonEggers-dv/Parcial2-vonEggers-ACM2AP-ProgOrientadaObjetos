package user_layer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JOptionPane;
import logic_layer.Cuenta;
import logic_layer.CajaAhorro;
import logic_layer.TipoMoneda;
import logic_layer.Usuario;
import logic_layer.GestorBancario;
import logic_layer.GestorUsuario;
import logic_layer.Mercado;
import logic_layer.Portafolio;
import logic_layer.TarjetaCredito;
import logic_layer.TarjetaDebito;

public class main {

    public static void main(String[] args) {
    	
    	GestorUsuario.inicializar();
    	Mercado.inicializar();
    	
    	Usuario usuarioLogueado = null;
    	
    	do {
            String[] opcionesLogin = {"Iniciar Sesión", "Registrar Nuevo Usuario", "Salir"};
            int seleccionLogin = JOptionPane.showOptionDialog(null, "Bienvenido al Sistema Bancario", "Login",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, opcionesLogin, opcionesLogin[0]);

            switch (seleccionLogin) {
                case 0:
                    usuarioLogueado = GestorUsuario.iniciarSesion();
                    break;
                case 1: 
                    GestorUsuario.registrarNuevoUsuario();
                    break;
                case 2:
                default:
                    JOptionPane.showMessageDialog(null, "Hasta luego.");
                    System.exit(0);
            }

        } while (usuarioLogueado == null);

        if (usuarioLogueado != null) {
 
            
            String titular = usuarioLogueado.getUsername();
    	
    	CajaAhorro cuentaPesos = new CajaAhorro("Usuario Ejemplo", "CA-123", TipoMoneda.PESOS);
        cuentaPesos.depositar(500000, "Depósito Inicial");

        CajaAhorro cuentaDolares = new CajaAhorro("Usuario Ejemplo", "CA-456-USD", TipoMoneda.DOLARES);
        CajaAhorro cuentaEuros = new CajaAhorro("Usuario Ejemplo", "CA-789-EUR", TipoMoneda.EURO);
        CajaAhorro cuentaReales = new CajaAhorro("Usuario Ejemplo", "CA-000-BRL", TipoMoneda.REAL);

        LinkedList<Cuenta> todasLasCuentas = new LinkedList<>();
        todasLasCuentas.add(cuentaPesos);
        todasLasCuentas.add(cuentaDolares);
        todasLasCuentas.add(cuentaEuros);
        todasLasCuentas.add(cuentaReales);
        
        Portafolio miPortafolio = new Portafolio();
        
        Map<Integer, Double> planes = new HashMap<>();
        planes.put(3, 0.15);
        planes.put(6, 0.30);
        planes.put(12, 0.50); 
        
        TarjetaDebito miDebito = new TarjetaDebito(
            cuentaPesos.getTitular(), "4510 1111 2222 3333", "12/28", "123", TipoMoneda.PESOS, cuentaPesos
        );
        
        TarjetaCredito miCredito = new TarjetaCredito(cuentaPesos.getTitular(), "4520 4444 5555 6666", "10/29", "456", TipoMoneda.PESOS, 150000.0, 0.05, planes);

        String[] opciones = {
            "1. Consultar Saldo",
            "2. Realizar un Depósito",
            "3. Realizar un Retiro",
            "4. Pagar un Servicio",
            "5. Ver Últimos Movimientos",
            "6. Gestionar Tarjetas",
            "7. Comprar Divisas Extranjeras",
            "8. Inversiones (bolsa)",
            "9. Salir"
        };

        int opcion = 0;
        do {
            String seleccion = (String) JOptionPane.showInputDialog(null, 
                "Seleccione una operación:",
                "Cajero Automático", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                opciones, 
                opciones[0]);

            if (seleccion == null) {
                opcion = 9; 
            } else {
                opcion = Integer.parseInt(seleccion.split("\\.")[0]);
            }

            switch (opcion) {
                case 1:
                	GestorBancario.consultarSaldosAgrupados(
                            cuentaPesos, 
                            cuentaDolares, 
                            cuentaEuros, 
                            cuentaReales);
                    break;
                case 2:
                	GestorBancario.realizarDeposito(cuentaPesos);
                    break;
                case 3:
                	GestorBancario.realizarRetiro(cuentaPesos);
                    break;
                case 4:
                	GestorBancario.iniciarPagoServicio(cuentaPesos, miCredito);
                    break;
                case 5:
                    GestorBancario.mostrarMovimientos(cuentaPesos);
                    break;
                case 6:
                    GestorBancario.gestionarTarjetas(cuentaPesos, miDebito, miCredito);
                    break;
                case 7:
                	GestorBancario.gestionarCambioDivisas(cuentaPesos, todasLasCuentas);
                    break;
                case 8:
                	GestorBancario.gestionarInversiones(todasLasCuentas, miPortafolio);
                	break;
                case 9:
                    JOptionPane.showMessageDialog(null, "Gracias por operar con nosotros.", "Salida", JOptionPane.INFORMATION_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(null, "Opción no válida.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        } while (opcion != 9);
    }


}
}