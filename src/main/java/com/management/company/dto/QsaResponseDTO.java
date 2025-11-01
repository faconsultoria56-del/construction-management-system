package com.management.company.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class QsaResponseDTO {
    @JsonProperty("nome_socio")
    private String nomeSocio;
    @JsonProperty("cpf_representante_legal")
    private String cpfRepresentanteLegal;
    @JsonProperty("cnpj_cpf_do_socio")
    private String cnpjCpfDoSocio;
    @JsonProperty("qualificacao_socio")
    private String qualificacaoSocio;
}
