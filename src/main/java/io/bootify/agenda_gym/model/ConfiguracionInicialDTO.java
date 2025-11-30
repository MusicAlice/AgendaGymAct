package io.bootify.agenda_gym.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time. OffsetDateTime;

public class ConfiguracionInicialDTO {

    private Long id;
    private Long usuarioId;
    
    // OBJETIVOS Y CONFIGURACIÓN (JSON)
    private String objetivosPrincipales;
    private String configuracionDias;
    private String habitosMetas;
    
    // CALORÍAS
    private Integer caloriasActual;
    private Integer caloriasMeta;
    private String objetivoCalorias;
    
    // PESO Y FECHA META
    private BigDecimal pesoInicial;
    private BigDecimal metaPeso;
    private LocalDate metaFecha;
    
    // MEDIDAS - BRAZO
    private BigDecimal medidaBrazoActual;
    private BigDecimal medidaBrazoObjetivo;
    private String metaBrazo;
    
    // MEDIDAS - ABDOMEN
    private BigDecimal medidaAbdomenActual;
    private BigDecimal medidaAbdomenObjetivo;
    private String metaAbdomen;
    
    // MEDIDAS - CINTURA
    private BigDecimal medidaCinturaActual;
    private BigDecimal medidaCinturaObjetivo;
    private String metaCintura;
    
    // MEDIDAS - CADERA
    private BigDecimal medidaCaderaActual;
    private BigDecimal medidaCaderaObjetivo;
    private String metaCadera;
    
    // MEDIDAS - MUSLO
    private BigDecimal medidaMusloActual;
    private BigDecimal medidaMusloObjetivo;
    private String metaMuslo;
    
    // MEDIDAS - PECHO
    private BigDecimal medidaPechoActual;
    private BigDecimal medidaPechoObjetivo;
    private String metaPecho;
    
    // MEDIDAS - GLÚTEO
    private BigDecimal medidaGluteoActual;
    private BigDecimal medidaGluteoObjetivo;
    private String metaGluteo;
    
    // HÁBITOS ALIMENTICIOS
    private String desayunoHabito;
    private String almuerzoHabito;
    private String cenaHabito;
    private String snackHabito;
    
    // BIENESTAR
    private String hidratacionActual;
    private String suenioActual;
    private String animoActual;
    
    // TIMESTAMPS
    private OffsetDateTime dateCreated;
    private OffsetDateTime lastUpdated;

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(Long usuarioId) { this.usuarioId = usuarioId; }

    public String getObjetivosPrincipales() { return objetivosPrincipales; }
    public void setObjetivosPrincipales(String objetivosPrincipales) { this. objetivosPrincipales = objetivosPrincipales; }

    public String getConfiguracionDias() { return configuracionDias; }
    public void setConfiguracionDias(String configuracionDias) { this.configuracionDias = configuracionDias; }

    public String getHabitosMetas() { return habitosMetas; }
    public void setHabitosMetas(String habitosMetas) { this.habitosMetas = habitosMetas; }

    public Integer getCaloriasActual() { return caloriasActual; }
    public void setCaloriasActual(Integer caloriasActual) { this.caloriasActual = caloriasActual; }

    public Integer getCaloriasMeta() { return caloriasMeta; }
    public void setCaloriasMeta(Integer caloriasMeta) { this.caloriasMeta = caloriasMeta; }

    public String getObjetivoCalorias() { return objetivoCalorias; }
    public void setObjetivoCalorias(String objetivoCalorias) { this.objetivoCalorias = objetivoCalorias; }

    public BigDecimal getPesoInicial() { return pesoInicial; }
    public void setPesoInicial(BigDecimal pesoInicial) { this. pesoInicial = pesoInicial; }

    public BigDecimal getMetaPeso() { return metaPeso; }
    public void setMetaPeso(BigDecimal metaPeso) { this. metaPeso = metaPeso; }

    public LocalDate getMetaFecha() { return metaFecha; }
    public void setMetaFecha(LocalDate metaFecha) { this. metaFecha = metaFecha; }

    public BigDecimal getMedidaBrazoActual() { return medidaBrazoActual; }
    public void setMedidaBrazoActual(BigDecimal medidaBrazoActual) { this.medidaBrazoActual = medidaBrazoActual; }

    public BigDecimal getMedidaBrazoObjetivo() { return medidaBrazoObjetivo; }
    public void setMedidaBrazoObjetivo(BigDecimal medidaBrazoObjetivo) { this.medidaBrazoObjetivo = medidaBrazoObjetivo; }

    public String getMetaBrazo() { return metaBrazo; }
    public void setMetaBrazo(String metaBrazo) { this.metaBrazo = metaBrazo; }

    public BigDecimal getMedidaAbdomenActual() { return medidaAbdomenActual; }
    public void setMedidaAbdomenActual(BigDecimal medidaAbdomenActual) { this.medidaAbdomenActual = medidaAbdomenActual; }

