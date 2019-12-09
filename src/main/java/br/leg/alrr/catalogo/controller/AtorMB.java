package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Ator;
import br.leg.alrr.catalogo.model.Documento;
import br.leg.alrr.catalogo.persistence.AtorDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 * Classe de gerenciamento das regras de negócio para a entidade Ator.
 *
 * @author Heliton Nascimento
 * @version 1.0
 * @since 2019-12-06
 * @see Ator
 * @see AtorDAO
 * @see Documento
 */
@ViewScoped
@Named
public class AtorMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AtorDAO atorDAO;
    
    private List<Ator> atores;
    private List<String> atribuicoesDoAtor;

    private Ator ator;
    private String atribuicaoAtor;

    private boolean excluirAtor;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
    }

    private void iniciar() {
        atores = new ArrayList<>();
        atribuicoesDoAtor = new ArrayList<>();

        ator = new Ator();
        ator.setStatus(true);
        atribuicaoAtor = new String();

        excluirAtor = false;

        listarTodosOsAtores();
    }

    @PreDestroy
    public void finalizar() {
        atorDAO = null;
        atores = null;
        ator = null;
        atribuicaoAtor = null;
        atribuicoesDoAtor = null;
    }

    public String salvarAtor() {
        try {
            if (ator.getId() != null) {
                ator.setAtribuicoes(atribuicoesDoAtor);
                atorDAO.atualizar(ator);
                FacesUtils.addInfoMessageFlashScoped("Ator atualizado com sucesso!");
            } else {
                ator.setAtribuicoes(atribuicoesDoAtor);
                atorDAO.salvar(ator);
                FacesUtils.addInfoMessageFlashScoped("Ator salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "ator.xhtml" + "?faces-redirect=true";
    }

    private void listarTodosOsAtores() {
        try {
            atores = atorDAO.listarTodos();
        } catch (NullPointerException e) {
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public void excluirAtor() {
        try {
            if (excluirAtor) {
                atorDAO.remover(ator);
                atores = null;
                listarTodosOsAtores();
                FacesUtils.addInfoMessage("Ator excluído com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void adcionarAtribuicao() {
        try {
            atribuicoesDoAtor.add(atribuicaoAtor);
            atribuicaoAtor = new String();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocorreu um erro ao adicionar uma atribuição!");
        }
    }

    public void excluirAtribuicao(){
        try {
            atribuicoesDoAtor.remove(atribuicaoAtor);
            atribuicaoAtor = new String();
        } catch (Exception e) {
            FacesUtils.addErrorMessage("Ocorreu um erro ao excluir uma atribuição!");
        }
    }
    
    public String cancelar() {
        return "ator.xhtml" + "?faces-redirect=true";
    }
    //==========================================================================

    public Ator getAtor() {
        return ator;
    }

    public void setAtor(Ator ator) {
        this.ator = ator;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public boolean isExcluirAtor() {
        return excluirAtor;
    }

    public void setExcluirAtor(boolean excluirAtor) {
        this.excluirAtor = excluirAtor;
    }

    public String getAtribuicaoAtor() {
        return atribuicaoAtor;
    }

    public void setAtribuicaoAtor(String atribuicaoAtor) {
        this.atribuicaoAtor = atribuicaoAtor;
    }

    public List<String> getAtribuicoesDoAtor() {
        return atribuicoesDoAtor;
    }

    public void setAtribuicoesDoAtor(List<String> atribuicoesDoAtor) {
        this.atribuicoesDoAtor = atribuicoesDoAtor;
    }
}
