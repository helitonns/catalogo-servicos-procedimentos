package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.FluxoDeTrabalho;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade FluxoDeTrabalho.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see FluxoDeTrabalho
 */
public class FluxoDeTrabalhoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(FluxoDeTrabalho o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar fluxo de trabalho.", e);
        }
    }

    public void atualizar(FluxoDeTrabalho o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar fluxo de trabalho.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from FluxoDeTrabalho o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar fluxos de trabalho.", e);
        }
    }
    
    public void remover(FluxoDeTrabalho o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover fluxo de trabalho.", e);
        }
    }
}
