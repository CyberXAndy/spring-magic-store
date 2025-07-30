package com.example.demo.controllers;

import com.example.demo.domain.InhousePart;
import com.example.demo.domain.Part;
import com.example.demo.service.InhousePartService;
import com.example.demo.service.InhousePartServiceImpl;
import com.example.demo.service.PartService;
import com.example.demo.service.PartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;

/**
 *
 *
 *
 *
 */
@Controller
public class AddInhousePartController{
    @Autowired
    private ApplicationContext context;

    @GetMapping("/showFormAddInPart")
    public String showFormAddInhousePart(Model theModel){
        InhousePart inhousepart=new InhousePart();
        theModel.addAttribute("inhousepart",inhousepart);
        return "InhousePartForm";
    }

    @PostMapping("/showFormAddInPart")
    public String submitForm(@Valid @ModelAttribute("inhousepart") InhousePart part,
                             BindingResult theBindingResult,
                             Model theModel) {
        return processInhousePartForm(part, theBindingResult, theModel);
    }

    @PostMapping("/saveinhouse")
    public String saveInhousePart(@Valid @ModelAttribute("inhousepart") InhousePart part,
                                  BindingResult theBindingResult,
                                  Model theModel) {
        return processInhousePartForm(part, theBindingResult, theModel);
    }

    private String processInhousePartForm(@Valid @ModelAttribute("inhousepart") InhousePart part,
                                         BindingResult theBindingResult,
                                         Model theModel) {
        theModel.addAttribute("inhousepart", part);

        if (!part.isValidInventory()) {
            theBindingResult.rejectValue("inv", "invalid.inventory", "Inventory must be between minInv and maxInv.");
        }

        if (part.getInv() < part.getMinInv()) {
            theBindingResult.rejectValue("inv", "low.inventory", "Low inventory: inventory is less than the minimum number of parts.");
        }

        if (part.getInv() > part.getMaxInv()) {
            theBindingResult.rejectValue("inv", "high.inventory", "High inventory: inventory is greater than the maximum number of parts.");
        }

        if (theBindingResult.hasErrors()) {
            return "InhousePartForm";
        }
        else{
            InhousePartService repo=context.getBean(InhousePartServiceImpl.class);
            InhousePart ip=repo.findById((int)part.getId());
            if(ip!=null)part.setProducts(ip.getProducts());
            repo.save(part);

            return "confirmationaddpart";}
    }
}
