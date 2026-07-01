package serpro.valida.pagamentos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import serpro.valida.pagamentos.repository.ClienteRepository;
import serpro.valida.pagamentos.repository.ContaRepository;

import serpro.valida.pagamentos.model.Cliente;
import serpro.valida.pagamentos.model.Conta;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ContaServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;



    @Test
    public void deveSalvarContaComSucesso() {
        Cliente usuarioSalvo = new Cliente();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setCpf("99999999999");
        usuarioSalvo.setEndereco("Rua das capivaras");
        usuarioSalvo.setIdade(29);
        usuarioSalvo.setNome("Marck");

        Conta contaInput = new Conta();
        contaInput.setCliente(usuarioSalvo);
        contaInput.setNumeroDocumento("99999");
        contaInput.setValorTotal(2341.0);

        Conta contaSalvo = new Conta();
        contaSalvo.setIdConta(1L);
        contaSalvo.setCliente(usuarioSalvo);
        contaSalvo.setNumeroDocumento("99999");
        contaSalvo.setValorTotal(2341.0);

        Mockito.when(clienteRepository.findByCpf("99999999999"))
            .thenReturn(Optional.of(usuarioSalvo));

        Mockito.when(contaRepository.save(contaInput)).thenReturn(contaSalvo);

        Conta resultado = contaService.adicionarConta(contaInput, "99999999999");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getIdConta());
        assertEquals("99999", resultado.getNumeroDocumento());
        assertEquals(2341.0, resultado.getValorTotal());
        
        Mockito.verify(contaRepository, Mockito.times(1)).save(Mockito.any(Conta.class));
    }

    @Test
    void deveLancarExcecaoQuandoContaNaoForEncontrada() {

        when(contaRepository.findByIdConta(99L))
            .thenThrow(new RuntimeException("conta inexistente"));

        assertThrows(RuntimeException.class, () -> contaService.getConta(99L));
    }


}
