package ac.su.inclassspringsecurity.repository;

import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.domain.Product;
import ac.su.inclassspringsecurity.domain.QProduct;
import com.querydsl.core.BooleanBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("basic repository action test")
    public void createProduct(){
        Product product = new Product();
        product.setName("테스트 상품");
        product.setPrice(1000);
        product.setStockCount(100);
        product.setStatus(ProductStatusEnum.IN_STOCK);
        product.setCreatedAt(String.valueOf(LocalDateTime.now()));
        product.setUpdatedAt(String.valueOf(LocalDateTime.now()));
        product.setDescription("상세정보를 입력합니다");
        product.setMemo("메모 입력");
        product.setImage("/static/path/to/image");
        Product savedProduct = productRepository.save(product);
        System.out.println(savedProduct);
    }

    @Test
    @DisplayName("상품 리스트 등록 테스트")
    public void createList() {
        List<Product> productList = new ArrayList<>();
        // 상품 10 개의 더미 데이터 생성 후 저장
        for (int i = 1; i <= 10; i++) {
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
        List<Product> savedProductList = productRepository.saveAll(productList);
        savedProductList.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품 이름으로 조회 테스트")
    public void readByName() {
        // 상품 10 개의 더미 데이터 생성 후 저장
        createList();

        // 상품 이름으로 상품 리스트 조회
        List<Product> productList = productRepository.findByName("테스트 상품 5");
        productList.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품 상태로 조회 테스트")
    public void readByStatus() {
        // 상품 10 개의 더미 데이터 생성 후 저장
        createList();
        // 상품 이름으로 상품 리스트 조회
        List<Product> productList = productRepository.findByStatus(ProductStatusEnum.PREPARING);
        productList.forEach(System.out::println);
    }

    // 2-2) 상품 재고 상태 필터링
    @Test
    @DisplayName("유효 상품 조회 테스트")
    public void readValidProducts() {
        // 상품 10 개의 더미 데이터 생성 후 저장
        createList();

        // 상품 상태가 PREPARING, IN_STOCK 인 상품 리스트 조회
        List<Product> inStockProductList = productRepository.findByStatusList(
                List.of(ProductStatusEnum.PREPARING, ProductStatusEnum.IN_STOCK));
        inStockProductList.forEach(System.out::println);
    }

    // 6) Predicate Boolean Builder & Pageable 을 사용해 상품 조회
    @Test
    @DisplayName("상품 페이징 조회 테스트")
    public void readWithPagination() {
        // 상품 10 개의 더미 데이터 생성 후 저장
        createList();

        // Boolean Builder 를 사용해 상품 가격이 5000 이상 10000 이하인 상품 리스트 조회
        BooleanBuilder predicate = new BooleanBuilder();
        predicate.and(QProduct.product.price.between(5000, 10000));

        // 페이징 처리를 적용해 상품 리스트 조회
        Pageable pageable = PageRequest.of(0, 3);
        Page<Product> productPage = productRepository.findAll(predicate, pageable);
        List<Product> productList = productPage.getContent();

        // 조회된 상품 출력
        productList.forEach(System.out::println);
    }

}
