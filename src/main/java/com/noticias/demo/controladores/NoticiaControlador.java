package com.noticias.demo.controladores;

import com.noticias.demo.entidades.Noticia;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.servicios.NoticiaServicio;
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
@RequestMapping("/noticia")
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio noticiaServicio;

    @GetMapping("/registrar/{id}")
    public String registrar() {
        return "noticia_form.html";
    }

    @PostMapping("/registro/{id}")
    public String registro(@PathVariable String id,
             @RequestParam(required = false) String titulo,
             @RequestParam(required = false) String cuerpo,
             @RequestParam(required = false) Boolean alta,
             @RequestParam("archivo") MultipartFile archivo,
             ModelMap modelo) {

        try {
            noticiaServicio.persistirNoticia(archivo, id, titulo, cuerpo, alta);
            System.out.println("titulo - " + titulo + "\ncuerpo - " + cuerpo + "\nalta -" + alta);
            System.out.println("CARGADO CON EXITO");
            modelo.put("exito", "La Noticia fue cargada correctamente");
        } catch (MiException ex) {
            //Logger.getLogger(NoticiaControlador.class.getName()).log(Level.SEVERE, null, ex);
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        }
        return "noticia_form.html";
    }

    @GetMapping("/lista")
    public String listarNoticias(ModelMap modelo) {
        List<Noticia> noticias = noticiaServicio.listarNoticias();

        modelo.addAttribute("noticias", noticias);

        return "noticia_list.html";
    }

   
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_modificar.html";
    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id,
            @RequestParam String titulo, @RequestParam String cuerpo,
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam(required = false) Boolean alta, ModelMap modelo) {

        try {
            noticiaServicio.modificarNoticia(archivo, id, titulo, cuerpo, alta);
            modelo.put("exito", "La noticia fue modificada correctamente");
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "index.html";
            //Logger.getLogger(NoticiaControlador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    
}
