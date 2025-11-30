package io.bootify.agenda_gym.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public class UsuarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{2,100}$",
             message = "El nombre solo puede contener letras y espacios, 2-100 caracteres")
    @Schema(example = "Angela Pérez")
    private String nombre;

    @NotBlank(message = "El correo es obligatorio")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
             message = "Correo inválido")
    @Size(max = 150)
    @Schema(example = "angela@example.com")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 255, message = "La contraseña debe tener al menos 6 caracteres")
    @Schema(example = "Secreto123!")
    private String password;

    @Schema(description = "Fecha de nacimiento (YYYY-MM-DD)", example = "1990-01-01")
    private LocalDate fechaNacimiento;

    @Schema(description = "Fecha de registro (ISO-8601)", example = "2025-10-31T15:00:00Z")
    private OffsetDateTime fechaRegistro;

    // getters y setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public OffsetDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(OffsetDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}