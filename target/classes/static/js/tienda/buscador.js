// buscador.js
function setupSearch() {
    const form = document.querySelector('.search-box');
    if (!form) return;
    form.addEventListener('submit', function(e) {
        e.preventDefault();
        const q = form.querySelector('input[name="q"]').value.trim();
        if (!q) return;
        fetch(`/api/tienda/productos/buscar?q=${encodeURIComponent(q)}`)
            .then(r => r.json())
            .then(prods => {
                renderProductos(prods);
                // opcional: colocar ancla a productos
                location.hash = '#productos';
            })
            .catch(err => console.error(err));
    });
}
