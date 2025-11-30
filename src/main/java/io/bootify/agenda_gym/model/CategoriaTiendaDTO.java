package io.bootify.agenda_gym.model;

public class CategoriaTiendaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String icono;
    private String imagenUrl;
    private Integer orden;

    public CategoriaTiendaDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(final String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(final String icono) {
        this.icono = icono;
    }

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Integer getOrden() {
        return orden;
    }

    public void setOrden(final Integer orden) {
        this.orden = orden;
    }
}