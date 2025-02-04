package io.github.felipeboaventura.REST.controller;

import io.github.felipeboaventura.REST.dto.AtualizacaoStatusPedidoDTO;
import io.github.felipeboaventura.REST.dto.InformacoesItensPedidosDTO;
import io.github.felipeboaventura.REST.dto.InformacoesPedidosDTO;
import io.github.felipeboaventura.REST.dto.PedidoDTO;
import io.github.felipeboaventura.domain.entity.ItemPedido;
import io.github.felipeboaventura.domain.entity.Pedido;
import io.github.felipeboaventura.domain.enun.StatusPedido;
import io.github.felipeboaventura.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {

    private PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(ACCEPTED)
    public Integer save(@RequestBody @Valid PedidoDTO dto) {
        Pedido pedido = service.salvar(dto);
        return pedido.getId();
    }

    @GetMapping("{id}")
    public InformacoesPedidosDTO getById(@PathVariable Integer id) {
        return service
                .obterPedidoCompleto(id)
                .map(p -> converter(p))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Pedido não encontrado."));
    }

    @PatchMapping("{id}")
    @ResponseStatus(NO_CONTENT)
    public void updateStatus(@RequestBody @Valid AtualizacaoStatusPedidoDTO dto,
                             @PathVariable Integer id) {
        String novoStatus = dto.getNovoStatus();
        service.atualizarStatus(id, StatusPedido.valueOf(novoStatus));
    }

    private InformacoesPedidosDTO converter(Pedido p) {
        return InformacoesPedidosDTO
                .builder()
                .codigo(p.getId())
                .dataPedido(p.getDataPedido().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .cpf(p.getCliente().getCpf())
                .nomeCliente(p.getCliente().getNome())
                .total(p.getTotal())
                .status(p.getStatus().name())
                .itens(converter(p.getItems()))
                .build();
    }

    private List<InformacoesItensPedidosDTO> converter(List<ItemPedido> itens) {
        if (CollectionUtils.isEmpty(itens)) {
            return Collections.emptyList();
        }

        return itens.stream()
                .map(
                        item -> InformacoesItensPedidosDTO.builder()
                                .descricao(item.getProduto().getDescricao())
                                .precoUnitario(item.getProduto().getPreco())
                                .quantidade(item.getQuantidade())
                                .build()
                ).collect(Collectors.toList());
    }
}
