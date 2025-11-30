package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.model.CalendarioDTO;
import io.bootify.agenda_gym.service.CalendarioService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/calendarios", produces = MediaType.APPLICATION_JSON_VALUE)
public class CalendarioResource {

    private final CalendarioService calendarioService;

    public CalendarioResource(final CalendarioService calendarioService) {
        this.calendarioService = calendarioService;
    }

    @GetMapping
    public ResponseEntity<List<CalendarioDTO>> getAllCalendarios() {
        return ResponseEntity.ok(calendarioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CalendarioDTO> getCalendario(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(calendarioService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCalendario(
            @RequestBody @Valid final CalendarioDTO calendarioDTO) {
        final Long createdId = calendarioService.create(calendarioDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCalendario(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CalendarioDTO calendarioDTO) {
        calendarioService.update(id, calendarioDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCalendario(@PathVariable(name = "id") final Long id) {
        calendarioService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
