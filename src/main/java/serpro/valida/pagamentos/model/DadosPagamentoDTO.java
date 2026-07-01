package serpro.valida.pagamentos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DadosPagamentoDTO {
    
    private Integer status;
    private ContribuinteDTO contribuinte;
    private List<DadoPagamentoDTO> dados;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContribuinteDTO {
        private String numero; 
        private Integer tipo; 
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DadoPagamentoDTO {
        private String numeroDocumento;
        private BigDecimal valorTotal;
        private String periodoApuracao;
        private String dataArrecadacao;
        private String dataVencimento;
        private BigDecimal valorMulta;
    }
}
