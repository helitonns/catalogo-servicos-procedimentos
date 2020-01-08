package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Atribuicao;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Atribuicao.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Atribuicao
 */
@Stateless
public class AtribuicaoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Atribuicao o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atribuição.", e);
        }
    }

    public void atualizar(Atribuicao o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar atribuição.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Atribuicao o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atribuuições.", e);
        }
    }
    
    public List listarTodos2(long id) throws DAOException{
        try {
        	return em.createQuery("select o from Atribuicao o where o.departamento.id =:id").setParameter("id",id).getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atribuuições.", e);
        }
    }
    
    
       public void remover(Atribuicao o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atribuição.", e);
        }
    }
}
