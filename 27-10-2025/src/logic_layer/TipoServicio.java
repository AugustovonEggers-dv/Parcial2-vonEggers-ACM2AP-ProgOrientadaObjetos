package logic_layer;

public enum TipoServicio {
    LUZ("Edenor","Edesur","Edea","EPEC","EPE Santa Fe","EMSA"),
    AGUA("AySA","Aguas Bonaerenses","Aguas Cordobesas","Aguas Santafesinas","Aguas de Corrientes","Aguas Rionegrinas"),
    GAS("Metrogas","Naturgy","Camuzzi","Ecogas","Litoral Gas","Distrigas"),
    INTERNET("Personal","IPlan","Claro","Telecentro","Movistar","Starlink"),
    TELEFONIA("Movistar","Claro","Personal","Tuenti"),
    IMPUESTOS("ARCA","Epagos","Rentas de Tucumán","ARBA"),
    SEGURO_AUTO("La Caja","Nación Seguros","AMCA","Sancor Seguros","San Cristóbal Seguros","Zúrich","Río Uruguay Seguros"),
    PREPAGA("Osde","Swiss Medical","Sancor Salud","Medifé","Galeno","Medicus","Emergencias","Hospital Alemán","Hospital Británico","Hospital Italiano"),
    TELEVISION("Telecentro","DirecTV","Flow","Supercanal"),
    AGIP("ABL","Patente","Infracciones"),
    SEGURO_CASA("Allianz","Zurich","Sancor Seguros","San Cristóbal Seguros","Río Uruguay Seguros"),
    PLAN_AUTOMÓVIL("Autoahorro Volkswagen","Plan Chevrolet","Toyota Plan","PSA Peugeout","Fiat Crédito","Plan Rombo Renault","Ford Plan Ovalo","Nissan Plan de Ahorro","Plan de Ahorro CarOne"),
    CREDITO_HIPOTECARIO("Banco Hipotecario","BBVA","Banco Santander","Banco Galicia","Banco Patagonia","Banco Nación","Banco Ciudad","Banco Provincia"),
    TARJETA_CREDITO("Tarjeta Cencosud","Banco Galicia","BBVA","Banco Santander","Banco Patagonia","Banco Galicia","ESCO","Tarjeta Carrefour","Tarjeta Coto","Efectivo Si"),
    STREAMING("Netflix","Amazon Prime Video","Disney+","HBO Max","Spotify","Crunchyroll"),
    GAMING("Nintendo Switch Online","PlayStation Plus","XBOX Live","XBOX Game Pass","Amazon Prime Gaming","Discord Nitro")
    ;
    
    private final String[] empresasDisponibles; 

    
    TipoServicio(String... empresas) {
        this.empresasDisponibles = empresas;
    }

   

    public String[] getEmpresasDisponibles() {
        return empresasDisponibles;
    }

   
    public boolean esEmpresaValida(String nombreEmpresa) {
        for (String empresa : empresasDisponibles) {
            if (empresa.equalsIgnoreCase(nombreEmpresa)) {
                return true;
            }
        }
        return false;
    }
}
