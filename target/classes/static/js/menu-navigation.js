// Navegación lateral: activar sección por data-section (delegación segura)
(function () {
    'use strict';

    function initSidebarNavigation() {
        const sidebar = document.querySelector('.sidebar');
        if (!sidebar) return;

        // Evitar registrar listeners duplicados (si el script se carga varias veces)
        if (sidebar.__menuNavigationInitialized) return;
        sidebar.__menuNavigationInitialized = true;

        // Delegación: interceptamos clicks en enlaces con data-section
        sidebar.addEventListener('click', function (event) {
            const link = event.target.closest('.menu-link[data-section]');
            if (!link) return; // no es un link de sección

            // Si el link tiene un href diferente que no sea "#" y apunta a otra página,
            // permitimos navegación normal. Sólo interceptamos si no tiene href o es '#'.
            const href = link.getAttribute('href');
            if (href && href.trim() !== '#' && !href.startsWith('javascript:')) {
                return; // dejar comportamiento por defecto (ir a nueva URL)
            }

            event.preventDefault();

            // Desactivar todos los links y activar el seleccionado
            sidebar.querySelectorAll('.menu-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            // Mostrar la sección correspondiente
            const sectionId = link.getAttribute('data-section');
            if (!sectionId) return;

            document.querySelectorAll('.content-section').forEach(section => section.classList.remove('active'));

            const targetSection = document.getElementById(sectionId);
            if (targetSection) {
                targetSection.classList.add('active');
                // Llevar el foco a la sección para accesibilidad
                try { targetSection.focus(); } catch (e) { /* no crítico */ }
            }

            // Actualizar hash sin recargar
            try {
                history.replaceState(null, '', '#' + sectionId);
            } catch (e) { /* ignore */ }
        });

        // Inicialización desde hash o estado actual en DOM
        (function initFromHashOrDefault() {
            const hash = (location.hash || '').replace('#', '');
            if (hash) {
                const link = sidebar.querySelector(`.menu-link[data-section="${hash}"]`);
                if (link) {
                    link.click();
                    return;
                }
            }
            // Si no hay hash, activar el primer link/data-section si ninguna está activa
            const anyActive = sidebar.querySelector('.menu-link.active');
            if (!anyActive) {
                const first = sidebar.querySelector('.menu-link[data-section]');
                if (first) first.classList.add('active');
                const sid = first ? first.getAttribute('data-section') : null;
                if (sid) {
                    document.querySelectorAll('.content-section').forEach(s => s.classList.remove('active'));
                    const s = document.getElementById(sid);
                    if (s) s.classList.add('active');
                }
            }
        })();
    }

    // Esperar a que el DOM esté listo para asegurar que .sidebar existe
    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initSidebarNavigation);
    } else {
        initSidebarNavigation();
    }
})();