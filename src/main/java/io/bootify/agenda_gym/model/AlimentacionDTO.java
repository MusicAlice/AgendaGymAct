package io.bootify.agenda_gym.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


public class AlimentacionDTO {

    private Long id;

    private LocalDate fecha;

    @Size(max = 20)
    private String desayuno;

    @Size(max = 20)
    private String almuerzo;

    @Size(max = 20)
    private String cena;

    @Size(max = 20)
    private String snack;

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

    public String getDesayuno() {
        return desayuno;
    }

    public void setDesayuno(final String desayuno) {
        this.desayuno = desayuno;
    }

    public String getAlmuerzo() {
        return almuerzo;
    }

    public void setAlmuerzo(final String almuerzo) {
        this.almuerzo = almuerzo;
    }

    public String getCena() {
        return cena;
    }

    public void setCena(final String cena) {
        this.cena = cena;
    }

    public String getSnack() {
        return snack;
    }

    public void setSnack(final String snack) {
        this.snack = snack;
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
