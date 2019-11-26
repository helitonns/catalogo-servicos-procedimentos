package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Privilegio;
import br.leg.alrr.catalogo.model.Sistema;
import br.leg.alrr.catalogo.persistence.PrivilegioDAO;
import br.leg.alrr.catalogo.persistence.SistemaDAO;
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
public class PrivilegioMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private PrivilegioDAO permissaoDAO;

    @EJB
    private SistemaDAO sistemaDAO;

    private Privilegio permissao;

    private ArrayList<Privilegio> permissoes;
    private ArrayList<Sistema> sistemas;

    private Long idSistema;
    private boolean removerPermissao = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        listarSistema();
        limparForm();
    }

    private void listarPermissao() {
        try {
            permissoes = (ArrayList<Privilegio>) permissaoDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarSistema() {
        try {
            sistemas = (ArrayList<Sistema>) sistemaDAO.listarTodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public String salvarPermissao() {
        try {
            permissao.setSistema(new Sistema(idSistema));

            if (permissao.getId() != null) {
                permissaoDAO.atualizar(permissao);
                FacesUtils.addInfoMessageFlashScoped("Permissão atualizada com sucesso!");
            } else {
                permissaoDAO.salvar(permissao);
                FacesUtils.addInfoMessageFlashScoped("Permissão salva com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "privilegio.xhtml" + "?faces-redirect=true";
    }

    public void removerPermissao() {
        try {
            if (removerPermissao) {
                permissaoDAO.remover(permissao);
                FacesUtils.addInfoMessage("Permissão removida com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        permissao = new Privilegio();
        permissao.setStatus(true);
        permissoes = new ArrayList<>();
        removerPermissao = false;
        listarPermissao();
        idSistema = 0l;
    }

    public String cancelar() {
        return "permissao.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public Privilegio getPermissao() {
        return permissao;
    }

    public void setPermissao(Privilegio permissao) {
        this.permissao = permissao;
    }

    public ArrayList<Privilegio> getPermissoes() {
        return permissoes;
    }

    public ArrayList<Sistema> getSistemas() {
        return sistemas;
    }

    public boolean isRemoverPermissao() {
        return removerPermissao;
    }

    public void setRemoverPermissao(boolean removerPermissao) {
        this.removerPermissao = removerPermissao;
    }

    public Long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

}
