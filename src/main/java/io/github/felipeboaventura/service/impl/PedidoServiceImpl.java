package io.github.felipeboaventura.service.impl;

import io.github.felipeboaventura.REST.dto.ItemPedidoDTO;
import io.github.felipeboaventura.REST.dto.PedidoDTO;
import io.github.felipeboaventura.domain.entity.Cliente;
import io.github.felipeboaventura.domain.entity.ItemPedido;
import io.github.felipeboaventura.domain.entity.Pedido;
import io.github.felipeboaventura.domain.entity.Produto;
import io.github.felipeboaventura.domain.enun.StatusPedido;
import io.github.felipeboaventura.domain.repository.Clientes;
import io.github.felipeboaventura.domain.repository.ItensPedidos;
import io.github.felipeboaventura.domain.repository.Pedidos;
import io.github.felipeboaventura.domain.repository.Produtos;
import io.github.felipeboaventura.exception.PedidoNaoEncontradoException;
import io.github.felipeboaventura.exception.RegraNegocioException;
import io.github.felipeboaventura.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final Pedidos repository;
    private final Clientes clientesRepository;
    private final Produtos produtosRepository;
    private final ItensPedidos itensPedidosRepository;


    @Override
    @Transactional
    public Pedido salvar(PedidoDTO dto) {
        Integer idCliente = dto.getCliente();
        Cliente cliente = clientesRepository
                .findById(idCliente)
                .orElseThrow(() -> new RegraNegocioException("Código de cliente invalido."));

        Pedido pedido = new Pedido();
        pedido.setTotal(dto.getTotal());
        pedido.setDataPedido(LocalDate.now());
        pedido.setCliente(cliente);
        pedido.setStatus(StatusPedido.REALIZADO);

        List<ItemPedido> itensPedidos = converterItens(pedido, dto.getItens());
        repository.save(pedido);
        itensPedidosRepository.saveAll(itensPedidos);
        pedido.setItems(itensPedidos);

        return pedido;
    }

    @Override
    public Optional<Pedido> obterPedidoCompleto(Integer id) {
        return repository.idItens(id);
    }

    @Override
    @Transactional
    public void atualizarStatus(Integer id, StatusPedido statusPedido) {
        repository.findById(id).map(pedido -> {
            pedido.setStatus(statusPedido);
            return repository.save(pedido);
        }).orElseThrow(() -> new PedidoNaoEncontradoException());
    }

    private List<ItemPedido> converterItens(Pedido pedido, List<ItemPedidoDTO> itens) {
        if (itens.isEmpty()) {
            throw new RegraNegocioException("Não é possivel realizar um pedido sem itens");
        }
        return itens
                .stream()
                .map(dto -> {
                    Integer idProduto = dto.getProduto();
                    Produto produto = produtosRepository
                            .findById(idProduto)
                            .orElseThrow(
                                    () -> new RegraNegocioException("Código do produto invalido: " + idProduto));

                    ItemPedido itemPedido = new ItemPedido();
                    itemPedido.setQuantidade(dto.getQuantidade());
                    itemPedido.setPedido(pedido);
                    itemPedido.setProduto(produto);
                    return itemPedido;

                }).collect(Collectors.toList());
    }
}
