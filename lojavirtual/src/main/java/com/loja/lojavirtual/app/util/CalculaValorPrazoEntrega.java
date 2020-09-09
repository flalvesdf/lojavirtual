package com.loja.lojavirtual.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.loja.lojavirtual.app.domain.CalculadoraCorreios;
import com.loja.lojavirtual.app.domain.Produtos;

public class CalculaValorPrazoEntrega {
	
	public static final String URL_WEBSERVICE_CORREIOS = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx";
	
	public static CalculadoraCorreios preparaObjetoParaCalculo(List<Produtos> produtos, String cepComprador) {
		CalculadoraCorreios calc = new CalculadoraCorreios();
		calc.setsCepDestino(cepComprador);
		
		Double somaPesos = 0.0;
		Integer somaAlturas = 0;
		Integer somaComprimentos = 0;
		Integer somaLarguras = 0;
		
		for (Produtos produto : produtos) {
			somaAlturas += (produto.getAltura()* produto.getQuantidade());
			somaComprimentos += (produto.getComprimento()* produto.getQuantidade());
			somaLarguras += (produto.getLargura()* produto.getQuantidade());
			somaPesos += (produto.getPeso()* produto.getQuantidade());
		}
		/*identificando os valores minimos e máximos de um pacote para os correios:
		 * https://www.correios.com.br/enviar-e-receber/precisa-de-ajuda/limites-de-dimensoes-e-peso
		 * 
		 *   Pacote e Caixa 	          Mínimo	Máximo
			Comprimento (C) 			  	15 cm	100 cm
			Largura (L)	        			10 cm	100 cm
			Altura (A)	         			 1 cm	100 cm
			Soma das dimensões: C + L + A	26 cm	200 cm
		 * 
		 * */
		Integer somaDimensoes = (somaAlturas + somaLarguras + somaComprimentos);
		
		if(somaDimensoes > 200) {
			Integer diferenca = somaDimensoes - 200;
			
			if(somaLarguras > somaComprimentos) {
				somaLarguras = somaLarguras - diferenca;
			}else {
				somaComprimentos =  somaComprimentos - diferenca;
			}
		}else if(somaDimensoes < 26){
			somaAlturas= 5;
			somaComprimentos = 15;
			somaLarguras = 10;
		}
		 
		if(somaPesos < 0.01 || somaPesos > 30) {
			//ajustar
			if(somaPesos < 0.01) somaPesos = 0.1;
			if(somaPesos > 30) somaPesos = 29.9;
		}
		
		if(somaComprimentos < 15 || somaComprimentos > 100) {
			if(somaComprimentos < 15) somaComprimentos = 15;
			if(somaComprimentos > 100) somaComprimentos = 100;
		}
		
		if(somaAlturas < 1 || somaAlturas > 100) {
			//ajustar
			if(somaAlturas < 1) somaAlturas = 1;
			if(somaAlturas > 100) somaAlturas = 100;
		}
		
		if(somaLarguras < 10 || somaLarguras > 100) {
			//ajustar
			if(somaLarguras < 10) somaLarguras = 10;
			if(somaLarguras > 100) somaLarguras = 100;
		}
		
		//if(somaDimensoes < 26 || somaDimensoes > 200) {
			//ajustar
		//}
		
//		if(somaPesos < 0.1) {
//			somaPesos = 0.1;
//		}
//		
//		if(somaAlturas < 20) {
//			somaAlturas = 20;
//		}
//		
//		if(somaComprimentos < 20) {
//			somaComprimentos = 20;
//		}
//		
//		if(somaLarguras < 20) {
//			somaLarguras = 20;
//		}
		
		//maior que 1 kg tem q ser por sedex
//		if(somaPesos>1.0) {
//			calc.setnCdServico(Utilitarios.SEDEX);
//		}
		
		calc.setnVlAltura(String.valueOf(somaAlturas));
		calc.setnVlComprimento(String.valueOf(somaComprimentos));
		calc.setnVlLargura(String.valueOf(somaLarguras));
		calc.setnVlPeso(String.valueOf(somaPesos));
		
		
		
		//calc.set
		return calc;
	}
			
