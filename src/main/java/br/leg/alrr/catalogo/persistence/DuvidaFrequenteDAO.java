package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.DuvidaFrequente;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade DuvidaFrequente.
 *
 * @author Heliton Nascimento
 * @since 2020-01-22
 * @version 1.0
 * @see DuvidaFrequente
 */
@Stateless
public class DuvidaFrequenteDAO {

    @PersistenceContext
    protected EntityManager em;

    public void salvar(DuvidaFrequente o) throws DAOException {
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar dúvidas frequentes.", e);
        }
    }

    public void atualizar(DuvidaFrequente o) throws DAOException {
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar dúvidas frequentes.", e);
        }
    }

    public List listarTodos() throws DAOException {
        try {
            return em.createQuery("select o from DuvidaFrequente o").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar dúvidas frequentes.", e);
        }
    }
    
    public List listarTodasAsDuvidasFrequentesAtivas() throws DAOException {
        try {
            return em.createQuery("select o from DuvidaFrequente o where o.status = TRUE").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar dúvidas frequentes.", e);
        }
    }
    
    public List listarTodasAsDuvidasFrequentesAtivasPorDepartamento(Departamento d) throws DAOException {
        try {
            return em.createQuery("select o from DuvidaFrequente o where o.departamento.id = :idDepartamento AND o.status = TRUE")
                    .setParameter("idDepartamento", d.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar dúvidas frequentes.", e);
        }
    }

    public void remover(DuvidaFrequente o) throws DAOException {
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover dúvidas frequentes.", e);
        }
    }
}
