package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.model.ProductoTiendaDTO;
import io.bootify.agenda_gym.model.CategoriaTiendaDTO;
import io.bootify.agenda_gym.service.TiendaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/tienda")
public class TiendaApiController {

    private final TiendaService tiendaService;

    public TiendaApiController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaTiendaDTO>> categorias() {
        return ResponseEntity.ok(tiendaService.getCategorias());
    }

    @GetMapping("/productos/destacados")
    public ResponseEntity<List<ProductoTiendaDTO>> destacados() {
        return ResponseEntity.ok(tiendaService.getProductosDestacados());
    }

    @GetMapping("/productos/categoria/{id}")
    public ResponseEntity<List<ProductoTiendaDTO>> productosPorCategoria(@PathVariable Long id) {
        return ResponseEntity.ok(tiendaService.getProductosPorCategoria(id));
    }

    @GetMapping("/productos/buscar")
    public ResponseEntity<List<ProductoTiendaDTO>> buscar(@RequestParam("q") String q) {
        return ResponseEntity.ok(tiendaService.buscarProductos(q));
    }

    @GetMapping("/productos/filtrar")
    public ResponseEntity<List<ProductoTiendaDTO>> filtrar(
            @RequestParam(value = "categoria", required = false) Long categoria,
            @RequestParam(value = "genero", required = false) String genero,
            @RequestParam(value = "precioMin", required = false) BigDecimal precioMin,
            @RequestParam(value = "precioMax", required = false) BigDecimal precioMax,
            @RequestParam(value = "orden", required = false) String orden
    ) {
        return ResponseEntity.ok(tiendaService.filtrar(categoria, genero, precioMin, precioMax, orden));
    }
}
