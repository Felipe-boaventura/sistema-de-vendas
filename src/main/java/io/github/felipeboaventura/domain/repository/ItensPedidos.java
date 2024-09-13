package io.github.felipeboaventura.domain.repository;

import io.github.felipeboaventura.domain.entity.ItemPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItensPedidos extends JpaRepository<ItemPedido, Integer> {
}
