package com.udea.EP21F1citasalud_back.service;

import com.udea.EP21F1citasalud_back.DTO.UserDTO;
import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.Rol;
import com.udea.EP21F1citasalud_back.entity.TipoDocumento;
import com.udea.EP21F1citasalud_back.entity.User;
import com.udea.EP21F1citasalud_back.repository.EstadoRepository;
import com.udea.EP21F1citasalud_back.repository.RolRepository;
import com.udea.EP21F1citasalud_back.repository.TipoDocumentoRepository;
import com.udea.EP21F1citasalud_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private TipoDocumentoRepository tipoDocumentoRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Obtiene todos los usuarios activos (estado con ID 1, asumiendo que 1 es el estado "activo")
     * @return Lista de usuarios activos
     */
    public List<UserDTO> obtenerTodosUsuariosActivos() {
        // Asumiendo que el estado con ID 1 es "activo"
        return userRepository.findByEstado_Id(1)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene todos los usuarios
     * @return Lista de todos los usuarios
     */
    public List<UserDTO> obtenerTodosUsuarios() {
        return userRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    /**
     * Busca un usuario por su ID
     * @param id ID del usuario
     * @return Usuario encontrado o null si no existe
     */
    public UserDTO buscarPorId(Long id) {
        Optional<User> usuarioOptional = userRepository.findById(id);
        return usuarioOptional.map(this::convertirADTO).orElse(null);
    }

    /**
     * Crea un nuevo usuario
     * @param usuarioDTO Datos del usuario a crear
     * @return Usuario creado
     */
    @Transactional
    public UserDTO crearUsuario(UserDTO usuarioDTO) {
        // Validamos que el correo no exista previamente
        if (userRepository.findByEmail(usuarioDTO.getEmail()).isPresent()) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }

        User usuario = convertirAEntidad(usuarioDTO);

        // Establecemos la fecha de registro
        usuario.setFecha_registro(LocalDate.now());

        // Encriptamos la contraseña
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        } else {
            throw new RuntimeException("La contraseña es obligatoria para crear un usuario");
        }

        User usuarioGuardado = userRepository.save(usuario);
        return convertirADTO(usuarioGuardado);
    }

    /**
     * Actualiza los datos de un usuario existente
     * @param id ID del usuario a actualizar
     * @param usuarioDTO Nuevos datos del usuario
     * @return Usuario actualizado o null si no existe
     */
    @Transactional
    public UserDTO actualizarUsuario(Long id, UserDTO usuarioDTO) {
        Optional<User> usuarioOptional = userRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            User usuario = usuarioOptional.get();

            // Verificamos que el email no esté en uso por otro usuario
            Optional<User> usuarioConMismoEmail = userRepository.findByEmail(usuarioDTO.getEmail());
            if (usuarioConMismoEmail.isPresent() && !usuarioConMismoEmail.get().getUsuario_id().equals(id)) {
                throw new RuntimeException("El correo electrónico ya está en uso por otro usuario");
            }

            // Actualizamos los campos del usuario
            usuario.setNombre(usuarioDTO.getNombre());
            usuario.setApellido(usuarioDTO.getApellido());
            usuario.setEmail(usuarioDTO.getEmail());
            usuario.setDocumento(usuarioDTO.getDocumento());
            usuario.setTelefono(usuarioDTO.getTelefono());

            // Actualizamos el tipo de documento si se proporcionó
            if (usuarioDTO.getTipoDocumentoId() != null) {
                TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(usuarioDTO.getTipoDocumentoId())
                        .orElseThrow(() -> new RuntimeException("El tipo de documento no existe"));
                usuario.setTipoDocumento(tipoDocumento);
            }

            // Actualizamos el estado si se proporcionó
            if (usuarioDTO.getEstadoId() != null) {
                Estado estado = estadoRepository.findById(usuarioDTO.getEstadoId())
                        .orElseThrow(() -> new RuntimeException("El estado no existe"));
                usuario.setEstado(estado);
            }

            // Actualizamos los roles si se proporcionaron
            if (usuarioDTO.getRolesIds() != null && !usuarioDTO.getRolesIds().isEmpty()) {
                List<Rol> roles = new ArrayList<>();
                for (Integer rolId : usuarioDTO.getRolesIds()) {
                    Rol rol = rolRepository.findById(rolId)
                            .orElseThrow(() -> new RuntimeException("El rol con ID " + rolId + " no existe"));
                    roles.add(rol);
                }
                usuario.setRoles(roles);
            }

            // Actualizamos la contraseña si se proporcionó
            if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
            }

            User usuarioActualizado = userRepository.save(usuario);
            return convertirADTO(usuarioActualizado);
        }

        return null;
    }

    /**
     * Cambia el estado de un usuario
     * @param id ID del usuario a modificar
     * @param estadoId ID del nuevo estado
     * @return Usuario actualizado o null si no existe
     */
    @Transactional
    public UserDTO cambiarEstadoUsuario(Long id, Integer estadoId) {
        Optional<User> usuarioOptional = userRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            User usuario = usuarioOptional.get();

            Estado estado = estadoRepository.findById(estadoId)
                    .orElseThrow(() -> new RuntimeException("El estado con ID " + estadoId + " no existe"));

            usuario.setEstado(estado);
            User usuarioActualizado = userRepository.save(usuario);
            return convertirADTO(usuarioActualizado);
        }

        return null;
    }

    /**
     * Inactiva un usuario (asumiendo que el estado con ID 2 es "inactivo")
     * @param id ID del usuario a inactivar
     * @return Usuario inactivado o null si no existe
     */
    @Transactional
    public UserDTO inactivarUsuario(Long id) {
        // Asumiendo que el estado con ID 2 es "inactivo"
        return cambiarEstadoUsuario(id, 2);
    }

    /**
     * Activa un usuario (asumiendo que el estado con ID 1 es "activo")
     * @param id ID del usuario a activar
     * @return Usuario activado o null si no existe
     */
    @Transactional
    public UserDTO activarUsuario(Long id) {
        // Asumiendo que el estado con ID 1 es "activo"
        return cambiarEstadoUsuario(id, 1);
    }

    /**
     * Elimina físicamente un usuario de la base de datos
     * @param id ID del usuario a eliminar
     * @return true si se eliminó correctamente, false si no existía
     */
    @Transactional
    public boolean eliminarUsuario(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Registra el último acceso del usuario
     * @param id ID del usuario
     */
    @Transactional
    public void registrarUltimoAcceso(Long id) {
        Optional<User> usuarioOptional = userRepository.findById(id);

        if (usuarioOptional.isPresent()) {
            User usuario = usuarioOptional.get();
            usuario.setUltimo_acceso(LocalDate.now());
            userRepository.save(usuario);
        }
    }

    /**
     * Convierte una entidad Usuario a DTO
     */
    private UserDTO convertirADTO(User usuario) {
        UserDTO dto = new UserDTO();
        dto.setId(usuario.getUsuario_id());
        dto.setNombre(usuario.getNombre());
        dto.setApellido(usuario.getApellido());
        dto.setEmail(usuario.getEmail());
        dto.setDocumento(usuario.getDocumento());
        dto.setTelefono(usuario.getTelefono());
        dto.setFechaRegistro(usuario.getFecha_registro());
        dto.setUltimoAcceso(usuario.getUltimo_acceso());

        // Establecemos el tipo de documento
        if (usuario.getTipoDocumento() != null) {
            dto.setTipoDocumentoId(usuario.getTipoDocumento().getId());
            dto.setTipoDocumentoNombre(usuario.getTipoDocumento().getNombre());
        }

        // Establecemos el estado
        if (usuario.getEstado() != null) {
            dto.setEstadoId(usuario.getEstado().getId());
            dto.setEstadoNombre(usuario.getEstado().getNombre());
        }

        // Establecemos los roles
        if (usuario.getRoles() != null && !usuario.getRoles().isEmpty()) {
            List<Integer> rolesIds = usuario.getRoles().stream()
                    .map(Rol::getId)
                    .collect(Collectors.toList());
            dto.setRolesIds(rolesIds);
        }

        // No incluimos la contraseña en el DTO por seguridad

        return dto;
    }

    /**
     * Convierte un DTO a entidad Usuario
     */
    private User convertirAEntidad(UserDTO dto) {
        User usuario = new User();

        if (dto.getId() != null) {
            usuario.setUsuario_id(dto.getId());
        }

        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setDocumento(dto.getDocumento());
        usuario.setTelefono(dto.getTelefono());

        // Establecemos el tipo de documento
        if (dto.getTipoDocumentoId() != null) {
            TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(dto.getTipoDocumentoId())
                    .orElseThrow(() -> new RuntimeException("El tipo de documento no existe"));
            usuario.setTipoDocumento(tipoDocumento);
        }

        // Establecemos el estado
        if (dto.getEstadoId() != null) {
            Estado estado = estadoRepository.findById(dto.getEstadoId())
                    .orElseThrow(() -> new RuntimeException("El estado no existe"));
            usuario.setEstado(estado);
        }

        // Establecemos los roles
        if (dto.getRolesIds() != null && !dto.getRolesIds().isEmpty()) {
            List<Rol> roles = new ArrayList<>();
            for (Integer rolId : dto.getRolesIds()) {
                Rol rol = rolRepository.findById(rolId)
                        .orElseThrow(() -> new RuntimeException("El rol con ID " + rolId + " no existe"));
                roles.add(rol);
            }
            usuario.setRoles(roles);
        }

        return usuario;
    }
}