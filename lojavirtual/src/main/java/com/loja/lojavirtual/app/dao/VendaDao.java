package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.Venda;

@Repository	
@Transactional
public class VendaDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Venda v) {
		em.persist(v);
	}

	public List<Venda> recuperarTodos() {
		return em.createQuery("select a from Venda a order by a.tsVenda desc", Venda.class).getResultList();
	}

	public List<Venda> recuperarPorId(Integer id) {
		return em.createQuery("select a from Venda a where a.id.id = :id", Venda.class).setParameter("id", id).getResultList();
	}
	
	public List<Venda> recuperarVendasProduto(Integer idProduto) {
		return em.createQuery("select a from Venda a where a.id.idProduto = :idProduto", Venda.class).setParameter("idProduto", idProduto).getResultList();
	}
	
	public List<Venda> recuperarVendasUsuario(Integer idUsuario) {
		return em.createQuery("select a from Venda a where a.id.idUsuario = :idUsuario", Venda.class).setParameter("idUsuario", idUsuario).getResultList();
	}
	
	public List<Venda> recuperarVendasUsuarioNaoFinalizada(Integer idUsuario) {
		return em.createQuery("select a from Venda a where a.vendaFinalizada = 'N' and a.id.idUsuario = :idUsuario", Venda.class).setParameter("idUsuario", idUsuario).getResultList();
	}
	
	public Integer getMaxId() {
		return em.createQuery("select max(a.id.id) from Venda a", Integer.class).getSingleResult();
	}
	
	public Integer recuperarVendaUsuarioNaoFinalizada(Integer idUsuario) {
		try {
			return em.createQuery("select distinct(a.id.id) from Venda a where a.vendaFinalizada = 'N' and a.id.idUsuario = :idUsuario", Integer.class).setParameter("idUsuario", idUsuario).getSingleResult();
		} catch (Exception e) {
			return 0;
		}
	}

	public void atualizar(Venda v) {
		em.merge(v);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Venda.class, id));
	}
}
