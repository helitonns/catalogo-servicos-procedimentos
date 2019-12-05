package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.AtribuicaoDepartamento;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade AtribuicaoDepartamento.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see AtribuicaoDepartamento
 */
@Stateless
public class AtribuicaoDepartamentoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(AtribuicaoDepartamento o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atribuição.", e);
        }
    }

    public void atualizar(AtribuicaoDepartamento o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar atribuição.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from AtribuicaoDepartamento o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atribuuições.", e);
        }
    }
    
       public void remover(AtribuicaoDepartamento o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atribuição.", e);
        }
    }
}
