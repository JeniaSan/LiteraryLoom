package com.polishuchenko.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderAddressDto {
    @NotBlank
    private String orderAddress;
}
