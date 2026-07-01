package serpro.valida.pagamentos.service;

import org.springframework.stereotype.Service;
import serpro.valida.pagamentos.repository.ContaRepository;
import serpro.valida.pagamentos.repository.ClienteRepository;

import serpro.valida.pagamentos.model.Conta;
import serpro.valida.pagamentos.model.Cliente;

@Service
public class ContaService {
    
    
    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    public Conta adicionarConta(Conta conta, String cpfCnpj){
        if(conta.getNumeroDocumento() == null){
            throw new RuntimeException("numero da conta precisa ser preenchido");
        }

        Cliente cliente = clienteRepository.findByCpf(cpfCnpj).
            orElseThrow(() -> new RuntimeException("Não é possivel cadastrar uma conta com usuario inexistente"));

        conta.setCliente(cliente);
        return contaRepository.save(conta);
    }

    public Conta getConta(Long id){
        return contaRepository.findByIdConta(id).orElseThrow(() -> new RuntimeException("Conta com id " + id + " não existe"));
    }

    public Conta atualizarConta(Conta contaNova, String cpfCnpj, Long id){
        if(contaNova.getNumeroDocumento() == null){
            throw new RuntimeException("numero da conta precisa ser preenchido");
        }
        Conta conta = contaRepository.findByIdConta(id).orElseThrow(() -> new RuntimeException("Conta não existe"));
        Cliente cliente = clienteRepository.findByCpf(cpfCnpj).
            orElseThrow(() -> new RuntimeException("Não é possivel cadastrar uma conta com usuario inexistente"));

        conta.setCliente(cliente);
        conta.setDataVencimento(contaNova.getDataVencimento());
        conta.setNumeroDocumento(contaNova.getNumeroDocumento());

        return contaRepository.save(conta);
    }
}