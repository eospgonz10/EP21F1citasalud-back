package com.udea.EP21F1citasalud_back.mapper;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.TipoDocumento;
import com.udea.EP21F1citasalud_back.entity.User;
import com.udea.EP21F1citasalud_back.DTO.EstadoDTO;
import com.udea.EP21F1citasalud_back.DTO.TipoDocumentoDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    /**
     * Convierte una entidad Usuario a un DTO
     * @param usuario Entidad a convertir
     * @return UsuarioDTO correspondiente
     */
    public UserDTO toDto(UserDTO usuario) {
        if (usuario == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDocumento(usuario.getDocumento());
        dto.setTipoDocumento(toTipoDocumentoDto(usuario.getTipoDocumento()));
        dto.setPassword(usuario.getPassword());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        dto.setEstado(toEstadoDto(usuario.getEstado()));

        return dto;
    }

    public User toEntity(UserDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return null;
        }

        User usuario = new User();
        usuario.setUsuarioId(usuarioDTO.getUsuarioId());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setDocumento(usuarioDTO.getDocumento());
        usuario.setTipoDocumento(toTipoDocumentoEntity(usuarioDTO.getTipoDocumento()));
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());
        usuario.setEstado(toEstadoEntity(usuarioDTO.getEstado()));

        return usuario;
    }

    /**
     * Convierte una lista de entidades Usuario a lista de DTOs
     * @param usuarios Lista de entidades a convertir
     * @return Lista de UsuarioDTO correspondientes
     */
    public List<UserDTO> toDtoList(List<User> usuarios) {
        if (usuarios == null) {
            return null;
        }

        return usuarios.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Actualiza los datos de una entidad Usuario existente con los datos de un DTO
     * @param usuario Entidad a actualizar
     * @param usuarioDTO DTO con los nuevos datos
     * @return Entidad Usuario actualizada
     */
    public User updateEntityFromDto(User usuario, UserDTO usuarioDTO) {
        if (usuarioDTO == null) {
            return usuario;
        }

        // Solo actualizamos los campos que no son null en el DTO
        if (usuarioDTO.getNombre() != null) {
            usuario.setNombre(usuarioDTO.getNombre());
        }
        if (usuarioDTO.getApellido() != null) {
            usuario.setApellido(usuarioDTO.getApellido());
        }
        if (usuarioDTO.getEmail() != null) {
            usuario.setEmail(usuarioDTO.getEmail());
        }
        if (usuarioDTO.getDocumento() != null) {
            usuario.setDocumento(usuarioDTO.getDocumento());
        }
        if (usuarioDTO.getTipoDocumento() != null) {
            usuario.setTipoDocumento(toTipoDocumentoEntity(usuarioDTO.getTipoDocumento()));
        }
        if (usuarioDTO.getPassword() != null) {
            usuario.setPassword(usuarioDTO.getPassword());
        }
        if (usuarioDTO.getTelefono() != null) {
            usuario.setTelefono(usuarioDTO.getTelefono());
        }
        if (usuarioDTO.getFechaRegistro() != null) {
            usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        }
        if (usuarioDTO.getUltimoAcceso() != null) {
            usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());
        }
        if (usuarioDTO.getEstado() != null) {
            usuario.setEstado(toEstadoEntity(usuarioDTO.getEstado()));
        }

        return usuario;
    }

    // Métodos auxiliares para mapear TipoDocumento
    private TipoDocumentoDTO toTipoDocumentoDto(TipoDocumento tipoDocumento) {
        if (tipoDocumento == null) {
            return null;
        }

        TipoDocumentoDTO dto = new TipoDocumentoDTO();
        dto.setIdTipoDocumento(tipoDocumento.getIdTipoDocumento());
        dto.setTipoDocumento(tipoDocumento.getTipoDocumento());

        return dto;
    }

    private TipoDocumento toTipoDocumentoEntity(TipoDocumentoDTO dto) {
        if (dto == null) {
            return null;
        }

        TipoDocumento tipoDocumento = new TipoDocumento();
        tipoDocumento.setIdTipoDocumento(dto.getIdTipoDocumento());
        tipoDocumento.setTipoDocumento(dto.getTipoDocumento());

        return tipoDocumento;
    }

    // Métodos auxiliares para mapear Estado
    private EstadoDTO toEstadoDto(Estado estado) {
        if (estado == null) {
            return null;
        }

        EstadoDTO dto = new EstadoDTO();
        dto.setIdEstado(estado.getIdEstado());
        dto.setNombreEstado(estado.getNombreEstado());
        dto.setDescripcion(estado.getDescripcion());

        return dto;
    }

    private Estado toEstadoEntity(EstadoDTO dto) {
        if (dto == null) {
            return null;
        }

        Estado estado = new Estado();
        estado.setIdEstado(dto.getIdEstado());
        estado.setNombreEstado(dto.getNombreEstado());
        estado.setDescripcion(dto.getDescripcion());

        return estado;
    }
}