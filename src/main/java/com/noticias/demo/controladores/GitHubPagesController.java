/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.noticias.demo.controladores;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 *
 * @author facun
 */
@Controller
public class GitHubPagesController {

    @GetMapping("/ejercicioNoticias11-pruebas/**")
    public String handleGitHubPages() {
        return "forward:/temp/index.html";
    }
}
