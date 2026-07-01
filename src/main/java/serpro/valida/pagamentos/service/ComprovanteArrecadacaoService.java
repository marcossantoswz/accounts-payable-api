package serpro.valida.pagamentos.service;

import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import serpro.valida.pagamentos.model.ComprovanteArrecadacao;

@Slf4j
@Service
public class ComprovanteArrecadacaoService {

    private final String tokenSerpro = "06aef429-a981-3ec5-a1f8-71d38d86481e";
    private final RestClient restClient = RestClient.create();

    public byte[] pdfBase64(String cpfCnpj, String numeroDoc) {

        Integer tipo = verificaTipo(cpfCnpj); 

        String urlApi = "https://gateway.apiserpro.serpro.gov.br/integra-contador-trial/v1/Emitir";
        String dadosJsonString = "{\"numeroDocumento\": \"" + numeroDoc + "\"}";

        Map<String, Object> corpoRequisicao = Map.of(
            "contratante", Map.of("numero", "99999999999999", "tipo", 2),
            "autorPedidoDados", Map.of("numero", "99999999999", "tipo", 1), 
            "contribuinte", Map.of("numero", cpfCnpj, "tipo", tipo),
            "pedidoDados", Map.of("idSistema", "PAGTOWEB", "idServico", "COMPARRECADACAO72", "dados", dadosJsonString)
        );

        try {
            String jsonBruto = restClient.post()
                    .uri(urlApi)
                    .header("Authorization", "Bearer " + tokenSerpro) 
                    .header("accept", "text/plain") // 3. CORRIGIDO: Adicionado o header accept
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(corpoRequisicao) 
                    .retrieve()
                    .body(String.class);

            ComprovanteArrecadacao resposta_pdf = new ComprovanteArrecadacao(jsonBruto);
            return java.util.Base64.getDecoder().decode(resposta_pdf.getPdf());
        }
        catch (Exception e) {
            log.error("Falha crítica ao emitir e decodificar o PDF: {}", e.getMessage());
            throw new RuntimeException("Erro ao processar o relatório fiscal", e);
        }
    }

    private Integer verificaTipo(String numeroDocumento) {
        if (numeroDocumento == null) throw new IllegalArgumentException("O documento não pode ser nulo");
        if (numeroDocumento.length() == 11) return 1; 
        if (numeroDocumento.length() == 14) return 2; 
        throw new IllegalArgumentException("O documento deve ter 11 ou 14 caracteres");
    }
}
