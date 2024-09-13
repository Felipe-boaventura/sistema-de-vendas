package io.github.felipeboaventura.service;

import io.github.felipeboaventura.REST.dto.PedidoDTO;
import io.github.felipeboaventura.domain.entity.Pedido;
import io.github.felipeboaventura.domain.enun.StatusPedido;

import java.util.Optional;

public interface PedidoService {

    Pedido salvar(PedidoDTO dto);

    Optional<Pedido> obterPedidoCompleto(Integer id);

    void atualizarStatus(Integer id, StatusPedido statusPedido);
}
