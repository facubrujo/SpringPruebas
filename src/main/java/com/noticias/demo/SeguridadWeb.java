package com.noticias.demo;

import com.noticias.demo.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb{
    
    @Autowired
    public UsuarioServicio UsuarioServicio;
    
    @Autowired  // encriptaion de contraseña?? 
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(UsuarioServicio)//recibe la contraseña desde el servicio
                .passwordEncoder(new BCryptPasswordEncoder());// devuelve la contraseña encriptada antes de persistirla
    }
            
            
    
    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http)throws Exception{
        http
            .authorizeRequests()// requerimientos de autorizacion-
                .requestMatchers("/admin/*").hasRole("ADMIN")// solo admin
         //       .requestMatchers("/admin/*").hasRole("PERIODISTA")// solo admin
                .requestMatchers("/css/*","/js/*","/img/*","/**")// para todos
                .permitAll()
            .and().formLogin() // maneja el login revisar
                .loginPage("/login")
                .loginProcessingUrl("/logincheck")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/inicio")// inicio es otro html
                .permitAll()
            .and().logout() // manejo del logout rev
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
            .and().csrf() // desabilita algo de spring security ...¿¿  -_-  ??
                .disable();
                
                
                return http.build();
    }
   
 
}
