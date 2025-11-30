// Validaciones de registro sincronizadas con validadores del servidor
document.addEventListener('DOMContentLoaded', function() {
    configurarValidacionesEnTiempoReal();
    inicializarMostrarOcultarPassword();
});

function configurarValidacionesEnTiempoReal() {
    const nombreInput = document.getElementById('register-nombre');
    const emailInput = document.getElementById('register-email');
    const passwordInput = document.getElementById('register-password');
    const fechaInput = document.getElementById('register-fechaNacimiento');

    if (nombreInput) {
        nombreInput.addEventListener('blur', function() { validarNombreUsuario(this); });
        nombreInput.addEventListener('input', function() { limpiarValidacion(this); });
    }
    if (emailInput) {
        emailInput.addEventListener('blur', validarEmailRegistro);
        emailInput.addEventListener('input', function() { limpiarValidacion(this); });
    }
    if (passwordInput) {
        passwordInput.addEventListener('input', () => validarPasswordCampo(passwordInput));
        passwordInput.addEventListener('blur', () => validarPasswordCampo(passwordInput));
    }
    if (fechaInput) {
        fechaInput.addEventListener('blur', function() { validarFechaNacimiento(this); });
        fechaInput.addEventListener('input', function() { limpiarValidacion(this); });
        
        // Establecer límites de fecha para el input
        const hoy = new Date();
        const minFecha = new Date();
        minFecha.setFullYear(hoy.getFullYear() - 75); // Mínimo: 75 años atrás
        const maxFecha = new Date();
        maxFecha.setFullYear(hoy.getFullYear() - 16); // Máximo: 16 años atrás
        
        fechaInput.setAttribute('min', minFecha.toISOString().split('T')[0]);
        fechaInput.setAttribute('max', maxFecha.toISOString().split('T')[0]);
    }
}

function limpiarValidacion(campo) {
    if (!campo) return;
    campo.classList.remove('is-invalid', 'is-valid');
    const feedback = campo.parentElement.querySelector('.invalid-feedback');
    if (feedback) feedback.remove();
}

// Nombre de usuario: letras, espacios, apóstrofes, 3-30, cada "palabra" >=3, no triple letras repetidas
function validarNombreUsuario(campo) {
    if (!campo) campo = document.getElementById('register-nombre');
    if (!campo) return false;
    const valor = campo.value.trim();
    const regex = /^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s']+$/;
    limpiarValidacion(campo);

    if (!valor) { mostrarErrorCampoRegistro(campo, 'El nombre de usuario no puede estar vacío'); return false; }
    if (valor.length < 3 || valor.length > 30) { mostrarErrorCampoRegistro(campo, 'El nombre debe tener entre 3 y 30 caracteres'); return false; }
    if (!regex.test(valor)) { mostrarErrorCampoRegistro(campo, 'El nombre solo puede contener letras, espacios y apóstrofes'); return false; }

    const palabras = valor.split(/\s+/).filter(p => p.length>0);
    for (let p of palabras) {
        if (p.length < 3) { mostrarErrorCampoRegistro(campo, 'Cada palabra del nombre debe tener al menos 3 letras'); return false; }
        if (/(.)\1\1/.test(p)) { mostrarErrorCampoRegistro(campo, 'Las palabras no pueden tener más de dos letras iguales seguidas'); return false; }
    }

    campo.classList.add('is-valid');
    return true;
}

