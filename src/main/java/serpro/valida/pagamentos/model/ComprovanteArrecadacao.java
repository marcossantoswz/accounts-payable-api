package serpro.valida.pagamentos.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

@Data
public class ComprovanteArrecadacao {

    private int status;
    private String pdf;

    public ComprovanteArrecadacao(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(json);
            this.status = root.get("status").asInt();
            String dadosStr = root.get("dados").asText();

            JsonNode dadosJson = mapper.readTree(dadosStr);

            this.pdf = dadosJson.get("pdf").asText();

        } catch (Exception e) {
            throw new RuntimeException("Erro ao processar JSON", e);
        }
    }
}