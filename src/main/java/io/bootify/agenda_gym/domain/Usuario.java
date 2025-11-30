package io.bootify.agenda_gym.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time. OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data. jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "usuario")
@EntityListeners(AuditingEntityListener.class)
public class Usuario {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
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

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "email", nullable = false, length = 150, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "fecha_registro")
    private OffsetDateTime fechaRegistro;

    @Column(name = "verificado", nullable = false)
    private boolean verificado = false;

    // ========== ROL DEL USUARIO ==========
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private Rol rol = Rol.USUARIO;

    // ========== FOTO DE PERFIL ==========
    @Column(name = "foto_url", length = 500)
    private String fotoUrl;

    // ========== TELÉFONO ==========
    @Column(name = "telefono", length = 20)
    private String telefono;

    // ========== CUENTA ACTIVA ==========
    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    // ========== ENTRENADOR ASIGNADO (para usuarios) ==========
    @ManyToOne(fetch = FetchType. LAZY)
    @JoinColumn(name = "entrenador_id")
    private Usuario entrenadorAsignado;

    // ========== USUARIOS ASIGNADOS (para entrenadores) ==========
    @OneToMany(mappedBy = "entrenadorAsignado")
    private Set<Usuario> clientesAsignados = new HashSet<>();

    // ========== PERMITIR QUE ENTRENADOR VEA AGENDA ==========
    @Column(name = "permitir_ver_agenda", nullable = false)
    private Boolean permitirVerAgenda = false;

    @OneToMany(mappedBy = "usuario")
    private Set<Progreso> usuarioProgresoes = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private Set<Alimentacion> usuarioAlimentacions = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private Set<CheckIn> usuarioCheckIns = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private Set<Calendario> usuarioCalendarios = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    private Set<RecuperacionClave> usuarioRecuperacionClaves = new HashSet<>();

    // ========== RELACIÓN CON CARRITO ==========
    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Carrito carrito;

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    // ==================== GETTERS Y SETTERS ====================

    public Long getId() { return id; }
    public void setId(final Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(final String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(final String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(final String password) { this.password = password; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(final LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public OffsetDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(final OffsetDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public boolean isVerificado() { return verificado; }
    public void setVerificado(final boolean verificado) { this.verificado = verificado; }

    public Rol getRol() { return rol; }
    public void setRol(final Rol rol) { this.rol = rol; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(final String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getTelefono() { return telefono; }
    public void setTelefono(final String telefono) { this.telefono = telefono; }

    public Boolean getActivo() { return activo; }
    public void setActivo(final Boolean activo) { this.activo = activo; }

    public Usuario getEntrenadorAsignado() { return entrenadorAsignado; }
    public void setEntrenadorAsignado(final Usuario entrenadorAsignado) { this.entrenadorAsignado = entrenadorAsignado; }

    public Set<Usuario> getClientesAsignados() { return clientesAsignados; }
    public void setClientesAsignados(final Set<Usuario> clientesAsignados) { this. clientesAsignados = clientesAsignados; }

    public Boolean getPermitirVerAgenda() { return permitirVerAgenda; }
    public void setPermitirVerAgenda(final Boolean permitirVerAgenda) { this.permitirVerAgenda = permitirVerAgenda; }

    public Carrito getCarrito() { return carrito; }
    public void setCarrito(final Carrito carrito) { this.carrito = carrito; }

    public Set<Progreso> getUsuarioProgresoes() { return usuarioProgresoes; }
    public void setUsuarioProgresoes(final Set<Progreso> usuarioProgresoes) { this. usuarioProgresoes = usuarioProgresoes; }

    public Set<Alimentacion> getUsuarioAlimentacions() { return usuarioAlimentacions; }
    public void setUsuarioAlimentacions(final Set<Alimentacion> usuarioAlimentacions) { this.usuarioAlimentacions = usuarioAlimentacions; }

    public Set<CheckIn> getUsuarioCheckIns() { return usuarioCheckIns; }
    public void setUsuarioCheckIns(final Set<CheckIn> usuarioCheckIns) { this. usuarioCheckIns = usuarioCheckIns; }

    public Set<Calendario> getUsuarioCalendarios() { return usuarioCalendarios; }
    public void setUsuarioCalendarios(final Set<Calendario> usuarioCalendarios) { this.usuarioCalendarios = usuarioCalendarios; }

    public Set<RecuperacionClave> getUsuarioRecuperacionClaves() { return usuarioRecuperacionClaves; }
    public void setUsuarioRecuperacionClaves(final Set<RecuperacionClave> usuarioRecuperacionClaves) { this.usuarioRecuperacionClaves = usuarioRecuperacionClaves; }

    public OffsetDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(final OffsetDateTime dateCreated) { this.dateCreated = dateCreated; }

    public OffsetDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(final OffsetDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    // ==================== MÉTODOS HELPER PARA ROLES ====================

    public boolean isAdmin() { return this.rol == Rol.ADMIN; }
    public boolean isGerente() { return this.rol == Rol.GERENTE; }
    public boolean isEntrenador() { return this.rol == Rol.ENTRENADOR; }
    public boolean isUsuario() { return this.rol == Rol.USUARIO; }

    // Verifica si tiene permisos de administrador (ADMIN)
    public boolean tienePermisosAdmin() {
        return this.rol == Rol.ADMIN;
    }

    // Verifica si tiene permisos de gerente (ADMIN o GERENTE)
    public boolean tienePermisosGerente() {
        return this. rol == Rol.ADMIN || this.rol == Rol. GERENTE;
    }

    // Verifica si tiene permisos de entrenador (ADMIN, GERENTE o ENTRENADOR)
    public boolean tienePermisosEntrenador() {
        return this.rol == Rol.ADMIN || this.rol == Rol.GERENTE || this.rol == Rol.ENTRENADOR;
    }

    // Obtener el nombre del rol en español para mostrar en UI
    public String getRolNombre() {
        return switch (this.rol) {
            case ADMIN -> "Administrador";
            case GERENTE -> "Gerente";
            case ENTRENADOR -> "Entrenador";
            case USUARIO -> "Usuario";
        };
    }

    // Obtener color del badge según rol
    public String getRolColor() {
        return switch (this.rol) {
            case ADMIN -> "#dc3545";      // Rojo
            case GERENTE -> "#fd7e14";    // Naranja
            case ENTRENADOR -> "#28a745"; // Verde
            case USUARIO -> "#6c757d";    // Gris
        };
    }
}