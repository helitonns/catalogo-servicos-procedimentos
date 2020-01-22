package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.DuvidaFrequente;
import br.leg.alrr.catalogo.model.UsuarioComDepartamento;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.DuvidaFrequenteDAO;
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
 * Classe de gerenciamento das regras de negócio para a entidade DuvidaFrequente.
 *
 * @author Heliton Nascimento
 * @since 2020-01-22
 * @version 1.0
 * @see DuvidaFrequente
 * @see DuvidaFrequenteDAO
 * @see Departamento
 */
@ViewScoped
@Named
public class DuvidaFrequenteMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DuvidaFrequenteDAO duvidaFrequenteDAO;
    
    @EJB
    private DepartamentoDAO departamentoDAO;
    
    private List<DuvidaFrequente> duvidasFrequentes;
    private List<Departamento> departamentos;

    private DuvidaFrequente duvidaFrequente;
    private Departamento departamento;
    
    private boolean excluirDuvida;


    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        
        listarDepartamentos();
        
        try {
            //VERIFICA SE HÁ DEPARTAMETO NA SESSÃO
            if (FacesUtils.getBean("departamento") != null) {
                departamento = (Departamento) FacesUtils.getBean("departamento");
                FacesUtils.removeBean("departamento");
                listarTodasAsDuvidasFrequentesDoDepartamento();
            } else{
                Autorizacao a = (Autorizacao) FacesUtils.getBean("autorizacao");
                if (!a.getPrivilegio().getDescricao().equalsIgnoreCase("SUPERADMIN")) {
                    UsuarioComDepartamento u = (UsuarioComDepartamento) FacesUtils.getBean("usuario");
                    departamento = u.getDepartamento();
                    listarTodasAsDuvidasFrequentesDoDepartamento();
                }
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar acessar atribuções.");
        }
    }

    private void iniciar() {
        duvidasFrequentes = new ArrayList<>();
        departamentos = new ArrayList<>();

        duvidaFrequente = new DuvidaFrequente();
        duvidaFrequente.setStatus(true);
        
        departamento = new Departamento();
        
        excluirDuvida = false;
    }

    @PreDestroy
    public void finalizar() {
        duvidaFrequenteDAO = null;
        duvidasFrequentes = null;
        duvidaFrequente = null;
        departamento = null;
    }

    public String salvarDuvidaFrequente() {
        try {
            duvidaFrequente.setDepartamento(departamento);
            if (duvidaFrequente.getId() != null) {
                duvidaFrequenteDAO.atualizar(duvidaFrequente);
                FacesUtils.addInfoMessageFlashScoped("Dúvida frequente atualizada com sucesso!");
            } else {
                duvidaFrequenteDAO.salvar(duvidaFrequente);
                FacesUtils.addInfoMessageFlashScoped("Dúvida frequente salva com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "duvida-frequente.xhtml" + "?faces-redirect=true";
    }

    private void listarTodasAsDuvidasFrequentesDoDepartamento() {
        try {
            duvidasFrequentes = duvidaFrequenteDAO.listarTodasAsDuvidasFrequentesAtivasPorDepartamento(departamento);
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    private void listarDepartamentos(){
        try {
            departamentos = departamentoDAO.listarTodosAtivos();
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public String excluirDuvidaFrequente() {
        try {
            if (excluirDuvida) {
                duvidaFrequenteDAO.remover(duvidaFrequente);
                FacesUtils.addInfoMessageFlashScoped("Dúvida excluída com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "duvida-frequente.xhtml" + "?faces-redirect=true";
    }

    
    public String cancelar() {
        return "duvida-frequente.xhtml" + "?faces-redirect=true";
    }
    
    //==========================================================================
    public DuvidaFrequente getDuvidaFrequente() {
        return duvidaFrequente;
    }

    public void setDuvidaFrequente(DuvidaFrequente duvidaFrequente) {
        this.duvidaFrequente = duvidaFrequente;
    }

    public List<DuvidaFrequente> getDuvidasFrequentes() {
        return duvidasFrequentes;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public boolean isExcluirDuvida() {
        return excluirDuvida;
    }

    public void setExcluirDuvida(boolean excluirDuvida) {
        this.excluirDuvida = excluirDuvida;
    }
    
    
}
