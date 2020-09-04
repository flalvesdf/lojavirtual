package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.loja.lojavirtual.app.domain.Produtos;

@Repository	
public class ProdutoDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Produtos prd) {
		em.persist(prd);
	}

	public List<Produtos> recuperarTodos() {
		return em.createQuery("select a from Produtos a order by a.nome asc", Produtos.class).getResultList();
	}

	public Produtos recuperarPorId(Integer id) {
		return em.createQuery("select a from Produtos a where a.id = :id", Produtos.class).setParameter("id", id).getSingleResult();
	}

	public void atualizar(Produtos prd) {
		em.merge(prd);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Produtos.class, id));
	}
}
