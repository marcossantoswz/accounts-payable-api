package serpro.valida.pagamentos.controller.mappers.conta;

import lombok.Getter;
import lombok.Setter;
//import serpro.valida.pagamentos.model.Cliente; 
import serpro.valida.pagamentos.controller.mappers.cliente.ClienteResponse;

@Setter
@Getter
public class ContaResponse {
    private Long idConta;
    private String numeroDocumento;
    private Double valorTotal;
    private String dataVencimento;
    private Double multa;
    private String status;
    //private ClienteResponse cliente;
}
