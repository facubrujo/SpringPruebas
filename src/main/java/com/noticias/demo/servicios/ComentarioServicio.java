package com.noticias.demo.servicios;

import com.noticias.demo.entidades.Comentario;
import com.noticias.demo.entidades.Noticia;
import com.noticias.demo.entidades.Usuario;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.repositorios.ComentarioRepositorio;
import com.noticias.demo.repositorios.UsuarioRepositorio;
import com.noticias.demo.repositorios.noticiaRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ComentarioServicio {

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ComentarioRepositorio comentarioRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private NoticiaServicio noticiaServicio;

    @Autowired
    private noticiaRepositorio noticiaRepositorio;

    public Comentario crearComentario(String comentarioTexto, String idUsuario, String idNoticia) throws MiException {
        System.out.println("INICIO SERVICIO ------------- CREAR COMENTARIO ------------");
        validar(comentarioTexto, idUsuario);
        System.out.println("2------COMENTARIO VALIDADO");
        Comentario comentario = new Comentario();

        System.out.println("2------BUSCANDO USUARIO QUE ESTA COMENTANDO");
        Usuario usuario = usuarioServicio.getOne(idUsuario);
        System.out.println("2------USUARIO ENCONTRADO : " + usuario.getNombre().toString());

        comentario.setComentario(comentarioTexto);
        comentario.setUsuario(usuario);
        comentario.setFecha(new Date());
        //System.out.println("2------ NOTICIA CONFIGURADA : " + noticia.toString());

        System.out.println("2------ BUSCANDO NOTICIA PARA INSERTAR COMENTARIO");
        Optional<Noticia> respuesta = noticiaRepositorio.findById(idNoticia);
        if (respuesta.isPresent()) {
            System.out.println("2------ NOTICIA ENCONTRADA");
            Noticia noticia = respuesta.get();
            System.out.println("2------ BUSCANDO LISTA DE COMENTARIOS DENTRO DE LA NOTICIA");
            noticia.getComentarios().add(comentario);
            System.out.println("2------ LISTA DE COMENTARIOS DENTRO DE LA NOTICIA -- ACTUALIZADA --");
        }

        System.out.println("FIN SERVICIO ------------- CREAR COMENTARIO ------------");
        return comentario;
    }

    @Transactional
    public void persistirComentario(String comentarioTexto, String idUsuario, String idNoticia) throws MiException {
        System.out.println("INICIO SERVICIO ------------- PERSISTIR COMENTARIO ------------");
        validar(comentarioTexto, idUsuario);
        Comentario comentario = crearComentario(comentarioTexto, idUsuario, idNoticia);
        comentarioRepositorio.save(comentario);
        System.out.println("FIN SERVICIO ------------- PERSISTIR COMENTARIO ------------");
    }

    private void validar(String comentario, String idUsuario) throws MiException {
        if (comentario.isEmpty() || comentario == null) {
            throw new MiException("el titulo no puede ser nulo o estar vacio");
        }
        if (idUsuario.isEmpty() || idUsuario == null) {
            throw new MiException("el cuerpo no puede ser nulo o estar vacio");
        }

    }

    public List<Comentario> listarComentarios() {
        List<Comentario> comentarios = new ArrayList();
       
        comentarios = comentarioRepositorio.findAll();

        return comentarios;
    }

}
