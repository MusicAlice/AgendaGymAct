// ui.js
function renderCategorias(categorias) {
    // Buscamos sección de categorias (se mantiene tu estructura)
    const row = document.querySelector('.categories-section .row');
    if (!row) return;
    row.innerHTML = ''; // reemplaza tarjetas hardcode
    categorias.forEach(cat => {
        const col = document.createElement('div');
        col.className = 'col-md-4';
        col.innerHTML = `
            <a href="#" class="category-card" data-categoria-id="${cat.id}">
                <span class="category-icon">${cat.icono || '🏷️'}</span>
                <h5>${escapeHtml(cat.nombre)}</h5>
                <p>${escapeHtml(cat.descripcion || '')}</p>
            </a>
        `;
        row.appendChild(col);
    });

    // añadir eventos para filtrado por categoria
    document.querySelectorAll('.category-card').forEach(el => {
        el.addEventListener('click', (e) => {
            e.preventDefault();
            const id = el.getAttribute('data-categoria-id');
            loadProductosPorCategoria(id);
        });
    });
}

function renderProductos(productos) {
    const container = document.querySelector('#productos .row.g-3');
    if (!container) return;
    container.innerHTML = '';
    productos.forEach(p => {
        const col = document.createElement('div');
        col.className = 'col-6 col-md-4 col-lg-3';
        col.innerHTML = `
            <div class="product-card">
                <div class="product-image">
                    <img src="${p.imagenUrl || '/img/productos/default.png'}" alt="${escapeHtml(p.nombre)}" onerror="this.src='/img/productos/default.png'">
                </div>
                <div class="product-body">
                    <div class="product-category">${escapeHtml(p.categoriaNombre || '')}</div>
                    <h6 class="product-name">
                        <a href="/producto/${p.id}">${escapeHtml(p.nombre)}</a>
                    </h6>
                    <div class="product-price">
                        <div class="price-current">
                            $ <span>${formatNumber(p.precio)}</span>
                        </div>
                    </div>
                    <a href="/registro" class="btn-add-cart">
                        <i class="bi bi-cart-plus"></i> Agregar al carrito
                    </a>
                </div>
            </div>
        `;
        container.appendChild(col);
    });
}

function escapeHtml(str) {
    if (!str) return '';
    return String(str).replace(/[&<>"']/g, (m) => ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[m]));
}

function formatNumber(n) {
    if (n == null) return '';
    return Number(n).toLocaleString('es-CO', {maximumFractionDigits:0});
}
