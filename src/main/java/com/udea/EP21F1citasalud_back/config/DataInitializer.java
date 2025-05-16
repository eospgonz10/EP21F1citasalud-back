package com.udea.EP21F1citasalud_back.config;

import com.udea.EP21F1citasalud_back.entity.Estado;
import com.udea.EP21F1citasalud_back.entity.TipoDocumento;
import com.udea.EP21F1citasalud_back.repository.EstadoRepository;
import com.udea.EP21F1citasalud_back.repository.TipoDocumentoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final EstadoRepository estadoRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;

    public DataInitializer(EstadoRepository estadoRepository, TipoDocumentoRepository tipoDocumentoRepository) {
        this.estadoRepository = estadoRepository;
        this.tipoDocumentoRepository = tipoDocumentoRepository;
    }

    @Override
    public void run(String... args) {
        // Estados por defecto
        if (!estadoRepository.existsById(1))
            estadoRepository.save(crearEstado(1, "Activo", "Estado activo"));
        if (!estadoRepository.existsById(2))
            estadoRepository.save(crearEstado(2, "Inactivo", "Estado inactivo"));
        if (!estadoRepository.existsById(3))
            estadoRepository.save(crearEstado(3, "Suspendido", "Estado suspendido"));

        // Tipos de documento por defecto
        if (!tipoDocumentoRepository.existsById(1))
            tipoDocumentoRepository.save(crearTipoDocumento(1, "Cédula de Ciudadanía"));
        if (!tipoDocumentoRepository.existsById(2))
            tipoDocumentoRepository.save(crearTipoDocumento(2, "Pasaporte"));
        if (!tipoDocumentoRepository.existsById(3))
            tipoDocumentoRepository.save(crearTipoDocumento(3, "Registro Civil"));
        if (!tipoDocumentoRepository.existsById(4))
            tipoDocumentoRepository.save(crearTipoDocumento(4, "Tarjeta de identidad"));
    }

    private Estado crearEstado(Integer id, String nombre, String descripcion) {
        Estado estado = new Estado();
        estado.setIdEstado(id);
        estado.setNombreEstado(nombre);
        estado.setDescripcion(descripcion);
        return estado;
    }

    private TipoDocumento crearTipoDocumento(Integer id, String nombre) {
        TipoDocumento tipo = new TipoDocumento();
        tipo.setIdTipoDocumento(id);
        tipo.setTipoDocumento(nombre);
        return tipo;
    }
}