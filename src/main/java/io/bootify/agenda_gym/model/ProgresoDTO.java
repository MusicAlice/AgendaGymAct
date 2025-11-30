package io.bootify.agenda_gym.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


public class ProgresoDTO {

    private Long id;

    private OffsetDateTime fechaRegistro;

    @Size(max = 30)
    private String tipoEntrenamiento;

    @Size(max = 30)
    private String ejerciciosRealizados;

    @Digits(integer = 5, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "33.08")
    private BigDecimal peso;

    @Digits(integer = 5, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "27.08")
    private BigDecimal medidasCintura;

    @Digits(integer = 5, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "36.08")
    private BigDecimal medidasPecho;

    @Digits(integer = 5, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "11.08")
    private BigDecimal medidasBrazo;

    private String observaciones;

    @NotNull
    private Long usuario;

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

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(final Long usuario) {
        this.usuario = usuario;
    }

}
