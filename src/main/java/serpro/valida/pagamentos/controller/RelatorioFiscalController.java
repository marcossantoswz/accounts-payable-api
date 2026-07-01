package serpro.valida.pagamentos.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import serpro.valida.pagamentos.service.RelatorioFiscalService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/relatorio")
public class RelatorioFiscalController {

    private final RelatorioFiscalService relatorioFiscalService;

    public RelatorioFiscalController(RelatorioFiscalService relatorioFiscalService) {
        this.relatorioFiscalService = relatorioFiscalService;
    }

    @GetMapping("/emitir/{numDocumento}")
    public ResponseEntity<byte[]> baixarRelatorioSituacaoFiscal(
            @PathVariable String numDocumento,
            @RequestParam String protocoloRelatorio) {
        try {
            byte[] pdfBytes = relatorioFiscalService.emitirRelatorioSituacaoFiscal(numDocumento, protocoloRelatorio);

            String nomeArquivo = "relatorio-situacao-fiscal-" + numDocumento + ".pdf";

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + nomeArquivo + "\"")
                    .body(pdfBytes);

        } catch (Exception e) {
            System.err.println("Erro ao emitir PDF do relatório: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
