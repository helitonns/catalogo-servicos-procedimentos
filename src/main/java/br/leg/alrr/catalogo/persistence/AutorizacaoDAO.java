package br.leg.alrr.catalogo.persistence;

import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Privilegio;
import br.leg.alrr.catalogo.model.Sistema;
import br.leg.alrr.catalogo.model.Usuario;
import br.leg.alrr.catalogo.util.DAOException;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Classe que gerencia a persistência da entidade Autorizacao.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Autorizacao
 */
@Stateless
public class AutorizacaoDAO{

    @PersistenceContext
    protected EntityManager em;
    
    public void salvar(Autorizacao o) throws DAOException{
        try {
            em.persist(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao salvar autorização.", e);
        }
    }

    public void atualizar(Autorizacao o) throws DAOException{
        try {
            em.merge(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao atualizar autorização.", e);
        }
    }

    public List listarTodas() throws DAOException{
        try {
            return em.createQuery("select o from Autorizacao o order by o.privilegio.sistema.nome, o.usuario.login").getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }
    
    public List listarTodasPorChaveDoSistema(String chave) throws DAOException{
        try {
            return em.createQuery("select o from Autorizacao o where o.privilegio.sistema.chave =:chaveSistema order by o.usuario.login asc")
                    .setParameter("chaveSistema", chave)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }
    
    public List listarTodasPorChaveDoSistemaSemOsSuperAdmin(String chave) throws DAOException{
        try {
            return em.createQuery("select o from Autorizacao o where o.privilegio.sistema.chave =:chaveSistema and o.privilegio.descricao <> 'SUPER_ADMIN' order by o.usuario.login asc")
                    .setParameter("chaveSistema", chave)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }
    
    public boolean verificarSeHaAutorizacaoParaUsuarioPermissao(Usuario u, Privilegio p) throws DAOException{
        try {
            List l = em.createQuery("select o from Autorizacao o where o.usuario.id =:idUsuario and o.privilegio.id =:idPermissao")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("idPermissao", p.getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }
    
    public boolean verificarSeHaAutorizacaoParaUsuarioNoSistema(Usuario u, Sistema s) throws DAOException{
        try {
            List l = em.createQuery("select o from Autorizacao o where o.usuario.id =:idUsuario and o.privilegio.sistema.id =:idSistema")
                    .setParameter("idUsuario", u.getId())
                    .setParameter("idSistema", s.getId())
                    .getResultList();
            return l.size() > 0;
        } catch (Exception e) {
            throw new DAOException("Erro ao verificar se há usuário com autorização para o sistema.", e);
        }
    }
    
    public Autorizacao verificarSeOUsuarioPossuiAutorizacao(String chave, String login, String senha) throws DAOException{
        try {
            return (Autorizacao) em.createQuery("select a from Autorizacao a where a.privilegio.sistema.chave =:chaveSistema and a.usuario.login =:loginUsuario and a.usuario.senha =:senhaUsuario and a.usuario.status = true and a.status = true")
                    .setParameter("chaveSistema", chave)
                    .setParameter("loginUsuario", login)
                    .setParameter("senhaUsuario", senha)
                    .getSingleResult();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }
    
    public List listarUsuariosQueTemPermissaoNoSistema(String chave) throws DAOException{
        try {
            return  em.createQuery("select u from Autorizacao a inner join a.usuario u where a.privilegio.sistema.chave =:chaveSistema")
                    .setParameter("chaveSistema", chave)
                    .getResultList();
        } catch (Exception e) {
            throw new DAOException("Erro ao listar autorizaçãos.", e);
        }
    }

    public void remover(Autorizacao o) throws DAOException{
        try {
            o = em.merge(o);
            em.remove(o);
        } catch (Exception e) {
            throw new DAOException("Erro ao remover autorização.", e);
        }
    }
    
    public Autorizacao buscarPorID(Long id) throws DAOException{
        try {
            return em.find(Autorizacao.class, id);
        } catch (Exception e) {
            throw new DAOException("Erro ao buscar autorização por ID.", e);
        }
    }
    
}