	public static CalculadoraCorreios calculaPrazoEntregaValor(CalculadoraCorreios calc) {
		
		String urlString = URL_WEBSERVICE_CORREIOS;
		
		Properties parameters = setParameters(calc);
		
		// o iterador, para criar a URL
		
		Iterator<Object> i = parameters.keySet().iterator();
		// o contador
		int counter = 0;

		// enquanto ainda existir parametros
		while (i.hasNext()) {
			// pega o nome
			String name = (String) i.next();
			// pega o valor
			String value = parameters.getProperty(name);
			// adiciona com um conector (? ou &)
			// o primeiro é ?, depois são &
			urlString += (++counter == 1 ? "?" : "&") + name + "=" + value;

		}
		
		try {
			// cria o objeto url
			URL url = new URL(urlString);

			// cria o objeto httpurlconnection
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			// seta o metodo
			connection.setRequestProperty("Request-Method", "GET");

			// seta a variavel para ler o resultado
			connection.setDoInput(true);
			connection.setDoOutput(false);

			// conecta com a url destino
			connection.connect();

			// abre a conexão pra input
			BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

			// le ate o final
			StringBuffer newData = new StringBuffer();
			String s = "";
			while (null != ((s = br.readLine()))) {
				newData.append(s);
			}
			br.close();

			// Prepara o XML que está em string para executar leitura por nodes
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			is.setCharacterStream(new StringReader(newData.toString()));
			Document doc = db.parse(is);
			NodeList nodes = doc.getElementsByTagName("cServico");
			
			// Faz a leitura dos nodes
			for (int j = 0; j < nodes.getLength(); j++) {
				Element element = (Element) nodes.item(j);

				NodeList valor = element.getElementsByTagName("Valor");
				NodeList prazoEntrega = element.getElementsByTagName("PrazoEntrega");
				
				Element line = (Element) valor.item(0);
				Element prazo = (Element)prazoEntrega.item(0);
				
				calc.setPrazoEntrega(getCharacterDataFromElement(prazo));
				
				String valorRetornado = getCharacterDataFromElement(line);
				valorRetornado = valorRetornado.replace(",", "."); 
				
				calc.setValorFrete(valorRetornado);
				
				NodeList erro = element.getElementsByTagName("MsgErro");
				line = (Element) erro.item(0);
				
				calc.setMsgErro("Erro: " + getCharacterDataFromElement(line));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return calc;
	}
	
	private static Properties setParameters(CalculadoraCorreios calc) {
		Properties parameters = new Properties();
		
		//Double calcular ;
		//double quantidade = 0; 
//		double pesoPorUnidadeAux = Double.parseDouble(calc.getnVlPeso());
//		quantidade = Double.parseDouble(calc.getnVlPeso());
//		calcular = quantidade * pesoPorUnidadeAux; 
		
		//calc.setnVlPeso(calcular.toString());

		parameters.setProperty("nCdEmpresa", calc.getnCdEmpresa());
		parameters.setProperty("sDsSenha", calc.getsDsSenha());
		parameters.setProperty("nCdServico", calc.getnCdServico());
		parameters.setProperty("sCepOrigem", calc.getsCepOrigem());
		parameters.setProperty("sCepDestino", calc.getsCepDestino());
		parameters.setProperty("nVlPeso", calc.getnVlPeso());
		parameters.setProperty("nCdFormato", calc.getnCdFormato());
		parameters.setProperty("nVlComprimento", calc.getnVlComprimento());
		parameters.setProperty("nVlAltura", calc.getnVlAltura());
		parameters.setProperty("nVlLargura", calc.getnVlLargura());
		parameters.setProperty("nVlDiametro", calc.getnVlDiametro());
		parameters.setProperty("sCdMaoPropria", calc.getsCdMaoPropria());
		parameters.setProperty("nVlValorDeclarado", calc.getnVlValorDeclarado());
		parameters.setProperty("sCdAvisoRecebimento", calc.getsCdAvisoRecebimento());
		parameters.setProperty("StrRetorno", calc.getStrRetorno());
		
		return parameters;
	}
	
	private static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof org.w3c.dom.CharacterData) {
			org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
			return cd.getData();
		}
		return "";
	}
}