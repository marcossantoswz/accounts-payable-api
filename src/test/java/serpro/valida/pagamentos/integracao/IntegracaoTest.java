package serpro.valida.pagamentos.integracao;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import serpro.valida.pagamentos.repository.ClienteRepository;
import serpro.valida.pagamentos.repository.ContaRepository;
import serpro.valida.pagamentos.model.Cliente;
import serpro.valida.pagamentos.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegracaoTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ContaRepository contaRepository;


    @Test
    void fluxoCadastroCliente() throws Exception{
        String dados = """
            {
                "nome": "Ana",
                "idade": 19,
                "cpf": "99999999999",
                "endereco": "Rua Mangaba"
            }
            """;
        clienteRepository.deleteAll();

        mockMvc.perform(post("http://localhost:8080/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dados))
                .andExpect(status().isCreated());

        Cliente cliente = clienteRepository.findByCpf("99999999999").orElseThrow(() -> new AssertionError("O cliente deveria existir"));

        assertEquals("Ana", cliente.getNome());
        assertEquals("99999999999", cliente.getCpf());

    }

    @Test
    void fluxoCadastroConta() throws Exception{
        String dadosCliente = """
            {
                "nome": "Ana",
                "idade": 19,
                "cpf": "99999999999",
                "endereco": "Rua Mangaba"
            }
            """;

        String dadosConta = """
            {
                "numeroDocumento": "999",
                "valorTotal": 19.4,
                "dataVencimento": "99-12-2000",
                "multa": 3.0,
                "status": "Paga"
            }
            """;

        clienteRepository.deleteAll();

        mockMvc.perform(post("http://localhost:8080/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosCliente))
                .andExpect(status().isCreated());

        mockMvc.perform(post("http://localhost:8080/conta/99999999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(dadosConta))
                .andExpect(status().isCreated());

        Conta conta = contaRepository.findById(1L).orElseThrow(() -> new AssertionError("O cliente deveria existir"));

        assertEquals("999", conta.getNumeroDocumento());
        assertEquals(19.4, conta.getValorTotal());

    }
}
