package com.loja.lojavirtual.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.loja.lojavirtual.app.dao.CategoriaDao;
import com.loja.lojavirtual.app.dao.ProdutoDao;
import com.loja.lojavirtual.app.dao.UsuarioDao;
import com.loja.lojavirtual.app.dao.VendaDao;
import com.loja.lojavirtual.app.domain.Produtos;
import com.loja.lojavirtual.app.domain.Usuario;
import com.loja.lojavirtual.app.util.ConstrutorExibicaoProduto;

@Controller
@RequestMapping("")
public class LojaVirtualController {
	
	@Autowired
	UsuarioDao usuDao;
	
	@Autowired
	ProdutoDao prdDao;
	
	@Autowired
	CategoriaDao catDao;
	
	@Autowired
	VendaDao vendaDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET) 
	public ModelAndView login(ModelMap model, HttpSession session) { 
		List<Produtos> produtos = prdDao.recuperarTodos();
		String resultado = "";
		for (Produtos produto: produtos) {
			resultado += ConstrutorExibicaoProduto.exibeProduto(produto);
		}
		
		model.addAttribute("categorias", catDao.recuperarTodos());
		model.addAttribute("produtos", resultado);
		
		return new ModelAndView("index", model);
	}
	
	@RequestMapping(value = "/carrinho", method = RequestMethod.GET) 
	public ModelAndView carrinho(ModelMap model, HttpSession session) { 
		return new ModelAndView("shopping-cart", model);
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET) 
	public ModelAndView checkout(ModelMap model, HttpSession session) { 
		return new ModelAndView("checkout", model);
	}
	
	@RequestMapping(value = "/contato", method = RequestMethod.GET) 
	public ModelAndView contato(ModelMap model, HttpSession session) { 
		return new ModelAndView("contact", model);
	}
	
	@RequestMapping(value = "/detalheProduto", method = RequestMethod.GET) 
	public ModelAndView detalheProduto(ModelMap model, HttpSession session) { 
		return new ModelAndView("shop-details", model);
	}
	
	@RequestMapping(value = "/cadastro", method = RequestMethod.GET) 
	public ModelAndView cadastro(ModelMap model, HttpSession session) { 
		model.addAttribute("categorias", catDao.recuperarTodos());
		return new ModelAndView("cadastro", model);
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET) 
	public ModelAndView wishlist(ModelMap model, HttpSession session) { 
		List<Produtos> produtos = prdDao.recuperarTodos();
		String resultado = "";
		for (Produtos produto: produtos) {
			resultado += ConstrutorExibicaoProduto.exibeProduto(produto);
		}
		
		model.addAttribute("categorias", catDao.recuperarTodos());
		model.addAttribute("produtos", resultado);
		return new ModelAndView("template", model);
	}

	@PutMapping("/cadastrarUsuario")
	public ModelAndView cadastrarUsuario(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		try {
			usu.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			//faz nada
		}
		
		usu.setLogin(usu.getEmail());
		usuDao.salvar(usu);
		model.addAttribute("mensagem", "Seus dados foram registrados com sucesso. Obrigado!");
		return new ModelAndView("index", model);
		
//		if(verificaSessao(session)) {
//			model.addAttribute("logado", mensagemLogado(session));
//			
//			if (result.hasErrors()) {
//				model.addAttribute("mensagem", "Existem erros nos dados informados.");
//				return new ModelAndView("adicionarFuncionario", model);
//			}
//			
//			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//			funci.setTs_cadastro(timestamp);
//			
//			Date dataNascConvertida = converteStringEmDate(funci.getDataNascimentoFormatada());
//			Date dataAdmissaoConvertida = converteStringEmDate(funci.getDataAdmissaoFormatada());
//			Date dataRescisaoConvertida = null;
//			if(null != funci.getDataRescisaoFormatada()) {
//				dataRescisaoConvertida = converteStringEmDate(funci.getDataRescisaoFormatada());
//			}
//			
//			if(null != dataNascConvertida) {
//				funci.setData_nascimento(dataNascConvertida);
//			}
//			
//			if(null != dataAdmissaoConvertida) {
//				funci.setData_admissao(dataAdmissaoConvertida);
//			}
//			
//			if(null != dataRescisaoConvertida) {
//				funci.setData_rescisao(dataRescisaoConvertida);
//			}
//			
//			funService.salvar(funci);
//			model.addAttribute("mensagem", "Funcionário "+ funci.getCpf() +" incluído com sucesso.");
//			
//			model.addAttribute("funcis", funService.recuperarTodos());
//			return new ModelAndView("listarFuncis", model);
//		}else {
//			model.addAttribute("mensagem", "Sua sessão no sistema foi finalizada. Por favor registre-se novamente");
//			return new ModelAndView("index", model);
//		}
	}
	
}
