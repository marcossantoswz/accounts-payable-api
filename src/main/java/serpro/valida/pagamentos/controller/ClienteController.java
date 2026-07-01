package serpro.valida.pagamentos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.validation.Valid;
import java.util.List;

import serpro.valida.pagamentos.controller.mappers.ClienteMapper;
import serpro.valida.pagamentos.controller.mappers.cliente.ClienteRequest;
import serpro.valida.pagamentos.controller.mappers.cliente.ClienteResponse;
import serpro.valida.pagamentos.model.Cliente;
import serpro.valida.pagamentos.service.ClienteService;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ClienteMapper clienteMapper;

    public ClienteController(ClienteService clienteService, ClienteMapper clienteMapper) {
        this.clienteService = clienteService;
        this.clienteMapper = clienteMapper;
    }
    @GetMapping()
    public List<Cliente> getTodosClientes() {
        return clienteService.buscarTodosClientes();
    }
    
    @PostMapping
    public ResponseEntity<?> criarCliente(@Valid @RequestBody ClienteRequest request) {
        try {
            Cliente cliente = clienteMapper.toCliente(request);//mapper request to cliente
            Cliente clienteSalvo = clienteService.adicionarCliente(cliente);
            ClienteResponse response = clienteMapper.toResponse(clienteSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar dados do cliente: " + e.getMessage());
        }
    }

    
    @GetMapping("/{cpf}")
    public ResponseEntity<?> buscarCliente(@PathVariable String cpf) {
        try {

            Cliente cliente = clienteService.buscarCliente(cpf);

            ClienteResponse response = clienteMapper.toResponse(cliente);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{cpfCnpj}")
    public ResponseEntity<?> deleteCliente(@PathVariable String cpfCnpj){
        try{
            clienteService.deletarCliente(cpfCnpj);
            return ResponseEntity.noContent().build();
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    
    }

    @PutMapping("/{cpfCnpj}")
    public ResponseEntity<?> putCliente(@PathVariable String cpfCnpj, @RequestBody ClienteRequest clienteAtualizado) {
        try{
            Cliente clienteNovo = clienteMapper.toCliente(clienteAtualizado);
            Cliente cliente = clienteService.atualizarClienteCompleto(cpfCnpj, clienteNovo);
            ClienteResponse response = clienteMapper.toResponse(cliente);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }

    @PatchMapping("/{cpfCnpj}")
    public ResponseEntity<?> patchCliente(@PathVariable String cpfCnpj, @RequestBody ClienteRequest clienteAtualizado) {
        try{
            Cliente clienteNovo = clienteMapper.toCliente(clienteAtualizado);
            Cliente cliente = clienteService.atualizarCliente(cpfCnpj, clienteNovo);
            ClienteResponse response = clienteMapper.toResponse(cliente);
            return ResponseEntity.ok(response);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }
    

}
