package com.chapjun.catalogsservice.service;

import com.chapjun.catalogsservice.jpa.CatalogEntity;

public interface CatalogService {

    Iterable<CatalogEntity> getAllCatalogs();
}
