package serpro.valida.pagamentos.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import serpro.valida.pagamentos.model.RelatorioFiscalDTO;

import java.util.Map;

@Slf4j
@Service
public class RelatorioFiscalService {

    private final RestClient restClient = RestClient.create();
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    
    // Alterar para carregar do application.properties no futuro
    private final String tokenSerpro = "06aef429-a981-3ec5-a1f8-71d38d86481e";

    public byte[] emitirRelatorioSituacaoFiscal(String numeroDocumento, String protocoloRelatorio) {
        String urlApi = "https://gateway.apiserpro.serpro.gov.br/integra-contador-trial/v1/Emitir";
        Integer tipo = verificaTipo(numeroDocumento);

        String dadosJsonString = "{\"protocoloRelatorio\":\"" + protocoloRelatorio + "\"}";

        Map<String, Object> corpoRequisicao = Map.of(
            "contratante", Map.of("numero", "00000000000000", "tipo", 2),
            "autorPedidoDados", Map.of("numero", "00000000000000", "tipo", 2),
            "contribuinte", Map.of("numero", numeroDocumento, "tipo", tipo),
            "pedidoDados", Map.of(
                "idSistema", "SITFIS",
                "idServico", "RELATORIOSITFIS92",
                "versaoSistema", "1.0",
                "dados", dadosJsonString
            )
        );

                try {
            String jsonBruto = restClient.post()
                    .uri(urlApi)
                    .header("Authorization", "Bearer " + tokenSerpro)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao)
                    .retrieve()
                    .body(String.class);

            log.info("JSON BRUTO DO PDF RECEBIDO.");

            if (jsonBruto != null && jsonBruto.contains("\"dados\":\"{")) {
                int posicaoDadosFinais = jsonBruto.lastIndexOf("\"dados\":\"{");
                String parteComeco = jsonBruto.substring(0, posicaoDadosFinais);
                String parteFinal = jsonBruto.substring(posicaoDadosFinais);

                parteFinal = parteFinal.replace("\"dados\":\"{", "\"dados\":[{");
                parteFinal = parteFinal.replace("}\",\"mensagens\"", "}],\"mensagens\"");
                
                parteFinal = parteFinal.replace("\\", "");

                jsonBruto = parteComeco + parteFinal;
            }

            RelatorioFiscalDTO resposta = objectMapper.readValue(jsonBruto, RelatorioFiscalDTO.class);

            if (resposta == null || resposta.getStatus() != 200 || resposta.getDados() == null || resposta.getDados().isEmpty()) {
                throw new Exception("Resposta inválida do SERPRO ou PDF ausente.");
            }

            String base64Puro = resposta.getDados().get(0).getPdf().trim();

            return java.util.Base64.getDecoder().decode(base64Puro);

        } catch (Exception e) {
            log.error("Falha crítica ao emitir e decodificar o PDF: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar o relatório fiscal", e);
        }

    }

    private Integer verificaTipo(String numeroDocumento) {
        if (numeroDocumento == null) throw new IllegalArgumentException("O documento não pode ser nulo");
        if (numeroDocumento.length() == 11) return 1; // cpf
        if (numeroDocumento.length() == 14) return 2; // cnpj
        throw new IllegalArgumentException("O documento deve ter 11 ou 14 caracteres");
    }
}
