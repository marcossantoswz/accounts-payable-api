package serpro.valida.pagamentos.controller;

import serpro.valida.pagamentos.service.DadosPagamentoDTOService; 
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/pagamentos")
public class DadosPagamentoDTOController{

    private final DadosPagamentoDTOService pagamentoService;

    public DadosPagamentoDTOController(DadosPagamentoDTOService pagamentoService){
        this.pagamentoService = pagamentoService;
    }

    @GetMapping("/{numDocumento}/{numDocumentoEsperado}")
    public ResponseEntity<Boolean> validaDocumentoIgual(@PathVariable String numDocumento, @PathVariable String numDocumentoEsperado) {
        try{
            boolean resultado = pagamentoService.validarDocIgual(numDocumento, numDocumentoEsperado);
            return ResponseEntity.ok(resultado);
        }
        catch (Exception e) {
            System.err.println("Erro ao validar documento: " + e.getMessage());
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @GetMapping("/{numDocumento}")
    public ResponseEntity<Boolean> existeMulta(@PathVariable String numDocumento) {
        try{
            boolean resultado = pagamentoService.teveMulta(numDocumento);
            return ResponseEntity.ok(resultado);
        }
        catch (Exception e) {
            System.err.println("Erro ao validar documento: " + e.getMessage());
            return ResponseEntity.internalServerError().body(false);
        }
    }

    @GetMapping("/mediaDarfs/{numDocumento}")
    public ResponseEntity<Double> valorMedioDasDarfs(@PathVariable String numDocumento) {
        try{
            double media = pagamentoService.valorMedioDasDarfs(numDocumento);
            return ResponseEntity.ok(media);
        }
        catch (Exception e) {
            System.err.println("Erro ao validar documento: " + e.getMessage());
            
            return ResponseEntity.internalServerError().body(0.0);
        }
    }

}