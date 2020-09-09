package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.Produtos;

@Repository	
@Transactional
public class ProdutoDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Produtos prd) {
		em.persist(prd);
	}

	public List<Produtos> recuperarTodos() {
		return em.createQuery("select a from Produtos a order by a.nome asc", Produtos.class).getResultList();
	}
	
	public List<Produtos> recuperarTodosPorCategoria(Integer categoria) {
//		try {
//			return em.createQuery("select a from Produtos a where a.categoria = :cat", Produtos.class).setParameter("cat", categoria).getResultList();
//		} catch (Exception e) {
//			return null;
//		}
		Query query = em.createQuery("select a from Produtos a where a.categoria = "+ categoria);
	    @SuppressWarnings("unchecked")
		List<Produtos> resultList = query.getResultList();
		return resultList;
	}

	public Produtos recuperarPorId(Integer id) {
		return em.createQuery("select a from Produtos a where a.id = :id", Produtos.class).setParameter("id", id).getSingleResult();
	}
	
	public List<Produtos> recuperarPorNome(String nome) {
		Query query = em.createQuery("select a from Produtos a where lower(a.nome) like '%"+nome.toLowerCase()+"%'");
	    @SuppressWarnings("unchecked")
		List<Produtos> resultList = query.getResultList();
		return resultList;
	}

	public void atualizar(Produtos prd) {
		em.merge(prd);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Produtos.class, id));
	}
}
