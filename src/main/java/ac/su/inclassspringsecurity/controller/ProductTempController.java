package ac.su.inclassspringsecurity.controller;

import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/thymeleaf")
public class ProductTempController {
    private final ProductService productService;

    @GetMapping("/ex01")
    public String showProduct(Model model) {
        Product product = new Product();
        model.addAttribute("data","This is assigned from controller!");
        model.addAttribute("data1",product);
        model.addAttribute("data2","Second Data");
        model.addAttribute("data3","Third Data");
        return "thymeleaf/ex01";
    }

    @GetMapping("/product-page")
    public String productPage (Model model,
                               @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                               @RequestParam(value = "size",required = false,defaultValue = "15") int size)
    {
        List<Product> products = productService.getValidProductsPage(page,size);
        model.addAttribute("products",products);
        return "thymeleaf/product-page";
    }

    @GetMapping("/products-layout-applied")
    public String productLayoutApplied(){

        return "thymeleaf/products-layout-applied";
    }
}
