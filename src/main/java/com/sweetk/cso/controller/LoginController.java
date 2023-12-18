package com.sweetk.cso.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class LoginController {

    @GetMapping("/login")
    public String getLoginPage(Model model, String error){
        if(error != null){
            model.addAttribute("error", error);
        }
        return "/web/login";
    }

}
