package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.model.ProgresoDTO;
import io.bootify.agenda_gym.service.ProgresoService;
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
@RequestMapping(value = "/api/progresos", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProgresoResource {

    private final ProgresoService progresoService;

    public ProgresoResource(final ProgresoService progresoService) {
        this.progresoService = progresoService;
    }

    @GetMapping
    public ResponseEntity<List<ProgresoDTO>> getAllProgresos() {
        return ResponseEntity.ok(progresoService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProgresoDTO> getProgreso(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(progresoService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createProgreso(@RequestBody @Valid final ProgresoDTO progresoDTO) {
        final Long createdId = progresoService.create(progresoDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateProgreso(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ProgresoDTO progresoDTO) {
        progresoService.update(id, progresoDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProgreso(@PathVariable(name = "id") final Long id) {
        progresoService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
