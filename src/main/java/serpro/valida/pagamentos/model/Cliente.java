package serpro.valida.pagamentos.model;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;


    private Integer idade;

    @Column(unique = true, nullable = false)
    private String cpf;

    private String endereco;

    private Boolean temMulta; 
    
    private Double mediaDarfs;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Conta> contas = new ArrayList<>();

}
