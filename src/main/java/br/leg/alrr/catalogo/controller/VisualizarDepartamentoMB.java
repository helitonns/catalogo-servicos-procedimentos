package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Atividade;
import br.leg.alrr.catalogo.model.Atribuicao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.Documento;
import br.leg.alrr.catalogo.model.FluxoDeTrabalho;
import br.leg.alrr.catalogo.persistence.AtividadeDAO;
import br.leg.alrr.catalogo.persistence.AtribuicaoDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.DocumentoDAO;
import br.leg.alrr.catalogo.persistence.FluxoDeTrabalhoDAO;
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
import javax.servlet.http.HttpServletResponse;

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
    
    @EJB
    private FluxoDeTrabalhoDAO fluxoDeTrabalhoDAO;
    
    @EJB
    private AtividadeDAO ativiadeDAO;
    
    @EJB
    private DocumentoDAO documentoDAO;

    private List<Atribuicao> atribuicoesDoDepartamento;
    private List<AtribuicaoFluxoDeTrabalho> atribuicoesComSeusFluxos;
    private List<Atividade> atividadesDoFluxoDeTrabalho;
    private List<Documento> documentosDoFluxoDeTrabalho;

    private Departamento departamento;
    private FluxoDeTrabalho fluxoDeTrabalho;
    
    private boolean exibirAtividadeDocumentos;
    
    // ==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        if (FacesUtils.getBean("departamento") != null) {
            departamento = (Departamento) FacesUtils.getBean("departamento");
            listarAtribuicoes();
            listarFluxosDeTrabalhoDaAtribuicao();
            FacesUtils.removeBean("departamento");
        }
    }

    private void iniciar() {
        exibirAtividadeDocumentos = false;
        
        departamento = new Departamento();
        fluxoDeTrabalho = new FluxoDeTrabalho();
        
        atribuicoesDoDepartamento = new ArrayList<>();
        atribuicoesComSeusFluxos = new ArrayList<>();
        atividadesDoFluxoDeTrabalho = new ArrayList<>();
        documentosDoFluxoDeTrabalho = new ArrayList<>();
    }
    //--------------------------------------------------------------------------
    private void listarAtribuicoes() {
        try {
            atribuicoesDoDepartamento = atribuicaoDAO.listarTodosPorIdDoDepartamento(departamento.getId());
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    private void listarFluxosDeTrabalhoDaAtribuicao() {
        try {
            for (Atribuicao a : atribuicoesDoDepartamento) {
                ArrayList<FluxoDeTrabalho> fdts = (ArrayList<FluxoDeTrabalho>) fluxoDeTrabalhoDAO.listarFluxoDeTrabalhoPorAtribuicao(a);
                
                if (fdts.size() > 0) {
                    for (FluxoDeTrabalho fdt : fdts) {
                        atribuicoesComSeusFluxos.add(new AtribuicaoFluxoDeTrabalho(a, fdt));
                    }
                }else{
                    atribuicoesComSeusFluxos.add(new AtribuicaoFluxoDeTrabalho(a));
                }
            }
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    public void listarAtividadesEDocumentosDoFluxoDeTrabalho(){
        listarAtividadesDoFluxoDeTrabalho();
        listarDocumentosDoFluxoDeTrabalho();
        exibirAtividadeDocumentos = true;
    }
    
    private void listarAtividadesDoFluxoDeTrabalho(){
        try {
            atividadesDoFluxoDeTrabalho = ativiadeDAO.listarAtividadesPorFluxoDeTrabalho(fluxoDeTrabalho);
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    private void listarDocumentosDoFluxoDeTrabalho(){
        try {
            documentosDoFluxoDeTrabalho = documentoDAO.listarDocumentosPorFluxoDeTrabalho(fluxoDeTrabalho);
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void visualizarDocumento(Long idDocumento) throws DAOException {
        Documento doc = documentoDAO.buscarPorID(idDocumento);

        FacesContext fc = FacesContext.getCurrentInstance();

        // Obtem o HttpServletResponse, objeto responsável pela resposta do servidor ao browser
        HttpServletResponse response = (HttpServletResponse) fc.getExternalContext().getResponse();

        // Limpa o buffer do response
        response.reset();

        // Seta o tipo de conteudo no cabecalho da resposta. No caso, indica que o conteudo sera um documento pdf.
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // Seta o tamanho do conteudo no cabecalho da resposta. No caso, o tamanho em bytes do arquivo
        response.setContentLength(doc.getConteudo().length);

        // Seta o nome do arquivo e a disposição: "inline" abre no próprio navegador.
        // Mude para "attachment" para indicar que deve ser feito um download
        response.setHeader("Content-disposition", "inline; filename=\"" + doc.getNome() + "\"");
        try {
            // Envia o conteudo do arquivo PDF para o response
            response.getOutputStream().write(doc.getConteudo());

            // Descarrega o conteudo do stream, forçando a escrita de qualquer byte ainda em buffer
            response.getOutputStream().flush();

            // Fecha o stream, liberando seus recursos
            response.getOutputStream().close();

            // Sinaliza ao JSF que a resposta HTTP para este pedido já foi gerada
            fc.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //==========================================================================
    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public List<Atribuicao> getAtribuicoesDoDepartamento() {
        return atribuicoesDoDepartamento;
    }

    public void setAtribuicoesDoDepartamento(List<Atribuicao> atribuicoesDoDepartamento) {
        this.atribuicoesDoDepartamento = atribuicoesDoDepartamento;
    }

    public List<AtribuicaoFluxoDeTrabalho> getAtribuicoesComSeusFluxos() {
        return atribuicoesComSeusFluxos;
    }

    public FluxoDeTrabalho getFluxoDeTrabalho() {
        return fluxoDeTrabalho;
    }

    public void setFluxoDeTrabalho(FluxoDeTrabalho fluxoDeTrabalho) {
        this.fluxoDeTrabalho = fluxoDeTrabalho;
    }

    public List<Atividade> getAtividadesDoFluxoDeTrabalho() {
        return atividadesDoFluxoDeTrabalho;
    }

    public List<Documento> getDocumentosDoFluxoDeTrabalho() {
        return documentosDoFluxoDeTrabalho;
    }

    public boolean isExibirAtividadeDocumentos() {
        return exibirAtividadeDocumentos;
    }
    //==========================================================================
    /**
     * Classe para ajudar na exibição das atribuições com os seus respectivos fluxos de trabalho.
     */
    public class AtribuicaoFluxoDeTrabalho{

        private Atribuicao atribuicao;
        private FluxoDeTrabalho fluxoDeTrabalho;

        public AtribuicaoFluxoDeTrabalho() {
        }
        
        public AtribuicaoFluxoDeTrabalho(Atribuicao atribuicao) {
            this.atribuicao = atribuicao;
        }
        
        public AtribuicaoFluxoDeTrabalho(Atribuicao atribuicao, FluxoDeTrabalho fluxoDeTrabalho) {
            this.atribuicao = atribuicao;
            this.fluxoDeTrabalho = fluxoDeTrabalho;
        }

        public Atribuicao getAtribuicao() {
            return atribuicao;
        }

        public void setAtribuicao(Atribuicao atribuicao) {
            this.atribuicao = atribuicao;
        }

        public FluxoDeTrabalho getFluxoDeTrabalho() {
            return fluxoDeTrabalho;
        }

        public void setFluxoDeTrabalho(FluxoDeTrabalho fluxoDeTrabalho) {
            this.fluxoDeTrabalho = fluxoDeTrabalho;
        }
    }
}
