package io.bootify.agenda_gym.rest;

import io.bootify.agenda_gym.domain.Producto;
import io.bootify.agenda_gym.domain.Carrito;
import io.bootify.agenda_gym.domain.Categoria;
import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.service.CarritoService;
import io.bootify.agenda_gym.service.CategoriaService;
import io.bootify.agenda_gym.service.ProductoService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
public class TiendaController {

    private final CategoriaService categoriaService;
    private final ProductoService productoService;
    private final CarritoService carritoService;

    public TiendaController(CategoriaService categoriaService,
                           ProductoService productoService,
                           CarritoService carritoService) {
        this.categoriaService = categoriaService;
        this.productoService = productoService;
        this.carritoService = carritoService;
    }

    @PostConstruct
    public void init() {
        categoriaService.inicializarCategoriasPorDefecto();
        productoService.inicializarProductosEjemplo();
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        List<Producto> productosDestacados = productoService.obtenerDestacados();
        List<Producto> productosNuevos = productoService.obtenerNuevos();
        List<Producto> productosOferta = productoService.obtenerEnOferta();
        
        model.addAttribute("categorias", categorias);
        model.addAttribute("productosDestacados", productosDestacados);
        model.addAttribute("productosNuevos", productosNuevos);
        model.addAttribute("productosOferta", productosOferta);
        model.addAttribute("todosProductos", productoService.obtenerProductosActivos());
        
        // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
            model.addAttribute("usuarioId", usuarioLogueado.getId());
            
            int itemsCarrito = carritoService.obtenerCantidadItems(usuarioLogueado.getId());
            model.addAttribute("itemsCarrito", itemsCarrito);
        }
        // =======================================================
        
        return "tienda/index";
    }

    // FALTABA: Mapeo para /tienda
    @GetMapping("/tienda")
    public String tienda(Model model, HttpSession session) {
        // Redirigir al home de la tienda (página principal)
        return index(model, session);
    }

    @GetMapping("/categoria/{id}")
public String productosPorCategoria(@PathVariable Long id, Model model, HttpSession session) {
    Optional<Categoria> categoria = categoriaService.buscarPorId(id);
    
    if (categoria.isEmpty()) {
        return "redirect:/";
    }
    
    List<Producto> productos = productoService.obtenerPorCategoria(id);
    List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
    
    model.addAttribute("categoriaActual", categoria.get());
    model.addAttribute("productos", productos);
    model.addAttribute("categorias", categorias);
    
    // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
    Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
    if (usuarioLogueado != null) {
        model.addAttribute("usuarioLogueado", usuarioLogueado);
        model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
    }
    // =======================================================
    
 // SIMPLIFICAR: Usar directamente ropa-deportiva para ID 1
if (id == 1) {
    return "tienda/ropa-deportiva";
} else {
    return "tienda/categoria"; // Para las otras categorías por ahora
}
}
    @GetMapping("/producto/{id}")
    public String detalleProducto(@PathVariable Long id, Model model, HttpSession session) {
        Optional<Producto> producto = productoService.buscarPorId(id);
        
        if (producto.isEmpty()) {
            return "redirect:/";
        }
        
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        List<Producto> relacionados = productoService
                .obtenerPorCategoria(producto.get().getCategoria().getId())
                .stream()
                .filter(p -> !p.getId().equals(id))
                .limit(4)
                .toList();
        
        model.addAttribute("producto", producto.get());
        model.addAttribute("categorias", categorias);
        model.addAttribute("relacionados", relacionados);
        
        // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
        }
        // =======================================================
        
        return "tienda/producto";
    }

    @GetMapping("/buscar")
    public String buscar(@RequestParam String q, Model model, HttpSession session) {
        List<Producto> resultados = productoService.buscar(q);
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        
        model.addAttribute("termino", q);
        model.addAttribute("productos", resultados);
        model.addAttribute("categorias", categorias);
        
        // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
        }
        // =======================================================
        
        return "tienda/busqueda";
    }

    @GetMapping("/carrito")
    public String verCarrito(@RequestParam(required = false) Long usuarioId, Model model, HttpSession session) {
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        model.addAttribute("categorias", categorias);
        
        // ========== USAR SESIÓN PARA EL CARRITO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
            
            Optional<Carrito> carrito = carritoService.obtenerCarritoConItems(usuarioLogueado.getId());
            model.addAttribute("carrito", carrito.orElse(null));
        } else if (usuarioId != null) {
            Optional<Carrito> carrito = carritoService.obtenerCarritoConItems(usuarioId);
            model.addAttribute("carrito", carrito.orElse(null));
        }
        // =================================================
        
        return "tienda/carrito";
    }

    @PostMapping("/carrito/agregar")
    @ResponseBody
    public String agregarAlCarrito(@RequestParam Long usuarioId,
                                   @RequestParam Long productoId,
                                   @RequestParam(defaultValue = "1") Integer cantidad) {
        try {
            carritoService.agregarProducto(usuarioId, productoId, cantidad);
            int totalItems = carritoService.obtenerCantidadItems(usuarioId);
            return "{\"success\": true, \"totalItems\": " + totalItems + "}";
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/carrito/actualizar")
    @ResponseBody
    public String actualizarCarrito(@RequestParam Long usuarioId,
                                    @RequestParam Long productoId,
                                    @RequestParam Integer cantidad) {
        try {
            carritoService.actualizarCantidad(usuarioId, productoId, cantidad);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/carrito/remover")
    @ResponseBody
    public String removerDelCarrito(@RequestParam Long usuarioId,
                                    @RequestParam Long productoId) {
        try {
            carritoService.removerProducto(usuarioId, productoId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @PostMapping("/carrito/vaciar")
    @ResponseBody
    public String vaciarCarrito(@RequestParam Long usuarioId) {
        try {
            carritoService.vaciarCarrito(usuarioId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"error\": \"" + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/ofertas")
    public String ofertas(Model model, HttpSession session) {
        List<Producto> productosOferta = productoService.obtenerEnOferta();
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        
        model.addAttribute("productos", productosOferta);
        model.addAttribute("categorias", categorias);
        
        // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
        }
        // =======================================================
        
        return "tienda/ofertas";
    }

    @GetMapping("/nuevos")
    public String nuevos(Model model, HttpSession session) {
        List<Producto> productosNuevos = productoService.obtenerNuevos();
        List<Categoria> categorias = categoriaService.obtenerCategoriasActivas();
        
        model.addAttribute("productos", productosNuevos);
        model.addAttribute("categorias", categorias);
        
        // ========== AGREGAR INFO DEL USUARIO LOGUEADO ==========
        Usuario usuarioLogueado = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuarioLogueado != null) {
            model.addAttribute("usuarioLogueado", usuarioLogueado);
            model.addAttribute("usuarioNombre", usuarioLogueado.getNombre());
        }
        // =======================================================
        
        return "tienda/nuevos";
    }
}