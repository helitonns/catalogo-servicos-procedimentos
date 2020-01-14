package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Atividade;
import br.leg.alrr.catalogo.model.Ator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.leg.alrr.catalogo.model.Atribuicao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.FluxoDeTrabalho;
import br.leg.alrr.catalogo.persistence.AtorDAO;
import br.leg.alrr.catalogo.persistence.AtribuicaoDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.FluxoDeTrabalhoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import javax.faces.event.ValueChangeEvent;

/**
 * Classe de gerenciamento das regras de negócio para a entidade
 * FLuxoDeTrabalho.
 *
 * @author Heliton Nascimento
 * @version 1.0
 * @since 2020-01-13
 * @see Atribuicao
 * @see Departamento
 * @see DepartamentoDAO
 * @see AtribuicaoDAO
 * @see Ator
 * @see AtorDAO
 *
 */
@Named
@ViewScoped
public class FluxoDeTrabalhoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AtribuicaoDAO atribuicaoDAO;

    @EJB
    private DepartamentoDAO departamentoDAO;

    @EJB
    private FluxoDeTrabalhoDAO fluxoDeTrabalhoDAO;

    private List<Atribuicao> atribuicoes;
    private List<Departamento> departamentos;
    private List<Atividade> atividades;
    private List<Ator> atores;
    private List<Ator> atoresSelecionados;

    private Atribuicao atribuicao;
    private Departamento departamento;
    private Atividade atividade;
    private FluxoDeTrabalho fluxoDeTrabalho;

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
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar acessar atribuções.");
        }
    }

    private void iniciar() {
        atribuicao = new Atribuicao();
        departamento = new Departamento();
        atividade = new Atividade();
        fluxoDeTrabalho = new FluxoDeTrabalho();

        atribuicoes = new ArrayList<>();
        departamentos = new ArrayList<>();
        atores = new ArrayList<>();
        atividades = new ArrayList<>();

        listarTodosDepartamentos();
    }

    public String salvarFluxoDeTrabalho() {
        try {

            fluxoDeTrabalho.setAtribuicao(atribuicao);
            fluxoDeTrabalho.setAtividades(atividades);
            for (Atividade a : atividades) {
                a.setFluxoDeTrabalho(fluxoDeTrabalho);
            }
            
            if (atribuicao.getId() != null) {
                fluxoDeTrabalhoDAO.atualizar(fluxoDeTrabalho);
                FacesUtils.addInfoMessageFlashScoped("FLuxo de trabalho atualizado com sucesso!");
            } else {
                fluxoDeTrabalhoDAO.salvar(fluxoDeTrabalho);
                FacesUtils.addInfoMessageFlashScoped("Fluxo de trabalho salvo com sucesso!");
            }
            iniciar();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "fluxo-de-trabalho" + "?faces-redirect=true";
    }

    public void selecionarDepartamento(ValueChangeEvent event) {
        try {
            if (event.getNewValue() != null) {
                departamento.setId(Long.parseLong(event.getNewValue().toString()));
                listarTodasAtribuicoesPorDepartamento();

                for (Departamento d : departamentos) {
                    if (d.getId().equals(departamento.getId())) {
                        atores = d.getAtores();
                    }
                }
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

    public void adicionarAtividade() {
        if (!atividade.getDescricao().equals("")) {
            if (atoresSelecionados.size() > 0) {
                atividade.setAtores(atoresSelecionados);
                atoresSelecionados = new ArrayList<>();
                atividades.add(atividade);
                atividade = new Atividade();
            }else{
                FacesUtils.addErrorMessageFlashScoped("É necessário que tenha pelo menos um ator vinculado à atividade!");
            }
        } else {
            FacesUtils.addErrorMessageFlashScoped("A descrição da atividade é campo obrigatório!");

        }
    }

    public String cancelar() {
        return "fluxo-de-trabalho.xhtml" + "?faces-redirect=true";
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

    public List<Ator> getAtores() {
        return atores;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public Atividade getAtividade() {
        return atividade;
    }

    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    public List<Ator> getAtoresSelecionados() {
        return atoresSelecionados;
    }

    public void setAtoresSelecionados(List<Ator> atoresSelecionados) {
        this.atoresSelecionados = atoresSelecionados;
    }

    public FluxoDeTrabalho getFluxoDeTrabalho() {
        return fluxoDeTrabalho;
    }

    public void setFluxoDeTrabalho(FluxoDeTrabalho fluxoDeTrabalho) {
        this.fluxoDeTrabalho = fluxoDeTrabalho;
    }

}
