package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.AtribuicaoAtor;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade AtribuicaoAtor.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see AtribuicaoAtor
 */
@Stateless
public class AtribuicaoAtorDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(AtribuicaoAtor o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atribuição.", e);
        }
    }

    public void atualizar(AtribuicaoAtor o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar atribuição.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from AtribuicaoAtor o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atribuuições.", e);
        }
    }
    
       public void remover(AtribuicaoAtor o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atribuição.", e);
        }
    }
}
