package serpro.valida.pagamentos.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serpro.valida.pagamentos.service.ComprovanteArrecadacaoService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/comprovanteFiscal")
public class ComprovanteArrecadacaoController {

    private final ComprovanteArrecadacaoService comprovanteArrecadacaoService;

    public ComprovanteArrecadacaoController(ComprovanteArrecadacaoService comprovanteArrecadacaoService) {
        this.comprovanteArrecadacaoService = comprovanteArrecadacaoService;
    }

    @GetMapping("/emitir/{cpfCnpj}/{numDocumento}")
    public ResponseEntity<byte[]> baixarRelatorioSituacaoFiscal(
            @PathVariable String cpfCnpj,
            @PathVariable String numDocumento
            ) {
        try {
            byte[] pdfBytes = comprovanteArrecadacaoService.pdfBase64(cpfCnpj, numDocumento);

            String nomeArquivo = "relatorio-situacao-fiscal-" + numDocumento + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"") //dowload do pdf com nome
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Erro ao emitir PDF do relatório: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}