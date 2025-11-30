package io.bootify.agenda_gym.domain;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Logro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "nombre_logro", nullable = false, length = 100)
    private String nombreLogro;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(length = 10)
    private String icono;

    private LocalDate fechaObtenido;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public String getNombreLogro() { return nombreLogro; }
    public void setNombreLogro(String nombreLogro) { this.nombreLogro = nombreLogro; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }

    public LocalDate getFechaObtenido() { return fechaObtenido; }
    public void setFechaObtenido(LocalDate fechaObtenido) { this.fechaObtenido = fechaObtenido; }
}
