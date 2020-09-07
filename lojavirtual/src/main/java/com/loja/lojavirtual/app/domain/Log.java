package com.loja.lojavirtual.app.domain;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="log")
public class Log {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "idprodutolog")// se ZERO, refere-se a log de alteracao de dados pessoais
	private Integer idProdutoLog;
	
	@Column(name = "idusuariolog")// se ZERO, refere-se a consulta sem usuário logado
	private Integer idUsuarioLog;
	
	@Column(name = "datahoralog")
	private Timestamp dataHora;
	
	@Column(name = "observacao")
	private String observacao;
	
	@Column(name = "tipolog")
	private String tipoLog;//A - log de visitas de produtos B - log de alteracao de dados pessoais

	@Column(name = "ip")
	private String ip;
	
	@Column(name = "secao")
	private String secao;
	
	public String getSecao() {
		return secao;
	}

	public void setSecao(String secao) {
		this.secao = secao;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdProdutoLog() {
		return idProdutoLog;
	}

	public void setIdProdutoLog(Integer idProdutoLog) {
		this.idProdutoLog = idProdutoLog;
	}

	public Integer getIdUsuarioLog() {
		return idUsuarioLog;
	}

	public void setIdUsuarioLog(Integer idUsuarioLog) {
		this.idUsuarioLog = idUsuarioLog;
	}

	public Timestamp getDataHora() {
		return dataHora;
	}

	public void setDataHora(Timestamp dataHora) {
		this.dataHora = dataHora;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getTipoLog() {
		return tipoLog;
	}

	public void setTipoLog(String tipoLog) {
		this.tipoLog = tipoLog;
	}
	
	public Log() {
		
	}
	
	public Log(Integer idProdutoLog, Integer idUsuarioLog, Timestamp dataHora, String observacao, String tipoLog,
			String ip, String secao) {
		this.idProdutoLog = idProdutoLog;
		this.idUsuarioLog = idUsuarioLog;
		this.dataHora = dataHora;
		this.observacao = observacao;
		this.tipoLog = tipoLog;
		this.ip = ip;
		this.secao = secao;
	}
}