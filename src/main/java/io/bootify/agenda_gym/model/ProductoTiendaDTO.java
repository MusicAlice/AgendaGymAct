package io.bootify.agenda_gym.model;

import java.math.BigDecimal;

public class ProductoTiendaDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String imagenUrl;
    private BigDecimal precio;
    private BigDecimal precioOferta;
    private Integer stock;
    private String genero;
    private Long categoriaId;
    private String categoriaNombre;

    public ProductoTiendaDTO() {
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

    public String getImagenUrl() {
        return imagenUrl;
    }

    public void setImagenUrl(final String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public void setPrecio(final BigDecimal precio) {
        this.precio = precio;
    }

    public BigDecimal getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(final BigDecimal precioOferta) {
        this.precioOferta = precioOferta;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(final Integer stock) {
        this.stock = stock;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(final String genero) {
        this.genero = genero;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(final Long categoriaId) {
        this.categoriaId = categoriaId;
    }

    public String getCategoriaNombre() {
        return categoriaNombre;
    }

    public void setCategoriaNombre(final String categoriaNombre) {
        this.categoriaNombre = categoriaNombre;
    }
}