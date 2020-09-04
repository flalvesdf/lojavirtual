package com.loja.lojavirtual.app.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class VendaId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "id")
	private Integer id;
	
	@Column(name = "idusuario")
	private Integer idUsuario;
	
	@Column(name = "idproduto")
	private Integer idProduto;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Integer getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Integer idProduto) {
		this.idProduto = idProduto;
	}
}
