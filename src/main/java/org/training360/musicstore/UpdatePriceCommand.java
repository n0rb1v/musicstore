package org.training360.musicstore;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
public class UpdatePriceCommand {
    @Positive
    private int price;


}
