package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Calendario;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.events.BeforeDeleteUsuario;
import io.bootify.agenda_gym.model.CalendarioDTO;
import io.bootify.agenda_gym.repos.CalendarioRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.util.NotFoundException;
import io.bootify.agenda_gym.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CalendarioService {

    private final CalendarioRepository calendarioRepository;
    private final UsuarioRepository usuarioRepository;

    public CalendarioService(final CalendarioRepository calendarioRepository,
            final UsuarioRepository usuarioRepository) {
        this.calendarioRepository = calendarioRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<CalendarioDTO> findAll() {
        final List<Calendario> calendarios = calendarioRepository.findAll(Sort.by("id"));
        return calendarios.stream()
                .map(calendario -> mapToDTO(calendario, new CalendarioDTO()))
                .toList();
    }

    public CalendarioDTO get(final Long id) {
        return calendarioRepository.findById(id)
                .map(calendario -> mapToDTO(calendario, new CalendarioDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CalendarioDTO calendarioDTO) {
        final Calendario calendario = new Calendario();
        mapToEntity(calendarioDTO, calendario);
        return calendarioRepository.save(calendario).getId();
    }

    public void update(final Long id, final CalendarioDTO calendarioDTO) {
        final Calendario calendario = calendarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(calendarioDTO, calendario);
        calendarioRepository.save(calendario);
    }

    public void delete(final Long id) {
        final Calendario calendario = calendarioRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        calendarioRepository.delete(calendario);
    }

    private CalendarioDTO mapToDTO(final Calendario calendario, final CalendarioDTO calendarioDTO) {
        calendarioDTO.setId(calendario.getId());
        calendarioDTO.setFecha(calendario.getFecha());
        calendarioDTO.setEstado(calendario.getEstado());
        calendarioDTO.setColorEstado(calendario.getColorEstado());
        calendarioDTO.setDescripcion(calendario.getDescripcion());
        calendarioDTO.setUsuario(calendario.getUsuario() == null ? null : calendario.getUsuario().getId());
        return calendarioDTO;
    }

    private Calendario mapToEntity(final CalendarioDTO calendarioDTO, final Calendario calendario) {
        calendario.setFecha(calendarioDTO.getFecha());
        calendario.setEstado(calendarioDTO.getEstado());
        calendario.setColorEstado(calendarioDTO.getColorEstado());
        calendario.setDescripcion(calendarioDTO.getDescripcion());
        final Usuario usuario = calendarioDTO.getUsuario() == null ? null : usuarioRepository.findById(calendarioDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        calendario.setUsuario(usuario);
        return calendario;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final Calendario usuarioCalendario = calendarioRepository.findFirstByUsuarioId(event.getId());
        if (usuarioCalendario != null) {
            referencedException.setKey("usuario.calendario.usuario.referenced");
            referencedException.addParam(usuarioCalendario.getId());
            throw referencedException;
        }
    }

}
