package ac.su.inclassspringsecurity.repository;


import ac.su.inclassspringsecurity.constant.ProductStatusEnum;
import ac.su.inclassspringsecurity.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, QuerydslPredicateExecutor<Product> {
    List<Product> findByName(String name);
    List<Product> findByStatus(ProductStatusEnum status);


    @Query("SELECT p FROM Product p WHERE p.status in :statusList")
    List<Product> findByStatusList(List<ProductStatusEnum> preparing);
}
