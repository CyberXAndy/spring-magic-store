package com.example.demo.controllers;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.PartService;
import com.example.demo.service.ProductService;
import com.example.demo.util.SecurityUtils;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 *
 *
 *
 *
 */

@Controller
public class MainScreenControllerr {
   // private final PartRepository partRepository;
   // private final ProductRepository productRepository;'

    private PartService partService;
    private ProductService productService;

    private List<Part> theParts;
    private List<Product> theProducts;

 /*   public MainScreenControllerr(PartRepository partRepository, ProductRepository productRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
    }*/

    public MainScreenControllerr(PartService partService,ProductService productService){
        this.partService=partService;
        this.productService=productService;
    }
    @GetMapping("/mainscreen")
    public String listPartsandProducts(Model theModel, @Param("partkeyword") String partkeyword, @Param("productkeyword") String productkeyword){
        // Sanitize input parameters to prevent XSS attacks
        String sanitizedPartKeyword = SecurityUtils.sanitizeInput(partkeyword);
        String sanitizedProductKeyword = SecurityUtils.sanitizeInput(productkeyword);
        
        // Limit input length to prevent potential buffer overflow
        sanitizedPartKeyword = SecurityUtils.limitLength(sanitizedPartKeyword, 100);
        sanitizedProductKeyword = SecurityUtils.limitLength(sanitizedProductKeyword, 100);
        
        //add to the spring model
        List<Part> partList=partService.listAll(sanitizedPartKeyword);
        theModel.addAttribute("parts",partList);
        theModel.addAttribute("partkeyword",sanitizedPartKeyword);
    //    theModel.addAttribute("products",productService.findAll());
        List<Product> productList=productService.listAll(sanitizedProductKeyword);
        theModel.addAttribute("products", productList);
        theModel.addAttribute("productkeyword",sanitizedProductKeyword);
        return "mainscreen";
    }
}
