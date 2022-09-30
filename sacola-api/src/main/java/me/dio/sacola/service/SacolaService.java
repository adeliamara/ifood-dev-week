package me.dio.sacola.service;

import me.dio.sacola.model.Item;
import me.dio.sacola.model.Sacola;
import me.dio.sacola.resource.ItemDto;

public interface SacolaService {
    Item incluirItemNaSacola(ItemDto itemDto);
    //observe NA ALINHA ACIMA que nesse caso temos que passar produto e sacola que sao objetos, por isso teremos que usar DTO

    Sacola verSacola(Long id);

    Sacola fecharSacola(Long id, int FormaPagamento);
}
