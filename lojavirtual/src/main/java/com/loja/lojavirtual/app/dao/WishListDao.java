package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.WishList;

@Repository	
@Transactional
public class WishListDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(WishList wl) {
		em.persist(wl);
	}

	public List<WishList> recuperarTodos() {
		return em.createQuery("select a from WishList a order by a.data asc", WishList.class).getResultList();
	}

	public List<WishList> recuperarPorId(Integer id) {
		return em.createQuery("select a from WishList a where a.id.idusuwl = :id", WishList.class).setParameter("id", id).getResultList();
	}

	public void atualizar(WishList wl) {
		em.merge(wl);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(WishList.class, id));
	}
}
