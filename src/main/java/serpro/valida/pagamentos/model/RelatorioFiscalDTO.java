package serpro.valida.pagamentos.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RelatorioFiscalDTO {

    private Integer status;
    private ContribuinteDTO contribuinte;
    private List<PdfConteudoDTO> dados;
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ContribuinteDTO {
        private String numero;
        private Integer tipo;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PdfConteudoDTO {
        private String pdf; 
    }
}