package com.example.demo.bootstrap;

import com.example.demo.domain.OutsourcedPart;
import com.example.demo.domain.Part;
import com.example.demo.domain.Product;
import com.example.demo.repositories.OutsourcedPartRepository;
import com.example.demo.repositories.PartRepository;
import com.example.demo.repositories.ProductRepository;
import com.example.demo.service.OutsourcedPartService;
import com.example.demo.service.OutsourcedPartServiceImpl;
import com.example.demo.service.ProductService;
import com.example.demo.service.ProductServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 *
 *
 *
 *
 */
@Component
public class BootStrapData implements CommandLineRunner {

    private final PartRepository partRepository;
    private final ProductRepository productRepository;

    private final OutsourcedPartRepository outsourcedPartRepository;

    public BootStrapData(PartRepository partRepository, ProductRepository productRepository, OutsourcedPartRepository outsourcedPartRepository) {
        this.partRepository = partRepository;
        this.productRepository = productRepository;
        this.outsourcedPartRepository=outsourcedPartRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (partRepository.count() == 0 && productRepository.count() == 0) {
            OutsourcedPart part1 = new OutsourcedPart();
            part1.setCompanyName("Wand Co.");
            part1.setName("Magic Wand");
            part1.setInv(10);
            part1.setPrice(15.0);

            OutsourcedPart part2 = new OutsourcedPart();
            part2.setCompanyName("Potion Co.");
            part2.setName("Potion Bottle");
            part2.setInv(20);
            part2.setPrice(5.0);

            OutsourcedPart part3 = new OutsourcedPart();
            part3.setCompanyName("Spell Book Co.");
            part3.setName("Spell Book");
            part3.setInv(5);
            part3.setPrice(25.0);

            OutsourcedPart part4 = new OutsourcedPart();
            part4.setCompanyName("Crystal Ball Co.");
            part4.setName("Crystal Ball");
            part4.setInv(8);
            part4.setPrice(40.0);

            OutsourcedPart part5 = new OutsourcedPart();
            part5.setCompanyName("Broomstick Co.");
            part5.setName("Broomstick");
            part5.setInv(12);
            part5.setPrice(30.0);

            partRepository.save(part1);
            partRepository.save(part2);
            partRepository.save(part3);
            partRepository.save(part4);
            partRepository.save(part5);

            Product product1 = new Product("Beginner Wizard Kit", 100.0, 10);
            Product product2 = new Product("Advanced Wizard Kit", 250.0, 5);
            Product product3 = new Product("Potion Making Set", 50.0, 20);
            Product product4 = new Product("Fortune Telling Set", 70.0, 8);
            Product product5 = new Product("Flying Starter Pack", 200.0, 12);

            productRepository.save(product1);
            productRepository.save(product2);
            productRepository.save(product3);
            productRepository.save(product4);
            productRepository.save(product5);

        }

        List<OutsourcedPart> outsourcedParts=(List<OutsourcedPart>) outsourcedPartRepository.findAll();
        for(OutsourcedPart part:outsourcedParts){
            System.out.println(part.getName()+" "+part.getCompanyName());
        }

        System.out.println("Started in Bootstrap");
        System.out.println("Number of Products"+productRepository.count());
        System.out.println(productRepository.findAll());
        System.out.println("Number of Parts"+partRepository.count());
        System.out.println(partRepository.findAll());

    }
}
