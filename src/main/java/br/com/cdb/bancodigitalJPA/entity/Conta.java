package br.com.cdb.bancodigitalJPA.entity;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tipo_de_conta")
public abstract class Conta {							
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) //ID vai ser incremento (1 a 1)
	private Long id;
						//se eu deletar a conta, ele já deleta, ai é menos dor de cabeça
	@OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<SaldoMoeda> saldos = new ArrayList<>();
	
	


	//ManyToOne - posso ter mais de uma conta, mas todas elas vão ter que ser relacionadas a um só objeto
	//o cliente :)
	//O JoinColumn é responsável por criar a Foreing Key
	@ManyToOne
	@JoinColumn(name = "cliente_id")		
	@JsonBackReference
	private Cliente cliente;
	//esse atributo eu mapeio ele no cliente, depois passo o nome dele no Cliente
	

	@OneToMany(mappedBy = "conta", fetch = FetchType.EAGER)
	@JsonManagedReference 		
	private List<Cartao> cartoes;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public List<Cartao> getCartoes() {
		return cartoes;
	}

	public void setCartao(List<Cartao> cartoes) {
		this.cartoes = cartoes;
	}
	
	public List<SaldoMoeda> getSaldos() {
		return saldos;
	}

	public void setSaldos(List<SaldoMoeda> saldos) {
		this.saldos = saldos;
	}

	
	@JsonProperty		//JsonProperty garante que ele vá aparecer no PostMan
	public String getTipoDeConta() {
		return this.getClass().getSimpleName().replace("Conta", "");
	}
}
