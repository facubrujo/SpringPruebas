package com.noticias.demo.servicios;

import com.noticias.demo.entidades.Imagen;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.repositorios.ImagenRepositorio;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ImagenServicio {
    
    
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    
    @Transactional
    public Imagen guardar(MultipartFile archivo)throws MiException{
        
        if(archivo != null){
            try {
                
                Imagen imagen = new Imagen();
                
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getOriginalFilename());// antes ---.getName()
                
                imagen.setContenido(archivo.getBytes());
                
                System.out.println("IMAGEN ANTES DE SER GUARDADA POR EL SERVICIO====="+archivo);
                System.out.println("IMAGEN ANTES DE SER GUARDADA POR EL SERVICIO LARGO====="+imagen.getContenido().length);
                System.out.println("IMAGEN ANTES DE SER GUARDADA POR EL SERVICIO CONTENIDO TO STRING======"+imagen.getContenido().toString());
                System.out.println("IMAGEN ANTES DE SER GUARDADA POR EL SERVICIO SOLO CONTENIDO====="+imagen.getContenido());
                
                return imagenRepositorio.save(imagen);
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    
    @Transactional
    public Imagen actualizar(MultipartFile archivo, String idImagen){
        
        
        if(archivo != null){
            try {
                
                Imagen imagen = new Imagen();
                
                if(idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    
                    if(respuesta.isPresent()){
                        imagen = respuesta.get();
                    }
                    
                }
                
                imagen.setMime(archivo.getContentType());
                
                imagen.setNombre(archivo.getOriginalFilename());//.getName()
                
                imagen.setContenido(archivo.getBytes());
                
                return imagenRepositorio.save(imagen);
                
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }
    
}
