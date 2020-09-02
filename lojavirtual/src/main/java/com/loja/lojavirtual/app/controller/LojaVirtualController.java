package com.loja.lojavirtual.app.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("")
public class LojaVirtualController {
	
	@RequestMapping(value = "/", method = RequestMethod.GET) 
	public ModelAndView login(ModelMap model, HttpSession session) { 
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
	
	@RequestMapping(value = "/wishlist", method = RequestMethod.GET) 
	public ModelAndView wishlist(ModelMap model, HttpSession session) { 
		return new ModelAndView("wish-list", model);
	}

}
