package com.example.demo.validators;

import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 *
 *
 *
 *
 */
@Component
public class EnufPartsValidator implements ConstraintValidator<ValidEnufParts, Product> {
    
    @Autowired
    private ProductService productService;
    
    @Override
    public void initialize(ValidEnufParts constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Product product, ConstraintValidatorContext constraintValidatorContext) {
        if (productService == null) return true;
        
        try {
            if (product.getId() != 0) {
                Product myProduct = productService.findById((int) product.getId());
                if (myProduct != null) {
                    for (Part p : myProduct.getParts()) {
                        if (p.getInv() < (product.getInv() - myProduct.getInv())) return false;
                        if (p.getInv() - 1 < p.getMinInv()) return false;
                    }
                }
                return true;
            } else {
                return true;
            }
        } catch (Exception e) {
            // If there's any issue accessing the service, allow validation to pass
            return true;
        }
    }
}
