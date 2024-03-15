package com.sweetk.cso.controller;

import com.sweetk.cso.common.config.AppConfig;
import com.sweetk.cso.service.CsoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Log4j2
@Controller
@RequiredArgsConstructor
public class MainController {

    final AppConfig appConfig;
    final CsoService csoService;

    @GetMapping({"/main", "/"})
    public String getMainPage(Model model){
        log.info("MainController Log");
        Pageable pageable = PageRequest.of(1, 5);   // 추후 dto request > paging 값을 수정
        //csoService.test(pageable);
        model.addAttribute("message", appConfig.getDescription());
        return "/web/main";
    }

}
