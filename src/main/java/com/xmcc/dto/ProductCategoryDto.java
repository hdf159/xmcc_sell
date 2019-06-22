package com.xmcc.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xmcc.entity.ProductCategory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryDto implements Serializable {
    @JsonProperty("name")
    private String categoryName;
    @JsonProperty("type")
    private Integer categoryType;
    @JsonProperty("foods")
    private List<ProductInfoDto> productInfoDtoList;

   public static ProductCategoryDto build(ProductCategory productCategory){
       ProductCategoryDto productCategoryDto = new ProductCategoryDto();
       BeanUtils.copyProperties(productCategory,productCategoryDto);
       return productCategoryDto;

   }


}
