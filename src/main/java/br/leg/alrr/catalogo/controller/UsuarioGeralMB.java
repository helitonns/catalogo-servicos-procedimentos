package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Usuario;
import br.leg.alrr.catalogo.persistence.UsuarioDAO;
import br.leg.alrr.catalogo.util.Criptografia;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class UsuarioGeralMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private UsuarioDAO usuarioDAO;
    
    private ArrayList<Usuario> usuarios;

    private Usuario usuario;

    private String senha;

    private boolean removerUsuario;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();
    }


    public void listarTodosUsuarios() {
        try {
            usuarios = (ArrayList<Usuario>) usuarioDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }
    
    public String salvarUsuario() {
        try {
            //verifca se a senha de usuário é forte, se sim permite o cadastro
            if (verificarForcaDaSenha(senha)) {
                if (usuario.getId() != null) {
                    usuario.setSenha(Criptografia.criptografarEmMD5(senha));
                    usuarioDAO.atualizar(usuario);
                    FacesUtils.addInfoMessageFlashScoped("Usuário atualizado com sucesso!");
                } else {
                    //verifica se já há usuario cadastrado com o mesmo login
                    if (!usuarioDAO.haUsuarioComEsteLogin(usuario.getLogin())) {
                        usuario.setSenha(Criptografia.criptografarEmMD5(senha));
                        usuarioDAO.salvar(usuario);
                        FacesUtils.addInfoMessageFlashScoped("Usuário salvo com sucesso!");
                    } else {
                        FacesUtils.addWarnMessageFlashScoped("O usuário não pode ser cadastrado, pois já há um usuário com este mesmo login!!!");
                    }
                }
            } else {
                FacesUtils.addWarnMessageFlashScoped("A senha deve atender aos seguintes requisitos: ter no mínimo 8 caracteres, possuir letra minúcula 'a', possuir letra maiúscula 'A' e número '123'!!!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped("Erro ao salvar usuário!");
        }
        return "usuario-geral.xhtml" + "?faces-redirect=true";
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

    public void removerUsuario() {
        try {
            if (removerUsuario) {
                usuarioDAO.remover(usuario);
                FacesUtils.addInfoMessage("Usuário removido com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        limparForm();
    }
    
    private void limparForm() {
        usuarios = new ArrayList<>();
        usuario = new Usuario();

        removerUsuario = false;
        listarTodosUsuarios();
    }

    public String cancelar() {
        return "usuario-geral.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public boolean isRemoverUsuario() {
        return removerUsuario;
    }

    public void setRemoverUsuario(boolean removerUsuario) {
        this.removerUsuario = removerUsuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
