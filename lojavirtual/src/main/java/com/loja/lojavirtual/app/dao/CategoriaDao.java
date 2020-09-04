package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.Categoria;

@Repository	
@Transactional
public class CategoriaDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Categoria cat) {
		em.persist(cat);
	}

	public List<Categoria> recuperarTodos() {
		return em.createQuery("select a from Categoria a order by a.nome asc", Categoria.class).getResultList();
	}

	public Categoria recuperarPorId(Integer id) {
		return em.createQuery("select a from Categoria a where a.id = :id", Categoria.class).setParameter("id", id).getSingleResult();
	}

	public void atualizar(Categoria cat) {
		em.merge(cat);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Categoria.class, id));
	}
}
