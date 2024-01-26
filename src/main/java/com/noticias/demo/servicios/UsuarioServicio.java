package com.noticias.demo.servicios;

import com.noticias.demo.entidades.Imagen;
import com.noticias.demo.entidades.Noticia;
import com.noticias.demo.entidades.Usuario;
import com.noticias.demo.enums.Rol;
import com.noticias.demo.excepciones.MiException;
import com.noticias.demo.repositorios.UsuarioRepositorio;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    public Usuario crearUsuario(MultipartFile archivo, String nombreUsuario, String nombre,
            String email, String password, String password2) throws MiException {

        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setFechaDeAlta(new Date());
        usuario.setRol(Rol.USER);
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuario.setActivo(Boolean.TRUE);
        usuario.setMisNoticias(null);
        usuario.setSueldoMensual(null);

        return usuario;
    }

    @Transactional
    public void persistirUsuario(String nombre, String nombreUsuario, String email,
            String password, String password2, MultipartFile archivo) throws MiException {

        validar(nombre, nombreUsuario, email, password, password2);
        Usuario usuario = crearUsuario(archivo, nombreUsuario, nombre, email, password, password2);
        usuarioRepositorio.save(usuario);
    }

    @Transactional
    public void crearPeriodista2(String idUsuario) throws MiException {

        System.out.println("CEAR PERIODISTA SERVICIO.... parametro id - " + idUsuario);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        System.out.println("USUARIO ENCONTRADO, RESPUESTA DEL SERVIDOR: " + respuesta);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            System.out.println("USUARIO ENCONTRADO---- INICIANDO UPGRADE TO PERIODISTA");
            usuario.setRol(Rol.PERIODISTA);
            List<Noticia> noticias = new ArrayList();
            usuario.setMisNoticias(noticias);
            usuario.setSueldoMensual(0);

            usuarioRepositorio.save(usuario);
            System.out.println("PERIODISTA PERSISTIDO Y UPGRADEADO CON EXITO");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {
            List<GrantedAuthority> permisos = new ArrayList();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString()); //ROLE_USER
            permisos.add(p);
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setAttribute("usuariosession", usuario);
            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        } else {
            return null;
        }
        //throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public Usuario getOne(String id) {
        return usuarioRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList();
        usuarios = usuarioRepositorio.findAll();
        return usuarios;

    }

    @Transactional(readOnly = true)
    public List<Usuario> listarPeriodistas() {
        List<Usuario> aux = usuarioRepositorio.findAll();
        List<Usuario> usuarios = new ArrayList();

        for (Usuario usuario : aux) {
            if (usuario.getRol() == Rol.PERIODISTA) {
                usuarios.add(usuario);
            }
        }
        return usuarios;

    }

    @Transactional
    public void actualizarUsuario(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MiException {
        validar(nombre, idUsuario, email, password, password2);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));
            usuario.setRol(Rol.USER);

            String idImagen = null;
            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getId();
                Imagen imagen = imagenServicio.actualizar(archivo, idImagen);
                usuario.setImagen(imagen);
                usuarioRepositorio.save(usuario);
            }
        }
    }

    @Transactional
    public void cambiarRolUser(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setRol(Rol.USER);
        }
    }

    @Transactional
    public void cambiarRolPeriodista(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setRol(Rol.PERIODISTA);
            List<Noticia> noticias = new ArrayList();
            usuario.setMisNoticias(noticias);
            usuario.setSueldoMensual(0);
        }
    }

    @Transactional
    public void cambiarRolAdmin(String id) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setRol(Rol.ADMIN);
            List<Noticia> noticias = new ArrayList();
            usuario.setMisNoticias(noticias);
            usuario.setSueldoMensual(0);
        }
    }

    @Transactional
    public void actualizarSalarioPeriodista(String id, Integer salario) {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setSueldoMensual(salario);
        }
    }

    private void validar(String nombre, String nombreUsuario, String email, String password, String password2) throws MiException {

        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("El nombre no puede ser nulo o estar vacio");
        }
        if (nombre.isEmpty() || nombreUsuario == null) {
            throw new MiException("El nombre de Usuario no puede ser nulo o estar vacio");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("El email no puede ser nulo o estar vacio");
        } else {            
            Usuario usuario = usuarioRepositorio.buscarPorEmail(email);
            
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                System.out.println("EL USUARIO CON EMAIL " + email + ", YA SE ENCUENTRA REGISTRADO");
                throw new MiException("EL USUARIO CON EMAIL " + email + ", YA SE ENCUENTRA REGISTRADO");
            }
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("El password no puede ser nulo o estar vacio");
        }
        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }

    }

}
