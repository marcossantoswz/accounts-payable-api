package serpro.valida.pagamentos.controller.mappers;

import org.mapstruct.Mapper;

import serpro.valida.pagamentos.controller.mappers.conta.ContaResponse;
import serpro.valida.pagamentos.controller.mappers.conta.ContaRequest;
import serpro.valida.pagamentos.controller.mappers.ClienteMapper;

import serpro.valida.pagamentos.model.Conta;

@Mapper(componentModel = "spring")
public interface ContaMapper {
    ContaResponse toResponse(Conta conta);
    Conta toConta(ContaRequest request);
}
