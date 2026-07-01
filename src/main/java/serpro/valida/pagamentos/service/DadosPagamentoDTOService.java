package serpro.valida.pagamentos.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import serpro.valida.pagamentos.model.DadosPagamentoDTO;

import java.util.Map;

@Slf4j
@Service
public class DadosPagamentoDTOService {

    // Simulação de uma variável para o token enquanto você não move para o application.properties
    private final String tokenSerpro = "06aef429-a981-3ec5-a1f8-71d38d86481e";

    private final RestClient restClient = RestClient.create();

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public DadosPagamentoDTO consultarPagamento(String numeroDocumento) {

        Integer tipo = verificaTipo(numeroDocumento); //verifica se é cpf ou cnpj

        String urlApi = "https://gateway.apiserpro.serpro.gov.br/integra-contador-trial/v1/Consultar";
        String dadosJsonString = "{\"codigoReceitaLista\": [\"9999\", \"9999\"],\"primeiroDaPagina\": 0,\"tamanhoDaPagina\": 100}";

        Map<String, Object> corpoRequisicao = Map.of(
            "contratante", Map.of("numero", "99999999999999", "tipo", 2),
            "autorPedidoDados", Map.of("numero", "99999999999999", "tipo", 2),
            "contribuinte", Map.of("numero", numeroDocumento, "tipo", tipo),
            "pedidoDados", Map.of("idSistema", "PAGTOWEB", "idServico", "PAGAMENTOS71", "dados", dadosJsonString)
        );

        try {
            String jsonBruto = restClient.post()
                    .uri(urlApi)
                    .header("Authorization", "Bearer " + tokenSerpro) 
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao) 
                    .retrieve()
                    .body(String.class);


            // 
            if (jsonBruto.contains("\"dados\":\"[")) {
                // Transforma "dados":"[" em "dados":[
                jsonBruto = jsonBruto.replace("\"dados\":\"[", "\"dados\":[");
                
                // Transforma ]","mensagens" em ],"mensagens"
                jsonBruto = jsonBruto.replace("]\",\"mensagens\"", "],\"mensagens\"");
                
                // Alvo: Limpa as barras de escape apenas das aspas que estão dentro do bloco de dados final
                // Encontra qualquer \" que esteja após a palavra dados do final e troca por "
                int posicaoDadosFinais = jsonBruto.lastIndexOf("\"dados\":[");
                String parteComeco = jsonBruto.substring(0, posicaoDadosFinais);
                String parteFinal = jsonBruto.substring(posicaoDadosFinais);
                
                // Limpa os escapes apenas na parte final da string
                parteFinal = parteFinal.replace("\\\"", "\"");
                
                // Junta as duas partes de volta
                jsonBruto = parteComeco + parteFinal;
            }

        return objectMapper.readValue(jsonBruto, DadosPagamentoDTO.class);


        } catch (Exception e) {
            log.error("Falha crítica no processamento do JSON do SERPRO: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar dados da API do Governo", e);
        }
    }



    public boolean validarDocIgual(String numeroDocumento, String cnpjEsperado) throws Exception {

        DadosPagamentoDTO resposta = consultarPagamento(numeroDocumento);

        if (resposta == null) {
            return false;
        }

        if (resposta.getStatus() != 200) {
            throw new Exception("Status: " + resposta.getStatus());
        }

        if (resposta.getContribuinte() == null) {
            return false;
        }

        String cnpjQuePagou = resposta.getContribuinte().getNumero();
        log.info("Comparando CNPJs - Recebido da API: {} | Esperado no sistema: {}", cnpjQuePagou, cnpjEsperado);
        if (cnpjQuePagou == null || !cnpjQuePagou.equals(cnpjEsperado)) {
            return false;
        }

        return true; 
    }

    public double valorMedioDasDarfs(String numeroDocumento) throws Exception {

        DadosPagamentoDTO resposta = consultarPagamento(numeroDocumento);

        if (resposta == null) {
            return 0;
        }

        if (resposta.getStatus() != 200) {
            throw new Exception("Status: " + resposta.getStatus());
        }

        if (resposta.getDados() == null || resposta.getDados().isEmpty()) {
            return 0.0;
        }
        
        java.math.BigDecimal somaTotal = java.math.BigDecimal.ZERO;
        Integer quantDarfs = resposta.getDados().size();
        for (DadosPagamentoDTO.DadoPagamentoDTO dado : resposta.getDados()) {
            if (dado.getValorTotal() != null) {
                somaTotal = somaTotal.add(dado.getValorTotal()); 
            }
        }

        java.math.BigDecimal media = somaTotal.divide(
            java.math.BigDecimal.valueOf(quantDarfs), 
            2, 
            java.math.RoundingMode.HALF_UP
        );

        return media.doubleValue();
    }

    public DadosPagamentoDTO darfsUsuario(String numeroDocumento) throws Exception {

        DadosPagamentoDTO resposta = consultarPagamento(numeroDocumento);

        return resposta;
    }

    public boolean teveMulta(String numeroDocumento) throws Exception {

        DadosPagamentoDTO resposta = consultarPagamento(numeroDocumento);

        if (resposta == null) {
            return false;
        }

        if (resposta.getStatus() != 200) {
            throw new Exception("Status: " + resposta.getStatus());
        }

        if (resposta.getDados() == null || resposta.getDados().isEmpty()) {
            return false;
        }

        // Percorre a lista de pagamentos retornada pelo SERPRO buscando se algum teve multa
        for (DadosPagamentoDTO.DadoPagamentoDTO dado : resposta.getDados()) {
            // Se a multa não for nula e for maior que zero, significa que teve multa
            log.info("Comparando multas - Recebido da API: {}", dado.getValorMulta());

            if (dado.getValorMulta() != null && dado.getValorMulta().compareTo(java.math.BigDecimal.ZERO) > 0) {
                return true; 
            }
        }

        return false;
    }


    public Integer verificaTipo(String numeroDocumento) {
        if (numeroDocumento == null) {
            throw new IllegalArgumentException("O documento não pode ser nulo");
        }
        if (numeroDocumento.length() == 11) return 1; // cpf
        else if (numeroDocumento.length() == 14) return 2; // cnpj
        else throw new IllegalArgumentException("O documento deve ter 11 ou 14 caracteres");
    }
}
