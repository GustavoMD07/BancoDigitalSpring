package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Seguro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String numeroApolice;
	private LocalDate dataContratacao;
	private String tipoDeSeguro;
	private String descricao;
	private BigDecimal valorApolice;
	private boolean ativo;
	
	@ManyToOne
	@JoinColumn(name = "cartao_id") //mapeia a FK com o nome de cartao_id
	@JsonBackReference
	private CartaoCredito cartao;
	
}
