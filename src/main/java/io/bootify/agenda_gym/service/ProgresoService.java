package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Progreso;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.events.BeforeDeleteUsuario;
import io.bootify.agenda_gym.model.ProgresoDTO;
import io.bootify.agenda_gym.repos.ProgresoRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.util.NotFoundException;
import io.bootify.agenda_gym.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ProgresoService {

    private final ProgresoRepository progresoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProgresoService(final ProgresoRepository progresoRepository,
            final UsuarioRepository usuarioRepository) {
        this.progresoRepository = progresoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<ProgresoDTO> findAll() {
        final List<Progreso> progresoes = progresoRepository.findAll(Sort.by("id"));
        return progresoes.stream()
                .map(progreso -> mapToDTO(progreso, new ProgresoDTO()))
                .toList();
    }

    public ProgresoDTO get(final Long id) {
        return progresoRepository.findById(id)
                .map(progreso -> mapToDTO(progreso, new ProgresoDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ProgresoDTO progresoDTO) {
        final Progreso progreso = new Progreso();
        mapToEntity(progresoDTO, progreso);
        return progresoRepository.save(progreso).getId();
    }

    public void update(final Long id, final ProgresoDTO progresoDTO) {
        final Progreso progreso = progresoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(progresoDTO, progreso);
        progresoRepository.save(progreso);
    }

    public void delete(final Long id) {
        final Progreso progreso = progresoRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        progresoRepository.delete(progreso);
    }

    private ProgresoDTO mapToDTO(final Progreso progreso, final ProgresoDTO progresoDTO) {
        progresoDTO.setId(progreso.getId());
        progresoDTO.setFechaRegistro(progreso.getFechaRegistro());
        progresoDTO.setTipoEntrenamiento(progreso.getTipoEntrenamiento());
        progresoDTO.setEjerciciosRealizados(progreso.getEjerciciosRealizados());
        progresoDTO.setPeso(progreso.getPeso());
        progresoDTO.setMedidasCintura(progreso.getMedidasCintura());
        progresoDTO.setMedidasPecho(progreso.getMedidasPecho());
        progresoDTO.setMedidasBrazo(progreso.getMedidasBrazo());
        progresoDTO.setObservaciones(progreso.getObservaciones());
        progresoDTO.setUsuario(progreso.getUsuario() == null ? null : progreso.getUsuario().getId());
        return progresoDTO;
    }

    private Progreso mapToEntity(final ProgresoDTO progresoDTO, final Progreso progreso) {
        progreso.setFechaRegistro(progresoDTO.getFechaRegistro());
        progreso.setTipoEntrenamiento(progresoDTO.getTipoEntrenamiento());
        progreso.setEjerciciosRealizados(progresoDTO.getEjerciciosRealizados());
        progreso.setPeso(progresoDTO.getPeso());
        progreso.setMedidasCintura(progresoDTO.getMedidasCintura());
        progreso.setMedidasPecho(progresoDTO.getMedidasPecho());
        progreso.setMedidasBrazo(progresoDTO.getMedidasBrazo());
        progreso.setObservaciones(progresoDTO.getObservaciones());
        final Usuario usuario = progresoDTO.getUsuario() == null ? null : usuarioRepository.findById(progresoDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        progreso.setUsuario(usuario);
        return progreso;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final Progreso usuarioProgreso = progresoRepository.findFirstByUsuarioId(event.getId());
        if (usuarioProgreso != null) {
            referencedException.setKey("usuario.progreso.usuario.referenced");
            referencedException.addParam(usuarioProgreso.getId());
            throw referencedException;
        }
    }

}
