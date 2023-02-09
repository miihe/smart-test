package ru.miihe.params;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Rates {

    private String productName;

    private LocalDate date;

    private Long cost;
}
