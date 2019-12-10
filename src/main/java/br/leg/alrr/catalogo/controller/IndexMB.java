package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.OrganigramNode;

/**
 * Classe que gerenciará as regras de negócio na página index do sistema.
 *
 * @author Heliton Nascimento
 * @version 1.0
 * @since 2019-12-09
 * @see Departamento
 * @see DepartamentoDAO
 */
@ViewScoped
@Named
public class IndexMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DepartamentoDAO departamentoDAO;

    private ArrayList<DepartamentoNo> departamentosNos;

    private Departamento departamentoPai;

    private OrganigramNode rootNode;
    private OrganigramNode selecionado;

    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        construirOrgonograma();
    }

    private void construirOrgonograma() {
        try {
            int numeroMaximoDeNivel = departamentoDAO.pesquisarOMaiorNivelDosDepartamentos();
            
            List<Departamento> dd = departamentoDAO.pesquisarDepartamentosPorNivel(1);
            departamentoPai = dd.get(0);
            
            //seta o rootnode do organagama
            //o primeiro parâmetro representa a categoria do nó e ele receberá como valor o nível do departamento
            //o segundo parâmetro representa o nome do nó
            //o terceiro parâmetro representa o pai do nó
            rootNode = new DefaultOrganigramNode(""+departamentoPai.getNivel(), departamentoPai.getNome(), null);
            rootNode.setSelectable(true);
            //Adiciona o nó ao departamento através da classe privada DepartamentoNo
            departamentosNos.add(new DepartamentoNo(departamentoPai, rootNode));
            
            //Laço que vai construir os nós do organograma por nível
            for (int i = 2; i <= numeroMaximoDeNivel; i++) {
                List<Departamento> departamentosDoNivel = departamentoDAO.pesquisarDepartamentosPorNivel(i);
                
                //Laço para cada itém de departamentosDoNivel encontrar o seu pai
                for (Departamento d : departamentosDoNivel) {
                    
                    for (DepartamentoNo d2 : departamentosNos) {
                        //Se o departamentoPai do nó for igual a algun nó que já estaja na lista
                        if (d.getDepartamentoPai().getId().equals(d2.getDepartamentoDoNo().getId())) {
                            OrganigramNode no = new DefaultOrganigramNode("2", d.getNome(), d2.no);
                            no.setSelectable(true);
                            departamentosNos.add(new DepartamentoNo(d, no));
                            break;
                        }
                    }
                }
            }
            
            //Setando um filho para os nós que não possuem nenhum
            for (DepartamentoNo dn1 : departamentosNos) {
                if (dn1.getNo().getChildCount() == 0) {
                    OrganigramNode on = new DefaultOrganigramNode(""+dn1.getDepartamentoDoNo().getNivel(), "Servidores", dn1.getNo());
                    dn1.getNo().setExpanded(false);
                }
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void nodeSelectListener(OrganigramNodeSelectEvent event) throws IOException {
        String nomeDoDepartamento = (String) event.getOrganigramNode().getData();
        
        for (DepartamentoNo dn : departamentosNos) {
            if (dn.getDepartamentoDoNo().getNome().equals(nomeDoDepartamento)) {
                FacesUtils.setBean("departamento", dn.getDepartamentoDoNo());
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("visualizar-departamento.xhtml?faces-redirect=true");
    }
    
    private void iniciar() {
        departamentoPai = new Departamento();
        departamentosNos = new ArrayList<>();
    }

    public String cancelar() {
        return "index.xhtml" + "?faces-redirect=true";
    }
    // ==========================================================================

    public OrganigramNode getRootNode() {
        return rootNode;
    }

    public void setRootNode(OrganigramNode rootNode) {
        this.rootNode = rootNode;
    }

    public OrganigramNode getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(OrganigramNode selecionado) {
        this.selecionado = selecionado;
    }
    //==========================================================================

    private class DepartamentoNo {

        private Departamento departamentoDoNo;
        private OrganigramNode no;

        //======================================================================
        public DepartamentoNo() {
        }

        public DepartamentoNo(Departamento departamentoDoNo, OrganigramNode no) {
            this.departamentoDoNo = departamentoDoNo;
            this.no = no;
        }

        public Departamento getDepartamentoDoNo() {
            return departamentoDoNo;
        }

        public void setDepartamentoDoNo(Departamento departamentoDoNo) {
            this.departamentoDoNo = departamentoDoNo;
        }

        public OrganigramNode getNo() {
            return no;
        }

        public void setNo(OrganigramNode no) {
            this.no = no;
        }
    }

}
