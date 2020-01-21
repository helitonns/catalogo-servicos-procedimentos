package br.leg.alrr.catalogo.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.leg.alrr.catalogo.model.Atribuicao;
import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.UsuarioComDepartamento;
import br.leg.alrr.catalogo.persistence.AtribuicaoDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import javax.faces.event.ValueChangeEvent;

/**
 * Classe de gerenciamento das regras de negócio para a entidade Atribuicao.
 *
 * @author Rafael
 * @version 1.0
 * @since 2019-12-27
 * @see Atribuicao
 * @see Departamento
 * @see DepartamentoDAO
 * @see AtribuicaoDAO
 *
 */
@Named
@ViewScoped
public class AtribuicaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AtribuicaoDAO atribuicaoDAO;

    @EJB
    private DepartamentoDAO departamentoDAO;

    private Atribuicao atribuicao;
    private Departamento departamento;

    private List<Atribuicao> atribuicoes;
    private List<Departamento> departamentos;

    private boolean removerAtribuicao = false;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        try {
            //VERIFICA SE HÁ DEPARTAMETO NA SESSÃO
            if (FacesUtils.getBean("departamento") != null) {
                departamento = (Departamento) FacesUtils.getBean("departamento");
                FacesUtils.removeBean("departamento");
                listarTodasAtribuicoesPorDepartamento();
            } else{
                Autorizacao a = (Autorizacao) FacesUtils.getBean("autorizacao");
                if (!a.getPrivilegio().getDescricao().equalsIgnoreCase("SUPERADMIN")) {
                    UsuarioComDepartamento u = (UsuarioComDepartamento) FacesUtils.getBean("usuario");
                    departamento = u.getDepartamento();
                    listarTodasAtribuicoesPorDepartamento();
                }
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar acessar atribuções.");
        }
    }

    private void iniciar() {
        atribuicao = new Atribuicao();
        departamento = new Departamento();
        atribuicoes = new ArrayList<>();
        departamentos = new ArrayList<>();
        listarTodosDepartamentos();
    }

    public String salvarAtribuicao() {
        try {
            atribuicao.setDepartamento(departamento);

            if (atribuicao.getId() != null) {
                atribuicaoDAO.atualizar(atribuicao);

                FacesUtils.addInfoMessageFlashScoped("Atribuição atualizada com sucesso!");
            } else {
                atribuicaoDAO.salvar(atribuicao);
                FacesUtils.addInfoMessageFlashScoped("Atribuição salva com sucesso!");
            }
            iniciar();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "atribuicao.xhtml" + "?faces-redirect=true";
    }

    public String excluirAtribuicao() {
        try {
            if (removerAtribuicao) {
                atribuicaoDAO.remover(atribuicao);
                FacesUtils.addInfoMessage("Atribuicao removida com sucesso!");
            }
            iniciar();
        } catch (DAOException e) {
            System.out.println(e.getCause());
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "atribuicao.xhtml" + "?faces-redirect=true";
    }

    public void selecionarDepartamento(ValueChangeEvent event) {
        try {
            if (event.getNewValue() != null) {
                departamento.setId(Long.parseLong(event.getNewValue().toString()));
                listarTodasAtribuicoesPorDepartamento();
            }
        } catch (NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarTodasAtribuicoesPorDepartamento() {
        try {
            atribuicoes = atribuicaoDAO.listarAtribuicoesPorDepartamento(departamento);
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    private void listarTodosDepartamentos() {
        try {
            departamentos = departamentoDAO.listarTodos();

        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public String redirecionarParaFluxoDeTrabalho() {
        FacesUtils.setBean("departamento", departamento);
        FacesUtils.setBean("atribuicao", atribuicao);
        return "fluxo-de-trabalho.xhtml" + "?faces-redirect=true";
    }

    public String cancelar() {
        return "atribuicao.xhtml" + "?faces-redirect=true";
    }

    //============================= GETTERS E SETTERS ==========================
    public Atribuicao getAtribuicao() {
        return atribuicao;
    }

    public void setAtribuicao(Atribuicao atribuicao) {
        this.atribuicao = atribuicao;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Atribuicao> getAtribuicoes() {
        return atribuicoes;
    }

    public void setAtribuicoes(List<Atribuicao> atribuicoes) {
        this.atribuicoes = atribuicoes;
    }

    public List<Departamento> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Departamento> departamentos) {
        this.departamentos = departamentos;
    }

    public boolean isRemoverAtribuicao() {
        return removerAtribuicao;
    }

    public void setRemoverAtribuicao(boolean removerAtribuicao) {
        this.removerAtribuicao = removerAtribuicao;
    }

}
