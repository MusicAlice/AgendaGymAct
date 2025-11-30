package io.bootify.agenda_gym.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;


public class CalendarioDTO {

    private Long id;

    @NotNull
    private LocalDate fecha;

    @Size(max = 20)
    private String estado;

    @Size(max = 10)
    private String colorEstado;

    private String descripcion;

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(final String estado) {
        this.estado = estado;
    }

    public String getColorEstado() {
        return colorEstado;
    }

    public void setColorEstado(final String colorEstado) {
        this.colorEstado = colorEstado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public Long getUsuario() {
        return usuario;
    }

    public void setUsuario(final Long usuario) {
        this.usuario = usuario;
    }

}
