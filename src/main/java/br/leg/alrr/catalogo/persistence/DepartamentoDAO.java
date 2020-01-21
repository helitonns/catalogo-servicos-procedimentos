package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Departamento.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Departamento
 */
@Stateless
public class DepartamentoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(Departamento o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar departamento.", e);
        }
    }

    public void atualizar(Departamento o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar departamento.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from Departamento o order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar departamentos.", e);
        }
    }
    
    public List listarTodosDepartamentoPai() throws DAOException{
        try {
            return em.createQuery("select o from Departamento o.departamentoPai order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar departamentos pai.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from Departamento o where o.status = true order by o.nome asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar sistemas.", e);
        }
    }
    
    public void remover(Departamento o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover departamento.", e);
        }
    }
    
    public int pesquisarOMaiorNivelDosDepartamentos() throws DAOException{
        try {
            return (int) em.createQuery("select MAX(o.nivel) from Departamento o where o.status = true").getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar o maior nível dos departamentos.", e);
        }
    }
    
    public List pesquisarDepartamentosPorNivel(int nivel) throws DAOException{
        try {
            return em.createQuery("select o from Departamento o where o.nivel = :nivel and o.status = true order by o.nome asc")
                    .setParameter("nivel", nivel)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar departamentos por nível.", e);
        }
    }
    
    public long buscarDepartamentoPaiPorId(long id) throws DAOException{
        try {
            return (long) em.createQuery("select o from Departamento o where o.departamentoPai = :id")
            		.setParameter("id", id)
            		.getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar departamento Pai por id.", e);
        }
    }
    
    public Departamento buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Departamento.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar Departamento por ID.", e);
        }
    }
    
    public int salvarDepartamentoParaUsuario(Long idUsuario, Long idDepartamento) throws DAOException{
        try {
            return  em.createNativeQuery("INSERT INTO catalogo_servicos_procedimentos.usuariocomdepartamento(id, departamento_id) VALUES (:idUsuario, :idDepartamento)")
                    .setParameter("idUsuario", idUsuario)
                    .setParameter("idDepartamento", idDepartamento)
                    .executeUpdate();
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar departamento para o usuário.", e);
        }
    }
}
