package ac.su.inclassspringsecurity.service;

import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.domain.QProduct;
import ac.su.inclassspringsecurity.repository.ProductRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    //DB 접근!
    private final ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> makeDummyData(int count) {
        List<Product> productList = new ArrayList<>();
        // 상품 10 개의 더미 데이터 생성 후 저장
        for (int i = 1; i <= count; i++) {
            Product product = new Product();
            product.setName("테스트 상품 " + i);
            product.setPrice(1000 * i);
            product.setStockCount(100 * i);
            // 상품 상태를 랜덤 설정
            if (Math.random() < 0.5) {
                // 50% 확률로 상품 상태를 PREPARING 또는 IN_STOCK 으로 설정
                product.setStatus(Math.random() < 0.5 ? ProductStatusEnum.PREPARING : ProductStatusEnum.IN_STOCK);
            } else {
                // 50% 확률로 상품 상태를 SOLD_OUT 또는 DELETED 으로 설정
                product.setStatus(Math.random() < 0.5 ? ProductStatusEnum.SOLD_OUT : ProductStatusEnum.DELETED);
            }
            product.setCreatedAt(String.valueOf(LocalDateTime.now()));
            product.setUpdatedAt(String.valueOf(LocalDateTime.now()));
            product.setDescription("상세정보를 입력합니다");
            product.setMemo("메모 입력");
            product.setImage("/static/path/to/image");
            productList.add(product);
        }
        // 상품 리스트 저장 및 확인
        return productRepository.saveAll(productList);
    }

    public Product createProduct(Product product) {
        // 추가된 제품 반환
        return productRepository.save(product);
    }

    public Product updateProduct(long id, Product product) {
        if (!productRepository.existsById(id)) {
            return null;
            //없었던 id인 경우 null
        }
        //기존에 존재하는 레코드 업데이트
        product.setId(id);
        return productRepository.save(product);
    }

    public boolean deleteProduct(long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Product> getProductsPage(int page, int size) {
        // 페이징 처리를 적용하여 상품 리스트 조회
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productRepository.findAll(pageable);
        // 조회된 상품 출력
        return productPage.getContent();
    }

    public List<Product> getValidProductsList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.or(QProduct.product.status.eq(ProductStatusEnum.IN_STOCK));
        predicate.or(QProduct.product.status.eq(ProductStatusEnum.PREPARING));
        Page<Product> productPage = productRepository.findAll(predicate, pageable);
        return productPage.getContent();

    }

    public Page<Product> getValidProductsPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.or(QProduct.product.status.eq(ProductStatusEnum.IN_STOCK));
        predicate.or(QProduct.product.status.eq(ProductStatusEnum.PREPARING));
        return productRepository.findAll(predicate, pageable);
    }

}
