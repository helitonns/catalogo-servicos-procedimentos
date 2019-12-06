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
    
    private Ator ator;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        listarTodosOsAtores();
    }
    
    private void iniciar(){
        atores = new ArrayList<>();
        ator = new Ator();
    }
    
    @PreDestroy
    public void sair(){
        atorDAO = null;
        atores = null;
        ator = null;
    }
    
    public String salvarAtor() {
        try {
            if (ator.getId() != null) {
                atorDAO.atualizar(ator);
                FacesUtils.addInfoMessageFlashScoped("Ator atualizado com sucesso!");
            } else {
                atorDAO.salvar(ator);
                FacesUtils.addInfoMessageFlashScoped("Ator salvo com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "ator.xhtml" + "?faces-redirect=true";
    }
    
    private void listarTodosOsAtores(){
        try {
            atores = atorDAO.listarTodos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
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

}
