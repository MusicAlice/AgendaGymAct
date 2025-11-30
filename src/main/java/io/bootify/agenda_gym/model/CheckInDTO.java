package io.bootify.agenda_gym.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


public class CheckInDTO {

    private Long id;

    private LocalDate fecha;

    @Size(max = 20)
    private String energia;

    @Size(max = 20)
    private String animo;

    @Size(max = 20)
    private String hidratacion;

    @Size(max = 20)
    private String suenio;

    private String observaciones;

    @NotNull
    private Long usuario;

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(final LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getEnergia() {
        return energia;
    }

    public void setEnergia(final String energia) {
        this.energia = energia;
    }

    public String getAnimo() {
        return animo;
    }

    public void setAnimo(final String animo) {
        this.animo = animo;
    }

    public String getHidratacion() {
        return hidratacion;
    }

    public void setHidratacion(final String hidratacion) {
        this.hidratacion = hidratacion;
    }

    public String getSuenio() {
        return suenio;
    }

    public void setSuenio(final String suenio) {
        this.suenio = suenio;
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
