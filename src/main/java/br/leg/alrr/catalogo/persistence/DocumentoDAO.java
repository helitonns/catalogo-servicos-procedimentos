package br.leg.alrr.catalogo.persistence;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import br.leg.alrr.catalogo.model.Documento;
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

}
