package io.github.felipeboaventura.domain.repository;

import io.github.felipeboaventura.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Produtos extends JpaRepository<Produto,Integer> {
}
