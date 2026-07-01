package serpro.valida.pagamentos.controller.mappers.cliente;

//import org.hibernate.validator.constraints.br.CPF;
import jakarta.validation.constraints.Min;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
    @NotBlank(message = "Nome é obrigatório")
    String nome,

    //@CPF(message = "Cpf inválido")
    String cpf,

    @NotBlank(message = "endereco é obrigatório")
    String endereco,

    @Min(value = 18, message = "Menores não são permitidos")
    Integer idade
) {}
