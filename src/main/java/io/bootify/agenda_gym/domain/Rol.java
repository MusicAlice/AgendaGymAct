package io.bootify.agenda_gym.domain;

public enum Rol {
    ADMIN,      // Gestiona todo: empleados, rangos, configuración
    GERENTE,    // Gestiona inventario y horarios de entrenadores
    ENTRENADOR, // Gestiona su disponibilidad y clientes
    USUARIO     // Usuario final: compra, agenda, perfil
}