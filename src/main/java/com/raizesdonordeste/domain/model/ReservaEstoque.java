package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.StatusReservaEstoque;

import java.time.LocalDateTime;

public class ReservaEstoque {

    private Long id;
    private Integer quantidade;
    private StatusReservaEstoque status;
    private LocalDateTime criadoEm;
    private LocalDateTime resolvidoEm;
}