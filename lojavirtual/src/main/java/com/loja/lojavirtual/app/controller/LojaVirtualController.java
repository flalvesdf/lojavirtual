package com.loja.lojavirtual.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.loja.lojavirtual.app.dao.LogDao;
import com.loja.lojavirtual.app.dao.ProdutoDao;
import com.loja.lojavirtual.app.dao.UsuarioDao;
import com.loja.lojavirtual.app.dao.VendaDao;
import com.loja.lojavirtual.app.dao.WishListDao;
import com.loja.lojavirtual.app.domain.Log;
import com.loja.lojavirtual.app.domain.Produtos;
import com.loja.lojavirtual.app.domain.Usuario;
import com.loja.lojavirtual.app.domain.WishList;
import com.loja.lojavirtual.app.domain.WishListId;
import com.loja.lojavirtual.app.util.ConstrutorExibicaoProduto;
import com.loja.lojavirtual.app.util.Mensagens;
import com.loja.lojavirtual.app.util.TipoLog;

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
	
	@Autowired
	WishListDao wlDao;
	
	@Autowired
	LogDao logDao;
	
	@RequestMapping(value = "/", method = RequestMethod.GET) 
	public ModelAndView home(ModelMap model, HttpSession session) {
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Página Inicial", TipoLog.HOME, getIp(), "Home");	
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("index", model);
	}
	
	@RequestMapping(value = "/carrinho", method = RequestMethod.GET) 
	public ModelAndView carrinho(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Carrinho", TipoLog.SECAO, getIp(), "carrinho");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("shopping-cart", model);
	}
	
	@RequestMapping(value = "/wishlist", method = RequestMethod.GET) 
	public ModelAndView wishlist(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Wish List", TipoLog.SECAO, getIp(), "wishlist");
		Usuario usuSessao = recuperarDaSessao(session);
		
		if(null == usuSessao) {
			model.addAttribute("mensagem", "Para acessar a Lista de Desejos você precisa se registrar.");
			model = informacoesBasicasPagina(model, session, log);
			return new ModelAndView("login", model);
		}
		
		List<WishList> wl = wlDao.recuperarPorId(usuSessao.getIdUsuario());
		List<WishList> wlNova = new ArrayList<WishList>();
		
		for(WishList wish:wl) {
			Produtos prd = prdDao.recuperarPorId(wish.getId().getIdprodutowl());
			wish.setProduto(prd);
			wlNova.add(wish);
		}
		
		String construtorExibicao = ConstrutorExibicaoProduto.exibirWishList(wlNova);
		model.addAttribute("wishlist", construtorExibicao);
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("wish-list", model);
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET) 
	public ModelAndView checkout(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Checkout", TipoLog.SECAO, getIp(), "checkout");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("checkout", model);
	}
	
	@RequestMapping(value = "/contato", method = RequestMethod.GET) 
	public ModelAndView contato(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Contato", TipoLog.SECAO, getIp(), "contato");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("contact", model);
	}
	
	@RequestMapping(value = "/detalheProduto", method = RequestMethod.GET) 
	public ModelAndView detalheProduto(ModelMap model, HttpSession session) { 
		return new ModelAndView("shop-details", model);
	}
	
	@RequestMapping(value = "/cadastro", method = RequestMethod.GET) 
	public ModelAndView cadastro(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Cadastro", TipoLog.CAD_USUARIO, getIp(), "cadastro");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("cadastro", model);
	}
	
	@RequestMapping(value = "/cadastroProduto", method = RequestMethod.GET) 
	public ModelAndView cadastroProduto(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: cadastroProduto", TipoLog.SECAO, getIp(), "cadastroProduto");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("cadastroProduto", model);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET) 
	public ModelAndView login(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: login", TipoLog.SECAO, getIp(), "login");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("login", model);
	}
	
	@RequestMapping(value = "/meusDados", method = RequestMethod.GET) 
	public ModelAndView meusDados(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Meus Dados", TipoLog.SECAO, getIp(), "meusDados");
		model = informacoesBasicasPagina(model, session, log);
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
					usu.setIdUsuario(usuarioNaBase.getIdUsuario());
					salvarNaSessao(usu, session);
					model = mensagem("Bem vindo "+ usu.getNome().split(" ")[0], model, session);
					Log log = new Log(0, usuarioNaBase.getIdUsuario(), new Timestamp(System.currentTimeMillis()), "Acesso ao site: Home - Logado", TipoLog.LOGIN, getIp(), "verificaLogin");
					model = informacoesBasicasPagina(model, session, log);
					//model.addAttribute("logado", usuarioNaBase.getNome().split(" ")[0]);
					return new ModelAndView("index", model);
				//senha nao confere	
				}else {
					model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
					Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Home - Não Logado/ Dados não conferem", TipoLog.LOGIN, getIp(), "verificaLogin");
					informacoesBasicasPagina(model, session, log);
					return new ModelAndView("login", model);
				}
			}else {
				model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Home - Não Logado/ Dados não conferem", TipoLog.LOGIN, getIp(), "verificaLogin");
				informacoesBasicasPagina(model, session, log);
				return new ModelAndView("login", model);
			}
		//nao encontrou o usuario na base	
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Home - Não Logado/ Dados não conferem", TipoLog.LOGIN, getIp(), "verificaLogin");
			informacoesBasicasPagina(model, session, log);
			return new ModelAndView("login", model);
		}
	}
	
	@RequestMapping(value = "/template", method = RequestMethod.GET) 
	public ModelAndView template(ModelMap model, HttpSession session) { 
		List<Produtos> produtos = prdDao.recuperarTodos();
		String resultado = "";
		for (Produtos produto: produtos) {
			resultado += ConstrutorExibicaoProduto.exibeProduto(produto);
		}
		
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Template", TipoLog.SECAO, getIp(), "template");
		model = informacoesBasicasPagina(model, session, log);
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
		
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Pesquisa:"+parametro + "|Retorno:"+produtos.size(), TipoLog.PESQUISA, getIp(), "pesquisar");
		model = informacoesBasicasPagina(model, session, log);
		model.addAttribute("retornoPesquisa", "Resultado da pesquisa por "+parametro);
		model.addAttribute("produtos", resultado.length()>0?resultado:"<center>Desculpe, não encontramos nenhum produto com o termo "+ parametro+"</center>");
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
		usuarioSessao.setIdUsuario(usu.getIdUsuario());
		salvarNaSessao(usuarioSessao, session);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Cadastro:"+usu.getEmail(), TipoLog.CAD_USUARIO, getIp(), "cadastrarUsuario");
		model = informacoesBasicasPagina(model, session, log);
		model = mensagem(Mensagens.MSG_DADOS_REGISTRADOS, model, session);
		return new ModelAndView("index", model);
	}
	
	@RequestMapping(value = "/salvarNaWishList", method = RequestMethod.GET) 
	public ModelAndView salvarNaWishList(@Validated @ModelAttribute("idProduto") Integer idProduto, BindingResult result,ModelMap model, HttpSession session) {
		
		Usuario usuSessao = recuperarDaSessao(session);
		
		if(null == usuSessao) {
			model = mensagem(Mensagens.MSG_SALVAR_WL_SEM_LOGIN, model, session);
			Log log = new Log(idProduto, 0, new Timestamp(System.currentTimeMillis()), "Tentando salvar produto na WishList sem login:"+idProduto, TipoLog.PRODUTO_SALVO_WISH_LIST, getIp(), "salvarNaWishList");
			gravarLog(log);
			
			Log log2 = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: login", TipoLog.SECAO, getIp(), "login");
			model = informacoesBasicasPagina(model, session, log2);
			return new ModelAndView("login", model);
			
			
		}else {
			WishList wl = new WishList();
			WishListId wli = new WishListId();
			wli.setIdprodutowl(idProduto);
			wli.setIdusuwl(usuSessao.getIdUsuario());
			wl.setId(wli);
			wl.setData(new Date());
			wl.setQuantidade(1);
			
			List<WishList> wlbase = wlDao.recuperarPorId(usuSessao.getIdUsuario());
			
			for(WishList wish: wlbase) {
				if(wish.getId().getIdprodutowl().intValue() == idProduto.intValue()) {
					Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Produto já existente na wishlist do usuário:"+idProduto, TipoLog.PESQUISA, getIp(), "salvarNaWishList");
					model = informacoesBasicasPagina(model, session, log);
					model = mensagem(Mensagens.MSG_PRODUTO_JA_SALVO_NA_WL, model, session);
					return new ModelAndView("index", model);
				}
			}
			
			wlDao.salvar(wl);
			Log log = new Log(idProduto, usuSessao.getIdUsuario(), new Timestamp(System.currentTimeMillis()), "Salvando produto na WishList:"+idProduto, TipoLog.PRODUTO_SALVO_WISH_LIST, getIp(), "salvarNaWishList");
			model = informacoesBasicasPagina(model, session, log);
			model = mensagem(Mensagens.MSG_SALVAR_WL_COM_LOGIN, model, session);
			return new ModelAndView("index", model);
		}
	}
	
	@PutMapping("/cadastrarProduto")
	public ModelAndView cadastrarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.salvar(prd);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Cadastro Produto:"+prd.getNome(), TipoLog.CAD_PRODUTO, getIp(), "cadastrarProduto");
		model = informacoesBasicasPagina(model, session, log);
		model = mensagem(Mensagens.MSG_PRODUTO_CADASTRADO, model, session);
		return new ModelAndView("index", model);
	}
	
	@PutMapping("/alterarProduto")
	public ModelAndView alterarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.atualizar(prd);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Alterado Produto:"+prd.getNome(), TipoLog.CAD_PRODUTO, getIp(), "alterarProduto");
		model = informacoesBasicasPagina(model, session, log);
		model = mensagem(Mensagens.MSG_PRODUTO_ALTERADO, model, session);
		return new ModelAndView("index", model);
	}
	
	@PutMapping("/alterarUsuario")
	public ModelAndView alterarUsuario(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		usu.setIp(getIp());
		usu.setLogin(usu.getEmail().toLowerCase());
		usuDao.atualizar(usu);
		Usuario usuarioSessao = new Usuario();
		usuarioSessao.setNome(usu.getNome().split(" ")[0]);
		usuarioSessao.setEmail(usu.getLogin());
		usuarioSessao.setIdUsuario(usu.getIdUsuario());
		salvarNaSessao(usuarioSessao, session);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Alterado Usuário:"+usu.getEmail(), TipoLog.CAD_USUARIO, getIp(), "alterarUsuario");
		model = informacoesBasicasPagina(model, session, log);
		model = mensagem(Mensagens.MSG_DADOS_ATUALIZADOS, model, session);
		return new ModelAndView("index", model);
	}
	
	private ModelMap informacoesBasicasPagina(ModelMap model, HttpSession session, Log log) {
		model.addAttribute("categorias", catDao.recuperarTodos());
		Usuario usuarioSessao = recuperarDaSessao(session);
		
		if(null != usuarioSessao) {
			log.setIdUsuarioLog(usuarioSessao.getIdUsuario());
			model.addAttribute("logado", "Bem vindo "+ usuarioSessao.getNome());
			model.addAttribute("linkParaUsuario", "meusDados");
			model.addAttribute("linkSair", "sair");
			model.addAttribute("linkWishlist", "wishlist");
			List<WishList> wl = wlDao.recuperarPorId(usuarioSessao.getIdUsuario());
			model.addAttribute("wl", wl.size());
		}else {
			model.addAttribute("logado", "Entrar");
			model.addAttribute("linkParaUsuario", "login?mensagem=\"\"");
			model.addAttribute("linkWishlist", "");
		}
		
		if(null != log) {
			gravarLog(log);
		}
		
		return model;
	}
	
	@GetMapping("/sair")
	public ModelAndView sair(ModelMap model, HttpSession session) {
		Usuario loginSessao = recuperarDaSessao(session);
		Log log = new Log(0, loginSessao.getIdUsuario(), new Timestamp(System.currentTimeMillis()), "Saindo do Site:"+loginSessao.getEmail(), TipoLog.SECAO, getIp(), "sair");	
		session.invalidate();
		model = mensagem(Mensagens.MSG_SESSAO_ENCERRADA, model, session);
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("index", model);
	}
	
	/*monta mensagens para exibir no site*/
	private ModelMap mensagem(String mensagem, ModelMap model, HttpSession session) {
		model.addAttribute("mensagem", mensagem);
		return model;
	}
	
	/*salva o usuário na sessao*/
	private void salvarNaSessao(@RequestParam Usuario usuario, HttpSession session) {
		session.setAttribute("usuario", usuario);
	}
	
	/*recupera o usuario da sessao*/
	private Usuario recuperarDaSessao(HttpSession session) {
		Usuario loginSessao = (Usuario)session.getAttribute("usuario");
		return loginSessao;
	}
	
	/*gravador de log do site*/
	private void gravarLog(Log log) {
		logDao.salvar(log);
	}
	
	/*obtem o IP do cliente*/
	private String getIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "0.0.0.0";
		}
	}
}
