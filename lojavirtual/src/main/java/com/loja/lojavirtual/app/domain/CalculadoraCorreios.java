package com.loja.lojavirtual.app.domain;

import com.loja.lojavirtual.app.util.Utilitarios;

public class CalculadoraCorreios {
	
	/*Seu código administrativo junto à ECT. O código está disponível no corpo do contrato firmado com os
	Correios.*/
	private String nCdEmpresa = "";
	
	/*Senha para acesso ao serviço, associada ao seu código administrativo. A senha inicial corresponde aos
	8 primeiros dígitos do CNPJ informado no contrato */
	private String sDsSenha = "";
	
	/*Para clientes sem contrato:
	Códigos Vigentes:
	Código Serviço
	04014 SEDEX à vista
	04510 PAC à vista
	04782 SEDEX 12 ( à vista)
	04790 SEDEX 10 (à vista)
	04804 SEDEX Hoje à vista
	Para clientes com contrato:
	Consultar os códigos no seu contrato.*/
	private String nCdServico = Utilitarios.PAC;
	
	/*CEP de Origem sem hífen.Exemplo: 05311900*/
	private String sCepOrigem = Utilitarios.CEP_ORIGEM;
	
	/*CEP de Destino sem hífen */
	private String sCepDestino;
	
	/*Peso da encomenda, incluindo sua embalagem. O	peso deve ser informado em quilogramas. Se o
	formato for Envelope, o valor máximo permitido será 1 kg.*/
	private String nVlPeso;
	
	/*Formato da encomenda (incluindo embalagem).
	Valores possíveis: 1, 2 ou 3
	1 – Formato caixa/pacote
	2 – Formato rolo/prisma
	3 - Envelope*/
	private String nCdFormato = "1";
	
	/*Comprimento da encomenda (incluindo embalagem),em centímetros.*/
	private String nVlComprimento;
	
	/*Altura da encomenda (incluindo embalagem), em	centímetros. 
	 * Se o formato for envelope, informar zero	(0).*/
	private String nVlAltura;
	
	/*Largura da encomenda (incluindo embalagem), em centímetros.*/
	private String nVlLargura;
	
	/*Diâmetro da encomenda (incluindo embalagem), em centímetros.*/
	private String nVlDiametro = "0";
	
	/*Indica se a encomenda será entregue com o serviço	adicional mão própria.
	Valores possíveis: S ou N (S – Sim, N – Não)*/
	private String sCdMaoPropria = "N";
	
	/*Indica se a encomenda será entregue com o serviço	adicional valor declarado. Neste campo deve ser
	apresentado o valor declarado desejado, em Reais. Se não optar pelo	serviço informar zero.*/
	private String nVlValorDeclarado = "0";
	
	/*Indica se a encomenda será entregue com o serviço	adicional aviso de recebimento.
	Valores possíveis: S ou N (S – Sim, N – Não)
	Se não optar pelo serviço informar ‘N’*/
	private String sCdAvisoRecebimento = "N";
	
	/*variavel do retorno XML*/
	private String StrRetorno = "xml";
	
	/*variavel do prazo de entrega*/
	private String prazoEntrega;
	
	/*variavel do valor do frete*/
	private String valorFrete;
	
	/*variavel para mensagem de erro do webservice dos correios*/
	private String msgErro;
	
	public String getMsgErro() {
		return msgErro;
	}

	public void setMsgErro(String msgErro) {
		this.msgErro = msgErro;
	}

	public String getPrazoEntrega() {
		return prazoEntrega;
	}

	public void setPrazoEntrega(String prazoEntrega) {
		this.prazoEntrega = prazoEntrega;
	}

	public String getValorFrete() {
		return valorFrete;
	}

	public void setValorFrete(String valorFrete) {
		this.valorFrete = valorFrete;
	}

	public String getStrRetorno() {
		return StrRetorno;
	}

	public void setStrRetorno(String strRetorno) {
		StrRetorno = strRetorno;
	}

	public String getnCdEmpresa() {
		return nCdEmpresa;
	}

	public void setnCdEmpresa(String nCdEmpresa) {
		this.nCdEmpresa = nCdEmpresa;
	}

	public String getsDsSenha() {
		return sDsSenha;
	}

	public void setsDsSenha(String sDsSenha) {
		this.sDsSenha = sDsSenha;
	}

	public String getnCdServico() {
		return nCdServico;
	}

	public void setnCdServico(String nCdServico) {
		this.nCdServico = nCdServico;
	}

	public String getsCepOrigem() {
		return sCepOrigem;
	}

	public void setsCepOrigem(String sCepOrigem) {
		this.sCepOrigem = sCepOrigem;
	}

	public String getsCepDestino() {
		return sCepDestino;
	}

	public void setsCepDestino(String sCepDestino) {
		this.sCepDestino = sCepDestino;
	}

	public String getnVlPeso() {
		return nVlPeso;
	}

	public void setnVlPeso(String nVlPeso) {
		this.nVlPeso = nVlPeso;
	}

	public String getnCdFormato() {
		return nCdFormato;
	}

	public void setnCdFormato(String nCdFormato) {
		this.nCdFormato = nCdFormato;
	}

	public String getnVlComprimento() {
		return nVlComprimento;
	}

	public void setnVlComprimento(String nVlComprimento) {
		this.nVlComprimento = nVlComprimento;
	}

	public String getnVlAltura() {
		return nVlAltura;
	}

	public void setnVlAltura(String nVlAltura) {
		this.nVlAltura = nVlAltura;
	}

	public String getnVlLargura() {
		return nVlLargura;
	}

	public void setnVlLargura(String nVlLargura) {
		this.nVlLargura = nVlLargura;
	}

	public String getnVlDiametro() {
		return nVlDiametro;
	}

	public void setnVlDiametro(String nVlDiametro) {
		this.nVlDiametro = nVlDiametro;
	}

	public String getsCdMaoPropria() {
		return sCdMaoPropria;
	}

	public void setsCdMaoPropria(String sCdMaoPropria) {
		this.sCdMaoPropria = sCdMaoPropria;
	}

	public String getnVlValorDeclarado() {
		return nVlValorDeclarado;
	}

	public void setnVlValorDeclarado(String nVlValorDeclarado) {
		this.nVlValorDeclarado = nVlValorDeclarado;
	}

	public String getsCdAvisoRecebimento() {
		return sCdAvisoRecebimento;
	}

	public void setsCdAvisoRecebimento(String sCdAvisoRecebimento) {
		this.sCdAvisoRecebimento = sCdAvisoRecebimento;
	}
}
