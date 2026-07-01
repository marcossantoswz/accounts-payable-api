package serpro.valida.pagamentos.controller.mappers.conta;
import lombok.Setter;
import lombok.Getter;
import serpro.valida.pagamentos.model.Cliente;

@Getter
@Setter
public class ContaRequest {
    private String numeroDocumento;
    private Double valorTotal;
    private String dataVencimento;
    private Double multa;
    private String status;
}
