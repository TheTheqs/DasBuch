package com.example.base_server.controller;

import com.example.base_server.service.RequisitionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/automation")
public class AutomationController {

    @Autowired
    private RequisitionService requisitionService;

    @PostMapping("/process")
    public String attendRequisition(){
        return requisitionService.attendRequisition();
    }
}
