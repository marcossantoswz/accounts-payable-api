package serpro.valida.pagamentos.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serpro.valida.pagamentos.model.Conta;
import serpro.valida.pagamentos.service.ContaService;
import serpro.valida.pagamentos.controller.mappers.ContaMapper;
import serpro.valida.pagamentos.controller.mappers.conta.ContaRequest;
import serpro.valida.pagamentos.controller.mappers.conta.ContaResponse;



@RestController
@RequestMapping("/conta")
public class ContaController {

    private final ContaService contaService;
    private final ContaMapper contaMapper;

    //injecao do service
    public ContaController(ContaService contaService, ContaMapper contaMapper) {
        this.contaService = contaService;
        this.contaMapper = contaMapper;
    }
    
    @PostMapping("/{cpfCnpj}")
    public ResponseEntity<?> cadastrarConta(@RequestBody ContaRequest request, @PathVariable String cpfCnpj) {
        try {
            Conta conta = contaMapper.toConta(request);
            Conta contaSalva = contaService.adicionarConta(conta, cpfCnpj);
            ContaResponse response = contaMapper.toResponse(contaSalva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body(e.getMessage());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno ao processar dados do conta: " + e.getMessage());
        }
    }


    @GetMapping("/{idConta}")
    public ResponseEntity<?> getConta(@RequestParam Long idConta) {
        try{
            Conta conta = contaService.getConta(idConta);
            return ResponseEntity.ok(conta);
        }
        catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cpfCnpj}/{idConta}")
    public ResponseEntity<?> atualizarConta(@RequestBody Conta contaNova, @PathVariable String cpfCnpj, @PathVariable Long idConta){
        try {
            Conta contaAtualizada = contaService.atualizarConta(contaNova, cpfCnpj, idConta);
            return ResponseEntity.ok(contaAtualizada);
            
        } catch(RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
}