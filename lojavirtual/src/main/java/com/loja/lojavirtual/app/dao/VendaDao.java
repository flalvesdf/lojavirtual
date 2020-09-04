package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.loja.lojavirtual.app.domain.Venda;

@Repository	
public class VendaDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Venda v) {
		em.persist(v);
	}

	public List<Venda> recuperarTodos() {
		return em.createQuery("select a from Venda a order by a.nome asc", Venda.class).getResultList();
	}

	public List<Venda> recuperarPorId(Integer id) {
		return em.createQuery("select a from Venda a where a.id = :id", Venda.class).setParameter("id", id).getResultList();
	}
	
	public List<Venda> recuperarVendasProduto(Integer idProduto) {
		return em.createQuery("select a from Venda a where a.idProduto = :idProduto", Venda.class).setParameter("idProduto", idProduto).getResultList();
	}
	
	public List<Venda> recuperarVendasUsuario(Integer idUsuario) {
		return em.createQuery("select a from Venda a where a.idUsuario = :idUsuario", Venda.class).setParameter("idUsuario", idUsuario).getResultList();
	}
	
	public List<Venda> recuperarVendasUsuarioNaoFinalizada(Integer idUsuario) {
		return em.createQuery("select a from Venda a where a.vendaFinalizada = `N` and a.idUsuario = :idUsuario", Venda.class).setParameter("idUsuario", idUsuario).getResultList();
	}

	public void atualizar(Venda v) {
		em.merge(v);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Venda.class, id));
	}
}
