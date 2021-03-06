package com.loja.lojavirtual.app.util;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import com.loja.lojavirtual.app.domain.CalculadoraCorreios;
import com.loja.lojavirtual.app.domain.Produtos;
import com.loja.lojavirtual.app.domain.Usuario;
import com.loja.lojavirtual.app.domain.Venda;
import com.loja.lojavirtual.app.domain.WishList;

public class ConstrutorExibicaoProduto {
	
	public static String exibeProdutoDestaque(Produtos produto) {

		StringBuffer sb = new StringBuffer();
		
		sb.append("<div class=\"hero__item set-bg\" data-setbg=\""+produto.getUrlImagem()+"\">");
		sb.append("<div class=\"hero__text\">");
		sb.append("<span>"+produto.getCategoriaProduto().getNome()+"</span>");
		sb.append("<h2>"+produto.getNome()+"</h2>");
		sb.append("<p>"+produto.getDescricao()+"</p>");
		
		sb.append("<a href=\"addCarrinho\" class=\"primary-btn\">Comprar</a>&nbsp;&nbsp;");
		sb.append("<a href=\"salvarNaWishList\" class=\"primary-btn\">Salvar</a>");
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();

	}

	public static String exibeProduto(Produtos produto) {

		StringBuffer sb = new StringBuffer();

		sb.append("<div class=\"col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat\">");
		sb.append("<div class=\"featured__item\">");
		sb.append("<div class=\"featured__item__pic set-bg\" data-setbg=\""+produto.getUrlImagem()+"\">");
		sb.append("<ul class=\"featured__item__pic__hover\">");
		sb.append("<li><a href=\"javascript:setarValor("+produto.getId()+");\" title=\"Adicionar a sua lista de desejos\"><i class=\"fa fa-heart\"></i></a></li>");
		sb.append("<li><a href=\"javascript:setarItemCarrinho("+produto.getId()+");\" title=\"Adicionar ao carrinho de compras\"><i class=\"fa fa-shopping-cart\"></i></a></li>");
		//sb.append("<li><a href=\"#\"><i class=\"fa fa-shopping-cart\"></i></a></li>");
		sb.append("</ul>");
		sb.append("</div>");
		sb.append(" <div class=\"featured__item__text\">");
		sb.append("<h6><a href=\"#\">"+produto.getNome()+"</a></h6>");
		sb.append("<h5>"+NumberFormat.getCurrencyInstance().format(produto.getPrecoVenda())+"</h5>");
		sb.append("</div>");
		sb.append("</div>");
		sb.append("</div>");

		return sb.toString();

	}

	public static String exibeProdutos(List<Produtos> produtos) {

		StringBuffer sb = new StringBuffer();

		for(Produtos produto: produtos) {
			sb.append("<div class=\"col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat\">");
			sb.append("<div class=\"featured__item\">");
			sb.append("<div class=\"featured__item__pic set-bg\" data-setbg=\""+produto.getUrlImagem()+"\">");
			sb.append("<ul class=\"featured__item__pic__hover\">");
			sb.append("<li><a href=\"javascript:setarValor("+produto.getId()+");\" title=\"Adicionar a sua lista de desejos\"><i class=\"fa fa-heart\"></i></a></li>");
			sb.append("<li><a href=\"javascript:setarItemCarrinho("+produto.getId()+");\" title=\"Adicionar ao carrinho de compras\"><i class=\"fa fa-shopping-cart\"></i></a></li>");
			//sb.append("<li><a href=\"#\"><i class=\"fa fa-shopping-cart\"></i></a></li>");
			sb.append("</ul>");
			sb.append("</div>");
			sb.append(" <div class=\"featured__item__text\">");
			sb.append("<h6><a href=\"#\">"+produto.getNome()+"</a></h6>");
			sb.append("<h5>"+NumberFormat.getCurrencyInstance().format(produto.getPrecoVenda())+"</h5>");
			sb.append("</div>");
			sb.append("</div>");
			sb.append("</div>");
		}
		
		return sb.toString();

	}

