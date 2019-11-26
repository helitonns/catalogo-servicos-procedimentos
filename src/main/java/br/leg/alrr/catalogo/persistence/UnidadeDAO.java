package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Unidade;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author heliton
 */
@Stateless
public class UnidadeDAO{

    @PersistenceContext
    protected EntityManager em;

     public void salvar(Unidade o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar unidade.", e);
        }
    }

    public void atualizar(Unidade o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar unidade.", e);
        }
    }
    
    public Unidade buscarReferencia(Unidade o) throws DAOException{
        try {
            return em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar unidade.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Unidade o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar unidades.", e);
        }
    }

    public void remover(Unidade o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover unidade.", e);
        }
    }
    
    public Unidade pesquisarPorID(Long id) throws DAOException{
        try {
            return (Unidade) em.createQuery("select o from Unidade o where o.id =:id")
                    .setParameter("id", id)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar unidade por ID.", e);
        }
    }
    
    public int salvarUnidadeParaUsuario(Long idUsuario, Long idUnidade) throws DAOException{
        try {
            return  em.createNativeQuery("INSERT INTO escolegis_academico.usuariocomunidade(id, unidade_id) VALUES (:idUsuario, :idUnidade)")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idUnidade", idUnidade)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar unidade para o usuário.", e);
        }
    }
}
