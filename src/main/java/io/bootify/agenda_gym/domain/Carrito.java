package io.bootify.agenda_gym.domain;

import jakarta.persistence.*;
import java.math. BigDecimal;
import java.time. OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation. CreatedDate;
import org.springframework. data.annotation.LastModifiedDate;
import org.springframework. data.jpa. domain.support.AuditingEntityListener;

@Entity
@Table(name = "carrito")
@EntityListeners(AuditingEntityListener.class)
public class Carrito {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    @SequenceGenerator(
            name = "carrito_sequence",
            sequenceName = "carrito_sequence",
            allocationSize = 1,
            initialValue = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "carrito_sequence"
    )
    private Long id;

    @OneToOne(fetch = FetchType. LAZY)
    @JoinColumn(name = "usuario_id", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ItemCarrito> items = new HashSet<>();

    @CreatedDate
    @Column(name = "date_created", nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public Set<ItemCarrito> getItems() { return items; }
    public void setItems(Set<ItemCarrito> items) { this.items = items; }
    public OffsetDateTime getDateCreated() { return dateCreated; }
    public void setDateCreated(OffsetDateTime dateCreated) { this.dateCreated = dateCreated; }
    public OffsetDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(OffsetDateTime lastUpdated) { this. lastUpdated = lastUpdated; }

    // Métodos helper
    public int getCantidadTotal() {
        return items.stream().mapToInt(ItemCarrito::getCantidad).sum();
    }

    public BigDecimal getSubtotal() {
        return items.stream()
                .map(item -> item.getProducto().getPrecioMostrar(). multiply(BigDecimal.valueOf(item.getCantidad())))
                . reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void agregarItem(Producto producto, int cantidad) {
        // Buscar si ya existe el producto en el carrito
        for (ItemCarrito item : items) {
            if (item. getProducto().getId().equals(producto. getId())) {
                item.setCantidad(item.getCantidad() + cantidad);
                return;
            }
        }
        // Si no existe, crear nuevo item
        ItemCarrito nuevoItem = new ItemCarrito();
        nuevoItem.setCarrito(this);
        nuevoItem. setProducto(producto);
        nuevoItem.setCantidad(cantidad);
        items.add(nuevoItem);
    }

    public void removerItem(Long productoId) {
        items.removeIf(item -> item.getProducto().getId(). equals(productoId));
    }

    public void vaciar() {
        items.clear();
    }
}