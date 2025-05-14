package com.udea.EP21F1citasalud_back.mapper;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.Rol;
import com.udea.EP21F1citasalud_back.entity.TipoDocumento;
import com.udea.EP21F1citasalud_back.entity.User;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * Convierte una entidad User a un DTO
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setId(user.getUsuario_id());
        dto.setNombre(user.getNombre());
        dto.setApellido(user.getApellido());
        dto.setEmail(user.getEmail());
        dto.setDocumento(user.getDocumento());
        dto.setTelefono(user.getTelefono());
        dto.setFechaRegistro(user.getFecha_registro());
        dto.setUltimoAcceso(user.getUltimo_acceso());

        if (user.getTipoDocumento() != null) {
            dto.setTipoDocumentoId(user.getTipoDocumento().getId_tipo_documento());
            dto.setTipoDocumentoNombre(user.getTipoDocumento().getTipo_documento());
        }

        if (user.getEstado() != null) {
            dto.setEstadoId(user.getEstado().getId_estado());
            dto.setEstadoNombre(user.getEstado().getNombre_estado());
        }

        if (user.getRoles() != null) {
            dto.setRolesIds(user.getRoles().stream()
                    .map(Rol::getRol_id)
                    .collect(Collectors.toList()));
        }

        // No incluir la contraseña en el DTO por seguridad

        return dto;
    }

    /**
     * Convierte un UserDTO a una entidad User
     * Nota: Este método no establece relaciones complejas - se deben manejar en el servicio
     */
    public User toEntity(UserDTO dto, TipoDocumento tipoDocumento, Estado estado, List<Rol> roles) {
        if (dto == null) {
            return null;
        }

        User user = new User();
        user.setUsuario_id(dto.getId());
        user.setNombre(dto.getNombre());
        user.setApellido(dto.getApellido());
        user.setEmail(dto.getEmail());
        user.setDocumento(dto.getDocumento());
        user.setTelefono(dto.getTelefono());
        user.setFecha_registro(dto.getFechaRegistro());
        user.setUltimo_acceso(dto.getUltimoAcceso());
        user.setTipoDocumento(tipoDocumento);
        user.setEstado(estado);
        user.setRoles(roles);

        // Si hay una contraseña en el DTO (caso de creación o actualización), usarla
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(dto.getPassword());
        }

        return user;
    }

    /**
     * Convierte una lista de entidades a una lista de DTOs
     */
    public List<UserDTO> toDTOList(List<User> users) {
        if (users == null) {
            return new ArrayList<>();
        }

        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
}
