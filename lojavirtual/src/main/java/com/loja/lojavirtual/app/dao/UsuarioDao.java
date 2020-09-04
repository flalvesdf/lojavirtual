package com.loja.lojavirtual.app.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.loja.lojavirtual.app.domain.Usuario;

@Repository	
@Transactional
public class UsuarioDao {
	
	@PersistenceContext
    private EntityManager em;
	
	public void salvar(Usuario usu) {
		em.persist(usu);
	}

	public List<Usuario> recuperarTodos() {
		return em.createQuery("select a from Usuario a order by a.nome asc", Usuario.class).getResultList();
	}

	public Usuario recuperarPorId(Integer id) {
		return em.createQuery("select a from Usuario a where a.idUsuario = :idUsuario", Usuario.class).setParameter("idUsuario", id).getSingleResult();
	}
	
	public Usuario recuperarPorEmail(String email) {
		return em.createQuery("select a from Usuario a where a.email = :email", Usuario.class).setParameter("email", email).getSingleResult();
	}

	public void atualizar(Usuario usu) {
		em.merge(usu);
	}

	public void excluir(Integer id) {
		em.remove(em.getReference(Usuario.class, id));
	}
}
