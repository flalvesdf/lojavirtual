package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.Log;

@Repository	
@Transactional
public class LogDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Log log) {
		em.persist(log);
	}

	public List<Log> recuperarTodos() {
		return em.createQuery("select a from Log a order by a.dataHoraLog asc", Log.class).getResultList();
	}

	public Log recuperarPorId(Integer id) {
		return em.createQuery("select a from Log a where a.id = :id", Log.class).setParameter("id", id).getSingleResult();
	}

	public void atualizar(Log log) {
		em.merge(log);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Log.class, id));
	}
}
