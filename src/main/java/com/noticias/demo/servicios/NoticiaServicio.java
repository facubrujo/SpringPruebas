package com.noticias.demo.servicios;

import com.noticias.demo.entidades.Comentario;
import com.noticias.demo.entidades.Imagen;
import com.noticias.demo.entidades.Noticia;
import com.noticias.demo.entidades.Usuario;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.repositorios.UsuarioRepositorio;
import com.noticias.demo.repositorios.noticiaRepositorio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NoticiaServicio {

    //--- Metodos ---
    // consulta (lista de noticias)
    // creacion --------------------------
    // modificacion { Titulo , Cuerpo , Alta  }
    // dar de baja
    @Autowired
    private noticiaRepositorio notRepo;

    @Autowired
    private ImagenServicio imagenServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void persistirNoticia(MultipartFile archivo, String id, String titulo, String cuerpo, Boolean alta) throws MiException {
        validar(titulo, cuerpo, id);
        Noticia noticia = crearNoticia(archivo, id, titulo, cuerpo, alta);
        notRepo.save(noticia);
    }

    public Noticia crearNoticia(MultipartFile archivo, String id, String titulo, String cuerpo, Boolean alta) throws MiException {
        Boolean aux = validarAlta(alta);
        validar(titulo, cuerpo, id);

        Noticia noticia = new Noticia();

        Imagen imagen = imagenServicio.guardar(archivo);
        noticia.setImagen(imagen);

        Usuario periodista = usuarioServicio.getOne(id);
        System.out.println("USUARIO PERIODISTA DE ESTA NOTICIA ES " + periodista);
        
        System.out.println("USUARIO PERIODISTA CARGANDO NOTICIA EN LISTA NOTICIAS DEPERIODISTA " + periodista);
        periodista.getMisNoticias().add(noticia);
        System.out.println("USUARIO PERIODISTA" + periodista.getMisNoticias().toString());
        System.out.println("SETEANDO PERIODISTA EN NOTICIA " + periodista);
        noticia.setPeriodista(periodista);

        noticia.setFecha(new Date());

        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        noticia.setAlta(aux);

        return noticia;
    }

    public Usuario devolverPeriodista(String idPeriodista) {
        Optional<Usuario> resp = usuarioRepositorio.findById(idPeriodista);
        if (resp.isPresent()) {
            Usuario usuario = resp.get();
            return usuario;
        }
        return null;
    }

    public List<Noticia> listarNoticias() {
        List<Noticia> noticias = new ArrayList();
        noticias = notRepo.findAll();

//        List<Noticia> noticiasOrdenadas = noticias.stream()
//                .sorted(Comparator.comparing(Noticia::getFecha))
//                .collect(Collectors.toList());

// Usar Streams para ordenar la lista por fecha en orden descendente

//        List<Noticia> noticiasOrdenadasDescendente = noticias.stream().
//                .sorted(Comparator.comparing(Noticia::getFecha).reversed())
//                .collect(Collectors.toList());
        return noticias;
    }
    
    public List<Noticia> listaNoticiasFiltradas() {
        List<Noticia> noticias = new ArrayList();
        List<Noticia> aux = new ArrayList();
        noticias = notRepo.findAll();
        for (Noticia noticia : noticias) {
            if(noticia.getAlta() == true){
                aux.add(noticia);
            }
            if(aux.size() > 1){
                return aux;
            }
        }
        return noticias;
    }
    
    public List<Noticia> obtenerUltimasDosNoticias() {
        return notRepo.findUltimasDosNoticias();
    }

    public List<Noticia> obtenerUltimasDosNoticiasUno() {
        return notRepo.findTop2ByOrderByFechaDesc();
    }

    public List<Noticia> ultimasNoticias() {
        List<Noticia> noticias = notRepo.findAll();

        List<Noticia> ulNoticias = new ArrayList<>();

        // Verificar si hay al menos 2 noticias en la lista
        if (noticias.size() >= 2) {
            // Obtener las dos noticias más recientes
            ulNoticias.add(noticias.get(noticias.size() - 1));  // Última noticia
            ulNoticias.add(noticias.get(noticias.size() - 2));  // Penúltima noticia
        } else {
            // Si hay menos de 2 noticias, agregar todas las disponibles
            ulNoticias.addAll(noticias);
        }

        System.out.println("------------------------------" + ulNoticias.toString());
        return ulNoticias;
    }

    public void eliminarNoticia(String id) {
        notRepo.deleteById(id);

    }

    @Transactional
    public void modificarNoticia(MultipartFile archivo, String id, String titulo, String cuerpo, Boolean alta) throws MiException {

        Boolean aux = validarAlta(alta);
        System.out.println("AUX============" + aux);
        validar(titulo, cuerpo, id);

        Optional<Noticia> respuesta = notRepo.findById(id);
        if (respuesta.isPresent()) {
            Noticia noticia = respuesta.get();
            noticia.setAlta(aux);
            noticia.setTitulo(titulo);
            noticia.setCuerpo(cuerpo);

            noticia.setFecha(new Date());

            if(!archivo.isEmpty()){
                Imagen imagen = imagenServicio.guardar(archivo);
                noticia.setImagen(imagen);
            }

            notRepo.save(noticia);
        }
    }
    
      

    public Noticia getOne(String id) {
        return notRepo.getOne(id);
    }

    private void validar(String titulo, String cuerpo, String id) throws MiException {
        if (titulo.isEmpty() || titulo == null) {
            throw new MiException("el titulo no puede ser nulo o estar vacio");
        }
        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiException("el cuerpo no puede ser nulo o estar vacio");
        }
        if (id == null) {
            throw new MiException("el ID DEL PERIODISTA NO SE ENCUENTRA");
        }

    }

    private Boolean validarAlta(Boolean alta) {
        if (alta == null) {
            alta = false;
        } else {
            alta = true;
        }
        return alta;
    }

}
