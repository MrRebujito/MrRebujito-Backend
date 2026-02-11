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
        aytoSevilla.setDireccion("PI. Nueva, 1, Casco Antiguo, 41001 Sevilla");
        aytoSevilla.setPassword("sevilla2026");
        aytoSevilla.setCorreo("contacto@sevilla.es");
        aytoSevilla.setLicenciaMax(10);
        aytoSevilla.setFoto("https://www.sevilla.org/resolveuid/807139c2b1a04e6092bef9c5122ad23e/@@images/image/large");
        ayuntamientoService.saveAyuntamiento(aytoSevilla);

        Ayuntamiento aytoArahal = new Ayuntamiento();
        aytoArahal.setNombre("Ayuntamiento de Arahal");
        aytoArahal.setUsername("arahal_ayto");
        aytoArahal.setPassword("arahal2026");
        aytoArahal.setTelefono("955841033");
        aytoArahal.setDireccion("Pl. Corredera, 1, 41600 Arahal, Sevilla");
        aytoArahal.setCorreo("contacto@arahal.es");
        aytoArahal.setLicenciaMax(5);
        aytoArahal.setFoto("https://www.turismosevilla.org/sites/default/files/2020-03/2018-09-13-Arahal-Ayuntamiento%20de%20Arahal%202.JPG");
        ayuntamientoService.saveAyuntamiento(aytoArahal);
        
        
        Ayuntamiento aytoCasariche = new Ayuntamiento();
        aytoCasariche.setNombre("Ayuntamiento de Casariche");
        aytoCasariche.setUsername("casariche_ayto");
        aytoCasariche.setPassword("casariche2026");
        aytoCasariche.setCorreo("contacto@casariche.es");
        aytoCasariche.setLicenciaMax(3);
        aytoCasariche.setFoto("https://www.casariche.es/wp-content/uploads/2019/04/ayuntamiento_tam_bueno.jpg");
        ayuntamientoService.saveAyuntamiento(aytoCasariche);
        
        
        
        Ayuntamiento aytoParadas = new Ayuntamiento();
        aytoParadas.setNombre("Ayuntamiento de Paradas");
        aytoParadas.setUsername("paradas_ayto");
        aytoParadas.setPassword("paradas2026");
        aytoParadas.setCorreo("contacto@paradas.es");
        aytoParadas.setLicenciaMax(3);
        aytoParadas.setFoto("https://upload.wikimedia.org/wikipedia/commons/e/e9/Ermita_de_San_Juan_de_Letr%C3%A1n.jpg");
        ayuntamientoService.saveAyuntamiento(aytoParadas);
        
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
        
        Producto tortilla = new Producto();
        tortilla.setNombre("Tortilla de Patatas");
        tortilla.setPrecio(12.00);
        tortilla.setTipoAlimento(TipoAlimento.COMIDA);
        productoService.saveProducto(tortilla);

        Producto croquetas = new Producto();
        croquetas.setNombre("Croquetas Caseras");
        croquetas.setPrecio(10.50);
        croquetas.setTipoAlimento(TipoAlimento.COMIDA);
        productoService.saveProducto(croquetas);

        Producto cerveza = new Producto();
        cerveza.setNombre("Cerveza 33cl");
        cerveza.setPrecio(2.50);
        cerveza.setTipoAlimento(TipoAlimento.BEBIDA);
        productoService.saveProducto(cerveza);

        Producto refresco = new Producto();
        refresco.setNombre("Refresco");
        refresco.setPrecio(2.20);
        refresco.setTipoAlimento(TipoAlimento.BEBIDA);
        productoService.saveProducto(refresco);

        Producto cafe = new Producto();
        cafe.setNombre("Café");
        cafe.setPrecio(1.50);
        cafe.setTipoAlimento(TipoAlimento.BEBIDA);
        productoService.saveProducto(cafe);


        // 4. Crear Socios
        Socio socio1 = new Socio();
        socio1.setNombre("Gabriel");
        socio1.setPrimerApellido("Salvatierra");
        socio1.setUsername("whereisgabo");
        socio1.setPassword("pass123");
        socio1.setCorreo("whereisgabo@gmail.com");
        socio1.setTelefono("600190222");
        socio1.setFoto("https://img.blogs.es/vizzagency/wp-content/uploads/2022/09/illojuanedit-819x1024.jpg");
        socioService.saveSocio(socio1);
        
        Socio socio2 = new Socio();
        socio2.setNombre("Daniel");
        socio2.setPrimerApellido("Rayo");
        socio2.setUsername("malegro32");
        socio2.setPassword("pass123");
        socio2.setCorreo("pelanganos@gmail.com");
        socio2.setTelefono("634111452");
        socio2.setFoto("https://pbs.twimg.com/profile_images/1791460581125259264/gGSLKr9E.jpg");
        socioService.saveSocio(socio2);
        
        
        Socio socio3 = new Socio();
        socio3.setNombre("Pedro");
        socio3.setPrimerApellido("Sánchez");
        socio3.setUsername("pedrosanchez");
        socio3.setPassword("pass123");
        socio3.setCorreo("psanchez@gmail.com");
        socio3.setTelefono("612111222");
        socio3.setFoto("https://pbs.twimg.com/profile_images/2021478499312390144/s5_QSmDW_400x400.jpg");
        socioService.saveSocio(socio3);
        
        Socio socio4 = new Socio();
        socio4.setNombre("Adrián");
        socio4.setPrimerApellido("Linares");
        socio4.setUsername("adrlinare");
        socio4.setPassword("pass123");
        socio4.setCorreo("rojodrian@gmail.com");
        socio4.setTelefono("712111222");
        socio4.setFoto("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTasGTkPc3XHt59EnK4bFqFCMV1s2NSz1-r8Q&s");
        socioService.saveSocio(socio4);
        

        // 5. Crear Casetas
        Caseta reelio = new Caseta();
        reelio.setNombre("Reelio");
        reelio.setUsername("reelio_caseta");
        reelio.setPassword("reelio123");
        reelio.setRazonS("Asociación Cultural de Reelio");
        reelio.setAforo(50);
        reelio.setPublica(true);
        reelio.setCorreo("info@reelio.com");
        reelio.setSocios(new ArrayList<>(Arrays.asList(socio1, socio2))); 
        reelio.setProductos(new ArrayList<>(Arrays.asList(rebujito, jamon))); 
        reelio.setSolicitudesLicencia(new ArrayList<>());
        reelio.setFoto("https://sevillasecreta.co/wp-content/uploads/2017/09/feria_de_abril_en_patio_de_la_Cartuja1.jpg");
        
        casetaService.saveCaseta(reelio);
        
        Caseta psoe = new Caseta();
        psoe.setNombre("PSOE Andalucía");
        psoe.setUsername("psoe_caseta");
        psoe.setPassword("psoe123");
        psoe.setRazonS("PSOE Andalucía");
        psoe.setAforo(80);
        psoe.setPublica(true);
        psoe.setCorreo("info@psoe.com");
        psoe.setSocios(new ArrayList<>(Arrays.asList(socio3, socio4))); 
        psoe.setProductos(new ArrayList<>(Arrays.asList(rebujito, jamon))); 
        psoe.setSolicitudesLicencia(new ArrayList<>());
        psoe.setFoto("https://static.grupojoly.com/clip/1df03b47-6db7-442a-9cf4-3ab7113c445a_source-aspect-ratio_1600w_0.jpg");
        
        casetaService.saveCaseta(psoe);

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
        
        
        try {
            // Simulamos que la caseta pide licencia al ayuntamiento creado
            SolicitudLicencia sol = new SolicitudLicencia();
            sol.setAyuntamiento(aytoParadas);
            sol.setEstadoLicencia(EstadoLicencia.APROBADA);
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