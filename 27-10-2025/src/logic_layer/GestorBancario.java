package logic_layer;

import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GestorBancario {

    private static Random random = new Random();
    private static final double TASA_CAMBIO_DOLAR = 1000.0;

    public static void iniciarPagoServicio(Cuenta cuentaQuePaga, TarjetaCredito tarjetaCredito) { 
        
        TipoServicio servicioSeleccionado = seleccionarTipoServicio();
        if (servicioSeleccionado == null) {
            JOptionPane.showMessageDialog(null, "Pago cancelado.");
            return; 
        }

        String empresaSeleccionada = seleccionarEmpresa(servicioSeleccionado);
        if (empresaSeleccionada == null) {
            JOptionPane.showMessageDialog(null, "Pago cancelado.");
            return; 
        }

        String codigoPago = ingresarCodigoPago();
        if (codigoPago == null) {
            JOptionPane.showMessageDialog(null, "Pago cancelado.");
            return; 
        }

        int minCentavos = 2000000;  
        int maxCentavos = 10000000;
        int rangoCentavos = maxCentavos - minCentavos + 1;
        int montoEnCentavos = minCentavos + random.nextInt(rangoCentavos);
        double montoAPagar = montoEnCentavos / 100.0;
        
        String concepto = "Pago " + servicioSeleccionado.name() + " (" + empresaSeleccionada + ")";

        String[] metodosPago = {
                String.format("Cuenta (Saldo: $%.2f)", cuentaQuePaga.getSaldo()),
                String.format("T. Crédito (Disp: $%.2f)", tarjetaCredito.getDisponible())
            };

        String metodoElegido = (String) JOptionPane.showInputDialog(null,
                String.format("Servicio: %s (%s)\nFactura N°: %s\n\nMonto a Pagar: $%.2f\n\nSeleccione el método de pago:",
                    servicioSeleccionado.name(),
                    empresaSeleccionada,
                    codigoPago,
                    montoAPagar),
                "Paso 4: Método de Pago",
                JOptionPane.QUESTION_MESSAGE, null, metodosPago, metodosPago[0]);

        if (metodoElegido == null) {
            JOptionPane.showMessageDialog(null, "Pago cancelado.");
            return;
        }
        
        RespuestaTransaccion respuesta; 

        if (metodoElegido.startsWith("Cuenta")) {
            int confirmacion = JOptionPane.showConfirmDialog(null, 
                String.format("Se debitarán $%.2f de su cuenta.\nSaldo actual: $%.2f\n¿Confirmar?",
                    montoAPagar, cuentaQuePaga.getSaldo()),
                "Confirmar Pago de Cuenta", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                respuesta = new RespuestaTransaccion(false, "Pago cancelado por el usuario.");
            } else {
                respuesta = cuentaQuePaga.retirar(montoAPagar, concepto);
            }

        } else {
            respuesta = manejarPagoConCredito(tarjetaCredito, montoAPagar, concepto);
        }
        mostrarRespuesta(respuesta);
    }
    
private static RespuestaTransaccion manejarPagoConCredito(TarjetaCredito tarjeta, double montoAPagar, String concepto) {
        String[] opcionesPago = {"1 pago (Sin interés)", "Pagar en cuotas (Con interés)"};
        String seleccion = (String) JOptionPane.showInputDialog(null,
            String.format("Monto: $%.2f\n¿Cómo desea pagar con su Tarjeta de Crédito?", montoAPagar),
            "Pagar con Crédito",
            JOptionPane.QUESTION_MESSAGE, null, opcionesPago, opcionesPago[0]);
        
        if (seleccion == null) return new RespuestaTransaccion(false, "Pago cancelado.");

        double montoTotalACargar = montoAPagar;
        String conceptoFinal = concepto + " (1 pago)";

        if (seleccion.startsWith("Pagar en cuotas")) {
            java.util.Map<Integer, Double> planes = tarjeta.getPlanesDeCuotas();
            if (planes == null || planes.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Esta tarjeta no tiene planes de cuotas habilitados.", "Error", JOptionPane.ERROR_MESSAGE);
                return new RespuestaTransaccion(false, "Operación cancelada.");
            }

            Object[] opcionesCuotas = planes.keySet().stream()
                .map(cuotas -> String.format("%d cuotas (Tasa: %.1f%%)", cuotas, planes.get(cuotas) * 100))
                .toArray();

            String seleccionCuotas = (String) JOptionPane.showInputDialog(null,
                "Seleccione el plan de cuotas:",
                "Plan de Financiación",
                JOptionPane.QUESTION_MESSAGE, null, opcionesCuotas, opcionesCuotas[0]);
            
            if (seleccionCuotas == null) return new RespuestaTransaccion(false, "Pago cancelado.");

            int cuotas = Integer.parseInt(seleccionCuotas.split(" ")[0]);
            double tasa = planes.get(cuotas);

            double interes = montoAPagar * tasa;
            montoTotalACargar = montoAPagar + interes;
            double valorCuota = montoTotalACargar / cuotas;
            conceptoFinal = String.format("%s (%d cuotas de $%.2f)", concepto, cuotas, valorCuota);

            int confirmacion = JOptionPane.showConfirmDialog(null,
                String.format("Resumen del pago en cuotas:\n\n" +
                              "Monto Original: $%.2f\n" +
                              "Interés (%d cuotas): $%.2f (Tasa %.1f%%)\n" +
                              "MONTO TOTAL A CARGAR: $%.2f\n\n" +
                              "Se cargarán %d cuotas de $%.2f\n" +
                              "¿Confirmar cargo total a la tarjeta?",
                              montoAPagar, cuotas, interes, tasa*100,
                              montoTotalACargar, cuotas, valorCuota),
                "Confirmación Final", JOptionPane.YES_NO_OPTION);
            
            if (confirmacion != JOptionPane.YES_OPTION) {
                return new RespuestaTransaccion(false, "Pago cancelado por el usuario.");
            }
        } 
        return tarjeta.realizarPago(montoTotalACargar, conceptoFinal);
    }
       
    private static TipoServicio seleccionarTipoServicio() {
        TipoServicio[] opciones = TipoServicio.values();
        
        
        String[] nombresServicios = Arrays.stream(opciones)
                                        .map(Enum::name) 
                                        .toArray(String[]::new);

        String seleccion = (String) JOptionPane.showInputDialog(null, 
            "Seleccione el servicio que desea pagar:", 
            "Paso 1: Seleccionar Servicio", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            nombresServicios, 
            nombresServicios[0]);
        
        if (seleccion == null) return null;

        for (TipoServicio ts : opciones) {
            if (ts.name().equals(seleccion)) {
                return ts;
            }
        }
        return null;
    }

    private static String seleccionarEmpresa(TipoServicio servicio) {
        String[] empresas = servicio.getEmpresasDisponibles();
        
        String empresaSeleccionada = (String) JOptionPane.showInputDialog(null, 
            "Servicio: " + servicio.name() + "\nSeleccione la empresa:",
            "Paso 2: Seleccionar Empresa", 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            empresas, 
            empresas[0]);
        
        return empresaSeleccionada; 
    }

    private static String ingresarCodigoPago() {
        String codigo = null;
        boolean esValido = false;
        
        while (!esValido) {
            codigo = JOptionPane.showInputDialog(null, 
                "Ingrese el código de pago (8 dígitos numéricos):", 
                "Paso 3: Identificar Factura", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (codigo == null) {
                return null; 
            }

            if (codigo.matches("\\d{8}")) {
                esValido = true;
            } else {
                JOptionPane.showMessageDialog(null, "Error: El código debe contener exactamente 8 números.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
            }
        }
        return codigo;
    }
    public static void consultarSaldo(Cuenta cuenta) {
        String mensaje = String.format("Su saldo actual es: $%.2f", cuenta.getSaldo());
        JOptionPane.showMessageDialog(null, mensaje, "Consulta de Saldo", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void realizarDeposito(Cuenta cuenta) {
        double monto = pedirMonto("Ingrese el monto a depositar:");
        if (monto == -1) return; 

        RespuestaTransaccion resp = cuenta.depositar(monto, "Depósito por Cajero");
        mostrarRespuesta(resp);
    }
    public static void realizarRetiro(Cuenta cuenta) {
        double monto = pedirMonto("Ingrese el monto a retirar:");
        if (monto == -1) return; 
        
        RespuestaTransaccion resp = cuenta.retirar(monto, "Retiro por Cajero");
        if (resp == null) {
            JOptionPane.showMessageDialog(null, "Error inesperado en la cuenta. La operación no devolvió respuesta.", "Error Crítico", JOptionPane.ERROR_MESSAGE);
        } else {
            mostrarRespuesta(resp);
        }
    }
    public static void mostrarMovimientos(Cuenta cuenta) {
        List<Movimiento> movimientos = cuenta.getMovimientos();
        
        if (movimientos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay movimientos para mostrar.", "Historial", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String historial = movimientos.stream()
                .map(Movimiento::toString)
                .collect(Collectors.joining("\n"));


        JOptionPane.showMessageDialog(null, "--- Últimos Movimientos ---\n" + historial, "Historial", JOptionPane.PLAIN_MESSAGE);
    }
    private static double pedirMonto(String mensaje) {
        double monto = -1;
        boolean montoValido = false;
        
        while (!montoValido) {
            String montoStr = JOptionPane.showInputDialog(null, mensaje, "Ingresar Monto", JOptionPane.QUESTION_MESSAGE);
            
            if (montoStr == null) {
                return -1; 
            }

            if (montoStr.matches("\\d+(\\.\\d{1,2})?")) {
                monto = Double.parseDouble(montoStr);
                if (monto > 0) {
                    montoValido = true;
                } else {
                    JOptionPane.showMessageDialog(null, "El monto debe ser positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Formato de monto no válido. Use solo números (ej: 1500.50).", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
		return monto;
    }
        
        private static void mostrarRespuesta(RespuestaTransaccion respuesta) {
            if (respuesta.esExitosa()) {
                JOptionPane.showMessageDialog(null, respuesta.getMensaje(), "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, respuesta.getMensaje(), "Operación Fallida", JOptionPane.ERROR_MESSAGE);
            }
        }
        
        public static void gestionarTarjetas(CajaAhorro cuenta, TarjetaDebito debito, TarjetaCredito credito) {
            String[] opciones = {
                "1. Gestionar Tarjeta de Débito",
                "2. Gestionar Tarjeta de Crédito",
                "3. Pagar Resumen T. de Crédito",
                "4. Aplicar Interés (Simular Fin de Mes)",
                "5. Volver al Menú Principal"
            };

            String seleccion;
            do {
                seleccion = (String) JOptionPane.showInputDialog(null, 
                    "Seleccione una opción de tarjetas:",
                    "Gestor de Tarjetas", 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    opciones, 
                    opciones[0]);

                if (seleccion == null) {
                    seleccion = "5. Volver al Menú Principal";
                }

                switch (seleccion.split("\\.")[0]) {
                    case "1":
                        gestionarEstadoTarjeta(debito);
                        break;
                    case "2":
                        gestionarEstadoTarjeta(credito);
                        break;
                    case "3":
                        pagarTarjetaCredito(credito, cuenta);
                        break;
                    case "4":
                        RespuestaTransaccion respInteres = credito.aplicarInteresFinanciacion();
                        mostrarRespuesta(respInteres);
                        break;
                }
            } while (!seleccion.startsWith("5")); 
        }
        
        private static void opcionesTarjeta(Tarjeta tarjeta) {
            String titulo = "Gestión de " + (tarjeta instanceof TarjetaDebito ? "T. Débito" : "T. Crédito");
            String[] opciones = {
                "1. Ver Datos de Tarjeta",
                "2. Habilitar Tarjeta",
                "3. Congelar Tarjeta",
                "4. Desactivar (Baja) Tarjeta",
                "5. Volver"
            };

            String seleccion;
            do {
                seleccion = (String) JOptionPane.showInputDialog(null, 
                    "Tarjeta: " + tarjeta.getNumeroVisible() + "\nEstado actual: " + tarjeta.getEstado().name(),
                    titulo, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    opciones, 
                    opciones[0]);

                if (seleccion == null) {
                    seleccion = "5. Volver";
                }
                
                RespuestaTransaccion resp = null;
                switch (seleccion.split("\\.")[0]) {
                    case "1":
                    	String titular = tarjeta.getTitular();
                        String numeroVisible = tarjeta.getNumeroVisible(); 
                        String moneda = tarjeta.getMoneda().name();
                        String estado = tarjeta.getEstado().getDescripcion(); 

                        String datos = "Titular: " + titular + "\n" +
                                       "Tarjeta: " + numeroVisible + "\n" +
                                       "Moneda: " + moneda + "\n" +
                                       "Estado: " + estado;
                        
                        
                        if (tarjeta instanceof TarjetaCredito) {
                            TarjetaCredito tc = (TarjetaCredito) tarjeta; 
                            datos += "\n---\n" +
                                     String.format("Límite de Crédito: $%.2f\n", tc.getLimiteCredito()) +
                                     String.format("Deuda Actual: $%.2f\n", tc.getSaldoConsumido()) +
                                     String.format("Disponible: $%.2f", tc.getDisponible());
                        }

                        JOptionPane.showMessageDialog(null, datos, "Datos de Tarjeta", JOptionPane.INFORMATION_MESSAGE);
                        break;
                    case "2":
                        resp = tarjeta.habilitar();
                        break;
                    case "3":
                        resp = tarjeta.congelar();
                        break;
                    case "4":
                        resp = tarjeta.desactivar();
                        break;
                }
                
                if (resp != null) {
                    mostrarRespuesta(resp); 
                }
                
            } while (!seleccion.startsWith("5"));
        }
        private static void pagarTarjetaCredito(TarjetaCredito tarjeta, Cuenta cuentaQuePaga) {
            double deuda = tarjeta.getSaldoConsumido();
            if (deuda <= 0) {
                JOptionPane.showMessageDialog(null, "Su tarjeta no registra deuda.", "Pago de Tarjeta", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            double monto = pedirMonto(String.format("Su saldo a pagar es: $%.2f\nIngrese el monto a pagar:", deuda));
            if (monto == -1) return; 

            String concepto = "Pago Tarjeta " + tarjeta.getNumeroVisible();
            RespuestaTransaccion respRetiro = cuentaQuePaga.retirar(monto, concepto);
            
            if (respRetiro.esExitosa()) {
                RespuestaTransaccion respPago = tarjeta.recibirPago(monto);
                mostrarRespuesta(respPago);
            } else {
                mostrarRespuesta(respRetiro);
            }
        }
        private static void gestionarEstadoTarjeta(Tarjeta tarjeta) {
            String titulo = "Gestión de " + (tarjeta instanceof TarjetaDebito ? "T. Débito" : "T. Crédito");
            
            
            java.util.ArrayList<String> opciones = new java.util.ArrayList<>(java.util.Arrays.asList(
                "1. Ver Datos de Tarjeta",
                "2. Habilitar Tarjeta",
                "3. Congelar Tarjeta",
                "4. Desactivar (Baja) Tarjeta"
            ));

            if (tarjeta instanceof TarjetaCredito) {
                opciones.add("5. Cambiar Moneda de Operación");
                opciones.add("6. Volver");
            } else {
                opciones.add("5. Volver"); 
            }

            String seleccion;
            do {
                seleccion = (String) JOptionPane.showInputDialog(null, 
                    "Tarjeta: " + tarjeta.getNumeroVisible() + "\nEstado actual: " + tarjeta.getEstado().name(),
                    titulo, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    opciones.toArray(), 
                    opciones.get(0));
          
                if (seleccion == null) {
                    seleccion = opciones.get(opciones.size() - 1);
                }
                
                RespuestaTransaccion resp = null;
                switch (seleccion.split("\\.")[0]) {
                    case "1":
                        
                        break;
                    case "2": resp = tarjeta.habilitar(); break;
                    case "3": resp = tarjeta.congelar(); break;
                    case "4": resp = tarjeta.desactivar(); break;
                    case "5":
                        if (tarjeta instanceof TarjetaCredito) {
                            cambiarMonedaTarjeta(tarjeta);
                        }
                        
                        break;
                    
                }
                
                if (resp != null) {
                    mostrarRespuesta(resp);
                }
                
            } while (!seleccion.startsWith(String.valueOf(opciones.size()))); 
        }
        private static void cambiarMonedaTarjeta(Tarjeta tarjeta) {
            if (tarjeta instanceof TarjetaDebito) {
                JOptionPane.showMessageDialog(null, 
                    "No se puede cambiar la moneda de una Tarjeta de Débito.\n" +
                    "La tarjeta está permanentemente ligada a su cuenta asociada.",
                    "Operación no permitida", JOptionPane.WARNING_MESSAGE);
                return;
            }
            

            TipoMoneda[] monedas = TipoMoneda.values(); 
            TipoMoneda nuevaMoneda = (TipoMoneda) JOptionPane.showInputDialog(null,
                "Seleccione la nueva moneda para esta tarjeta:",
                "Cambiar Moneda",
                JOptionPane.QUESTION_MESSAGE, null,
                monedas,
                tarjeta.getMoneda());
            
            if (nuevaMoneda == null) return;
            RespuestaTransaccion resp = tarjeta.cambiarMoneda(nuevaMoneda);
            mostrarRespuesta(resp);
        }
        public static void gestionarCambioDivisas(Cuenta cuentaPesos, LinkedList<Cuenta> todasLasCuentas) {
            if (cuentaPesos.getMoneda() != TipoMoneda.PESOS) {
                JOptionPane.showMessageDialog(null, "Error: Se requiere una cuenta en PESOS para comprar divisas.", "Error de Cuenta", JOptionPane.ERROR_MESSAGE);
                return;
            }

            TipoMoneda monedaAComprar = seleccionarMonedaCompra();
            if (monedaAComprar == null) return; 

            Cuenta cuentaDestino = buscarCuentaPorMoneda(todasLasCuentas, monedaAComprar);
            if (cuentaDestino == null) {
                JOptionPane.showMessageDialog(null, "No se encontró una cuenta en " + monedaAComprar.name() + " para acreditar los fondos.", "Error de Cuenta", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double montoDivisa = pedirMonto("Ingrese la cantidad de " + monedaAComprar.name() + " que desea comprar:");
            if (montoDivisa == -1) return; 
            
            double tasa = monedaAComprar.getTasaDeCambio();
            double costoPesos = montoDivisa * tasa;

            int confirmacion = JOptionPane.showConfirmDialog(null,
                String.format("Resumen de Compra:\n\n" +
                              "Monto a comprar: %.2f %s\n" +
                              "Tasa de cambio: 1 %s = %.2f ARS\n" +
                              "Costo total: %.2f ARS\n\n" +
                              "Débito de Cta. Pesos (Saldo: %.2f)\n" +
                              "Crédito en Cta. %s (Saldo: %.2f)",
                              montoDivisa, monedaAComprar.getSimbolo(),
                              monedaAComprar.getSimbolo(), tasa,
                              costoPesos,
                              cuentaPesos.getSaldo(),
                              monedaAComprar.name(), cuentaDestino.getSaldo()),
                "Confirmar Compra de Divisas", JOptionPane.YES_NO_OPTION);

            if (confirmacion != JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(null, "Compra cancelada.");
                return;
            }

            RespuestaTransaccion respRetiro = cuentaPesos.retirar(costoPesos, "Compra " + monedaAComprar.getSimbolo());
            if (!respRetiro.esExitosa()) {
                mostrarRespuesta(respRetiro); 
                return;
            }
            
            RespuestaTransaccion respDeposito = cuentaDestino.depositar(montoDivisa, "Acreditación Compra " + monedaAComprar.getSimbolo());
            mostrarRespuesta(respDeposito);
        }

        private static TipoMoneda seleccionarMonedaCompra() {

            TipoMoneda[] divisas = Arrays.stream(TipoMoneda.values())
                                         .filter(m -> m != TipoMoneda.PESOS)
                                         .toArray(TipoMoneda[]::new);

            TipoMoneda seleccion = (TipoMoneda) JOptionPane.showInputDialog(null,
                "Seleccione la divisa que desea comprar:",
                "Selección de Divisa",
                JOptionPane.QUESTION_MESSAGE, null,
                divisas,
                divisas[0]);
            
            return seleccion; 
        }

        private static Cuenta buscarCuentaPorMoneda(LinkedList<Cuenta> cuentas, TipoMoneda moneda) {
            for (Cuenta c : cuentas) {
                if (c.getMoneda() == moneda) {
                    return c;
                }
            }
            return null;
        }
        public static void consultarSaldosAgrupados(Cuenta... cuentas) {
            String mensaje = "Resumen de Saldos Actuales:\n\n";

            for (Cuenta cuenta : cuentas) {
                mensaje += String.format("Saldo %s (%s): %.2f\n", 
                    cuenta.getMoneda().name(),
                    cuenta.getMoneda().getSimbolo(), 
                    cuenta.getSaldo()                
                );
            }

            JOptionPane.showMessageDialog(null, mensaje, "Consulta de Saldos", JOptionPane.INFORMATION_MESSAGE);
        }
        public static void gestionarInversiones(LinkedList<Cuenta> cuentas, Portafolio portafolio) {
            String[] opciones = {
                "1. Ver Cotizaciones (Mercado)",
                "2. Comprar Activo",
                "3. Vender Activo",
                "4. Ver Mi Portafolio",
                "5. Volver"
            };
            
            String seleccion;
            do {
                seleccion = (String) JOptionPane.showInputDialog(null, "Seleccione una opción de inversión:",
                    "Gestor de Inversiones", JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);

                if (seleccion == null) seleccion = "5. Volver";

                switch (seleccion.split("\\.")[0]) {
                    case "1":
                        verMercado();
                        break;
                    case "2":
                        comprarActivo(cuentas, portafolio);
                        break;
                    case "3":
                        venderActivo(cuentas, portafolio);
                        break;
                    case "4":
                        verPortafolio(portafolio);
                        break;
                }
            } while (!seleccion.startsWith("5"));
        }
        private static void verMercado() {
            Mercado.fluctuarMercado(); 
            
            String mensaje = "Cotizaciones del Mercado (Actualizado):\n\n";
            for (ActivoFinanciero activo : Mercado.getActivosDisponibles()) {
                mensaje += activo.toString() + "\n";
            }
            JOptionPane.showMessageDialog(null, mensaje, "Mercado de Valores", JOptionPane.INFORMATION_MESSAGE);
        }

        private static void comprarActivo(LinkedList<Cuenta> cuentas, Portafolio portafolio) {

            Object[] activosOpciones = Mercado.getActivosDisponibles().toArray();
            ActivoFinanciero activoAComprar = (ActivoFinanciero) JOptionPane.showInputDialog(null,
                "Seleccione el activo que desea comprar:", "Comprar Activo",
                JOptionPane.QUESTION_MESSAGE, null, activosOpciones, activosOpciones[0]);
            
            if (activoAComprar == null) return; 

            Cuenta cuentaParaPagar = buscarCuentaPorMoneda(cuentas, activoAComprar.getMoneda());
            if (cuentaParaPagar == null) {
                JOptionPane.showMessageDialog(null, "No posee una cuenta en " + activoAComprar.getMoneda() + " para operar.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int cantidad = 0;
            try {
                cantidad = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántos desea comprar? (Tenencia actual: 0)"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cantidad <= 0) return;

            double costoTotal = activoAComprar.getPrecio() * cantidad;

            String concepto = "Compra " + cantidad + " " + activoAComprar.getTicker();
            RespuestaTransaccion respRetiro = cuentaParaPagar.retirar(costoTotal, concepto);
            
            if (respRetiro.esExitosa()) {
                portafolio.agregarTenencia(activoAComprar.getTicker(), cantidad);
                mostrarRespuesta(new RespuestaTransaccion(true, "Compra exitosa."));
            } else {
                mostrarRespuesta(respRetiro); 
            }
        }
        
        private static void venderActivo(LinkedList<Cuenta> cuentas, Portafolio portafolio) {
            Map<String, Integer> tenencias = portafolio.getTenencias();
            if (tenencias.isEmpty() || tenencias.values().stream().allMatch(c -> c == 0)) {
                JOptionPane.showMessageDialog(null, "No posee activos en su portafolio para vender.", "Vender Activo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Object[] tickersOpciones = tenencias.keySet().stream()
                .filter(ticker -> tenencias.get(ticker) > 0) 
                .toArray();
            String tickerAVender = (String) JOptionPane.showInputDialog(null,
                "Seleccione el activo que desea vender:", "Vender Activo",
                JOptionPane.QUESTION_MESSAGE, null, tickersOpciones, tickersOpciones[0]);
            
            if (tickerAVender == null) return; 
            ActivoFinanciero activo = Mercado.getActivo(tickerAVender);
            int cantidadPoseida = portafolio.getCantidad(tickerAVender);
            int cantidadAVender = 0;
            try {
                cantidadAVender = Integer.parseInt(JOptionPane.showInputDialog("¿Cuántos desea vender? (Tenencia actual: " + cantidadPoseida + ")"));
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Cantidad no válida.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (cantidadAVender <= 0) return;

            RespuestaTransaccion respVenta = portafolio.quitarTenencia(tickerAVender, cantidadAVender);
            
            if (respVenta.esExitosa()) {
                Cuenta cuentaDestino = buscarCuentaPorMoneda(cuentas, activo.getMoneda());
                double montoGanado = activo.getPrecio() * cantidadAVender;
                String concepto = "Venta " + cantidadAVender + " " + activo.getTicker();
                
                cuentaDestino.depositar(montoGanado, concepto);
                mostrarRespuesta(new RespuestaTransaccion(true, String.format("Venta exitosa. Se acreditaron %.2f %s", montoGanado, activo.getMoneda().getSimbolo())));
            } else {
                mostrarRespuesta(respVenta); 
            }
        }

        private static void verPortafolio(Portafolio portafolio) {
            Map<String, Integer> tenencias = portafolio.getTenencias();
            if (tenencias.isEmpty() || tenencias.values().stream().allMatch(c -> c == 0)) {
                JOptionPane.showMessageDialog(null, "Su portafolio está vacío.", "Mi Portafolio", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String mensaje = "Mi Portafolio Valorizado:\n\n";
            double valorTotalPesos = 0;
            double valorTotalDolares = 0;

            for (Map.Entry<String, Integer> tenencia : tenencias.entrySet()) {
                String ticker = tenencia.getKey();
                Integer cantidad = tenencia.getValue();
                if (cantidad == 0) continue;

                ActivoFinanciero activo = Mercado.getActivo(ticker);
                double valorizado = activo.getPrecio() * cantidad;
                mensaje += String.format("%s | Cantidad: %d | Valor: %.2f %s\n",
                    activo.getTicker(), cantidad, valorizado, activo.getMoneda().getSimbolo());
                
                if (activo.getMoneda() == TipoMoneda.PESOS) valorTotalPesos += valorizado;
                if (activo.getMoneda() == TipoMoneda.DOLARES) valorTotalDolares += valorizado;
            }
            
            mensaje += String.format("\nValor Total en Pesos: %.2f ARS\nValor Total en Dólares: %.2f USD", valorTotalPesos, valorTotalDolares);
            JOptionPane.showMessageDialog(null, mensaje, "Mi Portafolio", JOptionPane.INFORMATION_MESSAGE);
        }
}