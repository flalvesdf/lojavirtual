package com.loja.lojavirtual.app.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.NumberFormat;
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
import com.loja.lojavirtual.app.domain.Categoria;
import com.loja.lojavirtual.app.domain.Log;
import com.loja.lojavirtual.app.domain.Produtos;
import com.loja.lojavirtual.app.domain.Usuario;
import com.loja.lojavirtual.app.domain.Venda;
import com.loja.lojavirtual.app.domain.VendaId;
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
		List<Produtos> produtos = prdDao.recuperarProdutosDestaque();
		Categoria categoria = catDao.recuperarPorId(Integer.parseInt(produtos.get(0).getCategoria()));
		produtos.get(0).setCategoriaProduto(categoria);
		model.addAttribute("destaque", ConstrutorExibicaoProduto.exibeProdutoDestaque(produtos.get(0)));
		return new ModelAndView("index", model);
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET) 
	public ModelAndView home2(ModelMap model, HttpSession session) {
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Página Inicial", TipoLog.HOME, getIp(), "Home");	
		model = informacoesBasicasPagina(model, session, log);
		List<Produtos> produtos = prdDao.recuperarProdutosDestaque();
		Categoria categoria = catDao.recuperarPorId(Integer.parseInt(produtos.get(0).getCategoria()));
		produtos.get(0).setCategoriaProduto(categoria);
		model.addAttribute("destaque", ConstrutorExibicaoProduto.exibeProdutoDestaque(produtos.get(0)));
		return new ModelAndView("index", model);
	}
	
	@RequestMapping(value = "/carrinho", method = RequestMethod.GET) 
	public ModelAndView carrinho(ModelMap model, HttpSession session) { 
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Carrinho", TipoLog.SECAO, getIp(), "carrinho");
		model = informacoesBasicasPagina(model, session, log);
		
		Usuario uSessao = recuperarDaSessao(session);
		
		if(null == uSessao) {
			model.addAttribute("mensagem", "Para acessar o carrinho de compras você precisa se registrar.");
			model = informacoesBasicasPagina(model, session, log);
			return new ModelAndView("login", model);
		}else {
			List<Venda> carrinhoUsuario = vendaDao.recuperarVendasUsuarioNaoFinalizada(uSessao.getIdUsuario());
			List<Venda> novoCarrinhoUsuario = new ArrayList<>();
			
			for(Venda venda: carrinhoUsuario) {
				Produtos p = prdDao.recuperarPorId(venda.getId().getIdProduto());
				venda.setProduto(p);
				novoCarrinhoUsuario.add(venda);
			}
			
			Usuario usuComprador = usuDao.recuperarPorId(uSessao.getIdUsuario());
			String produtosCarrinho = ConstrutorExibicaoProduto.exibirProdutosCarrinho(novoCarrinhoUsuario, usuComprador);
			model.addAttribute("carrinho", produtosCarrinho);
			return new ModelAndView("carrinho", model);
		}
		
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
		
		Usuario comprador = usuDao.recuperarPorId(usuSessao.getIdUsuario());
		String construtorExibicao = ConstrutorExibicaoProduto.exibirWishList(wlNova, comprador);
		
		model.addAttribute("wishlist", construtorExibicao);
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("wish-list", model);
	}
	
	@RequestMapping(value = "/checkout", method = RequestMethod.GET) 
	public ModelAndView checkout(ModelMap model, HttpSession session) { 
//		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Checkout", TipoLog.SECAO, getIp(), "checkout");
//		model = informacoesBasicasPagina(model, session, log);
//		return new ModelAndView("checkout", model);
		
		
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Checkout", TipoLog.SECAO, getIp(), "checkout");
		model = informacoesBasicasPagina(model, session, log);
		
		Usuario uSessao = recuperarDaSessao(session);
		
		if(null == uSessao) {
			model.addAttribute("mensagem", "Para acessar o carrinho de compras você precisa se registrar.");
			model = informacoesBasicasPagina(model, session, log);
			return new ModelAndView("login", model);
		}else {
			List<Venda> carrinhoUsuario = vendaDao.recuperarVendasUsuarioNaoFinalizada(uSessao.getIdUsuario());
			List<Venda> novoCarrinhoUsuario = new ArrayList<>();
			
			for(Venda venda: carrinhoUsuario) {
				Produtos p = prdDao.recuperarPorId(venda.getId().getIdProduto());
				venda.setProduto(p);
				novoCarrinhoUsuario.add(venda);
			}
			
			Usuario comprador = usuDao.recuperarPorId(uSessao.getIdUsuario());
			String produtosCarrinho = ConstrutorExibicaoProduto.checkout(novoCarrinhoUsuario, comprador);
			model.addAttribute("carrinho", produtosCarrinho);
			return new ModelAndView("checkout", model);
		}
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
		
		usu.setIp(getIp());
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
	
	@RequestMapping(value = "/addCarrinho", method = RequestMethod.GET) 
	public ModelAndView addCarrinho(@Validated @ModelAttribute("idProdutoCarrinho") Integer idProdutoCarrinho, BindingResult result, ModelMap model, HttpSession session) {
		
		Usuario usuSessao = recuperarDaSessao(session);
		Produtos prd = prdDao.recuperarPorId(idProdutoCarrinho);
		
		if(null == usuSessao) {
			model = mensagem(Mensagens.MSG_ADD_CARRINHO_SEM_LOGIN, model, session);
			Log log = new Log(idProdutoCarrinho, 0, new Timestamp(System.currentTimeMillis()), "Tentando salvar produto no carrinho sem login:"+idProdutoCarrinho, TipoLog.PRODUTO_ADD_CARRINHO, getIp(), "addCarrinho");
			gravarLog(log);
			
			Log log2 = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: login", TipoLog.SECAO, getIp(), "login");
			model = informacoesBasicasPagina(model, session, log2);
			return new ModelAndView("login", model);
		}else {
			Integer idVendaUsuNaoFinalizada = vendaDao.recuperarVendaUsuarioNaoFinalizada(usuSessao.getIdUsuario());
			
			if(idVendaUsuNaoFinalizada == 0) {
				//tem q iniciar outra venda
				Integer maxId = vendaDao.getMaxId();
				if(maxId == null)maxId = 0;
				
				Venda itemNovo = new Venda();
				VendaId idItemnovo = new VendaId();
				idItemnovo.setId(maxId+1);
				idItemnovo.setIdProduto(idProdutoCarrinho);
				idItemnovo.setIdUsuario(usuSessao.getIdUsuario());
				itemNovo.setId(idItemnovo);
				itemNovo.setQuantidadeProduto(1);
				itemNovo.setVendaFinalizada("N");
				itemNovo.setValorUnidadeProduto(prd.getPrecoVenda());
				itemNovo.setValorTotalProduto(prd.getPrecoVenda() * 1);
				itemNovo.setTsVenda(new Timestamp(System.currentTimeMillis()));
				
				vendaDao.salvar(itemNovo);
				
			}else {
				List<Venda> vendaEmAberto = vendaDao.recuperarPorId(idVendaUsuNaoFinalizada);
				
				for(Venda venda: vendaEmAberto) {
					//produto já está no carrinho de compras
					if(venda.getId().getIdProduto() == idProdutoCarrinho) {
						Log log2 = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: carrinho", TipoLog.SECAO, getIp(), "addCarrinho");
						model = informacoesBasicasPagina(model, session, log2);
						return new ModelAndView("login", model);
					}
				}
				
				Venda itemNovo = new Venda();
				VendaId idItemnovo = new VendaId();
				idItemnovo.setId(idVendaUsuNaoFinalizada);
				idItemnovo.setIdProduto(idProdutoCarrinho);
				idItemnovo.setIdUsuario(usuSessao.getIdUsuario());
				itemNovo.setId(idItemnovo);
				itemNovo.setQuantidadeProduto(1);
				itemNovo.setVendaFinalizada("N");
				itemNovo.setValorUnidadeProduto(prd.getPrecoVenda());
				itemNovo.setValorTotalProduto(prd.getPrecoVenda() * 1);
				itemNovo.setTsVenda(new Timestamp(System.currentTimeMillis()));
				
				vendaDao.salvar(itemNovo);
				
			}
			
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Produto adicionado no carrinho:"+idProdutoCarrinho, TipoLog.PRODUTO_ADD_CARRINHO, getIp(), "addCarrinho");
			
			model = informacoesBasicasPagina(model, session, log);
			model = mensagem(Mensagens.MSG_ADD_CARRINHO_PRODUTO, model, session);
			
			List<Venda> carrinhoUsuario = vendaDao.recuperarVendasUsuarioNaoFinalizada(usuSessao.getIdUsuario());
			List<Venda> novoCarrinhoUsuario = new ArrayList<>();
			
			for(Venda venda: carrinhoUsuario) {
				Produtos p = prdDao.recuperarPorId(venda.getId().getIdProduto());
				venda.setProduto(p);
				novoCarrinhoUsuario.add(venda);
			}
			
			Usuario usuComprador = usuDao.recuperarPorId(usuSessao.getIdUsuario());
			String produtosCarrinho = ConstrutorExibicaoProduto.exibirProdutosCarrinho(novoCarrinhoUsuario, usuComprador);
			model.addAttribute("carrinho", produtosCarrinho);
			return new ModelAndView("carrinho", model);
		}
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
	
	
	
	@PutMapping("/alterarUsuario")
	public ModelAndView alterarUsuario(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		usu.setIp(getIp());
		usu.setLogin(usu.getEmail().toLowerCase());
		usu.setAdminLoja("N");
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
	
	/*Informacoes básicas da página*/
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
			List<Venda> carrinhoUsuario = vendaDao.recuperarVendasUsuarioNaoFinalizada(usuarioSessao.getIdUsuario());
			Double valor = 0.0;
			for(Venda item:carrinhoUsuario) {
				Produtos p = prdDao.recuperarPorId(item.getId().getIdProduto());
				valor += (p.getPrecoVenda() * item.getQuantidadeProduto());
			}
			
			model.addAttribute("shopcar", carrinhoUsuario.size());
			model.addAttribute("shopcarvalor", NumberFormat.getCurrencyInstance().format(valor));
			model.addAttribute("shopcarlink", "carrinho");
		}else {
			model.addAttribute("logado", "Entrar");
			model.addAttribute("linkParaUsuario", "login");
			model.addAttribute("linkWishlist", "login");
			model.addAttribute("shopcarlink", "login");
		}
		
		if(null != log) {
			gravarLog(log);
		}
		
		return model;
	}
	
	@RequestMapping(value="/categorias", method=RequestMethod.GET)
	public ModelAndView categorias(@Validated @ModelAttribute("idCategoria") Integer idCategoria, BindingResult result, ModelMap model, HttpSession session) {
		
		Categoria categoria = catDao.recuperarPorId(idCategoria);
		List<Produtos> produtosCategoria = prdDao.recuperarTodosPorCategoria(idCategoria);
		String resultado = "";
		
		if(null == produtosCategoria) {
			resultado = "";
		}else {
			resultado = ConstrutorExibicaoProduto.exibeProdutos(produtosCategoria);
		}

		model.addAttribute("retornoPesquisa", "Produtos da Categoria "+categoria.getNome());
		model.addAttribute("produtos", resultado.length()>0?resultado:"<center>Desculpe, não encontramos nenhum produto na categoria "+ categoria.getNome()+"</center>");
		
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: categoria:"+idCategoria, TipoLog.SECAO, getIp(), "categoria");
		model = informacoesBasicasPagina(model, session, log);
		return new ModelAndView("pesquisa", model);
	}
	
	@GetMapping("/sair")
	public ModelAndView sair(ModelMap model, HttpSession session) {
		Usuario loginSessao = recuperarDaSessao(session);
		Log log = new Log(0, loginSessao.getIdUsuario(), new Timestamp(System.currentTimeMillis()), "Saindo do Site:"+loginSessao.getEmail(), TipoLog.SECAO, getIp(), "sair");	
		gravarLog(log);
		model = mensagem(Mensagens.MSG_SESSAO_ENCERRADA, model, session);
		//model = informacoesBasicasPagina(model, session, log);
		session.invalidate();
		return new ModelAndView("redirect:home", model);
	}
	
	private Boolean isAdminSite(Usuario usuSessao) {
		if(usuSessao !=null ) {
			
			Usuario usuBd = usuDao.recuperarPorId(usuSessao.getIdUsuario());
			
			if(usuBd !=null && usuBd.getAdminLoja()!= null && usuBd.getAdminLoja().equals("S")) {
				return new Boolean(true);
			}else {
				return new Boolean(false);
			}
		}else {
			return new Boolean(false);
		}
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
	
	
	/**
	 * SECAO ADMIN DO SITE
	 */
	
	@RequestMapping(value = "/admin", method = RequestMethod.GET) 
	public ModelAndView admin(ModelMap model, HttpSession session) {
		
		Usuario usu = recuperarDaSessaoAdmin(session);
		
		if(null != usu) {
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa -  IP: "+getIp(), TipoLog.AREA_ADMIN, getIp(), "admin");
			model = informacoesBasicasAdmin(model, session, log);
			return new ModelAndView("admin", model);
		}else {
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa -  IP: "+getIp(), TipoLog.AREA_ADMIN, getIp(), "admin");
			model = informacoesBasicasAdmin(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@RequestMapping(value = "/verificaLoginAdmin", method = RequestMethod.POST) 
	public ModelAndView verificaLoginAdmin(@Validated @ModelAttribute("usu") Usuario usu, BindingResult result, ModelMap model, HttpSession session) {
		
		Usuario usuarioNaBase = usuDao.recuperarPorCPF(usu.getCpf());
		
		//encontrou o usuario na base
		if(null != usuarioNaBase) {
			if(null != usuarioNaBase.getCep() && null != usuarioNaBase.getSenha()) {
				//senha confere
				if(usuarioNaBase.getCpf().equals(usu.getCpf()) && usuarioNaBase.getSenha().equals(usu.getSenha())) {
					usu.setNome(usuarioNaBase.getNome());
					usu.setIdUsuario(usuarioNaBase.getIdUsuario());
					
					salvarNaSessaoAdmin(usu, session);
					model = mensagem("Bem vindo "+ usu.getNome().split(" ")[0], model, session);
					Log log = new Log(0, usuarioNaBase.getIdUsuario(), new Timestamp(System.currentTimeMillis()), "Acesso ao site: Login Administrativo", TipoLog.AREA_ADMIN, getIp(), "verificaLoginAdmin");
					model = informacoesBasicasAdmin(model, session, log);
					//model.addAttribute("logado", usuarioNaBase.getNome().split(" ")[0]);
					return new ModelAndView("admin", model);
				//senha nao confere	
				}else {
					model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
					Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Administrativo - Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "verificaLoginAdmin");
					informacoesBasicasAdmin(model, session, log);
					return new ModelAndView("loginAdmin", model);
				}
			}else {
				model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Administrativo - Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "verificaLoginAdmin");
				informacoesBasicasPagina(model, session, log);
				return new ModelAndView("loginAdmin", model);
			}
		//nao encontrou o usuario na base	
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_LOCALIZADOS, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso ao site: Administrativo - Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "verificaLoginAdmin");
			informacoesBasicasAdmin(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@RequestMapping(value = "/cadastroProduto", method = RequestMethod.GET) 
	public ModelAndView cadastroProduto(ModelMap model, HttpSession session) { 
		
		Usuario usuSessao = recuperarDaSessaoAdmin(session);
		
		if(null!= usuSessao) {
			if(isAdminSite(usuSessao).booleanValue()) {
				List<Categoria> categorias = catDao.recuperarTodos();
				model.addAttribute("categorias", categorias);
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa Permitida -  Usuário: "+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "cadastroProduto");
				model = informacoesBasicasAdmin(model, session, log);
				return new ModelAndView("cadastroProduto", model);
			}else {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida:"+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
				model = informacoesBasicasAdmin(model, session, log);
				model = mensagem(Mensagens.MSG_ADMIN_SITE, model, session);
				return new ModelAndView("loginAdmin", model);
			}
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida: Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
			informacoesBasicasAdmin(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@PutMapping("/cadastrarProduto")
	public ModelAndView cadastrarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.salvar(prd);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Cadastro Produto:"+prd.getNome(), TipoLog.CAD_PRODUTO, getIp(), "cadastrarProduto");
		model = informacoesBasicasAdmin(model, session, log);
		model = mensagem(Mensagens.MSG_PRODUTO_CADASTRADO, model, session);
		return new ModelAndView("admin", model);
	}
	
	@PutMapping("/alterarProduto")
	public ModelAndView alterarProduto(@Validated @ModelAttribute("produto") Produtos prd, BindingResult result, ModelMap model, HttpSession session) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());		
		prd.setTsAtualizacao(timestamp);
		prdDao.atualizar(prd);
		Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Alterado Produto:"+prd.getNome(), TipoLog.CAD_PRODUTO, getIp(), "alterarProduto");
		model = informacoesBasicasAdmin(model, session, log);
		model = mensagem(Mensagens.MSG_PRODUTO_ALTERADO, model, session);
		return new ModelAndView("admin", model);
	}
	
	@RequestMapping(value = "/alteraProduto", method = RequestMethod.GET) 
	public ModelAndView alteraProduto(@Validated @ModelAttribute("idProduto") Integer idProduto, BindingResult result, ModelMap model, HttpSession session) { 
		
		Usuario usuSessao = recuperarDaSessaoAdmin(session);
		
		if(null!= usuSessao) {
			if(isAdminSite(usuSessao).booleanValue()) {
				List<Categoria> categorias = catDao.recuperarTodos();
				model.addAttribute("categorias", categorias);
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa Permitida -  Usuário: "+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "alteraProduto");
				model = informacoesBasicasAdmin(model, session, log);
				Produtos produto = prdDao.recuperarPorId(idProduto);
				model.addAttribute("produto", produto);
				return new ModelAndView("alterarProduto", model);
			}else {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida:"+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
				model = informacoesBasicasAdmin(model, session, log);
				model = mensagem(Mensagens.MSG_ADMIN_SITE, model, session);
				return new ModelAndView("loginAdmin", model);
			}
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida: Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
			informacoesBasicasPagina(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@RequestMapping(value = "/listarProdutos", method = RequestMethod.GET) 
	public ModelAndView listarProdutos(ModelMap model, HttpSession session) { 
		
		Usuario usuSessao = recuperarDaSessaoAdmin(session);
		
		if(null!= usuSessao) {
			if(isAdminSite(usuSessao).booleanValue()) {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa Permitida -  Usuário: "+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "cadastroProduto");
				model = informacoesBasicasAdmin(model, session, log);
				model.addAttribute("produtos", prdDao.recuperarTodos());
				return new ModelAndView("listaProdutos", model);
			}else {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida:"+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
				model = informacoesBasicasAdmin(model, session, log);
				model = mensagem(Mensagens.MSG_ADMIN_SITE, model, session);
				return new ModelAndView("loginAdmin", model);
			}
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida: Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "cadastrarProduto");
			informacoesBasicasPagina(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@RequestMapping(value = "/listarUsuarios", method = RequestMethod.GET) 
	public ModelAndView listarUsuarios(ModelMap model, HttpSession session) { 
		
		Usuario usuSessao = recuperarDaSessaoAdmin(session);
		
		if(null!= usuSessao) {
			if(isAdminSite(usuSessao).booleanValue()) {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa Permitida -  Usuário: "+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "listarUsuarios");
				model = informacoesBasicasAdmin(model, session, log);
				model.addAttribute("usuarios", usuDao.recuperarTodos());
				return new ModelAndView("listaUsuarios", model);
			}else {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida:"+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "listarUsuarios");
				model = informacoesBasicasAdmin(model, session, log);
				model = mensagem(Mensagens.MSG_ADMIN_SITE, model, session);
				return new ModelAndView("loginAdmin", model);
			}
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida: Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "listarUsuarios");
			informacoesBasicasPagina(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	@RequestMapping(value = "/listarCategorias", method = RequestMethod.GET) 
	public ModelAndView listarCategorias(ModelMap model, HttpSession session) { 
		
		Usuario usuSessao = recuperarDaSessaoAdmin(session);
		
		if(null!= usuSessao) {
			if(isAdminSite(usuSessao).booleanValue()) {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa Permitida -  Usuário: "+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "listarCategorias");
				model = informacoesBasicasAdmin(model, session, log);
				model.addAttribute("categorias", catDao.recuperarTodos());
				return new ModelAndView("listaCategorias", model);
			}else {
				Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida:"+usuSessao.getLogin(), TipoLog.AREA_ADMIN, getIp(), "listarCategorias");
				model = informacoesBasicasAdmin(model, session, log);
				model = mensagem(Mensagens.MSG_ADMIN_SITE, model, session);
				return new ModelAndView("loginAdmin", model);
			}
		}else {
			model = mensagem(Mensagens.MSG_DADOS_N_CONFEREM, model, session);
			Log log = new Log(0, 0, new Timestamp(System.currentTimeMillis()), "Acesso à área administrativa não Permitida: Não Logado/ Dados não conferem", TipoLog.AREA_ADMIN, getIp(), "listarCategorias");
			informacoesBasicasPagina(model, session, log);
			return new ModelAndView("loginAdmin", model);
		}
	}
	
	/*Informacoes básicas da página*/
	private ModelMap informacoesBasicasAdmin(ModelMap model, HttpSession session, Log log) {
		//model.addAttribute("categorias", catDao.recuperarTodos());
		Usuario usuarioSessao = recuperarDaSessao(session);
		
		if(null != usuarioSessao) {
			model.addAttribute("linkSair", "sair");
		}
		
		if(null != log) {
			gravarLog(log);
		}
		
		return model;
	}
	
	/*salva o usuário na sessao*/
	private void salvarNaSessaoAdmin(@RequestParam Usuario usuario, HttpSession session) {
		session.setAttribute("admin", usuario);
	}
	
	/*recupera o usuario da sessao*/
	private Usuario recuperarDaSessaoAdmin(HttpSession session) {
		Usuario loginSessao = (Usuario)session.getAttribute("admin");
		return loginSessao;
	}
	
}
