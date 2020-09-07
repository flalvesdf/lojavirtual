package com.loja.lojavirtual.app.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="wishlist")
public class WishList {
	
	public WishList() {
		super();
	}

	@EmbeddedId
	private WishListId id;
	
	@Column(name = "data")
	private Date data;
	
	@Column(name = "quantidade")
	private Integer quantidade;
	
	@Transient
	private Produtos produto;
	
	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public WishListId getId() {
		return id;
	}

	public void setId(WishListId id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Produtos getProduto() {
		return produto;
	}

	public void setProduto(Produtos produto) {
		this.produto = produto;
	}
}