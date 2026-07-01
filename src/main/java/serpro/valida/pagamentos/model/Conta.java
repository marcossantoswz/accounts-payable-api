package serpro.valida.pagamentos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "contas")
public class Conta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConta;
    private String numeroDocumento;
    private Double valorTotal;
    private String dataVencimento;
    private Double multa;
    private String status; //paga ou naopaga

    @ManyToOne
    @JoinColumn(name = "cliente_cpf", referencedColumnName = "cpf")
    @JsonBackReference
    private Cliente cliente;
}
