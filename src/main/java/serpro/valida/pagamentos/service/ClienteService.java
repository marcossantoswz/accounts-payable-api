package serpro.valida.pagamentos.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import serpro.valida.pagamentos.repository.ClienteRepository;
import serpro.valida.pagamentos.model.Cliente;
import serpro.valida.pagamentos.model.Conta;
import serpro.valida.pagamentos.model.DadosPagamentoDTO;

@Service
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    private final RestClient restClient;
    private final DadosPagamentoDTOService dadosPagamento;
    private final ContaService contaService;

    public ClienteService(ClienteRepository clienteRepository, DadosPagamentoDTOService dadosPagamento, ContaService contaService) {
        this.clienteRepository = clienteRepository;
        this.dadosPagamento = dadosPagamento;
        this.contaService = contaService;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    public List<Cliente> buscarTodosClientes(){
        return clienteRepository.findAll();
    }

    
    public Cliente adicionarCliente(Cliente cliente) {
        if (cliente.getIdade() < 18) {
            throw new IllegalArgumentException("Menores de idade não podem ser cadastrados.");
        }
        
        if (clienteRepository.existsByCpf(cliente.getCpf())) {
            throw new IllegalArgumentException("Cliente já cadastrado com este CPF.");
        }

        //experimental: busca contas no serpro do usuario e as cadastra automaticamente
        if ("99999999999999".equals(cliente.getCpf())) { 
            
            //remover
            cliente.setMediaDarfs(this.restClient.get()
                .uri("/pagamentos/mediaDarfs/{numDocumento}", cliente.getCpf())
                .retrieve()
                .body(Double.class));

            //remover
            cliente.setTemMulta(this.restClient.get()
                .uri("/pagamentos/{numDocumento}", cliente.getCpf())
                .retrieve()
                .body(Boolean.class));

                DadosPagamentoDTO dadosDarfs = dadosPagamento.consultarPagamento(cliente.getCpf());

            clienteRepository.save(cliente);

            for (DadosPagamentoDTO.DadoPagamentoDTO dado : dadosDarfs.getDados()) {
                Conta conta = new Conta();
                conta.setDataVencimento(dado.getDataVencimento());

                if(dado.getValorMulta() != null){
                    conta.setMulta(dado.getValorMulta().doubleValue());
                }

                if(dado.getValorTotal() != null){
                conta.setValorTotal(dado.getValorTotal().doubleValue());
                }

                conta.setNumeroDocumento(dado.getNumeroDocumento());
                conta.setStatus("paga");

                contaService.adicionarConta(conta, cliente.getCpf());
            }

            return clienteRepository.save(cliente);

        }

       return clienteRepository.save(cliente);
    }

    
    public Cliente buscarCliente(String cpfCnpj) {
        return clienteRepository.findByCpf(cpfCnpj)
            .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public void deletarCliente(String cpfCnpj){
        Cliente cliente = clienteRepository.findByCpf(cpfCnpj).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        clienteRepository.delete(cliente);
    }

    public Cliente atualizarCliente(String cpfCnpj, Cliente clienteAtualizado){
        if(clienteAtualizado.getIdade() < 18){
            throw new RuntimeException("Idade menor que 18");
        }
        
        Cliente cliente = clienteRepository.findByCpf(cpfCnpj).orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        if (clienteAtualizado.getCpf() != null) {
            cliente.setCpf(clienteAtualizado.getCpf());
        }

        if (clienteAtualizado.getEndereco() != null) {
            cliente.setEndereco(clienteAtualizado.getEndereco());
        }

        if (clienteAtualizado.getNome() != null) {
            cliente.setNome(clienteAtualizado.getNome());
        }

        if (clienteAtualizado.getIdade() != null) {
            cliente.setIdade(clienteAtualizado.getIdade());
        }

        if (clienteAtualizado.getTemMulta() != null) {
            cliente.setTemMulta(clienteAtualizado.getTemMulta());
        }

        if (clienteAtualizado.getMediaDarfs() != null) {
            cliente.setMediaDarfs(clienteAtualizado.getMediaDarfs());
        }

        return clienteRepository.save(cliente);
    }

    
    public Cliente atualizarClienteCompleto(String cpfCnpj, Cliente clienteAtualizado) {
        if(clienteAtualizado.getIdade() < 18){
            throw new RuntimeException("Idade menor que 18");
        }
        Cliente cliente = clienteRepository.findByCpf(cpfCnpj).orElseThrow(() -> new RuntimeException("Usuario nao existe"));
        
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setCpf(clienteAtualizado.getCpf());
        cliente.setEndereco(clienteAtualizado.getEndereco());
        cliente.setIdade(clienteAtualizado.getIdade());
        cliente.setTemMulta(clienteAtualizado.getTemMulta());
        cliente.setMediaDarfs(clienteAtualizado.getMediaDarfs());

        return clienteRepository.save(cliente);
    }


}
