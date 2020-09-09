package com.loja.lojavirtual.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class CalculoFreteCorreios {

	public static void main(String[] args) {
		
		// Valores de nCdServico (Tipo de encomenda)
				/*
				 * 40010 SEDEX sem contrato 40045 SEDEX a Cobrar, sem contrato 40126
				 * SEDEX a Cobrar, com contrato 40215 SEDEX 10, sem contrato 40290 SEDEX
				 * Hoje, sem contrato 40096 SEDEX com contrato 40436 SEDEX com contrato
				 * 40444 SEDEX com contrato 40568 SEDEX com contrato 40606 SEDEX com
				 * contrato 41106 PAC sem contrato 41068 PAC com contrato 81019 e-SEDEX,
				 * com contrato 81027 e-SEDEX Prioritário, com conrato 81035 e-SEDEX
				 * Express, com contrato 81868 (Grupo 1) e-SEDEX, com contrato 81833
				 * (Grupo 2) e-SEDEX, com contrato 81850 (Grupo 3) e-SEDEX, com contrato
				 */
				
				// Dados pesquisa
				String nCdEmpresa = "";
				String sDsSenha = "";
				String nCdServico ="";
				String tipo="";
				String aux = JOptionPane.showInputDialog(null, "Digite o tipo de envio da encomenda \n1 Sedex, 2 Sedex 10 e 3 Pac");
				
			
				try {
					int codigo = Integer.parseInt(aux);
								
					switch (codigo) {
					
					case 1:
						System.out.println("Opcao 1 - Sedex");
						nCdServico = "04014";
						tipo = "Sedex";
						break;
					case 2:
						System.out.println("Opcao 2 - Sedex 10");
						nCdServico = "40215";
						tipo = "Sedex 10";
						break;
					case 3:
						System.out.println("Opcao 3 - PAC");
						nCdServico = "04510";
						tipo = "PAC";
						break;
					default:
						JOptionPane.showMessageDialog(null, "Codigo desconhecido!");
						System.exit(0);
					}
				}
				catch (NumberFormatException erro) {
					JOptionPane.showMessageDialog(null, "Codigo informado é inválido");
					System.exit(0);
				}
				
				String sCepOrigem = JOptionPane.showInputDialog("Digite o CEP de Origem");
				String sCepDestino = JOptionPane.showInputDialog("Digite o CEP de Destino");
				String pesoPorUnidade = JOptionPane.showInputDialog("Digite o peso por unidade \nEx. 1.230 - nao utilize virgula");
				double pesoPorUnidadeAux = Double.parseDouble(pesoPorUnidade);
				Double calcular ;
				double quantidade = 0; 
				String nVlPesoAux = JOptionPane.showInputDialog("Digite a quantidade a ser enviado");
				quantidade = Double.parseDouble(nVlPesoAux);
				calcular = quantidade * pesoPorUnidadeAux; 
				String nVlPeso = calcular.toString();
				String nCdFormato = "1";
				String nVlComprimento = JOptionPane.showInputDialog("Digite o comprimento da caixa - \nEx. 30");
				String nVlAltura = JOptionPane.showInputDialog("Digite a altura da caixa - \nEx. 30");
				String nVlLargura = JOptionPane.showInputDialog("Digite a largura da caixa - \nEx. 30");
				String nVlDiametro = "0";
				String sCdMaoPropria = "n";
				String nVlValorDeclarado = "0";
				String sCdAvisoRecebimento = "n";
				String StrRetorno = "xml";

				// URL do webservice correio para calculo de frete
				String urlString = "http://ws.correios.com.br/calculador/CalcPrecoPrazo.aspx";

				// os parametros a serem enviados
				Properties parameters = new Properties();

				parameters.setProperty("nCdEmpresa", nCdEmpresa);
				parameters.setProperty("sDsSenha", sDsSenha);
				parameters.setProperty("nCdServico", nCdServico);
				parameters.setProperty("sCepOrigem", sCepOrigem);
				parameters.setProperty("sCepDestino", sCepDestino);
				parameters.setProperty("nVlPeso", nVlPeso);
				parameters.setProperty("nCdFormato", nCdFormato);
				parameters.setProperty("nVlComprimento", nVlComprimento);
				parameters.setProperty("nVlAltura", nVlAltura);
				parameters.setProperty("nVlLargura", nVlLargura);
				parameters.setProperty("nVlDiametro", nVlDiametro);
				parameters.setProperty("sCdMaoPropria", sCdMaoPropria);
				parameters.setProperty("nVlValorDeclarado", nVlValorDeclarado);
				parameters.setProperty("sCdAvisoRecebimento", sCdAvisoRecebimento);
				parameters.setProperty("StrRetorno", StrRetorno);

				// o iterador, para criar a URL
				
				Iterator i = parameters.keySet().iterator();
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
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();

					// seta o metodo
					connection.setRequestProperty("Request-Method", "GET");

					// seta a variavel para ler o resultado
					connection.setDoInput(true);
					connection.setDoOutput(false);

					// conecta com a url destino
					connection.connect();

					// abre a conexão pra input
					BufferedReader br = new BufferedReader(new InputStreamReader(
							connection.getInputStream()));

					// le ate o final
					StringBuffer newData = new StringBuffer();
					String s = "";
					while (null != ((s = br.readLine()))) {
						newData.append(s);
					}
					br.close();

					// Prepara o XML que está em string para executar leitura por nodes
					DocumentBuilder db = DocumentBuilderFactory.newInstance()
							.newDocumentBuilder();
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
						JOptionPane.showMessageDialog(null, "A encomenda enviada por " 
						+ tipo
						+ "\nMedindo: "
						+ nVlComprimento + "x"
						+ nVlAltura + "x" 
						+ nVlLargura 
						+ "cm\nPesando: " 
						+ nVlPeso 
						+ " Kg \nCom prazo de entrega de: "
						+ getCharacterDataFromElement(prazo)
						+ " dia(s) úteis \nFicará em R$"
						+ getCharacterDataFromElement(line));

						NodeList erro = element.getElementsByTagName("MsgErro");
						line = (Element) erro.item(0);
						System.out
								.println("Erro: " + getCharacterDataFromElement(line));
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
		
		
		
	}
	
	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof org.w3c.dom.CharacterData) {
			org.w3c.dom.CharacterData cd = (org.w3c.dom.CharacterData) child;
			return cd.getData();
		}
		return "";
	}
}
