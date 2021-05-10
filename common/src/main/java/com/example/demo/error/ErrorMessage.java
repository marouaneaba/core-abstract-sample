package com.example.demo.error;

import com.example.demo.utils.ShopConstant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorMessage {
    UTILITY_CLASS_INSTANTIATE_ERROR("Utility class must not be instantiated."),
    SHOP_API_ENNDPOINTS_INSTANTIATE_ERROR("Shop api endpoint class must not be instantiated."),
    STOCK_SHOE_DUPLICATE_ERROR("The collection contains a duplication of shoe."),
    STOCK_CAPACITY_ERROR(String.format("Stock capacity limited of %d shoes.", ShopConstant.STOCK_MAX_CAPACITE));

    private final String description;
}
