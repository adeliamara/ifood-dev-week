package me.dio.sacola.service.impl;

import lombok.RequiredArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;
import me.dio.sacola.model.Item;
import me.dio.sacola.model.Restaurante;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.repository.ItemRepository;
import me.dio.sacola.repository.ProdutoRepository;
import me.dio.sacola.repository.SacolaRepository;
import me.dio.sacola.resource.ItemDto;
import me.dio.sacola.service.SacolaService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SacolaServiceImpl implements SacolaService {
    private final SacolaRepository sacolaRepository;
    private final ProdutoRepository produtoRepository;
    private  final ItemRepository itemRepository;
    @Override
    public Item incluirItemNaSacola(ItemDto itemDto) {
        Sacola sacola = verSacola(itemDto.getSacolaId());

        if(sacola.isFechada()){
            throw new RuntimeException("Essa sacola está fechada!");
        }

        //traz designer pattern 1:13h
        Item itemParaSerInserido = Item.builder()
                .quandidade(itemDto.getQuantidade())
                .sacola(sacola)
                .produto(produtoRepository.findById(itemDto.getProdutoId()).orElseThrow(
                        () -> {
                            throw new RuntimeException("Esse produto não existe!");
                        }
                ))
                .build();

        List<Item> itensDaSacola = sacola.getItens();

        if(itensDaSacola.isEmpty()){
            itensDaSacola.add(itemParaSerInserido);
        }else{
            Restaurante restauranteAtual = itensDaSacola.get(0).getProduto().getRestaurante();
            Restaurante restauranteNovoItem = itemParaSerInserido.getProduto().getRestaurante();

            if(restauranteAtual.equals(restauranteNovoItem)){
                itensDaSacola.add(itemParaSerInserido);
            }else{
                             throw new RuntimeException("Não é possível adicionar produtos de restaurentes diferentes. Feche a sacola ou esvazie!");
            }
        }

        List<Double> valorDosItens = new ArrayList<>();

        for (Item itemDaSacola: itensDaSacola) {
            double valorDoItem =
                    itemDaSacola.getProduto().getValorUnitario() * itemDaSacola.getQuandidade();
            valorDosItens.add(valorDoItem);
        }

        double valorTotalSacola = valorDosItens.stream()
                .mapToDouble(valorTotalDeCadaItem -> valorTotalDeCadaItem)
                .sum();

        sacola.setValorTotal(valorTotalSacola);
        sacolaRepository.save(sacola);

        return itemParaSerInserido;
    }

    @Override
    public Sacola verSacola(Long id) {
        return sacolaRepository.findById(id).orElseThrow(
                () -> {
                    throw new RuntimeException("Essa sacola nao existe!");
                }
        );
    }

    @Override
    public Sacola fecharSacola(Long id, int numeroFormaPagamento) {
        Sacola sacola = verSacola(id);

        if(sacola.getItens().isEmpty()){
            throw new RuntimeException("Inclua itens na sacola!");
        }

        FormaPagamento formaPagamento =
            numeroFormaPagamento == 0 ? FormaPagamento.DINHEIRO : FormaPagamento.MAQUINETA;

        sacola.setFormaPagamento(formaPagamento);
        sacola.setFechada(true);

        return sacolaRepository.save(sacola);

    }
}
