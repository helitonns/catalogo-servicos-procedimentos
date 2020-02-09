package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.Mensagem;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.MensagemDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import static org.primefaces.behavior.confirm.ConfirmBehavior.PropertyKeys.message;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.organigram.OrganigramNodeSelectEvent;
import org.primefaces.model.DefaultOrganigramNode;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.OrganigramNode;
import org.primefaces.model.TreeNode;

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

    @EJB
    private MensagemDAO mensagemDAO;

    private ArrayList<DepartamentoNo> departamentosNos;
    private ArrayList<DepartamentoArvore> departamentosArvores;
    private ArrayList<Mensagem> mensagens;

    private Departamento departamentoPai;

    private OrganigramNode rootNode;
    private OrganigramNode selecionado;

    private TreeNode rootArvore;

    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        if (FacesUtils.getURL().contains("index2")) {
            construirArvore();
        } else {
            construirOrgonograma();
        }

        listarMensagens();
        System.out.println("Mensagens: "+mensagens.size());

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
            rootNode = new DefaultOrganigramNode("" + departamentoPai.getNivel(), departamentoPai.getNome(), null);
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

                            //Se o nível do nó for maior que 2 ele virá não expandido
                            if (i >= 2) {
                                no.setExpanded(false);
                            }

                            departamentosNos.add(new DepartamentoNo(d, no));
                            break;
                        }
                    }
                }
            }

            ArrayList<DepartamentoNo> departamentosNosDosFilhos = new ArrayList<>();
            //Setando um filho para os nós que não possuem nenhum
            for (DepartamentoNo dn1 : departamentosNos) {
                if (dn1.getNo().getChildCount() == 0) {
                    OrganigramNode on = new DefaultOrganigramNode("" + (dn1.getDepartamentoDoNo().getNivel() + 1), "Servidores", dn1.getNo());
                    dn1.getNo().setExpanded(false);

                    Departamento dep = new Departamento();
                    dep.setNivel(numeroMaximoDeNivel);
                    dep.setNome("servidores");
                    departamentosNosDosFilhos.add(new DepartamentoNo(dep, on));
                }
            }
            departamentosNos.addAll(departamentosNosDosFilhos);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void construirArvore() {
        try {
            int numeroMaximoDeNivel = departamentoDAO.pesquisarOMaiorNivelDosDepartamentos();

            List<Departamento> dd = departamentoDAO.pesquisarDepartamentosPorNivel(1);
            departamentoPai = dd.get(0);

            //seta o rootnode do organagama
            //o primeiro parâmetro representa a categoria do nó e ele receberá como valor o nível do departamento
            //o segundo parâmetro representa o nome do nó
            //o terceiro parâmetro representa o pai do nó
            rootArvore = new DefaultTreeNode("0", "unidades", null);

            //Adiciona o nó ao departamento através da classe privada DepartamentoArvore
            departamentosArvores.add(new DepartamentoArvore(new Departamento(0l), rootArvore));

            TreeNode na = new DefaultTreeNode("" + departamentoPai.getNivel(), departamentoPai, rootArvore);
            na.setExpanded(true);
            departamentosArvores.add(new DepartamentoArvore(departamentoPai, na));

            //Laço que vai construir os nós do organograma por nível
            for (int i = 2; i <= numeroMaximoDeNivel; i++) {
                List<Departamento> departamentosDoNivel = departamentoDAO.pesquisarDepartamentosPorNivel(i);

                //Laço para cada itém de departamentosDoNivel encontrar o seu pai
                for (Departamento d : departamentosDoNivel) {

                    for (DepartamentoArvore d2 : departamentosArvores) {
                        //Se o departamentoPai do nó for igual a algun nó que já estaja na lista
                        if (d.getDepartamentoPai().getId().equals(d2.getDepartamentoDoNo().getId())) {
                            TreeNode no = new DefaultTreeNode("" + d.getNivel(), d, d2.no);
                            departamentosArvores.add(new DepartamentoArvore(d, no));
                            break;
                        }
                    }
                }
            }

        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void nodeSelectListener(OrganigramNodeSelectEvent event) throws IOException {
        for (DepartamentoNo dn : departamentosNos) {
            if (dn.getNo().equals(selecionado)) {
                FacesUtils.setBean("departamento", dn.getDepartamentoDoNo());
            }
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("visualizar-departamento.xhtml?faces-redirect=true");
    }

    public void onNodeSelect(NodeSelectEvent event) throws IOException {
        Departamento depSelecionado = (Departamento) event.getTreeNode().getData();

        for (DepartamentoArvore dn : departamentosArvores) {
            if (dn.getDepartamentoDoNo().getId() == depSelecionado.getId()) {
                FacesUtils.setBean("departamento", dn.getDepartamentoDoNo());
            }
        }

        FacesContext.getCurrentInstance().getExternalContext().redirect("visualizar-departamento.xhtml?faces-redirect=true");
    }

    private void iniciar() {
        departamentoPai = new Departamento();
        departamentosNos = new ArrayList<>();
        departamentosArvores = new ArrayList<>();
    }

    public void recolherOrganograma() {
        for (DepartamentoNo dn1 : departamentosNos) {
            if (dn1.getDepartamentoDoNo().getNivel() > 1) {
                dn1.getNo().setExpanded(false);
            }
        }
    }

    public void expandirOrganograma() {
        for (DepartamentoNo dn1 : departamentosNos) {
            if (dn1.getNo().getChildCount() > 0) {
                dn1.getNo().setExpanded(true);
            }
        }
        for (DepartamentoNo dn1 : departamentosNos) {
            if (dn1.getNo().getData().toString().equalsIgnoreCase("servidores")) {
                dn1.getNo().getParent().setExpanded(false);
            }
        }
    }

    private void listarMensagens() {
        try {
            System.out.println(LocalDate.now());
            mensagens = (ArrayList<Mensagem>) mensagemDAO.listarTodasAsMensagensAtivasParaAData(LocalDate.now());
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
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

    public TreeNode getRootArvore() {
        return rootArvore;
    }

    public void setRootArvore(TreeNode rootArvore) {
        this.rootArvore = rootArvore;
    }

    public ArrayList<Mensagem> getMensagens() {
        return mensagens;
    }
    //==========================================================================

    private class DepartamentoNo {

        private Departamento departamentoDoNo;
        private OrganigramNode no;

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

    private class DepartamentoArvore {

        private Departamento departamentoDoNo;
        private TreeNode no;

        public DepartamentoArvore() {
        }

        public DepartamentoArvore(Departamento departamentoDoNo, TreeNode no) {
            this.departamentoDoNo = departamentoDoNo;
            this.no = no;
        }

        public Departamento getDepartamentoDoNo() {
            return departamentoDoNo;
        }

        public void setDepartamentoDoNo(Departamento departamentoDoNo) {
            this.departamentoDoNo = departamentoDoNo;
        }

        public TreeNode getNo() {
            return no;
        }

        public void setNo(TreeNode no) {
            this.no = no;
        }
    }

}
