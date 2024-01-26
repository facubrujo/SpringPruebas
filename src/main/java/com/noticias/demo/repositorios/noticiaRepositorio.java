package com.noticias.demo.repositorios;

import com.noticias.demo.entidades.Noticia;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface noticiaRepositorio extends JpaRepository<Noticia, String> {

    @Query("SELECT n FROM Noticia n WHERE n.fecha = (SELECT MAX(n2.fecha) FROM Noticia n2)")
     List<Noticia> findUltimasDosNoticias();

     List<Noticia> findTop2ByOrderByFechaDesc();
     
}
