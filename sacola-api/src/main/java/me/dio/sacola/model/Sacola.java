package me.dio.sacola.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.dio.sacola.enumeration.FormaPagamento;

import javax.persistence.*;
import java.util.List;


@AllArgsConstructor //construtor com todos atributos importado do lombok
@Builder //desingner patterns
@Data //getters and insets
@Entity //definindo como entidade
@NoArgsConstructor //constutor sem argumento solicitado pelo hibernet
@JsonIgnoreProperties({"hibernateLazyInitializer", "Handler"})


public class Sacola {
    @Id //id entidade
    @GeneratedValue(strategy = GenerationType.AUTO) //geracao dos atributos
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) //um cliente paramuitas sacolas
    @JsonIgnore
    private Cliente cliente;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Item> itens;
    private Double valorTotal;
    @Enumerated
    private FormaPagamento FormaPagamento;
    private boolean fechada;


}
