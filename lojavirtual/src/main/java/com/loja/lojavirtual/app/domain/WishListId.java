package com.loja.lojavirtual.app.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class WishListId implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Column(name = "idusuwl")
	private Integer idusuwl;
	
	@Column(name = "idprodutowl")
	private Integer idprodutowl;

	public Integer getIdusuwl() {
		return idusuwl;
	}

	public void setIdusuwl(Integer idusuwl) {
		this.idusuwl = idusuwl;
	}

	public Integer getIdprodutowl() {
		return idprodutowl;
	}

	public void setIdprodutowl(Integer idprodutowl) {
		this.idprodutowl = idprodutowl;
	}
}
