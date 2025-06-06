package br.com.cdb.bancodigitalJPA.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "idCliente")
public class ClienteSuper extends Cliente {

	private BigDecimal limiteCredito = BigDecimal.valueOf(5000);
	
	@Column(precision = 4, scale = 3)
	private BigDecimal taxaRendimento = BigDecimal.valueOf(0.007);
	
	private BigDecimal taxaManutencao = BigDecimal.valueOf(8);
	
	private BigDecimal valorApolice = BigDecimal.valueOf(50);
	
	
	@Override
	public BigDecimal getTaxaManutencao() {
		return taxaManutencao;
	}

	@Override
	public BigDecimal getTaxaRendimento() {
		return taxaRendimento;
	}

	@Override
	public BigDecimal getLimiteCredito() {
		return limiteCredito;
	}
	
	public void setLimiteCredito(BigDecimal limiteCredito) {
		this.limiteCredito = limiteCredito;
	}

	public void setTaxaRendimento(BigDecimal taxaRendimento) {
		this.taxaRendimento = taxaRendimento;
	}

	public void setTaxaManutencao(BigDecimal taxaManutencao) {
		this.taxaManutencao = taxaManutencao;
	}
	
	public BigDecimal getValorApolice() {
		return valorApolice;
	}

	public void setValorApolice(BigDecimal valorApolice) {
		this.valorApolice = valorApolice;
	}

}
