package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Logro;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.model.CheckInDTO;
import io.bootify.agenda_gym.model.ProgresoDTO;
import io.bootify.agenda_gym.repos.LogroRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogroService {

    private final LogroRepository logroRepository;

    public LogroService(LogroRepository logroRepository) {
        this.logroRepository = logroRepository;
    }

    // 🔹 Obtener todos los logros de un usuario
    public List<Logro> getLogrosByUsuarioId(Long usuarioId) {
        return logroRepository.findByUsuarioId(usuarioId);
    }

    // 🔹 Crear un logro específico
    public Logro crearLogro(Usuario usuario, String nombre, String descripcion, String icono) {
        Logro logro = new Logro();
        logro.setUsuario(usuario);
        logro.setNombreLogro(nombre);
        logro.setDescripcion(descripcion);
        logro.setIcono(icono);
        logro.setFechaObtenido(LocalDate.now());
        return logroRepository.save(logro);
    }

    // 🔹 Asignar logros automáticamente basado en reglas
    public void asignarLogrosAutomaticamente(Usuario usuario) {

        // Ejemplo de logro: "Constante" → entrenar 7 días seguidos
        boolean yaTieneConstante = getLogrosByUsuarioId(usuario.getId())
                .stream().anyMatch(l -> l.getNombreLogro().equals("Constante"));
        if (!yaTieneConstante && checkEntrenoConstante(usuario)) {
            crearLogro(usuario, "Constante", "Entrenaste 7 días seguidos.", "⭐");
        }

        // Ejemplo de logro: "Hidratado" → beber más de 2L diarios por 5 días
        boolean yaTieneHidratado = getLogrosByUsuarioId(usuario.getId())
                .stream().anyMatch(l -> l.getNombreLogro().equals("Hidratado"));
        if (!yaTieneHidratado && checkHidratacion(usuario)) {
            crearLogro(usuario, "Hidratado", "Bebiste más de 2L diarios por 5 días.", "💧");
        }

        // Ejemplo de logro: "Recuperado" → dormir bien toda la semana
        boolean yaTieneRecuperado = getLogrosByUsuarioId(usuario.getId())
                .stream().anyMatch(l -> l.getNombreLogro().equals("Recuperado"));
        if (!yaTieneRecuperado && checkSueño(usuario)) {
            crearLogro(usuario, "Recuperado", "Dormiste bien toda la semana.", "😴");
        }

        // Ejemplo de logro: "Progreso real" → mejorar medidas/peso 3 semanas seguidas
        boolean yaTieneProgresoReal = getLogrosByUsuarioId(usuario.getId())
                .stream().anyMatch(l -> l.getNombreLogro().equals("Progreso real"));
        if (!yaTieneProgresoReal && checkProgresoReal(usuario)) {
            crearLogro(usuario, "Progreso real", "Mejoraste tus medidas o peso 3 semanas seguidas.", "🏋️‍♀️");
        }
    }

    // 🔹 Métodos privados para reglas de logros (simples placeholders, se adaptan a tus datos)
    private boolean checkEntrenoConstante(Usuario usuario) {
        // Aquí deberías consultar los check-ins/calendario de entrenamiento
        // Por ahora ejemplo simulado:
        return false; // cambia según tu lógica real
    }

    private boolean checkHidratacion(Usuario usuario) {
        // Consultar check-ins de hidratación
        return false; // cambia según tu lógica real
    }

    private boolean checkSueño(Usuario usuario) {
        // Consultar check-ins de sueño
        return false; // cambia según tu lógica real
    }

    private boolean checkProgresoReal(Usuario usuario) {
        // Consultar progreso (peso/medidas)
        return false; // cambia según tu lógica real
    }
}
