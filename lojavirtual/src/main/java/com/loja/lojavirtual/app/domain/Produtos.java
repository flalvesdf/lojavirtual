package com.loja.lojavirtual.app.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "produtos")
public class Produtos {
	
	@Id
	@Column(name="id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="fabricante", nullable = false)
	private String fabricante;
	
	@Column(name="nome", nullable = false)
	private String nome;
	
	@Column(name="descricao", nullable = false)
	private String descricao;
	
	@Column(name="precovenda", nullable = false)
	private Double precoVenda;
	
	@Column(name="precocompra", nullable = false)
	private Double precoCompra;
	
	@Column(name="quantidade", nullable = false)
	private Integer quantidade;
	
	@Column(name="urlimagem", nullable = false)
	private String urlImagem;
	
	@Column(name="ativo", nullable = false)
	private String ativo;
	
	@Column(name="tsatualizacao", nullable = false)
	private Timestamp tsAtualizacao;
	
	@Column(name="produtodestaque", nullable = false)
	private String produtoDestaque;
	
	public String getProdutoDestaque() {
		return produtoDestaque;
	}

	public void setProdutoDestaque(String produtoDestaque) {
		this.produtoDestaque = produtoDestaque;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Double getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(Double precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Double getPrecoCompra() {
		return precoCompra;
	}

	public void setPrecoCompra(Double precoCompra) {
		this.precoCompra = precoCompra;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public String getUrlImagem() {
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	public String getAtivo() {
		return ativo;
	}

	public void setAtivo(String ativo) {
		this.ativo = ativo;
	}

	public Timestamp getTsAtualizacao() {
		return tsAtualizacao;
	}

	public void setTsAtualizacao(Timestamp tsAtualizacao) {
		this.tsAtualizacao = tsAtualizacao;
	}
}
