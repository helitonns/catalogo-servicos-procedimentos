package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Departamento;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.leg.alrr.catalogo.model.Documento;
import br.leg.alrr.catalogo.model.FluxoDeTrabalho;
import br.leg.alrr.catalogo.util.DAOException;

/**
 * Classe que gerencia a persistência da entidade Documento.
 * 
 * @author Rafael
 * @since 2019-12-30
 * @version 1.0
 * @see Documento
 */

@Stateless
public class DocumentoDAO {
	
	@PersistenceContext
    protected EntityManager em;

    public void salvar(Documento o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar o documento.", e);
        }
    }
    
    public void atualizar(Documento o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar departamento.", e);
        }
    }
    
    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Documento o order by o.id asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar documentos.", e);
        }
    }
    
    public Documento buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Documento.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar o Documento por ID.", e);
        }
    }

    public List listarDocumentosPorFluxoDeTrabalho(FluxoDeTrabalho ft) throws DAOException{
        try {
            return em.createQuery("select o from Documento o where o.fluxoDeTrabalho.id = :idFluxo order by o.id asc")
                    .setParameter("idFluxo", ft.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar documentos por fluxo de trabalho: "+e.getCause(), e);
        }
    }
    
    public List listarDocumentosPorDepartamento(Departamento d) throws DAOException{
        try {
            return em.createQuery("select o from Documento o where o.departamento.id = :idDepartamento order by o.id asc")
                    .setParameter("idDepartamento", d.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar documentos por departamento.", e);
        }
    }
    
    public List listarDocumentosAtivosPorDepartamento(Departamento d) throws DAOException{
        try {
            return em.createQuery("select o from Documento o where o.departamento.id = :idDepartamento and o.status = true order by o.id asc")
                    .setParameter("idDepartamento", d.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar documentos por departamento.", e);
        }
    }
    
    public int removerDocumentoPorId(Documento d) throws DAOException {
        try {
            return em.createNativeQuery("DELETE FROM catalogo_servicos_procedimentos.documento d WHERE d.id = :idDocumento")
                    .setParameter("idDocumento", d.getId())
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao remover documento.", e);
        }
    }
    
}
