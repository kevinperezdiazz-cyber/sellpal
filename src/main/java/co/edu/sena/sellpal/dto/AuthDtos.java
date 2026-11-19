package co.edu.sena.sellpal.dto;

import jakarta.validation.constraints.*;

public final class AuthDtos {
    private AuthDtos() {}

    public record RegistroRequest(
            @NotBlank @Size(min = 3, max = 120) String nombre,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 72) String password,
            @NotBlank String passwordConfirm,
            @NotBlank String documento,
            String telefono,
            String direccion,
            String ciudad) {}

    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}

    public record UsuarioResponse(
            Long id,
            String nombre,
            String email,
            String rol,
            String documento,
            String telefono,
            String direccion,
            String ciudad,
            boolean activo) {}

    public record LoginResponse(String token, UsuarioResponse usuario) {}

    public record PerfilRequest(
            @NotBlank String nombre,
            @NotBlank @Email String email,
            String telefono,
            String direccion) {}

    public record PasswordRequest(
            @NotBlank String actual,
            @NotBlank @Size(min = 8, max = 72) String nueva,
            @NotBlank String confirmacion) {}
}
