package com.antonio.core.product.services;

import com.antonio.core.product.persistence.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import com.antonio.api.core.product.Product;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  @Mappings({
    @Mapping(target = "serviceAddress", ignore = true)
  })
  Product entityToApi(ProductEntity entity);

  @Mappings({
    @Mapping(target = "id", ignore = true), @Mapping(target = "version", ignore = true)
  })
  ProductEntity apiToEntity(Product api);
}
