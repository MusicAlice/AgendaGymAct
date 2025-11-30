// app.js
document.addEventListener('DOMContentLoaded', () => {
    window.tienda = {}; // namespace global simple
    Promise.all([
        fetch('/api/tienda/categorias').then(r => r.json()),
        fetch('/api/tienda/productos/destacados').then(r => r.json())
    ]).then(([categorias, destacados]) => {
        window.tienda.categorias = categorias;
        window.tienda.productosDestacados = destacados;
        renderCategorias(categorias);
        renderProductos(destacados);
        setupSearch();
        setupFilters();
    }).catch(err => {
        console.error('Error cargando tienda:', err);
    });
});
