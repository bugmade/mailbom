package com.sweetk.cso.controller;

import com.sweetk.cso.dto.pharmComp.PharmCompListReq;
import com.sweetk.cso.dto.pharmComp.PharmCompListRes;
import com.sweetk.cso.service.PharmCompService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Log4j2
@Controller
@RequestMapping("/pharmComp")
@RequiredArgsConstructor
public class PharmCompController {

    private final PharmCompService pharmCompService;

    @GetMapping("/list")
    public String list(PharmCompListReq req, Model model) {
        Page<PharmCompListRes> result = pharmCompService.getList(req, PageRequest.of(req.getPageNo()-1, req.getPageSize()));
        model.addAttribute("result", result);

        log.info("### PharmCompController: list");
        log.info(result.getContent());
        log.info("### PharmCompController: list");

        return "/web/pharmComp/list";
    }
}
