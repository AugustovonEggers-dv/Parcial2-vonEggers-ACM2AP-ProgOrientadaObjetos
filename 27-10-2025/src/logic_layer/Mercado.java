package logic_layer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mercado {
    
    private static Map<String, ActivoFinanciero> activos = new HashMap<>();
    private static Random random = new Random();

    public static void inicializar() {
        activos.put("AAPL", new ActivoFinanciero("AAPL", "Apple Inc.", TipoMoneda.DOLARES, 170.50));
        activos.put("KO", new ActivoFinanciero("KO", "Coca-Cola", TipoMoneda.DOLARES, 60.20));
        activos.put("TSLA", new ActivoFinanciero("TSLA", "Tesla, Inc.", TipoMoneda.DOLARES, 250.00));
        activos.put("MSFT", new ActivoFinanciero("MSFT", "Microsoft Corporation", TipoMoneda.DOLARES, 26.00));
        activos.put("GOOGL", new ActivoFinanciero("GOOGL", "Alphabet Inc.", TipoMoneda.DOLARES, 7.00));
        activos.put("META", new ActivoFinanciero("META", "Meta Platforms, Inc.", TipoMoneda.DOLARES, 40.00));
        activos.put("NFLX", new ActivoFinanciero("NFLX", "Netflix, Inc.", TipoMoneda.DOLARES, 35.00));
        activos.put("ORCL", new ActivoFinanciero("ORCL", "Oracle Corporation", TipoMoneda.DOLARES, 132.00));
        activos.put("IBM", new ActivoFinanciero("IBM", "International Business Machines Corporation", TipoMoneda.DOLARES, 30.00));
        activos.put("CRM", new ActivoFinanciero("CRM", "Salesforce, Inc.", TipoMoneda.DOLARES, 21.70));
        activos.put("PLTR", new ActivoFinanciero("PLTR", "Palantir Technologies Inc.", TipoMoneda.DOLARES, 100.00));
        activos.put("ADBE", new ActivoFinanciero("ADBE", "Adobe Inc.", TipoMoneda.DOLARES, 11.00));
        activos.put("NVDA", new ActivoFinanciero("NVDA", "NVIDIA Corporation", TipoMoneda.DOLARES, 12.00));
        activos.put("JPM", new ActivoFinanciero("JPM", "JPMorgan Chase & Co.", TipoMoneda.DOLARES, 31.00));
        activos.put("MA", new ActivoFinanciero("MA", "Mastercard Incorporated", TipoMoneda.DOLARES, 25.00));
        activos.put("V", new ActivoFinanciero("V", "Visa Inc.", TipoMoneda.DOLARES, 28.00));
        activos.put("C", new ActivoFinanciero("C", "Citigroup Inc.", TipoMoneda.DOLARES, 50.00));
        activos.put("WMT", new ActivoFinanciero("WMT", "Walmart Inc.", TipoMoneda.DOLARES, 8.00));
        activos.put("HD", new ActivoFinanciero("HD", "The Home Depot, Inc.", TipoMoneda.DOLARES, 17.00));
        activos.put("AMZN", new ActivoFinanciero("AMZN", "Amazon, Inc", TipoMoneda.DOLARES, 2.00));
        activos.put("JNJ", new ActivoFinanciero("JNJ", "Johnson & Johnson", TipoMoneda.DOLARES, 18.00));
        activos.put("MCD", new ActivoFinanciero("MCD", "McDonald's Corporation", TipoMoneda.DOLARES, 18.00));
        activos.put("DISN", new ActivoFinanciero("DISN", "The Walt Disney Company", TipoMoneda.DOLARES, 14.00));
        activos.put("COST", new ActivoFinanciero("COST", "Costco Wholesale Corporation", TipoMoneda.DOLARES, 28.00));
        activos.put("ABNB", new ActivoFinanciero("ABNB", "Airbnb, Inc", TipoMoneda.DOLARES, 17.00));
        activos.put("XOM", new ActivoFinanciero("XOM", "Exxon Mobil Corporation", TipoMoneda.DOLARES, 17.00));
        activos.put("SHEL", new ActivoFinanciero("SHEL", "Shell plc", TipoMoneda.DOLARES, 56.00));
        activos.put("SHEL", new ActivoFinanciero("CAT", "Caterpillar Inc.", TipoMoneda.DOLARES, 43.00));
        activos.put("T", new ActivoFinanciero("T", "AT&T Inc.", TipoMoneda.DOLARES, 12.00));
        activos.put("PG", new ActivoFinanciero("PG", "The Procter & Gamble Company", TipoMoneda.DOLARES, 15.00));
        activos.put("SAN", new ActivoFinanciero("SAN", "Banco Santander, S.A.", TipoMoneda.DOLARES, 61.00));
        activos.put("FDX", new ActivoFinanciero("FDX", "FedEx Corporation", TipoMoneda.DOLARES, 38.00));
        activos.put("SONY", new ActivoFinanciero("SONY", "Sony Group Corporation", TipoMoneda.DOLARES, 5.00));
        activos.put("PEP", new ActivoFinanciero("PEP", "PepsiCo, Inc.", TipoMoneda.DOLARES, 12.00));
        activos.put("SIEGY", new ActivoFinanciero("SIEGY","Siemens Aktiengesellschaft",TipoMoneda.DOLARES,140.00));
        activos.put("RACE", new ActivoFinanciero("RACE","Ferrari N.V.",TipoMoneda.DOLARES,402.00));
        activos.put("MS", new ActivoFinanciero("MS","Morgan Stanley",TipoMoneda.DOLARES,164.00));
        activos.put("NTDOY", new ActivoFinanciero("NTDOY","Nintendo Co., Ltd.",TipoMoneda.DOLARES,22.00));
        activos.put("CL", new ActivoFinanciero("CL","Colgate-Palmolive Company",TipoMoneda.DOLARES,75.00));
        activos.put("MMM", new ActivoFinanciero("MMM","3M Company",TipoMoneda.DOLARES,160.00));
        activos.put("HSBC", new ActivoFinanciero("HSBC","HSBC Holdings plc",TipoMoneda.DOLARES,70.00));
        activos.put("EBAY", new ActivoFinanciero("EBAY","eBay Inc.",TipoMoneda.DOLARES,82.00));
        activos.put("SPOT", new ActivoFinanciero("SPOT","Spotify Technology S.A.",TipoMoneda.DOLARES,622.00));
        
        activos.put("GGAL", new ActivoFinanciero("GGAL", "Grupo Galicia", TipoMoneda.PESOS, 1500.00));
        activos.put("YPF", new ActivoFinanciero("YPF", "YPF", TipoMoneda.PESOS, 12000.00));
        activos.put("PAM", new ActivoFinanciero("PAM", "Pampa Energía", TipoMoneda.PESOS, 85000.00));
        activos.put("MELI", new ActivoFinanciero("MELI", "MercadoLibre, Inc.", TipoMoneda.PESOS, 2300000.00));
        activos.put("SUPV", new ActivoFinanciero("SUPV", "Banco Suppervielle S.A.", TipoMoneda.PESOS, 12000.00));
        activos.put("BBAR", new ActivoFinanciero("BBAR", "Banco BBVA Argentina S.A.", TipoMoneda.PESOS, 16000.00));
        activos.put("BMA", new ActivoFinanciero("BMA", "Banco Macro S.A.", TipoMoneda.PESOS, 90000.00));
        activos.put("IRS", new ActivoFinanciero("IRS", "IRSA Inversiones y Representaciones Sociedad Anónima", TipoMoneda.PESOS, 15000.00));
        activos.put("LOMA", new ActivoFinanciero("LOMA", "Loma Negra Compañía Industrial Argentina S.A.", TipoMoneda.PESOS, 10000.00));
        activos.put("TEO", new ActivoFinanciero("TEO", "Telecom Argentina S.A.", TipoMoneda.PESOS, 11000.00));
        activos.put("GCLA", new ActivoFinanciero("GCLA", "Grupo Clarín S.A.", TipoMoneda.PESOS, 2545.00));
    }

    public static ActivoFinanciero getActivo(String ticker) {
        return activos.get(ticker);
    }
    
    public static List<ActivoFinanciero> getActivosDisponibles() {
        return new ArrayList<>(activos.values());
    }

    public static void fluctuarMercado() {
        for (ActivoFinanciero activo : activos.values()) {
            activo.fluctuarPrecio(random);
        }
    }
}
