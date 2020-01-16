package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Atividade;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Atividade.
 *
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Atividade
 */
@Stateless
public class AtividadeDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Atividade o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar atividade.", e);
        }
    }

    public void atualizar(Atividade o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar atividade.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from Atividade o order by o.descricao asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar atividades.", e);
        }
    }
    
    public int removerAtividadePorId(Atividade a) throws DAOException {
        try {
            return em.createNativeQuery("DELETE FROM catalogo_servicos_procedimentos.atividade a WHERE a.id = :idAtividade")
                    .setParameter("idAtividade", a.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atividades.", e);
        }
    }

    public void remover(Atividade o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover atividade.", e);
        }
    }
}
