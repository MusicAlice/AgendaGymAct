package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.model.RecuperacionClaveDTO;
import io.bootify.agenda_gym.service.RecuperacionClaveService;
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
@RequestMapping(value = "/api/recuperacionClaves", produces = MediaType.APPLICATION_JSON_VALUE)
public class RecuperacionClaveResource {

    private final RecuperacionClaveService recuperacionClaveService;

    public RecuperacionClaveResource(final RecuperacionClaveService recuperacionClaveService) {
        this.recuperacionClaveService = recuperacionClaveService;
    }

    @GetMapping
    public ResponseEntity<List<RecuperacionClaveDTO>> getAllRecuperacionClaves() {
        return ResponseEntity.ok(recuperacionClaveService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecuperacionClaveDTO> getRecuperacionClave(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(recuperacionClaveService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createRecuperacionClave(
            @RequestBody @Valid final RecuperacionClaveDTO recuperacionClaveDTO) {
        final Long createdId = recuperacionClaveService.create(recuperacionClaveDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateRecuperacionClave(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final RecuperacionClaveDTO recuperacionClaveDTO) {
        recuperacionClaveService.update(id, recuperacionClaveDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteRecuperacionClave(@PathVariable(name = "id") final Long id) {
        recuperacionClaveService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
