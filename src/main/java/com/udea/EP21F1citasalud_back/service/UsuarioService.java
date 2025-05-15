package com.udea.EP21F1citasalud_back.service;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.entity.User;
import com.udea.EP21F1citasalud_back.mapper.UserMapper;
import com.udea.EP21F1citasalud_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UsuarioService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UsuarioService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de DTOs de usuarios
     */
    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        List<User> usuarios = userRepository.findAll();
        return userMapper.toDtoList(usuarios);
    }

    /**
     * Obtiene un usuario por su ID
     * @param id ID del usuario a buscar
     * @return DTO del usuario encontrado o Optional vacío si no existe
     */
    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    /**
     * Crea un nuevo usuario
     * @param usuarioDTO DTO con los datos del usuario a crear
     * @return DTO del usuario creado
     */
    public UserDTO createUser(UserDTO usuarioDTO) {
        // Aseguramos que no tenga ID para crear uno nuevo
        usuarioDTO.setUsuarioId(null);

        User usuario = userMapper.toEntity(usuarioDTO);
        usuario = userRepository.save(usuario);

        return userMapper.toDto(usuario);
    }

    /**
     * Actualiza un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO DTO con los nuevos datos
     * @return DTO del usuario actualizado o Optional vacío si no existe
     */
    public Optional<UserDTO> updateUser(Long id, UserDTO usuarioDTO) {
        return userRepository.findById(id)
                .map(usuario -> {
                    User usuarioActualizado = userMapper.updateEntityFromDto(usuario, usuarioDTO);
                    usuarioActualizado = userRepository.save(usuarioActualizado);
                    return userMapper.toDto(usuarioActualizado);
                });
    }
}