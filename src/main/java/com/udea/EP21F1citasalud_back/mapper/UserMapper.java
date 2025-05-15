package com.udea.EP21F1citasalud_back.mapper;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.entity.User;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.TipoDocumento;
import com.udea.EP21F1citasalud_back.repository.EstadoRepository;
import com.udea.EP21F1citasalud_back.repository.TipoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    public UserDTO toDto(User usuario) {
        if (usuario == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        dto.setUsuarioId(usuario.getUsuarioId());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDocumento(usuario.getDocumento());
        dto.setTipoDocumento(usuario.getTipoDocumento() != null ? Long.valueOf(usuario.getTipoDocumento().getIdTipoDocumento()) : null
        );
        dto.setPassword(usuario.getPassword());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaRegistro(usuario.getFechaRegistro());
        dto.setUltimoAcceso(usuario.getUltimoAcceso());
        dto.setEstado(usuario.getEstado() != null ? Long.valueOf(usuario.getEstado().getIdEstado()) : null
        );
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
        usuario.setPassword(usuarioDTO.getPassword());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());

        // Buscar entidades por ID
        if (usuarioDTO.getTipoDocumento() != null) {
            TipoDocumento tipoDoc = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null);
            usuario.setTipoDocumento(tipoDoc);
        } else {
            usuario.setTipoDocumento(null);
        }

        if (usuarioDTO.getEstado() != null) {
            Estado estado = estadoRepository.findById(usuarioDTO.getEstado()).orElse(null);
            usuario.setEstado(estado);
        } else {
            usuario.setEstado(null);
        }

        return usuario;
    }
    public User updateEntityFromDto(User usuario, UserDTO usuarioDTO) {
        if (usuarioDTO.getNombre() != null) usuario.setNombre(usuarioDTO.getNombre());
        if (usuarioDTO.getApellido() != null) usuario.setApellido(usuarioDTO.getApellido());
        if (usuarioDTO.getEmail() != null) usuario.setEmail(usuarioDTO.getEmail());
        if (usuarioDTO.getDocumento() != null) usuario.setDocumento(usuarioDTO.getDocumento());
        if (usuarioDTO.getPassword() != null) usuario.setPassword(usuarioDTO.getPassword());
        if (usuarioDTO.getTelefono() != null) usuario.setTelefono(usuarioDTO.getTelefono());
        if (usuarioDTO.getFechaRegistro() != null) usuario.setFechaRegistro(usuarioDTO.getFechaRegistro());
        if (usuarioDTO.getUltimoAcceso() != null) usuario.setUltimoAcceso(usuarioDTO.getUltimoAcceso());

        // Actualiza tipoDocumento si viene en el DTO
        if (usuarioDTO.getTipoDocumento() != null) {
            usuario.setTipoDocumento(
                    tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumento()).orElse(null)
            );
        }

        // Actualiza estado si viene en el DTO
        if (usuarioDTO.getEstado() != null) {
            usuario.setEstado(
                    estadoRepository.findById(usuarioDTO.getEstado()).orElse(null)
            );
        }

        return usuario;
    }

    public List<UserDTO> toDtoList(List<User> usuarios) {
        if (usuarios == null) {
            return null;
        }
        return usuarios.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}