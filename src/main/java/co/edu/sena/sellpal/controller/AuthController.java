package co.edu.sena.sellpal.controller;

import co.edu.sena.sellpal.dto.AuthDtos;
import co.edu.sena.sellpal.entity.Usuario;
import co.edu.sena.sellpal.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager auth;
    private final UsuarioService users;
    private final SecurityContextRepository securityContextRepository;

    public AuthController(AuthenticationManager auth,
                          UsuarioService users,
                          SecurityContextRepository securityContextRepository) {
        this.auth = auth;
        this.users = users;
        this.securityContextRepository = securityContextRepository;
    }

    @PostMapping("/registro")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthDtos.UsuarioResponse registro(@Valid @RequestBody AuthDtos.RegistroRequest r) {
        Usuario u = users.registrar(r);
        // No inicia sesión automática: queda pendiente de aprobación
        return users.respuesta(u);
    }

    @PostMapping("/login")
    public AuthDtos.LoginResponse login(@Valid @RequestBody AuthDtos.LoginRequest r,
                                        HttpServletRequest request) {
        try {
            Authentication authentication = auth.authenticate(
                    new UsernamePasswordAuthenticationToken(r.email(), r.password()));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);
            securityContextRepository.saveContext(context, request, null);

            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    context);

            Usuario u = users.porEmail(r.email());
            return new AuthDtos.LoginResponse(null, users.respuesta(u));
        } catch (DisabledException e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Tu cuenta está pendiente de aprobación por un administrador de INVERCAJAS. " +
                            "Te notificaremos cuando sea activada.");
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Correo o contraseña incorrectos.");
        }
    }

    @GetMapping("/me")
    public AuthDtos.UsuarioResponse me(@AuthenticationPrincipal UserDetails principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay sesión activa");
        }
        return users.respuesta(users.porEmail(principal.getUsername()));
    }

    @PutMapping("/perfil")
    public AuthDtos.UsuarioResponse perfil(@AuthenticationPrincipal UserDetails principal,
                                           @Valid @RequestBody AuthDtos.PerfilRequest r) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay sesión activa");
        }
        return users.respuesta(users.actualizarPerfil(principal.getUsername(), r));
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void password(@AuthenticationPrincipal UserDetails principal,
                         @Valid @RequestBody AuthDtos.PasswordRequest r) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No hay sesión activa");
        }
        users.cambiarPassword(principal.getUsername(), r);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
    }
}
