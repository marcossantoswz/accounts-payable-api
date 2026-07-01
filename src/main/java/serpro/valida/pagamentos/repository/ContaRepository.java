package serpro.valida.pagamentos.repository;

import serpro.valida.pagamentos.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {
    
    Optional<Conta> findByIdConta(Long idConta);
    Boolean existsByIdConta(Long idConta);
}