package com.loja.lojavirtual.app.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="vendas")
public class Venda {
	
	@EmbeddedId
	private VendaId id;
	
	@Column(name = "tsvenda")
	private Timestamp tsVenda;
	
	@Column(name = "valorunidadeproduto")
	private Double valorUnidadeProduto;
	
	@Column(name = "valortotalproduto")
	private Double valorTotalProduto;
	
	@Column(name = "quantidadeproduto")
	private Integer quantidadeProduto;
	
	@Column(name = "tipopagamento")
	private String tipoPagamento;
	
	@Column(name = "vendafinalizada")
	private String vendaFinalizada;
	
	@Transient
	private Produtos produto;
	
	public Produtos getProduto() {
		return produto;
	}

	public void setProduto(Produtos produto) {
		this.produto = produto;
	}

	public String getTipoPagamento() {
		return tipoPagamento;
	}

	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}

	public String getVendaFinalizada() {
		return vendaFinalizada;
	}

	public void setVendaFinalizada(String vendaFinalizada) {
		this.vendaFinalizada = vendaFinalizada;
	}

	public VendaId getId() {
		return id;
	}

	public void setId(VendaId id) {
		this.id = id;
	}

	public Timestamp getTsVenda() {
		return tsVenda;
	}

	public void setTsVenda(Timestamp tsVenda) {
		this.tsVenda = tsVenda;
	}

	public Double getValorUnidadeProduto() {
		return valorUnidadeProduto;
	}

	public void setValorUnidadeProduto(Double valorUnidadeProduto) {
		this.valorUnidadeProduto = valorUnidadeProduto;
	}

	public Double getValorTotalProduto() {
		return valorTotalProduto;
	}

	public void setValorTotalProduto(Double valorTotalProduto) {
		this.valorTotalProduto = valorTotalProduto;
	}

	public Integer getQuantidadeProduto() {
		return quantidadeProduto;
	}

	public void setQuantidadeProduto(Integer quantidadeProduto) {
		this.quantidadeProduto = quantidadeProduto;
	}
}