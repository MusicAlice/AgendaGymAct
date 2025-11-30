package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.CheckIn;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.events.BeforeDeleteUsuario;
import io.bootify.agenda_gym.model.CheckInDTO;
import io.bootify.agenda_gym.repos.CheckInRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym.util.NotFoundException;
import io.bootify.agenda_gym.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CheckInService {

    private final CheckInRepository checkInRepository;
    private final UsuarioRepository usuarioRepository;
    private final LogroService logroService; // 🔹 Servicio de logros

    public CheckInService(final CheckInRepository checkInRepository,
                          final UsuarioRepository usuarioRepository,
                          final LogroService logroService) {
        this.checkInRepository = checkInRepository;
        this.usuarioRepository = usuarioRepository;
        this.logroService = logroService;
    }

    public List<CheckInDTO> findAll() {
        final List<CheckIn> checkIns = checkInRepository.findAll(Sort.by("id"));
        return checkIns.stream()
                .map(checkIn -> mapToDTO(checkIn, new CheckInDTO()))
                .toList();
    }

    public CheckInDTO get(final Long id) {
        return checkInRepository.findById(id)
                .map(checkIn -> mapToDTO(checkIn, new CheckInDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CheckInDTO checkInDTO) {
        final CheckIn checkIn = new CheckIn();
        mapToEntity(checkInDTO, checkIn);
        final CheckIn saved = checkInRepository.save(checkIn);

        // 🔹 Recalcular logros automáticamente
        if (saved.getUsuario() != null) {
            logroService.asignarLogrosAutomaticamente(saved.getUsuario());
        }

        return saved.getId();
    }

    public void update(final Long id, final CheckInDTO checkInDTO) {
        final CheckIn checkIn = checkInRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(checkInDTO, checkIn);
        final CheckIn saved = checkInRepository.save(checkIn);

        // 🔹 Recalcular logros automáticamente
        if (saved.getUsuario() != null) {
            logroService.asignarLogrosAutomaticamente(saved.getUsuario());
        }
    }

    public void delete(final Long id) {
        final CheckIn checkIn = checkInRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        checkInRepository.delete(checkIn);
    }

    private CheckInDTO mapToDTO(final CheckIn checkIn, final CheckInDTO checkInDTO) {
        checkInDTO.setId(checkIn.getId());
        checkInDTO.setFecha(checkIn.getFecha());
        checkInDTO.setEnergia(checkIn.getEnergia());
        checkInDTO.setAnimo(checkIn.getAnimo());
        checkInDTO.setHidratacion(checkIn.getHidratacion());
        checkInDTO.setSuenio(checkIn.getSuenio());
        checkInDTO.setObservaciones(checkIn.getObservaciones());
        checkInDTO.setUsuario(checkIn.getUsuario() == null ? null : checkIn.getUsuario().getId());
        return checkInDTO;
    }

    private CheckIn mapToEntity(final CheckInDTO checkInDTO, final CheckIn checkIn) {
        checkIn.setFecha(checkInDTO.getFecha());
        checkIn.setEnergia(checkInDTO.getEnergia());
        checkIn.setAnimo(checkInDTO.getAnimo());
        checkIn.setHidratacion(checkInDTO.getHidratacion());
        checkIn.setSuenio(checkInDTO.getSuenio());
        checkIn.setObservaciones(checkInDTO.getObservaciones());
        final Usuario usuario = checkInDTO.getUsuario() == null ? null : usuarioRepository.findById(checkInDTO.getUsuario())
                .orElseThrow(() -> new NotFoundException("usuario not found"));
        checkIn.setUsuario(usuario);
        return checkIn;
    }

    @EventListener(BeforeDeleteUsuario.class)
    public void on(final BeforeDeleteUsuario event) {
        final ReferencedException referencedException = new ReferencedException();
        final CheckIn usuarioCheckIn = checkInRepository.findFirstByUsuarioId(event.getId());
        if (usuarioCheckIn != null) {
            referencedException.setKey("usuario.checkIn.usuario.referenced");
            referencedException.addParam(usuarioCheckIn.getId());
            throw referencedException;
        }
    }
}
