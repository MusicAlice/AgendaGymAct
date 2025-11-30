package io.bootify.agenda_gym.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
public class Progreso {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private OffsetDateTime fechaRegistro;

    @Column(length = 30)
    private String tipoEntrenamiento;

    @Column(length = 30)
    private String ejerciciosRealizados;

    @Column(precision = 5, scale = 2)
    private BigDecimal peso;

    @Column(precision = 5, scale = 2)
    private BigDecimal medidasCintura;

    @Column(precision = 5, scale = 2)
    private BigDecimal medidasPecho;

    @Column(precision = 5, scale = 2)
    private BigDecimal medidasBrazo;

    @Column(columnDefinition = "text")
    private String observaciones;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public OffsetDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(final OffsetDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getTipoEntrenamiento() {
        return tipoEntrenamiento;
    }

    public void setTipoEntrenamiento(final String tipoEntrenamiento) {
        this.tipoEntrenamiento = tipoEntrenamiento;
    }

    public String getEjerciciosRealizados() {
        return ejerciciosRealizados;
    }

    public void setEjerciciosRealizados(final String ejerciciosRealizados) {
        this.ejerciciosRealizados = ejerciciosRealizados;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(final BigDecimal peso) {
        this.peso = peso;
    }

    public BigDecimal getMedidasCintura() {
        return medidasCintura;
    }

    public void setMedidasCintura(final BigDecimal medidasCintura) {
        this.medidasCintura = medidasCintura;
    }

    public BigDecimal getMedidasPecho() {
        return medidasPecho;
    }

    public void setMedidasPecho(final BigDecimal medidasPecho) {
        this.medidasPecho = medidasPecho;
    }

    public BigDecimal getMedidasBrazo() {
        return medidasBrazo;
    }

    public void setMedidasBrazo(final BigDecimal medidasBrazo) {
        this.medidasBrazo = medidasBrazo;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(final String observaciones) {
        this.observaciones = observaciones;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(final Usuario usuario) {
        this.usuario = usuario;
    }

    public OffsetDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(final OffsetDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(final OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

}
