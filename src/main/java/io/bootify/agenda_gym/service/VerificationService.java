package io.bootify.agenda_gym.service;

import io.bootify.agenda_gym.domain.Usuario;
import io.bootify.agenda_gym.domain.VerificationToken;
import io.bootify.agenda_gym.repos.VerificationTokenRepository;
import io.bootify.agenda_gym.repos.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationService {

    private final VerificationTokenRepository tokenRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;

    @Value("${app.base.url:http://localhost:8080}")
    private String baseUrl;

    @Autowired
    public VerificationService(VerificationTokenRepository tokenRepository,
                               UsuarioRepository usuarioRepository,
                               EmailService emailService) {
        this.tokenRepository = tokenRepository;
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    /**
     * Crear y enviar token (por defecto para tienda)
     */
    public void crearYEnviarToken(Usuario usuario) {
        crearYEnviarToken(usuario, false);
    }

    /**
     * Crear y enviar token con opción de especificar si es para agenda
     */
    public void crearYEnviarToken(Usuario usuario, boolean esAgenda) {
        System.out.println("=== CREANDO TOKEN PARA USUARIO ===");
        System.out.println("Usuario ID: " + usuario.getId());
        System.out.println("Usuario Email: " + usuario.getEmail());
        System.out.println("Es Agenda: " + esAgenda);
        
        try {
            VerificationToken token = generarToken(usuario);
            System.out.println("✅ Token generado: " + token.getToken());
            System.out.println("✅ Token guardado con ID: " + token.getId());
            
            if (esAgenda) {
                enviarCorreoVerificacionAgenda(usuario, token);
            } else {
                enviarCorreoVerificacion(usuario, token);
            }
            System.out.println("✅ Email enviado exitosamente");
        } catch (Exception e) {
            System.out.println("❌ Error al crear/enviar token: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public VerificationToken generarToken(Usuario usuario) {
        System.out.println("=== GENERANDO TOKEN ===");
        String tokenStr = UUID.randomUUID().toString();
        System.out.println("Token string generado: " + tokenStr);
        
        VerificationToken verificationToken = new VerificationToken(tokenStr, usuario);
        verificationToken.setFechaExpiracion(LocalDateTime.now().plusHours(24));
        
        System.out.println("Guardando token en BD...");
        VerificationToken tokenGuardado = tokenRepository.save(verificationToken);
        System.out.println("✅ Token guardado en BD con ID: " + tokenGuardado.getId());
        
        return tokenGuardado;
    }

    /**
     * Correo de verificación para TIENDA (va a /verify-account)
     */
    public void enviarCorreoVerificacion(Usuario usuario, VerificationToken token) {
        String asunto = "🎉 Verifica tu cuenta en GymTrack";
        String enlace = baseUrl + "/verify-account?token=" + token.getToken();

        System.out.println("=== ENVIANDO CORREO (TIENDA) ===");
        System.out.println("Destinatario: " + usuario.getEmail());
        System.out.println("Enlace de verificación: " + enlace);

        String mensajeHtml = generarHtmlEmail(usuario, enlace, false);
        emailService.enviarCorreo(usuario.getEmail(), asunto, mensajeHtml);
    }

    /**
     * Correo de verificación para AGENDA (va a /verify-account-agenda)
     */
    public void enviarCorreoVerificacionAgenda(Usuario usuario, VerificationToken token) {
        String asunto = "📅 Verifica tu cuenta en GymTrack - Tu Agenda te espera";
        String enlace = baseUrl + "/verify-account-agenda?token=" + token.getToken();

        System.out.println("=== ENVIANDO CORREO (AGENDA) ===");
        System.out.println("Destinatario: " + usuario.getEmail());
        System.out.println("Enlace de verificación: " + enlace);

        String mensajeHtml = generarHtmlEmail(usuario, enlace, true);
        emailService.enviarCorreo(usuario.getEmail(), asunto, mensajeHtml);
    }

    /**
     * Genera el HTML del email según el tipo (tienda o agenda)
     */
    private String generarHtmlEmail(Usuario usuario, String enlace, boolean esAgenda) {
        String colorPrimario = esAgenda ? "#ff9800" : "#667eea";
        String colorSecundario = esAgenda ? "#ffb74d" : "#764ba2";
        String emoji = esAgenda ? "📅" : "🏋️‍♂️";
        String subtitulo = esAgenda ? "Tu agenda de entrenamiento" : "Tu compañero de entrenamiento";
        String botonTexto = esAgenda ? "📅 Verificar y crear mi agenda" : "✅ Verificar mi cuenta";
        
        String beneficio1 = esAgenda ? "Crear tu agenda de entrenamiento personalizada" : "Crear rutinas personalizadas según tus objetivos";
        String beneficio2 = esAgenda ? "Configurar tus metas y objetivos fitness" : "Hacer seguimiento de tu progreso día a día";
        String beneficio3 = esAgenda ? "Conectar con entrenadores profesionales" : "Recibir recomendaciones personalizadas";
        String beneficio4 = esAgenda ? "Seguir tu progreso semana a semana" : "Alcanzar tus metas de forma efectiva";

        return "<!DOCTYPE html>" +
            "<html lang=\"es\">" +
            "<head>" +
            "    <meta charset=\"UTF-8\">" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
            "    <title>Verificación de cuenta - GymTrack</title>" +
            "</head>" +
            "<body style=\"margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f0f4f8;\">" +
            "    <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color: #f0f4f8; padding: 40px 20px;\">" +
            "        <tr>" +
            "            <td align=\"center\">" +
            "                <table role=\"presentation\" width=\"600\" cellspacing=\"0\" cellpadding=\"0\" style=\"background-color: #ffffff; border-radius: 16px; box-shadow: 0 4px 24px rgba(0,0,0,0.08); overflow: hidden;\">" +
            "                    <tr>" +
            "                        <td style=\"background: linear-gradient(135deg, " + colorPrimario + " 0%, " + colorSecundario + " 100%); padding: 40px 30px; text-align: center;\">" +
            "                            <div style=\"font-size: 48px; margin-bottom: 10px;\">" + emoji + "</div>" +
            "                            <h1 style=\"color: #ffffff; margin: 0; font-size: 28px; font-weight: 700;\">GymTrack</h1>" +
            "                            <p style=\"color: rgba(255,255,255,0.9); margin: 8px 0 0; font-size: 14px;\">" + subtitulo + "</p>" +
            "                        </td>" +
            "                    </tr>" +
            "                    <tr>" +
            "                        <td style=\"padding: 40px 40px 30px;\">" +
            "                            <div style=\"text-align: center; margin-bottom: 30px;\">" +
            "                                <span style=\"font-size: 64px;\">🎉</span>" +
            "                            </div>" +
            "                            <h2 style=\"color: #1a1a2e; margin: 0 0 20px; font-size: 24px; text-align: center; font-weight: 600;\">" +
            "                                ¡Hola, " + usuario.getNombre() + "!" +
            "                            </h2>" +
            "                            <p style=\"color: #4a5568; font-size: 16px; line-height: 1.6; text-align: center; margin: 0 0 30px;\">" +
            "                                ¡Gracias por unirte a <strong>GymTrack</strong>! Estás a un paso de comenzar tu viaje hacia una vida más saludable y activa." +
            "                            </p>" +
            "                            <p style=\"color: #4a5568; font-size: 16px; line-height: 1.6; text-align: center; margin: 0 0 30px;\">" +
            "                                Para activar tu cuenta, solo haz clic en el botón de abajo:" +
            "                            </p>" +
            "                            <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
            "                                <tr>" +
            "                                    <td align=\"center\" style=\"padding: 10px 0 30px;\">" +
            "                                        <a href=\"" + enlace + "\" style=\"display: inline-block; background: linear-gradient(135deg, #28a745 0%, #20c997 100%); color: #ffffff; text-decoration: none; padding: 16px 40px; border-radius: 50px; font-size: 16px; font-weight: 600; box-shadow: 0 4px 15px rgba(40, 167, 69, 0.3);\">" +
            "                                            " + botonTexto +
            "                                        </a>" +
            "                                    </td>" +
            "                                </tr>" +
            "                            </table>" +
            "                            <div style=\"background-color: #f8f9fa; border-radius: 8px; padding: 20px; margin-bottom: 20px;\">" +
            "                                <p style=\"color: #6c757d; font-size: 13px; margin: 0 0 10px; text-align: center;\">" +
            "                                    Si el botón no funciona, copia y pega este enlace en tu navegador:" +
            "                                </p>" +
            "                                <p style=\"color: " + colorPrimario + "; font-size: 12px; word-break: break-all; text-align: center; margin: 0;\">" +
            "                                    " + enlace +
            "                                </p>" +
            "                            </div>" +
            "                            <div style=\"text-align: center; padding: 15px; background-color: #fff3cd; border-radius: 8px; border-left: 4px solid #ffc107;\">" +
            "                                <p style=\"color: #856404; font-size: 14px; margin: 0;\">" +
            "                                    ⏰ <strong>Este enlace expirará en 24 horas</strong>" +
            "                                </p>" +
            "                            </div>" +
            "                        </td>" +
            "                    </tr>" +
            "                    <tr>" +
            "                        <td style=\"padding: 0 40px 30px;\">" +
            "                            <h3 style=\"color: #1a1a2e; font-size: 18px; margin: 0 0 20px; text-align: center;\">" +
            "                                🚀 Con GymTrack podrás:" +
            "                            </h3>" +
            "                            <table role=\"presentation\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\">" +
            "                                <tr><td style=\"padding: 8px 0;\"><span style=\"color: #28a745; font-size: 18px; margin-right: 10px;\">✓</span><span style=\"color: #4a5568; font-size: 14px;\">" + beneficio1 + "</span></td></tr>" +
            "                                <tr><td style=\"padding: 8px 0;\"><span style=\"color: #28a745; font-size: 18px; margin-right: 10px;\">✓</span><span style=\"color: #4a5568; font-size: 14px;\">" + beneficio2 + "</span></td></tr>" +
            "                                <tr><td style=\"padding: 8px 0;\"><span style=\"color: #28a745; font-size: 18px; margin-right: 10px;\">✓</span><span style=\"color: #4a5568; font-size: 14px;\">" + beneficio3 + "</span></td></tr>" +
            "                                <tr><td style=\"padding: 8px 0;\"><span style=\"color: #28a745; font-size: 18px; margin-right: 10px;\">✓</span><span style=\"color: #4a5568; font-size: 14px;\">" + beneficio4 + "</span></td></tr>" +
            "                            </table>" +
            "                        </td>" +
            "                    </tr>" +
            "                    <tr>" +
            "                        <td style=\"background-color: #f8f9fa; padding: 30px 40px; text-align: center; border-top: 1px solid #e9ecef;\">" +
            "                            <p style=\"color: #6c757d; font-size: 14px; margin: 0 0 10px;\">¡Nos vemos en el gimnasio! 💪</p>" +
            "                            <p style=\"color: #adb5bd; font-size: 12px; margin: 0;\">Si no solicitaste esta cuenta, puedes ignorar este correo.</p>" +
            "                        </td>" +
            "                    </tr>" +
            "                </table>" +
            "                <p style=\"color: #adb5bd; font-size: 12px; margin-top: 20px; text-align: center;\">© 2025 GymTrack. Todos los derechos reservados.</p>" +
            "            </td>" +
            "        </tr>" +
            "    </table>" +
            "</body>" +
            "</html>";
    }

    /**
     * Verifica el token y retorna el usuarioId si es válido
     */
    public Long verificarTokenYObtenerUsuarioId(String tokenStr) {
        System.out.println("=== DEBUG VERIFICACIÓN TOKEN ===");
        System.out.println("Token recibido: " + tokenStr);
        
        Optional<VerificationToken> optionalToken = tokenRepository.findByToken(tokenStr);
        
        if (optionalToken.isEmpty()) {
            System.out.println("❌ Token NO encontrado en la base de datos");
            return null;
        }

        VerificationToken verificationToken = optionalToken.get();
        System.out.println("✅ Token encontrado - ID: " + verificationToken.getId());
        System.out.println("Usuario asociado: " + verificationToken.getUsuario().getEmail());
        System.out.println("Fecha expiración: " + verificationToken.getFechaExpiracion());
        System.out.println("Fecha actual: " + LocalDateTime.now());

        if (verificationToken.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            System.out.println("❌ Token EXPIRADO");
            tokenRepository.delete(verificationToken);
            return null;
        }
        System.out.println("✅ Token VÁLIDO (no expirado)");

        Usuario usuario = verificationToken.getUsuario();
        System.out.println("✅ Antes de marcar como verificado: " + usuario.isVerificado());
        
        usuario.setVerificado(true);
        usuarioRepository.save(usuario);
        
        System.out.println("✅ Después de marcar como verificado: " + usuario.isVerificado());

        tokenRepository.delete(verificationToken);
        System.out.println("✅ Token verificado y eliminado exitosamente");

        return usuario.getId();
    }

    public boolean verificarToken(String tokenStr) {
        return verificarTokenYObtenerUsuarioId(tokenStr) != null;
    }

    public VerificationToken crearTokenYEnviarParaUsuarioId(Long usuarioId) {
        System.out.println("=== CREANDO TOKEN FORZADO PARA USUARIO ID: " + usuarioId + " ===");
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (usuarioOpt.isEmpty()) {
            System.out.println("❌ Usuario no encontrado");
            return null;
        }
        
        Usuario usuario = usuarioOpt.get();
        VerificationToken token = generarToken(usuario);
        enviarCorreoVerificacion(usuario, token);
        
        return token;
    }

    public List<VerificationToken> listarTokensRecientes() {
        System.out.println("=== LISTANDO TOKENS RECIENTES ===");
        List<VerificationToken> tokens = tokenRepository.findTop100ByOrderByIdDesc();
        System.out.println("Total tokens encontrados: " + tokens.size());
        return tokens;
    }
}