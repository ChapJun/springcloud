package com.chapjun.catalogsservice.repository;

import com.chapjun.catalogsservice.jpa.CatalogEntity;
import org.springframework.data.repository.CrudRepository;

public interface CatalogRepository extends CrudRepository<CatalogEntity, Long> {
    CatalogEntity findByProductId(String productId);
}
