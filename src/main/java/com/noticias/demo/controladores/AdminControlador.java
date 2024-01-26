package com.noticias.demo.controladores;

import com.noticias.demo.entidades.Usuario;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.servicios.UsuarioServicio;
import org.springframework.stereotype.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/admin")
public class AdminControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping("/dashboard")
    public String panelAdministrativo() {
        return "panel.html";
    }

    @GetMapping("/usuarios")
    public String listar(ModelMap modelo) {
        List<Usuario> usuarios = usuarioServicio.listarUsuarios();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
    }

    @GetMapping("/periodistas")
    public String listarPeriodistas(ModelMap modelo) {
        List<Usuario> periodistas = usuarioServicio.listarPeriodistas();
        modelo.addAttribute("periodistas", periodistas);
        return "periodista_list.html";
    }

    @PostMapping("/periodistas/{id}")
    public String actualizarSalarioPeriodista(@PathVariable String id, @RequestParam Integer salario, ModelMap modelo) {
        usuarioServicio.actualizarSalarioPeriodista(id, salario);
        modelo.put("exito", "Usuario actualizado correctamente");
        return "redirect:/admin/periodistas";

    }

    @GetMapping("/modificar/{id}")// agregue un /{id} - antes no lo tenia
    public String usuarioModificar(@PathVariable String id, ModelMap modelo) {

        modelo.put("usuario", usuarioServicio.getOne(id));

        return "usuario_modificar.html";
    }
//
//    @PostMapping("/modificar/{id}")
//    public String actualizarUsuario(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
//            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
//        try {
//            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2);
//            modelo.put("exito", "Usuario actualizado correctamente");
//            return "redirect:/admin/usuarios";
//        } catch (MiException ex) {
//            modelo.put("error", ex.getMessage());
//            modelo.put("nombre", nombre);
//            modelo.put("email", email);
//            return "usuario_modificar.html";
//        }
//
//    }

    @PostMapping("/modificarUsuarioForm/{id}")
    public String actualizarUsuario(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "redirect:/admin/usuarios";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }

    }

    @GetMapping("/modificarRolUser/{id}")
    public String cambiarRolUser(@PathVariable String id) {
        usuarioServicio.cambiarRolUser(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificarRolPeriodista/{id}")
    public String cambiarRolPeriodista(@PathVariable String id) {
        usuarioServicio.cambiarRolPeriodista(id);
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/modificarRolAdmin/{id}")
    public String cambiarRolAdmin(@PathVariable String id) {
        usuarioServicio.cambiarRolAdmin(id);
        return "redirect:/admin/usuarios";
    }




}
