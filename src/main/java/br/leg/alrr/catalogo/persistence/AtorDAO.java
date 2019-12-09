package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Ator;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Ator.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Ator
 */
@Stateless
public class AtorDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Ator o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar ator.", e);
        }
    }

    public void atualizar(Ator o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar ator.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Ator o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atores.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Ator o where o.status = true order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar sistemas.", e);
        }
    }
    
    public void remover(Ator o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover ator.", e);
        }
    }
    
    public Ator pegarReferencia(Long idAtor) throws DAOException{
        try {
            return em.find(Ator.class, idAtor);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover ator.", e);
        }
    }
}
