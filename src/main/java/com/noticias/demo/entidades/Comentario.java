package com.noticias.demo.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Date;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) // Generar Id numerico AuntoIncremental

    private Long id;

    private String comentario;

    @ManyToOne
    private Usuario usuario;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fecha;


    public Comentario() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    
    

    @Override
    public String toString() {
        return "Comentario{" + "id=" + id + ", comentario=" + comentario + ", usuario=" + usuario + '}';
    }

    
   
}
