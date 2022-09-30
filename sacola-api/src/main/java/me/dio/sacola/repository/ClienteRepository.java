package me.dio.sacola.repository;


import me.dio.sacola.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository //informando que se conecta com DB
//METODOS DO JPA

//EXTENDER: POLIFORMISMO

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
