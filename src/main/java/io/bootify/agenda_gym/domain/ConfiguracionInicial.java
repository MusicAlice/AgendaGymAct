 package io.bootify.agenda_gym.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time. OffsetDateTime;

@Entity
@Table(name = "configuracion_inicial")
public class ConfiguracionInicial {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    // OBJETIVOS Y CONFIGURACIÓN (JSON)
    @Column(name = "objetivos_principales", length = 500)
    private String objetivosPrincipales;

    @Column(name = "configuracion_dias", columnDefinition = "TEXT")
    private String configuracionDias;

    @Column(name = "habitos_metas", length = 500)
    private String habitosMetas;

    // CALORÍAS
    @Column(name = "calorias_actual")
    private Integer caloriasActual;

    @Column(name = "calorias_meta")
    private Integer caloriasMeta;

    @Column(name = "objetivo_calorias", length = 50)
    private String objetivoCalorias;

    // PESO Y FECHA META
    @Column(name = "peso_inicial", precision = 5, scale = 2)
    private BigDecimal pesoInicial;

    @Column(name = "meta_peso", precision = 5, scale = 2)
    private BigDecimal metaPeso;

    @Column(name = "meta_fecha")
    private LocalDate metaFecha;

    // MEDIDAS - BRAZO
    @Column(name = "medida_brazo_actual", precision = 5, scale = 2)
    private BigDecimal medidaBrazoActual;

    @Column(name = "medida_brazo_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaBrazoObjetivo;

    @Column(name = "meta_brazo", length = 50)
    private String metaBrazo;

    // MEDIDAS - ABDOMEN
    @Column(name = "medida_abdomen_actual", precision = 5, scale = 2)
    private BigDecimal medidaAbdomenActual;

    @Column(name = "medida_abdomen_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaAbdomenObjetivo;

    @Column(name = "meta_abdomen", length = 50)
    private String metaAbdomen;

    // MEDIDAS - CINTURA
    @Column(name = "medida_cintura_actual", precision = 5, scale = 2)
    private BigDecimal medidaCinturaActual;

    @Column(name = "medida_cintura_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaCinturaObjetivo;

    @Column(name = "meta_cintura", length = 50)
    private String metaCintura;

    // MEDIDAS - CADERA
    @Column(name = "medida_cadera_actual", precision = 5, scale = 2)
    private BigDecimal medidaCaderaActual;

    @Column(name = "medida_cadera_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaCaderaObjetivo;

    @Column(name = "meta_cadera", length = 50)
    private String metaCadera;

    // MEDIDAS - MUSLO
    @Column(name = "medida_muslo_actual", precision = 5, scale = 2)
    private BigDecimal medidaMusloActual;

    @Column(name = "medida_muslo_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaMusloObjetivo;

    @Column(name = "meta_muslo", length = 50)
    private String metaMuslo;

    // MEDIDAS - PECHO
    @Column(name = "medida_pecho_actual", precision = 5, scale = 2)
    private BigDecimal medidaPechoActual;

    @Column(name = "medida_pecho_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaPechoObjetivo;

    @Column(name = "meta_pecho", length = 50)
    private String metaPecho;

    // MEDIDAS - GLÚTEO
    @Column(name = "medida_gluteo_actual", precision = 5, scale = 2)
    private BigDecimal medidaGluteoActual;

    @Column(name = "medida_gluteo_objetivo", precision = 5, scale = 2)
    private BigDecimal medidaGluteoObjetivo;

    @Column(name = "meta_gluteo", length = 50)
    private String metaGluteo;

    // HÁBITOS ALIMENTICIOS
    @Column(name = "desayuno_habito", length = 50)
    private String desayunoHabito;

    @Column(name = "almuerzo_habito", length = 50)
    private String almuerzoHabito;

    @Column(name = "cena_habito", length = 50)
    private String cenaHabito;

    @Column(name = "snack_habito", length = 50)
    private String snackHabito;

    // BIENESTAR
    @Column(name = "hidratacion_actual", length = 50)
    private String hidratacionActual;

    @Column(name = "suenio_actual", length = 50)
    private String suenioActual;

    @Column(name = "animo_actual", length = 50)
    private String animoActual;

    // TIMESTAMPS
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

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
    public void setMetaAbdomen(String metaAbdomen) { this.metaAbdomen = metaAbdomen; }

    public BigDecimal getMedidaCinturaActual() { return medidaCinturaActual; }
    public void setMedidaCinturaActual(BigDecimal medidaCinturaActual) { this. medidaCinturaActual = medidaCinturaActual; }

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
    public void setMedidaMusloActual(BigDecimal medidaMusloActual) { this. medidaMusloActual = medidaMusloActual; }

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

    @PrePersist
    public void prePersist() {
        dateCreated = OffsetDateTime.now();
        lastUpdated = OffsetDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }
}