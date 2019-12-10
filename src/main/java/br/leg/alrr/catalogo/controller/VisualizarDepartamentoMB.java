package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;

import javax.annotation.PostConstruct;
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

    private Departamento departamento;

    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        
        
        if (FacesUtils.getBean("departamento") != null) {
            departamento = (Departamento) FacesUtils.getBean("departamento");
            FacesUtils.removeBean("departamento");
        }
    }

  
    private void iniciar() {
        departamento = new Departamento();
    }
    
    //==========================================================================

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    
}
