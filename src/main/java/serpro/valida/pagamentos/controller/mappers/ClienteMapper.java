package serpro.valida.pagamentos.controller.mappers;

import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import serpro.valida.pagamentos.controller.mappers.cliente.ClienteRequest;
import serpro.valida.pagamentos.controller.mappers.cliente.ClienteResponse;
import serpro.valida.pagamentos.model.Cliente;

@Mapper(componentModel = "spring", uses = {ContaMapper.class},  unmappedTargetPolicy = ReportingPolicy.IGNORE) // Permite injetar o mapper no seu Controller/Service
public interface ClienteMapper {

    Cliente toCliente(ClienteRequest request);

    ClienteResponse toResponse(Cliente cliente);
}
