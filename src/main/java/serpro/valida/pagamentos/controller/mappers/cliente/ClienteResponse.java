package serpro.valida.pagamentos.controller.mappers.cliente;

import java.util.ArrayList;
import java.util.List;
import serpro.valida.pagamentos.controller.mappers.conta.ContaResponse;  

public record ClienteResponse( Long id,
    String nome,
    Integer idade,
    String cpf,
    String endereco,
    Boolean temMulta,
    Double mediaDarfs,
    List<ContaResponse> contas
) {
    public ClienteResponse {
        if (contas == null) {
            contas = new ArrayList<>();
        }
    }
}
