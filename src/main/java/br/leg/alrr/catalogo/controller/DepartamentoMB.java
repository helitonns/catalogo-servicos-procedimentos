/**
 * 
 */
package br.leg.alrr.catalogo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;


import br.leg.alrr.catalogo.model.Ator;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.persistence.AtorDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;




/**
 * Classe de gerenciamento das regras de negócio para a entidade Departamento.
 * 
 * @author rafaell
 * @version 1.0
 * @since 2019-12-07
 * @see Departamento
 * @see DepartamentoDAO
 * @see AtorDAO
 *
 *
 */
@Named
@ViewScoped
public class DepartamentoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private DepartamentoDAO departamentoDAO;
	
	@EJB
	private AtorDAO atorDAO;
	
	
	
	private List<Ator> atores;
	private List<Ator> atoresSelecionados;
	private List<Ator> atoresAdicionados;
	
	private Departamento departamento;
	private Departamento departamentoPai;
	
	private List<Departamento> departamentos;
	private List<Departamento> departamentoPais;
	
	private Ator removerAtor;
	private Ator ator;
	private long idDeptoPai;
	private boolean ativarPai;
	private boolean deleteDepartamento;
	
	
	

	//==========================================================================
	
	@PostConstruct
    public void init() {
        iniciar();
        listarTodosOsDepartamentos();
        listarTodosOsAtores();
       
    }
	
	private void iniciar(){
		ator = new Ator();
        atores = new ArrayList<>();
        departamento = new Departamento();
        atoresSelecionados = new ArrayList<>();
        atoresAdicionados = new ArrayList<>();
        ativarPai = true;
        deleteDepartamento = false;
        idDeptoPai = 0l;;
        
        
        
    }
	
	public String salvarDepartamento() {
		
		 try {
			 departamento.setDepartamentoPai(departamentoPai);
			 departamento.setAtores(atoresAdicionados);
			 
			 if (departamento.getId() != null) {
				 departamentoDAO.atualizar(departamento);
	             FacesUtils.addInfoMessageFlashScoped("departamento atualizado com sucesso!");
	         } else {
	        	 	if (ativarPai == false) {
	        	 		departamento.setNivel(1);
	        	 		departamentoDAO.salvar(departamento);
						FacesUtils.addInfoMessageFlashScoped("departamento salvo com sucesso!");
	        	 	}else {
	        	 		departamentoDAO.salvar(departamento);
						FacesUtils.addInfoMessageFlashScoped("departamento salvo com sucesso!");
	        	 	}
	         }
			 limpaForm();
		 	}catch (DAOException e) {
				 // TODO Auto-generated catch block
				e.printStackTrace();
			 }
	         	
	         
		
		return "departamento.xhtml" + "?faces-redirect=true";
		
	}
	
	public void excluirDepartamento() {
        try {
        	
        	
            if (deleteDepartamento) {
            	
            	departamentoDAO.remover(departamento);
                departamentos = null;
                listarTodosOsDepartamentos();
                iniciar();
                FacesUtils.addInfoMessage("Departamento excluído com sucesso!");
            	}
            
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }
	
	
	 private void listarTodosOsDepartamentos() {
	        try {
	            departamentos = departamentoDAO.listarTodos();
	        } catch (NullPointerException e) {
	        } catch (DAOException e) {
	            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
	        }
	    }
	
	 private void listarTodosOsAtores() {
	        try {
	            atores = atorDAO.listarTodos();
	        } catch (NullPointerException e) {
	        } catch (DAOException e) {
	            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
	        }
	    }
	
	 private void listarTodosOsDepartamentosPai() {
	        try {
	            departamentoPais = departamentoDAO.listarTodosDepartamentoPai();
	        } catch (NullPointerException e) {
	        } catch (DAOException e) {
	            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
	        }
	    }
	
	public void adicionarAtor() {
        
        for (Ator at : atoresSelecionados) {
            atoresAdicionados.add(at);
         
        }
    }
	
   
	
	public void removerAtor() {
        try {
            for (Ator a : atoresAdicionados) {
                if (a == removerAtor) {
                    atoresAdicionados.remove(a);
                   
                }
            }
        } catch (Exception e) {
        }
    }
	
	public String cancelar() {
        return "departamento.xhtml" + "?faces-redirect=true";
    }
	
	private void limpaForm(){
		ator = new Ator();
        atores = new ArrayList<>();
        departamento = new Departamento();
        atoresSelecionados = new ArrayList<>();
        atoresAdicionados = new ArrayList<>();
        departamentoPai = new Departamento();
	}

	
	//=============================GETTERS E SETTERS=============================================
	
	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	public List<Ator> getAtores() {
		return atores;
	}

	public void setAtores(List<Ator> atores) {
		this.atores = atores;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}


	public List<Ator> getAtoresSelecionados() {
		return atoresSelecionados;
	}

	public void setAtoresSelecionados(List<Ator> atoresSelecionados) {
		this.atoresSelecionados = atoresSelecionados;
	}

	public List<Ator> getAtoresAdicionados() {
		return atoresAdicionados;
	}

	public void setAtoresAdicionados(List<Ator> atoresAdicionados) {
		this.atoresAdicionados = atoresAdicionados;
	}

	public Departamento getDepartamentoPai() {
		return departamentoPai;
	}

	public void setDepartamentoPai(Departamento departamentoPai) {
		this.departamentoPai = departamentoPai;
	}

	public Ator getRemoverAtor() {
		return removerAtor;
	}

	public void setRemoverAtor(Ator removerAtor) {
		this.removerAtor = removerAtor;
	}

	public Ator getAtor() {
		return ator;
	}

	public void setAtor(Ator ator) {
		this.ator = ator;
	}

	public List<Departamento> getDepartamentoPais() {
		return departamentoPais;
	}

	public void setDepartamentoPais(List<Departamento> departamentoPais) {
		this.departamentoPais = departamentoPais;
	}

	public boolean isAtivarPai() {
		return ativarPai;
	}

	public void setAtivarPai(boolean ativarPai) {
		this.ativarPai = ativarPai;
	}

	public long getIdDeptoPai() {
		return idDeptoPai;
	}

	public void setIdDeptoPai(long idDeptoPai) {
		this.idDeptoPai = idDeptoPai;
	}

	public boolean isDeleteDepartamento() {
		return deleteDepartamento;
	}

	public void setDeleteDepartamento(boolean deleteDepartamento) {
		this.deleteDepartamento = deleteDepartamento;
	}
	
	
	
	

}
