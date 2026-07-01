package serpro.valida.pagamentos.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import serpro.valida.pagamentos.repository.ClienteRepository;
import serpro.valida.pagamentos.model.Cliente;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository repository;

    @InjectMocks
    private ClienteService service;

    @Test
    public void deveSalvarUsuarioComSucesso() {
        Cliente usuarioInput = new Cliente();
        usuarioInput.setCpf("99999999999");
        usuarioInput.setEndereco("Rua das capivaras");
        usuarioInput.setIdade(29);
        usuarioInput.setNome("Marck");

        Cliente usuarioSalvo = new Cliente();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setCpf("99999999999");
        usuarioSalvo.setEndereco("Rua das capivaras");
        usuarioSalvo.setIdade(29);
        usuarioSalvo.setNome("Marck");

        Mockito.when(repository.save(usuarioInput)).thenReturn(usuarioSalvo);

        Cliente resultado = service.adicionarCliente(usuarioInput);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("99999999999", resultado.getCpf());
        
        Mockito.verify(repository, Mockito.times(1)).save(usuarioInput);
    }

    @Test
    public void verificaClienteSalvo() {
        Cliente usuarioSalvo = new Cliente();
        usuarioSalvo.setId(1L);
        usuarioSalvo.setCpf("99999999999");
        usuarioSalvo.setEndereco("Rua das capivaras");
        usuarioSalvo.setIdade(29);
        usuarioSalvo.setNome("Marck");

        Mockito.when(repository.findByCpf("99999999999")).thenReturn(Optional.of(usuarioSalvo));

        Cliente resultado = service.buscarCliente("99999999999");

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("99999999999", resultado.getCpf());
        
        Mockito.verify(repository, Mockito.times(1)).findByCpf(usuarioSalvo.getCpf());
    }
}

