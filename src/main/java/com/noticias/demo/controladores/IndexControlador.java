package com.noticias.demo.controladores;

import com.noticias.demo.entidades.Comentario;
import com.noticias.demo.entidades.Noticia;
import com.noticias.demo.entidades.Usuario;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.repositorios.UsuarioRepositorio;
import com.noticias.demo.servicios.ComentarioServicio;
import com.noticias.demo.servicios.NoticiaServicio;
import com.noticias.demo.servicios.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/")
public class IndexControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ComentarioServicio comentarioServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @GetMapping("/")
    public String listar(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        List<Noticia> ultimasNoticias = noticiaServicio.obtenerUltimasDosNoticiasUno();
        List<Noticia> noticiasFiltradas = noticiaServicio.listaNoticiasFiltradas();

        modelo.addAttribute("noticias", noticias);
        modelo.addAttribute("ultimasNoticias", ultimasNoticias);
        modelo.addAttribute("noticiasFiltradas", noticiasFiltradas);

        return "index.html";
    }


    @GetMapping("/registrar-periodista/{id}")
    public String registrarPeriodista(ModelMap modelo) {

        return "periodista_registro.html";
    }

    @PostMapping("/registro-periodista/{id}")
    public String registrarPeriodista2(@RequestParam String id, ModelMap modelo) {
        System.out.println("CREAR PERIODISTA CONTROLADOR... id del usuario a modificar" + id);
        try {
            usuarioServicio.crearPeriodista2(id);

            modelo.put("exito", "usuario registrado correctamente");
            return "index.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            //   modelo.put("nombre", nombre);
            //   modelo.put("email", email);
            return "periodista_registro.html";
        }
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {
            modelo.put("error", "Usuario o contraseña invalidas!");
        }

        return "login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo) {

        Usuario logueado = (Usuario) session.getAttribute("usuariosession");

        if (logueado.getRol().toString().equals("ADMIN")) {// si el rol que se loguea es ADMIN devuelve vista admin
            return "redirect:/admin/dashboard";
        }

        return "inicio.html";
    }

    //------------- USUARIOS PERFIL Y MODIFICAR ------------------
    @GetMapping("/{id}")
    public String perfilUsuario() {

        return "usuario_perfil.html";
    }

    @PostMapping("/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
        try {
            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "usuario_perfil.html";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_perfil.html";
        }

    }

//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_PERIODISTA', 'ROLE_ADMIN')")
//    @GetMapping("/usuario_perfil")
//    public String usuarioPerfil(HttpSession session, ModelMap modelo) {
//
//        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
//
//        if (logueado.getRol().toString().equals("ADMIN")) {// si el rol que se loguea es ADMIN devuelve vista admin
//            return "redirect:/admin/dashboard";
//        }
//
//        return "usuario_perfil.html";
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @GetMapping("/usuario_perfil/{id}")
//    public String perfil(ModelMap modelo, HttpSession session) {
//        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
//        modelo.put("usuario", usuario);
//        return "usuario_perfil.html";
//    }
//
//    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
//    @PostMapping("/usuario_perfil/{id}")
//    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombre, @RequestParam String email,
//            @RequestParam String password, @RequestParam String password2, ModelMap modelo) {
//        try {
//            usuarioServicio.actualizarUsuario(archivo, id, nombre, email, password, password2);
//            modelo.put("exito", "Usuario actualizado correctamente");
//            return "usuario_perfil.html";
//        } catch (MiException ex) {
//            modelo.put("error", ex.getMessage());
//            modelo.put("nombre", nombre);
//            modelo.put("email", email);
//            return "usuario_perfil.html";
//        }
//
//    }
//    
    //-------------------------- COMENTARIOS -----------------------------------------------
    @PostMapping("/")
    public String comentar(@RequestParam String comentario,
            @RequestParam String id,// id de usuario
            @RequestParam String idNoticia,
            ModelMap modelo) {

        try {
            System.out.println("1----- TRATANDO DE CARGAR COMENTARIO");
            comentarioServicio.persistirComentario(comentario, id, idNoticia);
            System.out.println("1----- COMENTARIO REALIZADO CORRECTAMENTE");
            modelo.put("exito", "COMENTARIO REALIZADO CORRECTAMENTE");
            return "redirect:/";
        } catch (MiException ex) {
            System.out.println("1----------- EXCEPCION DE CARGA DE COMENTARIO");
            Logger.getLogger(IndexControlador.class.getName()).log(Level.SEVERE, null, ex);
        }

        return "redirect:/";
    }

}
