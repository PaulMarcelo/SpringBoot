package org.paul.test.springboot.app.repositories;

import org.paul.test.springboot.app.models.Banco;
import org.paul.test.springboot.app.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface BancoRepository extends JpaRepository<Banco, Long> {
//    List<Banco> findAll();
//
//    Banco findById(Long id);
//
//    void update(Banco banco);

}
