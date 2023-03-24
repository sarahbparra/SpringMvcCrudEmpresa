package com.example.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.entities.Correo;
import com.example.entities.Departamento;
import com.example.entities.Empleado;
import com.example.entities.Telefono;
import com.example.services.CorreoService;
import com.example.services.DepartamentoService;
import com.example.services.EmpleadoService;
import com.example.services.TelefonoService;

@Controller
@RequestMapping("/")

public class MainController {

  private static final Logger LOG = Logger.getLogger("MainController"); 

  @Autowired
  private EmpleadoService empleadoService; 
  
  @Autowired
  private DepartamentoService departamentoService; 

  @Autowired
  private TelefonoService telefonoService; 

  @Autowired
  private CorreoService correoService; 

  /**
   * Método que muestre listado de empleados */ 
  
  //Al querer mostrar empleados, se utiliza GetMapping 
  @GetMapping("/listar")

  public ModelAndView listar(){

    List<Empleado> empleados = empleadoService.findAll(); 

    ModelAndView mav = new ModelAndView("views/listarEmpleados");
    mav.addObject("empleados", empleados); 

    return mav; 
  }

  /**
   * Método para dar de alta a empleados.
   * Primero se busca que se muestre el formulario.
   * Posteriormente, un método que recibe los datos. 
   * Hay que hacer dos métodos (uno correos, otro tlfnos)
   */

   @GetMapping("/frmAltaEmpleado")
   public String formularioAltaEmpleado (Model model){

    List<Departamento> departamentos = departamentoService.findAll(); 

    Empleado empleado = new Empleado();

    model.addAttribute("empleado", empleado); 
    model.addAttribute("departamentos", departamentos); 

    return "views/formularioAltaEmpleado"; 
   }

   @PostMapping("/altaModificacionEmpleado")
   public String altaEmpleado (@ModelAttribute(name = "empleado") Empleado empleado, 
   @RequestParam(name = "emails") String emailsRecibidos, 
   @RequestParam(name = "telefonos") String telefonosRecibidos){

    LOG.info("emails Recibidos" + emailsRecibidos);

    LOG.info("telefonos Recibidos" + telefonosRecibidos);

    empleadoService.save(empleado);

    List<String> listaEmails = null; 

    if(emailsRecibidos != null){

      String[] arrayCorreos = emailsRecibidos.split(";");
      
      listaEmails = Arrays.asList(arrayCorreos); 

    }

    if(listaEmails != null){

      correoService.deleteByEmpleado(empleado);

      listaEmails.stream().forEach(c -> {
        Correo correoObject = Correo.builder()
        .email(c)
        .empleado(empleado)
        .build(); 

        correoService.save(correoObject);
      });
    }

    List<String> listaTelefonos = null; 

    if(telefonosRecibidos != null){

      String[] arrayTelefonos = telefonosRecibidos.split(";"); 

      listaTelefonos = Arrays.asList(arrayTelefonos); 
    }

    if(listaTelefonos != null){

      telefonoService.deleteByEmpleado(empleado);

      listaTelefonos.stream().forEach(n -> {
        Telefono telefonoObject = Telefono
        .builder()
        .numero(n)
        .empleado(empleado)
        .build(); 

        telefonoService.save(telefonoObject);
      });
    }


    return "redirect:/listar"; 

   }


   /**
    * Método para eliminar empleados. 
    */

    @GetMapping("/borrar/{id}")
    public String borrarEmpleado (@PathVariable(name = "id") int idEmpleado){

      empleadoService.delete(empleadoService.findById(idEmpleado));
      return "redirect:/listar"; 

    }

    /**
     * Método para actualizar empleados.
     */

     @GetMapping("/frmActualizar/{id}")
     public String frmActualizarEmpleado(@PathVariable(name = "id") int idEmpleado, Model model){

      Empleado empleado = empleadoService.findById(idEmpleado); 

      List<Correo> todoCorreos = correoService.findAll(); 

      List<Correo> correosEmpleado = todoCorreos.stream()
      .filter(c -> c.getEmpleado().getId() == idEmpleado)
      .collect(Collectors.toList()); 

      String direccionEmail = correosEmpleado
      .stream()
      .map(c -> c.getEmail())
      .collect(Collectors.joining(";"));
      
      List<Telefono> todoTelefonos = telefonoService.findAll(); 

      List<Telefono> telefonosEmpleado = todoTelefonos.stream()
      .filter(t -> t.getEmpleado().getId() == idEmpleado)
      .collect(Collectors.toList()); 

      String numeroTelefono = telefonosEmpleado
      .stream()
      .map(t -> t.getNumero())
      .collect(Collectors.joining(";")); 

      List<Departamento> departamentos = departamentoService.findAll(); 

      model.addAttribute("empleado", empleado); 
      model.addAttribute("correos", direccionEmail); 
      model.addAttribute("telefonos", numeroTelefono); 
      model.addAttribute("departamentos", departamentos); 

      return "views/formularioAltaEmpleado"; 
     }

     /**
      * Método para obtener detalles del Empleado
      */

      @GetMapping("/detalles/{id}")
     public String detallesEmpleado(@PathVariable(name = "id") int id, Model model){

      Empleado empleado = empleadoService.findById(id); 
      
      List<Correo> correos = correoService.findByEmpleado(empleado); 
      List<String> direccionEmail = correos.stream()
      .map(c -> c.getEmail())
      .collect(Collectors.toList()); 

      List<Telefono> telefonos = telefonoService.findByEmpleado(empleado); 
      List<String> numeroTelefono = telefonos.stream()
      .map(t -> t.getNumero())
      .collect(Collectors.toList());

      model.addAttribute("empleado", empleado); 
      model.addAttribute("correos", direccionEmail); 
      model.addAttribute("telefonos", numeroTelefono); 

      return "views/detallesEmpleado"; 

     }
}
