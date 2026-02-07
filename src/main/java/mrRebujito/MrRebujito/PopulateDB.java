package mrRebujito.MrRebujito;

import java.util.ArrayList;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import mrRebujito.MrRebujito.entity.*;
import mrRebujito.MrRebujito.service.*;

@Component
public class PopulateDB implements CommandLineRunner {

    @Autowired
    private AdministradorService adminService;

    @Autowired
    private AyuntamientoService ayuntamientoService;

    @Autowired
    private SocioService socioService;

    @Autowired
    private CasetaService casetaService;

    @Autowired
    private ProductoService productoService;
    
    @Autowired
    private SolicitudLicenciaService solicitudLicenciaService;

    @Override
    public void run(String... args) throws Exception {
        

    	if (!adminService.getAllAdministradores().isEmpty()) {
            System.out.println("Base de datos cargada");
            return;
        }

        System.out.println("Iniciando la carga de datos de prueba...");

        // 1. Crear Administrador
        Administrador admin = new Administrador();
        admin.setNombre("Administrador Principal");
        admin.setPrimerApellido("Sistema");
        admin.setUsername("admin");
        admin.setPassword("admin123");
        admin.setCorreo("admin@mrrebujito.com");
        admin.setFoto("https://api.dicebear.com/7.x/avataaars/svg?seed=admin");
        adminService.saveAdministrador(admin);

        // 2. Crear Ayuntamientos
        Ayuntamiento aytoSevilla = new Ayuntamiento();
        aytoSevilla.setNombre("Ayuntamiento de Sevilla");
        aytoSevilla.setUsername("sevilla_ayto");
        aytoSevilla.setPassword("sevilla2024");
        aytoSevilla.setCorreo("contacto@sevilla.es");
        aytoSevilla.setLicenciaMax(10);
        aytoSevilla.setFoto("https://www.sevilla.org/resolveuid/807139c2b1a04e6092bef9c5122ad23e/@@images/image/large");
        ayuntamientoService.saveAyuntamiento(aytoSevilla);

        // 3. Crear Productos
        Producto rebujito = new Producto();
        rebujito.setNombre("Rebujito 1L");
        rebujito.setPrecio(8.50);
        rebujito.setTipoAlimento(TipoAlimento.BEBIDA);
        productoService.saveProducto(rebujito);

        Producto jamon = new Producto();
        jamon.setNombre("Ración de Jamón");
        jamon.setPrecio(15.00);
        jamon.setTipoAlimento(TipoAlimento.COMIDA);
        productoService.saveProducto(jamon);

        // 4. Crear Socios
        Socio socio1 = new Socio();
        socio1.setNombre("Gabriel");
        socio1.setPrimerApellido("Salvatierra");
        socio1.setUsername("whereisgabo");
        socio1.setPassword("pass123");
        socio1.setCorreo("whereisgabo@gmail.com");
        socio1.setTelefono("600111222");
        socioService.saveSocio(socio1);

        // 5. Crear Casetas
        Caseta casetaLaFesta = new Caseta();
        casetaLaFesta.setNombre("Reelio");
        casetaLaFesta.setUsername("reelio_caseta");
        casetaLaFesta.setPassword("reelio123");
        casetaLaFesta.setRazonS("Asociación Cultural de Reelio");
        casetaLaFesta.setAforo(50);
        casetaLaFesta.setPublica(true);
        casetaLaFesta.setCorreo("info@reelio.com");
        casetaLaFesta.setSocios(new ArrayList<>(Arrays.asList(socio1))); 
        casetaLaFesta.setProductos(new ArrayList<>(Arrays.asList(rebujito, jamon))); 
        casetaLaFesta.setSolicitudesLicencia(new ArrayList<>());
        
        casetaService.saveCaseta(casetaLaFesta);

        // 6. Pruebas de Relaciones y Reglas de Negocio
        System.out.println("Ejecutando pruebas de relaciones...");

        try {
            // Simulamos que la caseta pide licencia al ayuntamiento creado
            SolicitudLicencia sol = new SolicitudLicencia();
            sol.setAyuntamiento(aytoSevilla);
            sol.setEstadoLicencia(EstadoLicencia.PENDIENTE);
            solicitudLicenciaService.save(sol);
            
            /*if (casetaLaFesta.getSolicitudesLicencia() == null) {
                casetaLaFesta.setSolicitudesLicencia(new ArrayList<>());
            }
            
            casetaLaFesta.getSolicitudesLicencia().add(sol);
            casetaService.saveCaseta(casetaLaFesta);*/
            System.out.println("Solicitud de licencia creada con éxito.");
        } catch (Exception e) {
            System.err.println("Error en la prueba de solicitud: " + e.getMessage());
        }

        System.out.println("Carga de datos completada con éxito.");
    }
}