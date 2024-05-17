package ac.su.inclassspringsecurity.controller;

import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 생성자 주입 방식 -> Annotation 으로 대체 가능
//    public ProductController(ProductService productService) {
//        this.productService = productService;
//    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(
                value -> new ResponseEntity<>(value, HttpStatus.OK)
        ).orElseGet(
                () -> new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestBody Product product
    ) {
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable("id") long id,
            @RequestBody Product product
    ) {
        //upsert 동작
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct != null) {
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        }
        Product createdProduct = productService.createProduct(product);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping("/make-dummy")
    public ResponseEntity<List<Product>> makeDummyData(@RequestParam("count") int count) {
        // count 값이 1 이상 100 이하가 되도록 제약조건 추가하기!
        if (count < 1 || count > 100) {
            return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
        }
        List<Product> products = productService.makeDummyData(count);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

}