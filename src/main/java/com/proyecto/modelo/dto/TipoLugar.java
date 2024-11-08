package com.proyecto.modelo.dto;

// TipoLugar.java
public enum TipoLugar {
    MUSEO,
    TEATRO,
    BIBLIOTECA,
    CENTRO_CULTURAL,
    SALA_EXPOSICIONES,
    GALERIA,
    AUDITORIO,
    ARCHIVO,
    PLAZA,
    CASA,
    PARQUE,
    CINE,
    PLAZA_TOROS,
    IGLESIA,
    CASTILLO,
    PALACIO,
    CATEDRAL,
    CONVENTO,
    OTROS;

    public static TipoLugar fromString(String texto) {
    	 if (texto == null || texto.isEmpty()) {
    	        return TipoLugar.OTROS; // Valor por defecto
    	    }
        
        for (TipoLugar tipo : TipoLugar.values()) {
            if (texto.toLowerCase().contains(tipo.name().toLowerCase().replace("_", " "))) {
                return tipo;
            }
        }
        return OTROS;
    }
}
