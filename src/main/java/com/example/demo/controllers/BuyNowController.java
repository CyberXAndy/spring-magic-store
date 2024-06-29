package com.example.demo.controllers;

import com.example.demo.domain.Product;
import com.example.demo.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class BuyNowController {

    private final ProductRepository productRepository;

    @Autowired
    public BuyNowController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/buyNow")
    @ResponseBody
    public String buyNow(@RequestParam Long productID) {
        Optional<Product> optionalProduct = productRepository.findById(productID);

        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();

            if (product.getInv() > 0) {
                int newInventory = product.getInv() - 1;
                product.setInv(newInventory);
                productRepository.save(product);

                return "<!DOCTYPE html>" +
                        "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                        "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                        "<style>" +
                        "body { background-color: #222; color: #eee; font-family: Verdana, sans-serif; padding-top: 50px; }" +
                        ".container { max-width: 800px; margin: 0 auto; background-color: #333; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "h1, h2 { color: #f8d347; text-align: center; }" +
                        ".btn-primary { background-color: #6a5acd; border-color: #6a5acd; }" +
                        ".btn-primary:hover { background-color: #483d8b; border-color: #483d8b; }" +
                        ".btn-primary:focus { box-shadow: 0 0 0 0.2rem rgba(106, 90, 205, 0.5); }" +
                        "</style>" +
                        "<title>Owl's Secret Magic Store</title>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<h1>🦉 Owl's Secret Magic Store 🪄</h1>" +
                        "<hr>" +
                        "<h2>Success!</h2>" +
                        "<p>You bought " + product.getName() + ". New inventory: " + newInventory + "</p>" +
                        "<a href=\"/mainscreen\" class=\"btn btn-primary\">Back to Main Screen</a>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
            } else {
                return "<!DOCTYPE html>" +
                        "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">" +
                        "<head>" +
                        "<meta charset=\"UTF-8\">" +
                        "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                        "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                        "<style>" +
                        "body { background-color: #222; color: #eee; font-family: Verdana, sans-serif; padding-top: 50px; }" +
                        ".container { max-width: 800px; margin: 0 auto; background-color: #333; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                        "h1, h2 { color: #f8d347; text-align: center; }" +
                        ".btn-primary { background-color: #6a5acd; border-color: #6a5acd; }" +
                        ".btn-primary:hover { background-color: #483d8b; border-color: #483d8b; }" +
                        ".btn-primary:focus { box-shadow: 0 0 0 0.2rem rgba(106, 90, 205, 0.5); }" +
                        "</style>" +
                        "<title>Owl's Secret Magic Store</title>" +
                        "</head>" +
                        "<body>" +
                        "<div class=\"container\">" +
                        "<h1>🦉 Owl's Secret Magic Store 🪄</h1>" +
                        "<hr>" +
                        "<h2>Failure!</h2>" +
                        "<p>You DID NOT buy " + product.getName() + ". No inventory available.</p>" +
                        "<a href=\"/mainscreen\" class=\"btn btn-primary\">Back to Main Screen</a>" +
                        "</div>" +
                        "</body>" +
                        "</html>";
            }
        } else {
            return "<!DOCTYPE html>" +
                    "<html lang=\"en\" xmlns:th=\"http://www.thymeleaf.org\">" +
                    "<head>" +
                    "<meta charset=\"UTF-8\">" +
                    "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" +
                    "<link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\">" +
                    "<style>" +
                    "body { background-color: #222; color: #eee; font-family: Verdana, sans-serif; padding-top: 50px; }" +
                    ".container { max-width: 800px; margin: 0 auto; background-color: #333; padding: 20px; border-radius: 10px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }" +
                    "h1, h2 { color: #f8d347; text-align: center; }" +
                    ".btn-primary { background-color: #6a5acd; border-color: #6a5acd; }" +
                    ".btn-primary:hover { background-color: #483d8b; border-color: #483d8b; }" +
                    ".btn-primary:focus { box-shadow: 0 0 0 0.2rem rgba(106, 90, 205, 0.5); }" +
                    "</style>" +
                    "<title>Owl's Secret Magic Store</title>" +
                    "</head>" +
                    "<body>" +
                    "<div class=\"container\">" +
                    "<h1>🦉 Owl's Secret Magic Store 🪄</h1>" +
                    "<hr>" +
                    "<h2>Product not found</h2>" +
                    "<p>The product you are trying to buy does not exist.</p>" +
                    "<a href=\"/mainscreen\" class=\"btn btn-primary\">Back to Main Screen</a>" +
                    "</div>" +
                    "</body>" +
                    "</html>";
        }
    }
}