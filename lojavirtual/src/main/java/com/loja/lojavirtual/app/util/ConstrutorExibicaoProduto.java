package com.loja.lojavirtual.app.util;

import java.text.NumberFormat;

import com.loja.lojavirtual.app.domain.Produtos;

public class ConstrutorExibicaoProduto {

	public static String exibeProduto(Produtos produto) {
		
		StringBuffer sb = new StringBuffer();
		
		 sb.append("<div class=\"col-lg-3 col-md-4 col-sm-6 mix oranges fresh-meat\">");
		 sb.append("<div class=\"featured__item\">");
		 sb.append("<div class=\"featured__item__pic set-bg\" data-setbg=\""+produto.getUrlImagem()+"\">");
		 sb.append("<ul class=\"featured__item__pic__hover\">");
		 sb.append("<li><a href=\"#\" title=\"Adicionar a sua lista de desejos\"><i class=\"fa fa-heart\"></i></a></li>");
		 sb.append("<li><a href=\"#\" title=\"Adicionar ao carrinho de compras\"><i class=\"fa fa-shopping-cart\"></i></a></li>");
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
}
