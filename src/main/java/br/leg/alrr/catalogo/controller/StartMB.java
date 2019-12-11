package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Usuario;
import br.leg.alrr.catalogo.persistence.AutorizacaoDAO;
import br.leg.alrr.catalogo.persistence.UsuarioDAO;
import br.leg.alrr.catalogo.util.Criptografia;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@SessionScoped
public class StartMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    @EJB
    private UsuarioDAO usuarioDAO;

    private Usuario usuario;
    private Autorizacao autorizacao;
    
    private String login = "";
    private String senha = "";
    private String senha1 = "";

    //===========================================================
    @PostConstruct
    private void init() {
        usuario = new Usuario();
    }

    public String logar2() {
        try {
            String[] s = FacesUtils.getURL().split("/");

            autorizacao = autorizacaoDAO.verificarSeOUsuarioPossuiAutorizacao(s[1], login, Criptografia.criptografarEmMD5(senha));
            
            //ENCONTROU UM USUARIO COM AUTORIZAÇÃO
            if (autorizacao != null) {
                usuario = usuarioDAO.pesquisarPorLoginESenha(login, Criptografia.criptografarEmMD5(senha));
                FacesUtils.setBean("usuario", usuario);
                FacesUtils.setBean("autorizacao", autorizacao);
                return "/pages/superadmin/autorizacao.xhtml" + "?faces-redirect=true";
            } else {
                FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Usuário e/ou senha incorreto");
            System.out.println(e.getMessage());
        }

        return "/login.xhtml" + "?faces-redirect=true";
    }

    public String sair() {
        try {
            FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        } catch (Exception e) {
        }
        return "/login.xhtml" + "?faces-redirect=true";
    }

    public void trocarSenha() {
        try {
            if (verificarForcaDaSenha(senha1)) {
                usuario.setSenha(Criptografia.criptografarEmMD5(senha1));
                usuarioDAO.atualizar(usuario);
                FacesUtils.addInfoMessage("Senha atualizada com sucesso!!!");
            } else {
                FacesUtils.addWarnMessage("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', "
                        + "possuir letra maiúscula 'A' e número '123'!!!");
            }

        } catch (DAOException e) {
            FacesUtils.addInfoMessage("Senha atualizada com sucesso!!!");
        }
    }

    private boolean verificarForcaDaSenha(String senha) {
        if (senha.length() < 8) {
            return false;
        }

        boolean achouNumero = false;
        boolean achouMaiuscula = false;
        boolean achouMinuscula = false;
        boolean achouSimbolo = false;

        for (char c : senha.toCharArray()) {
            if (c >= '0' && c <= '9') {
                achouNumero = true;
            } else if (c >= 'A' && c <= 'Z') {
                achouMaiuscula = true;
            } else if (c >= 'a' && c <= 'z') {
                achouMinuscula = true;
            } else {
                achouSimbolo = true;
            }
        }

        //defini os parâmetros que serão avalidados
        //neste caso não irá levar em consideração o requisito "símbolo"
        return achouNumero && achouMaiuscula && achouMinuscula;
    }

    //===========================================================
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean isUsuarioRelatorio() {
        return autorizacao.getPrivilegio().getDescricao().equals("RELATORIO");
    }
    
    public boolean isOperador() {
        return autorizacao.getPrivilegio().getDescricao().equals("OPERADOR");
    }

    public boolean isAdmin() {
        return autorizacao.getPrivilegio().getDescricao().equals("ADMIN");
    }

    public boolean isSuperAdmin() {
        return autorizacao.getPrivilegio().getDescricao().equals("SUPER_ADMIN");
    }
    
    public String getSenha1() {
        return senha1;
    }

    public void setSenha1(String senha1) {
        this.senha1 = senha1;
    }

}
