package com.loja.lojavirtual.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.loja.lojavirtual.app.dao.CategoriaDao;
import com.loja.lojavirtual.app.dao.ProdutoDao;
import com.loja.lojavirtual.app.dao.UsuarioDao;
import com.loja.lojavirtual.app.dao.VendaDao;
import com.loja.lojavirtual.app.domain.Produtos;
import com.loja.lojavirtual.app.domain.Usuario;
import com.loja.lojavirtual.app.util.ConstrutorExibicaoProduto;
import com.loja.lojavirtual.app.util.Mensagens;

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
	public ModelAndView home(ModelMap model, HttpSession session) { 
		model = informacoesBasicasPagina(model, session);
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
		model = informacoesBasicasPagina(model, session);
		return new ModelAndView("cadastro", model);
	}
	
	@RequestMapping(value = "/cadastroProduto", method = RequestMethod.GET) 
	public ModelAndView cadastroProduto(ModelMap model, HttpSession session) { 
		model = informacoesBasicasPagina(model, session);
		return new ModelAndView("cadastroProduto", model);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public ModelAndView login(ModelMap model, HttpSession session) { 
		model = informacoesBasicasPagina(model, session);
		return new ModelAndView("login", model);
	}
	
	@RequestMapping(value = "/meusDados", method = RequestMethod.GET) 
	public ModelAndView meusDados(ModelMap model, HttpSession session) { 
		model = informacoesBasicasPagina(model, session);
		Usuario usuSessao = recuperarDaSessao(session);
		model.addAttribute("usu", usuDao.recuperarPorEmail(usuSessao.getEmail()));
		return new ModelAndView("meusDados", model);
	}
	
	@RequestMapping(value = "/verificaLogin", method = RequestMethod.POST) 
	public ModelAndView verificaLogin(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		Usuario usuarioNaBase = usuDao.recuperarPorEmail(usu.getEmail());
		
		//encontrou o usuario na base
		if(null != usuarioNaBase) {
			if(null != usuarioNaBase.getLogin() && null != usuarioNaBase.getSenha()) {
				//senha confere
				if(usuarioNaBase.getSenha().equals(usu.getSenha())) {
					usu.setNome(usuarioNaBase.getNome());
					salvarNaSessao(usu, session);
					model = mensagem("Bem vindo "+ usu.getNome().split(" ")[0], model, session);
					model = informacoesBasicasPagina(model, session);
					//model.addAttribute("logado", usuarioNaBase.getNome().split(" ")[0]);
					return new ModelAndView("index", model);
				//senha nao confere	
				}else {
					model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
					informacoesBasicasPagina(model, session);
					return new ModelAndView("login", model);
				}
			}else {
				model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
				informacoesBasicasPagina(model, session);
				return new ModelAndView("login", model);
			}
		//nao encontrou o usuario na base	
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
			informacoesBasicasPagina(model, session);
			return new ModelAndView("login", model);
		}
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET) 
	public ModelAndView wishlist(ModelMap model, HttpSession session) { 
		List<Produtos> produtos = prdDao.recuperarTodos();
		String resultado = "";
		for (Produtos produto: produtos) {
			resultado += ConstrutorExibicaoProduto.exibeProduto(produto);
		}
		
		model = informacoesBasicasPagina(model, session);
		model.addAttribute("produtos", resultado);
		return new ModelAndView("template", model);
	}
	
	@RequestMapping(value = "/pesquisar", method = RequestMethod.GET) 
	public ModelAndView pesquisar(@ModelAttribute("pesquisar") String parametro, ModelMap model, HttpSession session) { 
		List<Produtos> produtos = prdDao.recuperarPorNome(parametro);
		String resultado = "";
		for (Produtos produto: produtos) {
			resultado += ConstrutorExibicaoProduto.exibeProduto(produto);
		}
		
		model = informacoesBasicasPagina(model, session);
		model.addAttribute("produtos", resultado);
		return new ModelAndView("pesquisa", model);
	}

	@PutMapping("/cadastrarUsuario")
	public ModelAndView cadastrarUsuario(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		try {
			usu.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			//faz nada
		}
		
		usu.setLogin(usu.getEmail().toLowerCase());
		usu.setAdminLoja("N");//default N
		usuDao.salvar(usu);
		Usuario usuarioSessao = new Usuario();
		usuarioSessao.setNome(usu.getNome().split(" ")[0]);
		usuarioSessao.setEmail(usu.getLogin());
		salvarNaSessao(usuarioSessao, session);
		model = informacoesBasicasPagina(model, session);
		model = mensagem(Mensagens.MSG_DADOS_REGISTRADOS, model, session);
		return new ModelAndView("index", model);
	}
	
	@PutMapping("/cadastrarProduto")
	public ModelAndView cadastrarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.salvar(prd);
		
		model = informacoesBasicasPagina(model, session);
		model = mensagem(Mensagens.MSG_PRODUTO_CADASTRADO, model, session);
		return new ModelAndView("index", model);
	}
	
	@PutMapping("/alterarProduto")
	public ModelAndView alterarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.atualizar(prd);
		
		model = informacoesBasicasPagina(model, session);
		model = mensagem(Mensagens.MSG_PRODUTO_ALTERADO, model, session);
		return new ModelAndView("index", model);
	}
	
	@PutMapping("/alterarUsuario")
	public ModelAndView alterarUsuario(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		try {
			usu.setIp(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			//faz nada
		}
		
		usu.setLogin(usu.getEmail().toLowerCase());
		usuDao.atualizar(usu);
		Usuario usuarioSessao = new Usuario();
		usuarioSessao.setNome(usu.getNome().split(" ")[0]);
		usuarioSessao.setEmail(usu.getLogin());
		salvarNaSessao(usuarioSessao, session);
		model = informacoesBasicasPagina(model, session);
		model = mensagem(Mensagens.MSG_DADOS_ATUALIZADOS, model, session);
		return new ModelAndView("index", model);
	}
	
	private ModelMap informacoesBasicasPagina(ModelMap model, HttpSession session) {
		model.addAttribute("categorias", catDao.recuperarTodos());
		Usuario usuarioSessao = recuperarDaSessao(session);
		
		if(null != usuarioSessao) {
			model.addAttribute("logado", "Bem vindo "+ usuarioSessao.getNome());
			model.addAttribute("linkParaUsuario", "meusDados");
			model.addAttribute("linkSair", "sair");
		}else {
			model.addAttribute("logado", "Entrar");
			model.addAttribute("linkParaUsuario", "login");
		}
		
		return model;
	}
	
	private ModelMap mensagem(String mensagem, ModelMap model, HttpSession session) {
		model.addAttribute("mensagem", mensagem);
		return model;
	}
	
	private void salvarNaSessao(@RequestParam Usuario usuario, HttpSession session) {
		session.setAttribute("usuario", usuario);
	}
	
	private Usuario recuperarDaSessao(HttpSession session) {
		Usuario loginSessao = (Usuario)session.getAttribute("usuario");
		return loginSessao;
	}
	
	@GetMapping("/sair")
	public ModelAndView sair(ModelMap model, HttpSession session) {
		session.invalidate();
		model = mensagem(Mensagens.MSG_SESSAO_ENCERRADA, model, session);
		model = informacoesBasicasPagina(model, session);
		return new ModelAndView("index", model);
	}
}