	public static String exibirWishList(List<WishList> wl, Usuario comprador) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th class=\"shoping__product\">Produtos</th>");
		sb.append("<th>Preço</th>");
		sb.append("<th>Quantidade</th>");
		sb.append("<th>Total</th>");
		sb.append("<th></th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		List<Produtos> lsProdutos = new ArrayList<Produtos>();
		Double valorFinal = 0.0;
		
		for(WishList wish:wl) {

			sb.append("<tr>");
			sb.append("<td class=\"shoping__cart__item\">");
			sb.append("<img src=\""+wish.getProduto().getUrlImagem()+"\" alt=\"\">");
			sb.append("<h5>"+wish.getProduto().getNome()+"</h5>");
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__price\">");
			sb.append(NumberFormat.getCurrencyInstance().format(wish.getProduto().getPrecoVenda()));
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__quantity\">");
			sb.append("<div class=\"quantity\">");
			sb.append("<div class=\"pro-qty\">");
			sb.append("<input type=\"text\" value=\""+wish.getQuantidade()+"\">");
			sb.append("</div>");
			sb.append(" </div>");
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__total\">");
			sb.append(NumberFormat.getCurrencyInstance().format(wish.getProduto().getPrecoVenda() * wish.getQuantidade()));
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__item__close\">");
			sb.append("<span class=\"icon_close\"></span>&nbsp;");
			sb.append("<a href=\"#\"><i class=\"fa fa-shopping-bag\"></i></a>");
			sb.append("</td>");
			sb.append("</tr>");

			valorFinal += (wish.getProduto().getPrecoVenda() * wish.getQuantidade());
			wish.getProduto().setQuantidade(wish.getQuantidade());
			lsProdutos.add(wish.getProduto());
		}

		
		CalculadoraCorreios calculo = CalculaValorPrazoEntrega.preparaObjetoParaCalculo(lsProdutos, comprador.getCep());
		calculo = CalculaValorPrazoEntrega.calculaPrazoEntregaValor(calculo);
		valorFinal += Double.parseDouble(calculo.getValorFrete());
		String dimensoes = "Peso: "+calculo.getnVlPeso() + " / Altura: "+calculo.getnVlAltura() + " / Largura: "+ calculo.getnVlLargura() + " / Comprimento: "+ calculo.getnVlComprimento();
		//System.out.print(dimensoes);
		sb.append("<tr><td class=\"shoping__cart__price\" colspan=\"5\" align=\"right\">Entrega para o CEP "+comprador.getCep()+" : "+ calculo.getPrazoEntrega() + " dias. Valor transporte: " +NumberFormat.getCurrencyInstance().format(Double.parseDouble(calculo.getValorFrete())) + "</td></tr>");
		sb.append("<tr><td class=\"shoping__cart__price\" colspan=\"5\" align=\"right\">Mensagem Correios: "+ calculo.getMsgErro() + dimensoes +" </td></tr>");
		sb.append("<tr><td class=\"shoping__cart__price\" colspan=\"5\" align=\"right\">"+NumberFormat.getCurrencyInstance().format(valorFinal) + "</td></tr>");

		sb.append("</tbody>");
		sb.append("</table>");

		return sb.toString();
	}
	
	public static String checkout(List<Venda> itens, Usuario comprador) {
		StringBuffer sb = new StringBuffer();
		
		sb.append("<ul>");
		Double valorFinal = 0.0;
		List<Produtos> lsProdutos = new ArrayList<Produtos>();
		
		for(Venda item: itens) {
			sb.append("<li>(" +item.getQuantidadeProduto() +") "+item.getProduto().getNome()+"<span>"+NumberFormat.getCurrencyInstance().format(item.getProduto().getPrecoVenda())+"</span></li>");
			valorFinal += (item.getProduto().getPrecoVenda() * item.getQuantidadeProduto());
			item.getProduto().setQuantidade(item.getQuantidadeProduto());
			lsProdutos.add(item.getProduto());
		}
		
		sb.append("</ul>");
		
		CalculadoraCorreios calculo = CalculaValorPrazoEntrega.preparaObjetoParaCalculo(lsProdutos, comprador.getCep());
		calculo = CalculaValorPrazoEntrega.calculaPrazoEntregaValor(calculo);
		valorFinal += Double.parseDouble(calculo.getValorFrete());
		
		sb.append("<div class=\"checkout__order__total\">Total <span>Entrega: "+ calculo.getPrazoEntrega() + " dias. Valor transporte: " +NumberFormat.getCurrencyInstance().format(Double.parseDouble(calculo.getValorFrete())) + "</span></div>");
		sb.append("<div class=\"checkout__order__total\">Total <span>"+NumberFormat.getCurrencyInstance().format(valorFinal)+"</span></div>");
		
		return sb.toString();
	}


	public static String exibirProdutosCarrinho(List<Venda> itens, Usuario comprador) {
		StringBuffer sb = new StringBuffer();

		sb.append("<table>");
		sb.append("<thead>");
		sb.append("<tr>");
		sb.append("<th class=\"shoping__product\">Produtos</th>");
		sb.append("<th>Preço</th>");
		sb.append("<th>Quantidade</th>");
		sb.append("<th>Total</th>");
		sb.append("<th></th>");
		sb.append("</tr>");
		sb.append("</thead>");
		sb.append("<tbody>");

		Double valorFinal = 0.0;
		List<Produtos> lsProdutos = new ArrayList<Produtos>();
		for(Venda item:itens) {

			sb.append("<tr>");
			sb.append("<td class=\"shoping__cart__item\">");
			sb.append("<img src=\""+item.getProduto().getUrlImagem()+"\" alt=\"\">");
			sb.append("<h5>"+item.getProduto().getNome()+"</h5>");
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__price\">");
			sb.append(NumberFormat.getCurrencyInstance().format(item.getProduto().getPrecoVenda()));
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__quantity\">");
			sb.append("<div class=\"quantity\">");
			sb.append("<div class=\"pro-qty\">");
			sb.append("<input type=\"text\" value=\""+item.getQuantidadeProduto()+"\">");
			sb.append("</div>");
			sb.append(" </div>");
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__total\">");
			sb.append(NumberFormat.getCurrencyInstance().format(item.getProduto().getPrecoVenda() * item.getQuantidadeProduto()));
			sb.append("</td>");
			sb.append("<td class=\"shoping__cart__item__close\">");
			sb.append("<span class=\"icon_close\"></span>&nbsp;");
			//sb.append("<a href=\"#\"><i class=\"fa fa-shopping-bag\"></i></a>");
			sb.append("</td>");
			sb.append("</tr>");

			valorFinal += (item.getProduto().getPrecoVenda() * item.getQuantidadeProduto());
			item.getProduto().setQuantidade(item.getQuantidadeProduto());
			lsProdutos.add(item.getProduto());
		}

		
		CalculadoraCorreios calculo = CalculaValorPrazoEntrega.preparaObjetoParaCalculo(lsProdutos, comprador.getCep());
		calculo = CalculaValorPrazoEntrega.calculaPrazoEntregaValor(calculo);
		valorFinal += Double.parseDouble(calculo.getValorFrete());
		
		sb.append("<tr><td class=\"shoping__cart__price\" colspan=\"5\" align=\"right\">Entrega: "+ calculo.getPrazoEntrega() + " dias. Valor transporte: " +NumberFormat.getCurrencyInstance().format(Double.parseDouble(calculo.getValorFrete())) + "</td></tr>");
		sb.append("<tr><td class=\"shoping__cart__price\" colspan=\"5\" align=\"right\">"+NumberFormat.getCurrencyInstance().format(valorFinal) + "</td></tr>");

		sb.append("</tbody>");
		sb.append("</table>");

		return sb.toString();
	}
}
