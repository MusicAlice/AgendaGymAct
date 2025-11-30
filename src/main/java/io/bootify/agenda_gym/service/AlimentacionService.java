package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Alimentacion;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.events.BeforeDeleteUsuario;
import io.bootify.agenda_gym.model.AlimentacionDTO;
import io.bootify.agenda_gym.repos.AlimentacionRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.util.NotFoundException;
import io.bootify.agenda_gym.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AlimentacionService {

    private final AlimentacionRepository alimentacionRepository;
    private final UsuarioRepository usuarioRepository;

    public AlimentacionService(final AlimentacionRepository alimentacionRepository,
            final UsuarioRepository usuarioRepository) {
        this.alimentacionRepository = alimentacionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<AlimentacionDTO> findAll() {
        final List<Alimentacion> alimentacions = alimentacionRepository.findAll(Sort.by("id"));
        return alimentacions.stream()
                .map(alimentacion -> mapToDTO(alimentacion, new AlimentacionDTO()))
                .toList();
    }

    public AlimentacionDTO get(final Long id) {
        return alimentacionRepository.findById(id)
                .map(alimentacion -> mapToDTO(alimentacion, new AlimentacionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final AlimentacionDTO alimentacionDTO) {
        final Alimentacion alimentacion = new Alimentacion();
        mapToEntity(alimentacionDTO, alimentacion);
        return alimentacionRepository.save(alimentacion).getId();
    }

    public void update(final Long id, final AlimentacionDTO alimentacionDTO) {
        final Alimentacion alimentacion = alimentacionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(alimentacionDTO, alimentacion);
        alimentacionRepository.save(alimentacion);
    }

    public void delete(final Long id) {
        final Alimentacion alimentacion = alimentacionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        alimentacionRepository.delete(alimentacion);
    }

    private AlimentacionDTO mapToDTO(final Alimentacion alimentacion,
            final AlimentacionDTO alimentacionDTO) {
        alimentacionDTO.setId(alimentacion.getId());
        alimentacionDTO.setFecha(alimentacion.getFecha());
        alimentacionDTO.setDesayuno(alimentacion.getDesayuno());
        alimentacionDTO.setAlmuerzo(alimentacion.getAlmuerzo());
        alimentacionDTO.setCena(alimentacion.getCena());
        alimentacionDTO.setSnack(alimentacion.getSnack());
        alimentacionDTO.setObservaciones(alimentacion.getObservaciones());
        alimentacionDTO.setUsuario(alimentacion.getUsuario() == null ? null : alimentacion.getUsuario().getId());
        return alimentacionDTO;
    }

    private Alimentacion mapToEntity(final AlimentacionDTO alimentacionDTO,
            final Alimentacion alimentacion) {
        alimentacion.setFecha(alimentacionDTO.getFecha());
        alimentacion.setDesayuno(alimentacionDTO.getDesayuno());
        alimentacion.setAlmuerzo(alimentacionDTO.getAlmuerzo());
        alimentacion.setCena(alimentacionDTO.getCena());
        alimentacion.setSnack(alimentacionDTO.getSnack());
        alimentacion.setObservaciones(alimentacionDTO.getObservaciones());
        final Usuario usuario = alimentacionDTO.getUsuario() == null ? null : usuarioRepository.findById(alimentacionDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        alimentacion.setUsuario(usuario);
        return alimentacion;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final Alimentacion usuarioAlimentacion = alimentacionRepository.findFirstByUsuarioId(event.getId());
        if (usuarioAlimentacion != null) {
            referencedException.setKey("usuario.alimentacion.usuario.referenced");
            referencedException.addParam(usuarioAlimentacion.getId());
            throw referencedException;
        }
    }

}
