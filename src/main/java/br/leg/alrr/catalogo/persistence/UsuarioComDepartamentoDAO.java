package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.UsuarioComDepartamento;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade UsuarioComDepartamento.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see UsuarioComDepartamento
 */
@Stateless
public class UsuarioComDepartamentoDAO{

    @PersistenceContext
    protected EntityManager em;

    public void salvar(UsuarioComDepartamento o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar usuário.", e);
        }
    }

    public void atualizar(UsuarioComDepartamento o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar usuário.", e);
        }
    }

    public List listarTodos() throws DAOException{
        try {
            return em.createQuery("select o from UsuarioComDepartamento o order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosAtivos() throws DAOException{
        try {
            return em.createQuery("select o from UsuarioComDepartamento o where o.status = true order by o.login asc").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários.", e);
        }
    }
    
    public List listarTodosPorDepartamento(Departamento d) throws DAOException{
        try {
            return em.createQuery("select o from UsuarioComDepartamento o where o.departamento = :idDepartamento order by o.login asc")
                    .setParameter("idDepartamento", d.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários por departamento.", e);
        }
    }
    
    public List listarTodosPorDepartamentoSemOSuperAdmin(Departamento u) throws DAOException{
        try {
            return em.createQuery("select o from UsuarioComDepartamento o where o.departamento = :idDepartamento and o. != 'SUPER_ADMIN' order by o.login asc")
                    .setParameter("idDepartamento", u.getId())
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar usuários por unidade.", e);
        }
    }

    public void remover(UsuarioComDepartamento o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover usuário.", e);
        }
    }

    public UsuarioComDepartamento pesquisarPorLogin(String login) throws DAOException{
        try {
            return (UsuarioComDepartamento) em.createQuery("select u from UsuarioComDepartamento u where u.login =:login")
                    .setParameter("login", login)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }

    public UsuarioComDepartamento pesquisarPorLoginESenha(String login, String senha) throws DAOException{
        try {
            return (UsuarioComDepartamento) em.createQuery("select u from UsuarioComDepartamento u where u.login =:login and u.senha =:senha")
                    .setParameter("login", login)
                    .setParameter("senha", senha)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login e senha.", e);
        }
    }
    
    public boolean haUsuarioComDepartamentoComEsteLogin(String login) throws DAOException{
        try {
            return em.createQuery("select u from UsuarioComDepartamento u where u.login =:login")
                    .setParameter("login", login)
                    .getResultList().size() >= 1;
        } catch (Exception e) {
            throw new DAOException("Erro ao pesquisar usuário por login.", e);
        }
    }
}
