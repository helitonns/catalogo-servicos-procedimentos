package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Atribuicao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.persistence.AtribuicaoDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

/**
 * Classe que serve para visualizar o departamento selecionado.
 *
 * @author Heliton Nascimento
 * @version 1.0
 * @since 2019-12-10
 * @see Departamento
 * @see DepartamentoDAO
 */
@ViewScoped
@Named
public class VisualizarDepartamentoMB implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @EJB
    private AtribuicaoDAO atribuicaoDAO;

    private Departamento departamento;
    
    private List<Atribuicao> atribuicoesPorDepartamento;
    

    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        
        
        if (FacesUtils.getBean("departamento") != null) {
            departamento = (Departamento) FacesUtils.getBean("departamento");
            listarAtribuicoes();
            FacesUtils.removeBean("departamento");
        }
    }

  
    private void iniciar() {
        departamento = new Departamento();
        atribuicoesPorDepartamento = new ArrayList<>();
    }
    
    private void listarAtribuicoes() {
        try {
        	atribuicoesPorDepartamento = atribuicaoDAO.listarTodos2(departamento.getId());
        } catch (NullPointerException e) {
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    //==========================================================================

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }


	public List<Atribuicao> getAtribuicoesPorDepartamento() {
		return atribuicoesPorDepartamento;
	}


	public void setAtribuicoesPorDepartamento(List<Atribuicao> atribuicoesPorDepartamento) {
		this.atribuicoesPorDepartamento = atribuicoesPorDepartamento;
	}
    
    
    
    
}
