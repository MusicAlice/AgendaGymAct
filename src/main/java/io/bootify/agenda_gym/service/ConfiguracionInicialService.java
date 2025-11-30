package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym. domain.ConfiguracionInicial;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.model.ConfiguracionInicialDTO;
import io.bootify. agenda_gym.repos.ConfiguracionInicialRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import io.bootify.agenda_gym. util. NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConfiguracionInicialService {

    private final ConfiguracionInicialRepository configuracionInicialRepository;
    private final UsuarioRepository usuarioRepository;

    public ConfiguracionInicialService(
            final ConfiguracionInicialRepository configuracionInicialRepository,
            final UsuarioRepository usuarioRepository) {
        this.configuracionInicialRepository = configuracionInicialRepository;
        this. usuarioRepository = usuarioRepository;
    }

    public List<ConfiguracionInicialDTO> findAll() {
        final List<ConfiguracionInicial> configuraciones = configuracionInicialRepository. findAll(Sort.by("id"));
        return configuraciones. stream()
                .map(configuracion -> mapToDTO(configuracion, new ConfiguracionInicialDTO()))
                .toList();
    }

    public ConfiguracionInicialDTO get(final Long id) {
        return configuracionInicialRepository.findById(id)
                .map(configuracion -> mapToDTO(configuracion, new ConfiguracionInicialDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ConfiguracionInicialDTO configuracionInicialDTO) {
        final ConfiguracionInicial configuracionInicial = new ConfiguracionInicial();
        mapToEntity(configuracionInicialDTO, configuracionInicial);
        return configuracionInicialRepository.save(configuracionInicial).getId();
    }

    public void update(final Long id, final ConfiguracionInicialDTO configuracionInicialDTO) {
        final ConfiguracionInicial configuracionInicial = configuracionInicialRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(configuracionInicialDTO, configuracionInicial);
        configuracionInicialRepository.save(configuracionInicial);
    }

    public void delete(final Long id) {
        configuracionInicialRepository.deleteById(id);
    }

    public ConfiguracionInicialDTO findByUsuarioId(final Long usuarioId) {
        return configuracionInicialRepository.findByUsuarioId(usuarioId)
                .map(configuracion -> mapToDTO(configuracion, new ConfiguracionInicialDTO()))
                .orElse(null);
    }

    public boolean existsByUsuarioId(final Long usuarioId) {
        return configuracionInicialRepository. existsByUsuarioId(usuarioId);
    }

    private ConfiguracionInicialDTO mapToDTO(final ConfiguracionInicial configuracion,
            final ConfiguracionInicialDTO dto) {
        
        dto.setId(configuracion.getId());
        dto.setUsuarioId(configuracion.getUsuario() == null ? null : configuracion.getUsuario().getId());
        
        // OBJETIVOS Y CONFIGURACIÓN (JSON)
        dto.setObjetivosPrincipales(configuracion.getObjetivosPrincipales());
        dto.setConfiguracionDias(configuracion.getConfiguracionDias());
        dto.setHabitosMetas(configuracion.getHabitosMetas());
        
        // CALORÍAS
        dto.setCaloriasActual(configuracion.getCaloriasActual());
        dto. setCaloriasMeta(configuracion.getCaloriasMeta());
        dto.setObjetivoCalorias(configuracion. getObjetivoCalorias());
        
        // PESO Y FECHA META
        dto.setPesoInicial(configuracion.getPesoInicial());
        dto.setMetaPeso(configuracion.getMetaPeso());
        dto. setMetaFecha(configuracion.getMetaFecha());
        
        // MEDIDAS - BRAZO
        dto.setMedidaBrazoActual(configuracion.getMedidaBrazoActual());
        dto.setMedidaBrazoObjetivo(configuracion.getMedidaBrazoObjetivo());
        dto.setMetaBrazo(configuracion.getMetaBrazo());
        
        // MEDIDAS - ABDOMEN
        dto.setMedidaAbdomenActual(configuracion. getMedidaAbdomenActual());
        dto.setMedidaAbdomenObjetivo(configuracion.getMedidaAbdomenObjetivo());
        dto.setMetaAbdomen(configuracion.getMetaAbdomen());
        
        // MEDIDAS - CINTURA
        dto.setMedidaCinturaActual(configuracion.getMedidaCinturaActual());
        dto.setMedidaCinturaObjetivo(configuracion.getMedidaCinturaObjetivo());
        dto.setMetaCintura(configuracion.getMetaCintura());
        
        // MEDIDAS - CADERA
        dto.setMedidaCaderaActual(configuracion.getMedidaCaderaActual());
        dto.setMedidaCaderaObjetivo(configuracion.getMedidaCaderaObjetivo());
        dto.setMetaCadera(configuracion.getMetaCadera());
        
        // MEDIDAS - MUSLO
        dto.setMedidaMusloActual(configuracion.getMedidaMusloActual());
        dto.setMedidaMusloObjetivo(configuracion.getMedidaMusloObjetivo());
        dto. setMetaMuslo(configuracion.getMetaMuslo());
        
        // MEDIDAS - PECHO
        dto.setMedidaPechoActual(configuracion.getMedidaPechoActual());
        dto.setMedidaPechoObjetivo(configuracion.getMedidaPechoObjetivo());
        dto.setMetaPecho(configuracion.getMetaPecho());
        
        // MEDIDAS - GLÚTEO
        dto.setMedidaGluteoActual(configuracion.getMedidaGluteoActual());
        dto.setMedidaGluteoObjetivo(configuracion.getMedidaGluteoObjetivo());
        dto.setMetaGluteo(configuracion.getMetaGluteo());
        
        // HÁBITOS ALIMENTICIOS
        dto.setDesayunoHabito(configuracion.getDesayunoHabito());
        dto.setAlmuerzoHabito(configuracion.getAlmuerzoHabito());
        dto. setCenaHabito(configuracion.getCenaHabito());
        dto.setSnackHabito(configuracion.getSnackHabito());
        
        // BIENESTAR
        dto.setHidratacionActual(configuracion.getHidratacionActual());
        dto.setSuenioActual(configuracion.getSuenioActual());
        dto.setAnimoActual(configuracion. getAnimoActual());
        
        // TIMESTAMPS
        dto.setDateCreated(configuracion.getDateCreated());
        dto.setLastUpdated(configuracion.getLastUpdated());
        
        return dto;
    }

    private ConfiguracionInicial mapToEntity(final ConfiguracionInicialDTO dto,
            final ConfiguracionInicial config) {
        
        // Asignar usuario
        if (dto.getUsuarioId() != null) {
            final Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                    .orElseThrow(() -> new NotFoundException("Usuario no encontrado"));
            config.setUsuario(usuario);
        }
        
        // OBJETIVOS Y CONFIGURACIÓN (JSON)
        config.setObjetivosPrincipales(dto.getObjetivosPrincipales());
        config.setConfiguracionDias(dto.getConfiguracionDias());
        config.setHabitosMetas(dto. getHabitosMetas());
        
        // CALORÍAS
        config.setCaloriasActual(dto.getCaloriasActual());
        config.setCaloriasMeta(dto.getCaloriasMeta());
        config.setObjetivoCalorias(dto.getObjetivoCalorias());
        
        // PESO Y FECHA META
        config.setPesoInicial(dto.getPesoInicial());
        config. setMetaPeso(dto.getMetaPeso());
        config. setMetaFecha(dto. getMetaFecha());
        
        // MEDIDAS - BRAZO
        config.setMedidaBrazoActual(dto.getMedidaBrazoActual());
        config.setMedidaBrazoObjetivo(dto.getMedidaBrazoObjetivo());
        config.setMetaBrazo(dto.getMetaBrazo());
        
        // MEDIDAS - ABDOMEN
        config. setMedidaAbdomenActual(dto.getMedidaAbdomenActual());
        config.setMedidaAbdomenObjetivo(dto.getMedidaAbdomenObjetivo());
        config. setMetaAbdomen(dto.getMetaAbdomen());
        
        // MEDIDAS - CINTURA
        config. setMedidaCinturaActual(dto.getMedidaCinturaActual());
        config.setMedidaCinturaObjetivo(dto.getMedidaCinturaObjetivo());
        config.setMetaCintura(dto.getMetaCintura());
        
        // MEDIDAS - CADERA
        config.setMedidaCaderaActual(dto.getMedidaCaderaActual());
        config.setMedidaCaderaObjetivo(dto. getMedidaCaderaObjetivo());
        config.setMetaCadera(dto.getMetaCadera());
        
        // MEDIDAS - MUSLO
        config.setMedidaMusloActual(dto. getMedidaMusloActual());
        config.setMedidaMusloObjetivo(dto.getMedidaMusloObjetivo());
        config. setMetaMuslo(dto. getMetaMuslo());
        
        // MEDIDAS - PECHO
        config.setMedidaPechoActual(dto.getMedidaPechoActual());
        config.setMedidaPechoObjetivo(dto.getMedidaPechoObjetivo());
        config.setMetaPecho(dto.getMetaPecho());
        
        // MEDIDAS - GLÚTEO
        config.setMedidaGluteoActual(dto.getMedidaGluteoActual());
        config.setMedidaGluteoObjetivo(dto.getMedidaGluteoObjetivo());
        config.setMetaGluteo(dto.getMetaGluteo());
        
        // HÁBITOS ALIMENTICIOS
        config.setDesayunoHabito(dto.getDesayunoHabito());
        config.setAlmuerzoHabito(dto.getAlmuerzoHabito());
        config.setCenaHabito(dto.getCenaHabito());
        config. setSnackHabito(dto.getSnackHabito());
        
        // BIENESTAR
        config.setHidratacionActual(dto.getHidratacionActual());
        config.setSuenioActual(dto.getSuenioActual());
        config.setAnimoActual(dto.getAnimoActual());
        
        return config;
    }
}