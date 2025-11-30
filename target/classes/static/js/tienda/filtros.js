// filtros.js
function setupFilters() {
    // Creamos controles mínimos dinámicamente en la sección de productos
    const container = document.querySelector('#productos .container');
    if (!container) return;

    // panel filtros
    const filtrosPanel = document.createElement('div');
    filtrosPanel.className = 'mb-4 d-flex gap-3 align-items-center flex-wrap';
    filtrosPanel.innerHTML = `
        <div>
            <select id="filtroCategoria" class="form-select">
                <option value="">Todas las categorias</option>
            </select>
        </div>
        <div>
            <select id="filtroGenero" class="form-select">
                <option value="">Todos</option>
                <option value="femenino">Femenino</option>
                <option value="masculino">Masculino</option>
                <option value="unisex">Unisex</option>
            </select>
        </div>
        <div>
            <input id="precioMin" type="number" class="form-control" placeholder="Precio min">
        </div>
        <div>
            <input id="precioMax" type="number" class="form-control" placeholder="Precio max">
        </div>
        <div>
            <button id="aplicarFiltros" class="btn btn-primary">Aplicar</button>
        </div>
    `;
    container.insertBefore(filtrosPanel, container.querySelector('.section-title').nextSibling);

    // llenar categorias
    const selCat = filtrosPanel.querySelector('#filtroCategoria');
    (window.tienda.categorias || []).forEach(c => {
        const opt = document.createElement('option');
        opt.value = c.id;
        opt.textContent = c.nombre;
        selCat.appendChild(opt);
    });

    filtrosPanel.querySelector('#aplicarFiltros').addEventListener('click', () => {
        const categoria = selCat.value || '';
        const genero = filtrosPanel.querySelector('#filtroGenero').value || '';
        const precioMin = filtrosPanel.querySelector('#precioMin').value || '';
        const precioMax = filtrosPanel.querySelector('#precioMax').value || '';

        const params = new URLSearchParams();
        if (categoria) params.set('categoria', categoria);
        if (genero) params.set('genero', genero);
        if (precioMin) params.set('precioMin', precioMin);
        if (precioMax) params.set('precioMax', precioMax);

        fetch(`/api/tienda/productos/filtrar?${params.toString()}`)
            .then(r => r.json())
            .then(prods => renderProductos(prods))
            .catch(err => console.error(err));
    });
}
