package org.training360.musicstore;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Instrument {
    private long id;
    private String brand;
    private InstrumentType instrumentType;
    private int price;
    private LocalDate postDate;
}
