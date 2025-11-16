package com.management.company.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CnpjResponseDTO {
    private String uf;
    private String cep;
    private String bairro;
    private String numero;
    private String municipio;
    private String logradouro;
    private String complemento;
    @JsonProperty("razao_social")
    private String razaoSocial;
    @JsonProperty("nome_fantasia")
    private String nomeFantasia;
    @JsonProperty("descricao_situacao_cadastral")
    private String descricaoSituacaoCadastral;
    @JsonProperty("ddd_telefone_1")
    private String dddTelefone1;
    private List<QsaResponseDTO> qsa;
}