// Email: requiere al menos un carácter alfanumérico en la parte local (evita +++++@...), y formato básico
function validarEmailRegistro() {
    const campo = document.getElementById('register-email');
    if (!campo) return false;
    const valor = campo.value.trim();
    limpiarValidacion(campo);

    if (!valor) { mostrarErrorCampoRegistro(campo, 'El correo no puede estar vacío'); return false; }

    // Regexp general (longitud y formato) y comprobación adicional de contenido en la parte local
    const mainRegex = /^(?=.{3,150}$)[A-Za-z0-9._%+\-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    if (!mainRegex.test(valor)) { mostrarErrorCampoRegistro(campo, 'Formato de correo inválido'); return false; }

    const partes = valor.split('@');
    if (partes.length !== 2) { mostrarErrorCampoRegistro(campo, 'Formato de correo inválido'); return false; }
    const local = partes[0];
    const dominio = partes[1];

    if (local.length < 3) { mostrarErrorCampoRegistro(campo, 'La parte del usuario debe tener al menos 3 caracteres'); return false; }
    if (!/[A-Za-z0-9]/.test(local)) { mostrarErrorCampoRegistro(campo, 'La parte del usuario del correo debe contener letras o números válidos'); return false; }
    if (dominio.length < 3) { mostrarErrorCampoRegistro(campo, 'El dominio del correo es demasiado corto'); return false; }

    campo.classList.add('is-valid');
    return true;
}

// Password: política fuerte (>=12, mayúscula, minúscula, número, símbolo)
function validarPasswordCampo(campo) {
    if (!campo) return false;
    const valor = campo.value;
    limpiarValidacion(campo);

    if (!valor) { mostrarErrorCampoRegistro(campo, 'La contraseña no puede estar vacía'); return false; }
    if (valor.length < 12) { mostrarErrorCampoRegistro(campo, 'La contraseña debe tener al menos 12 caracteres'); return false; }
    if (!/[A-Z]/.test(valor)) { mostrarErrorCampoRegistro(campo, 'La contraseña debe contener al menos una MAYÚSCULA'); return false; }
    if (!/[a-z]/.test(valor)) { mostrarErrorCampoRegistro(campo, 'La contraseña debe contener al menos una minúscula'); return false; }
    if (!/[0-9]/.test(valor)) { mostrarErrorCampoRegistro(campo, 'La contraseña debe contener al menos un número'); return false; }
    if (!/[!@#$%^&*(),.?":{}|<>_\-\\[\]\/~`+=;:]/.test(valor)) { mostrarErrorCampoRegistro(campo, 'La contraseña debe contener al menos un símbolo'); return false; }

    const debiles = ['123456','12345678','password','qwerty','abc123','111111','1234567890','iloveyou','admin','welcome','monkey','000000','password1'];
    if (debiles.includes(valor.toLowerCase())) { mostrarErrorCampoRegistro(campo, 'Contraseña demasiado común'); return false; }
    if (/(\d)\1\1/.test(valor) || /(abc|def|qwe|asd|zxc|1234|4321|abcd)/i.test(valor)) { mostrarErrorCampoRegistro(campo, 'Contraseña parece secuencia simple'); return false; }

    campo.classList.add('is-valid');
    return true;
}

// Fecha de nacimiento: obligatorio y edad mínima 16 años, máxima 75 años (CORREGIDO)
function validarFechaNacimiento(campo) {
    if (!campo) campo = document.getElementById('register-fechaNacimiento');
    if (!campo) return false;
    const valor = campo.value;
    limpiarValidacion(campo);

    if (!valor) { 
        mostrarErrorCampoRegistro(campo, 'La fecha de nacimiento no puede estar vacía'); 
        return false; 
    }
    
    const fechaNacimiento = new Date(valor);
    const hoy = new Date();
    
    // Calcular edad
    let edad = hoy.getFullYear() - fechaNacimiento.getFullYear();
    const mes = hoy.getMonth() - fechaNacimiento.getMonth();
    if (mes < 0 || (mes === 0 && hoy.getDate() < fechaNacimiento.getDate())) {
        edad--;
    }
    
    // Validar edad mínima (16 años)
    if (edad < 16) { 
        mostrarErrorCampoRegistro(campo, 'Debes tener al menos 16 años'); 
        return false; 
    }
    
    // Validar edad máxima (75 años)
    if (edad > 75) { 
        mostrarErrorCampoRegistro(campo, 'La edad máxima permitida es 75 años'); 
        return false; 
    }

    campo.classList.add('is-valid');
    return true;
}

function mostrarErrorCampoRegistro(campo, mensaje) {
    campo.classList.add('is-invalid'); 
    campo.classList.remove('is-valid');
    const feedbackExistente = campo.parentElement.querySelector('.invalid-feedback');
    if (feedbackExistente) feedbackExistente.remove();
    const feedback = document.createElement('div');
    feedback.className = 'invalid-feedback';
    feedback.textContent = mensaje;
    feedback.style.display = 'block';
    campo.parentElement.appendChild(feedback);
}

function validarFormularioRegistro() {
    let valido = true;
    if (!validarNombreUsuario(document.getElementById('register-nombre'))) valido = false;
    if (!validarEmailRegistro()) valido = false;
    if (!validarPasswordCampo(document.getElementById('register-password'))) valido = false;
    if (!validarFechaNacimiento(document.getElementById('register-fechaNacimiento'))) valido = false;

    if (!valido) {
        const primerError = document.querySelector('.is-invalid');
        if (primerError) primerError.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
    return valido;
}

// Compatibilidad: si el template no incluye toggle, lo crea
function inicializarMostrarOcultarPassword() {
    const passwordInput = document.getElementById('register-password');
    if (!passwordInput) return;
    const container = passwordInput.parentElement;
    let toggleBtn = container.querySelector('.toggle-password-btn');
    if (!toggleBtn) {
        toggleBtn = document.createElement('button');
        toggleBtn.type = 'button';
        toggleBtn.textContent = '👁️';
        toggleBtn.className = 'toggle-password-btn';
        toggleBtn.style.marginLeft = '8px';
        toggleBtn.style.cursor = 'pointer';
        toggleBtn.title = 'Mostrar/Ocultar contraseña';
        toggleBtn.addEventListener('click', function() {
            passwordInput.type = passwordInput.type === 'password' ? 'text' : 'password';
            this.textContent = passwordInput.type === 'password' ? '👁️' : '🙈';
            passwordInput.focus();
        });
        container.appendChild(toggleBtn);
    }
}

// VALIDACIÓN PARA FECHA META EN CONFIGURACIÓN INICIAL
function validarFechaMeta(campo) {
    if (!campo) return false;
    const valor = campo.value;
    limpiarValidacion(campo);

    if (!valor) { 
        mostrarErrorCampoRegistro(campo, 'La fecha meta no puede estar vacía'); 
        return false; 
    }
    
    const fechaMeta = new Date(valor);
    const hoy = new Date();
    const maxFecha = new Date();
    maxFecha.setMonth(hoy.getMonth() + 12); // 12 meses máximo
    
    // Validar que la fecha no sea anterior a hoy
    if (fechaMeta < hoy) {
        mostrarErrorCampoRegistro(campo, 'La fecha meta no puede ser anterior a hoy');
        return false;
    }
    
    // Validar que no sea mayor a 12 meses
    if (fechaMeta > maxFecha) {
        mostrarErrorCampoRegistro(campo, 'La fecha meta no puede ser mayor a 12 meses desde hoy');
        return false;
    }

    campo.classList.add('is-valid');
    return true;
}

// Configurar validación para fecha meta en configuración inicial
function configurarValidacionFechaMeta() {
    const fechaMetaInput = document.querySelector('input[name="metaFecha"], input[th\\:field*="metaFecha"]');
    if (fechaMetaInput) {
        fechaMetaInput.addEventListener('blur', function() { validarFechaMeta(this); });
        fechaMetaInput.addEventListener('input', function() { limpiarValidacion(this); });
        
        // Establecer atributos min y max para el input date
        const hoy = new Date().toISOString().split('T')[0];
        const maxFecha = new Date();
        maxFecha.setMonth(maxFecha.getMonth() + 12);
        const maxFechaStr = maxFecha.toISOString().split('T')[0];
        
        fechaMetaInput.setAttribute('min', hoy);
        fechaMetaInput.setAttribute('max', maxFechaStr);
    }
}

// Inicializar validación de fecha meta cuando el DOM esté listo
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', configurarValidacionFechaMeta);
} else {
    configurarValidacionFechaMeta();
}