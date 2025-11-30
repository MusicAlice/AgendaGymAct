package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.model.AlimentacionDTO;
import io.bootify.agenda_gym.service.AlimentacionService;
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
@RequestMapping(value = "/api/alimentacions", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlimentacionResource {

    private final AlimentacionService alimentacionService;

    public AlimentacionResource(final AlimentacionService alimentacionService) {
        this.alimentacionService = alimentacionService;
    }

    @GetMapping
    public ResponseEntity<List<AlimentacionDTO>> getAllAlimentacions() {
        return ResponseEntity.ok(alimentacionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlimentacionDTO> getAlimentacion(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(alimentacionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createAlimentacion(
            @RequestBody @Valid final AlimentacionDTO alimentacionDTO) {
        final Long createdId = alimentacionService.create(alimentacionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateAlimentacion(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final AlimentacionDTO alimentacionDTO) {
        alimentacionService.update(id, alimentacionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteAlimentacion(@PathVariable(name = "id") final Long id) {
        alimentacionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