    public BigDecimal getMedidaAbdomenObjetivo() { return medidaAbdomenObjetivo; }
    public void setMedidaAbdomenObjetivo(BigDecimal medidaAbdomenObjetivo) { this.medidaAbdomenObjetivo = medidaAbdomenObjetivo; }

    public String getMetaAbdomen() { return metaAbdomen; }
    public void setMetaAbdomen(String metaAbdomen) { this. metaAbdomen = metaAbdomen; }

    public BigDecimal getMedidaCinturaActual() { return medidaCinturaActual; }
    public void setMedidaCinturaActual(BigDecimal medidaCinturaActual) { this.medidaCinturaActual = medidaCinturaActual; }

    public BigDecimal getMedidaCinturaObjetivo() { return medidaCinturaObjetivo; }
    public void setMedidaCinturaObjetivo(BigDecimal medidaCinturaObjetivo) { this.medidaCinturaObjetivo = medidaCinturaObjetivo; }

    public String getMetaCintura() { return metaCintura; }
    public void setMetaCintura(String metaCintura) { this.metaCintura = metaCintura; }

    public BigDecimal getMedidaCaderaActual() { return medidaCaderaActual; }
    public void setMedidaCaderaActual(BigDecimal medidaCaderaActual) { this.medidaCaderaActual = medidaCaderaActual; }

    public BigDecimal getMedidaCaderaObjetivo() { return medidaCaderaObjetivo; }
    public void setMedidaCaderaObjetivo(BigDecimal medidaCaderaObjetivo) { this.medidaCaderaObjetivo = medidaCaderaObjetivo; }

    public String getMetaCadera() { return metaCadera; }
    public void setMetaCadera(String metaCadera) { this.metaCadera = metaCadera; }

    public BigDecimal getMedidaMusloActual() { return medidaMusloActual; }
    public void setMedidaMusloActual(BigDecimal medidaMusloActual) { this.medidaMusloActual = medidaMusloActual; }

    public BigDecimal getMedidaMusloObjetivo() { return medidaMusloObjetivo; }
    public void setMedidaMusloObjetivo(BigDecimal medidaMusloObjetivo) { this.medidaMusloObjetivo = medidaMusloObjetivo; }

    public String getMetaMuslo() { return metaMuslo; }
    public void setMetaMuslo(String metaMuslo) { this.metaMuslo = metaMuslo; }

    public BigDecimal getMedidaPechoActual() { return medidaPechoActual; }
    public void setMedidaPechoActual(BigDecimal medidaPechoActual) { this.medidaPechoActual = medidaPechoActual; }

    public BigDecimal getMedidaPechoObjetivo() { return medidaPechoObjetivo; }
    public void setMedidaPechoObjetivo(BigDecimal medidaPechoObjetivo) { this.medidaPechoObjetivo = medidaPechoObjetivo; }

    public String getMetaPecho() { return metaPecho; }
    public void setMetaPecho(String metaPecho) { this.metaPecho = metaPecho; }

    public BigDecimal getMedidaGluteoActual() { return medidaGluteoActual; }
    public void setMedidaGluteoActual(BigDecimal medidaGluteoActual) { this.medidaGluteoActual = medidaGluteoActual; }

    public BigDecimal getMedidaGluteoObjetivo() { return medidaGluteoObjetivo; }
    public void setMedidaGluteoObjetivo(BigDecimal medidaGluteoObjetivo) { this.medidaGluteoObjetivo = medidaGluteoObjetivo; }

    public String getMetaGluteo() { return metaGluteo; }
    public void setMetaGluteo(String metaGluteo) { this.metaGluteo = metaGluteo; }

    public String getDesayunoHabito() { return desayunoHabito; }
    public void setDesayunoHabito(String desayunoHabito) { this.desayunoHabito = desayunoHabito; }

    public String getAlmuerzoHabito() { return almuerzoHabito; }
    public void setAlmuerzoHabito(String almuerzoHabito) { this.almuerzoHabito = almuerzoHabito; }

    public String getCenaHabito() { return cenaHabito; }
    public void setCenaHabito(String cenaHabito) { this.cenaHabito = cenaHabito; }

    public String getSnackHabito() { return snackHabito; }
    public void setSnackHabito(String snackHabito) { this.snackHabito = snackHabito; }

    public String getHidratacionActual() { return hidratacionActual; }
    public void setHidratacionActual(String hidratacionActual) { this.hidratacionActual = hidratacionActual; }

    public String getSuenioActual() { return suenioActual; }
    public void setSuenioActual(String suenioActual) { this.suenioActual = suenioActual; }

    public String getAnimoActual() { return animoActual; }
    public void setAnimoActual(String animoActual) { this.animoActual = animoActual; }

    public OffsetDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(OffsetDateTime dateCreated) { this.dateCreated = dateCreated; }

    public OffsetDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(OffsetDateTime lastUpdated) { this.lastUpdated = lastUpdated; }
}