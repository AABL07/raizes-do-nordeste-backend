package com.raizesdonordeste.domain.model;

import com.raizesdonordeste.domain.enums.FinalidadeConsentimento;
import com.raizesdonordeste.domain.enums.StatusConsentimento;

import java.time.LocalDateTime;

public class Consentimento {

    private Long id;
    private FinalidadeConsentimento finalidade;
    private StatusConsentimento status;
    private String versaoTermo;
    private LocalDateTime concedidoEm;
    private LocalDateTime revogadoEm;
}