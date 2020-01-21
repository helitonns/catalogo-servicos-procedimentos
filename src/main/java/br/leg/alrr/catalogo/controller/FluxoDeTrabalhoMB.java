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
import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Departamento;
import br.leg.alrr.catalogo.model.Documento;
import br.leg.alrr.catalogo.model.FluxoDeTrabalho;
import br.leg.alrr.catalogo.model.UsuarioComDepartamento;
import br.leg.alrr.catalogo.persistence.AtividadeDAO;
import br.leg.alrr.catalogo.persistence.AtorDAO;
import br.leg.alrr.catalogo.persistence.AtribuicaoDAO;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.DocumentoDAO;
import br.leg.alrr.catalogo.persistence.FluxoDeTrabalhoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpServletResponse;
import org.primefaces.event.FileUploadEvent;

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

    @EJB
    private AtividadeDAO atividadeDAO;

    @EJB
    private DocumentoDAO documentoDAO;

    private List<Atribuicao> atribuicoes;
    private List<Departamento> departamentos;
    private List<Atividade> atividades;
    private List<Atividade> atividadesQueDevemSerExcluidas;
    private List<Ator> atores;
    private List<Ator> atoresSelecionados;
    private List<FluxoDeTrabalho> fluxosDeTrabalho;
    private List<Documento> documentos;
    private List<Documento> documentosQueDevemSerExcluidos;

    private Atribuicao atribuicao;
    private Departamento departamento;
    private Atividade atividade;
    private FluxoDeTrabalho fluxoDeTrabalho;
    private Documento documento;

    private String nomeArquivo;
    private boolean excluirFluxo;

    //==========================================================================
    @PostConstruct
    public void init() {
        iniciar();

        try {
            //VERIFICA SE HÁ DEPARTAMETO NA SESSÃO
            if (FacesUtils.getBean("departamento") != null) {
                departamento = (Departamento) FacesUtils.getBean("departamento");
                atores = departamento.getAtoresNaoDuplicados();
                FacesUtils.removeBean("departamento");
                listarTodasAtribuicoesPorDepartamento();
                atribuicao = (Atribuicao) FacesUtils.getBean("atribuicao");
                FacesUtils.removeBean("atribuicao");
                listarFluxoDeTrabalhoPorAtribuicao();
            }else{
                Autorizacao a = (Autorizacao) FacesUtils.getBean("autorizacao");
                if (!a.getPrivilegio().getDescricao().equalsIgnoreCase("SUPERADMIN")) {
                    UsuarioComDepartamento u = (UsuarioComDepartamento) FacesUtils.getBean("usuario");
                    departamento = u.getDepartamento();
                    atores = departamento.getAtoresNaoDuplicados();
                    listarTodasAtribuicoesPorDepartamento();
                }
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar acessar atribuções.");
        }
    }

    private void iniciar() {
        excluirFluxo = false;

        atribuicao = new Atribuicao();
        departamento = new Departamento();
        atividade = new Atividade();
        fluxoDeTrabalho = new FluxoDeTrabalho();
        documento = new Documento();
        documento.setStatus(true);

        atribuicoes = new ArrayList<>();
        departamentos = new ArrayList<>();
        atores = new ArrayList<>();
        atividades = new ArrayList<>();
        atividadesQueDevemSerExcluidas = new ArrayList<>();
        fluxosDeTrabalho = new ArrayList<>();
        documentos = new ArrayList<>();
        documentosQueDevemSerExcluidos = new ArrayList<>();

        listarTodosDepartamentos();
    }

    //--------------------------------------------------------------------------
    public String salvarFluxoDeTrabalho() {
        try {

            fluxoDeTrabalho.setAtribuicao(atribuicao);

            //ATUALIZAR
            if (fluxoDeTrabalho.getId() != null) {
                //VERIFICA SE HÁ ATIVIDADES PARA SER EXLUÍDA
                if (atividadesQueDevemSerExcluidas.size() > 0) {

                    atividades.removeAll(atividadesQueDevemSerExcluidas);

                    for (Atividade atividadeParaExclusao : atividadesQueDevemSerExcluidas) {
                        atividadeDAO.removerAtividadePorId(atividadeParaExclusao);
                    }

                    atividadesQueDevemSerExcluidas = null;
                    atividadesQueDevemSerExcluidas = new ArrayList<>();
                }

                //DEVE-SE SETAR ATIVIDADES EM FLUXO E FLUXO EM ATIVIDADES
                fluxoDeTrabalho.setAtividades(atividades);
                for (Atividade a : atividades) {
                    a.setFluxoDeTrabalho(fluxoDeTrabalho);
                }

                //SETANDO A LISTA DE DOCUMENTOS
                fluxoDeTrabalho.setDocumentos(documentos);
                for (Documento d : documentos) {
                    d.setFluxoDeTrabalho(fluxoDeTrabalho);
                }
                //VERIFICA SE HÁ DOCUEMNTOS PARA SEREM EXCLUÍDOS
                for (Documento d : documentosQueDevemSerExcluidos) {
                    documentoDAO.removerDocumentoPorId(d);
                }

                fluxoDeTrabalhoDAO.atualizar(fluxoDeTrabalho);
                FacesUtils.addInfoMessageFlashScoped("Fluxo de trabalho atualizado com sucesso!");
            } //SALVAR
            else {
                //DEVE-SE SETAR ATIVIDADES EM FLUXO E FLUXO EM ATIVIDADES
                fluxoDeTrabalho.setAtividades(atividades);
                for (Atividade a : atividades) {
                    a.setFluxoDeTrabalho(fluxoDeTrabalho);
                }

                //SETANDO A LISTA DE DOCUMENTOS
                fluxoDeTrabalho.setDocumentos(documentos);
                for (Documento d : documentos) {
                    d.setFluxoDeTrabalho(fluxoDeTrabalho);
                }

                fluxoDeTrabalhoDAO.salvar(fluxoDeTrabalho);
                FacesUtils.addInfoMessageFlashScoped("Fluxo de trabalho salvo com sucesso!");
            }
            iniciar();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "fluxo-de-trabalho" + "?faces-redirect=true";
    }

    //--------------------------------------------------------------------------
    private void listarTodosDepartamentos() {
        try {
            departamentos = departamentoDAO.listarTodos();

        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
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

    public void selecionarAtribuicao(ValueChangeEvent event) {
        try {
            if (event.getNewValue() != null) {
                atribuicao.setId(Long.parseLong(event.getNewValue().toString()));
                listarFluxoDeTrabalhoPorAtribuicao();
            }
        } catch (NumberFormatException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarFluxoDeTrabalhoPorAtribuicao() {
        try {
            fluxosDeTrabalho = fluxoDeTrabalhoDAO.listarFluxoDeTrabalhoPorAtribuicao(atribuicao);
            listarAtividadesDosFluxosDeTrabalho();
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }

    private void listarAtividadesDosFluxosDeTrabalho() {
        try {
            for (FluxoDeTrabalho f : fluxosDeTrabalho) {
                f.setAtividades(fluxoDeTrabalhoDAO.listarAtividadesPorFLuxoDeTrabalho(f));
            }
        } catch (DAOException e) {
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
            } else {
                FacesUtils.addErrorMessageFlashScoped("É necessário que tenha pelo menos um ator vinculado à atividade!");
            }
        } else {
            FacesUtils.addErrorMessageFlashScoped("A descrição da atividade é campo obrigatório!");

        }
    }

    //--------------------------------------------------------------------------
    public String excluirFluxo() {
        try {
            if (excluirFluxo) {
                fluxoDeTrabalhoDAO.remover(fluxoDeTrabalho);
                FacesUtils.addInfoMessageFlashScoped("Fluxo de trabalho excluído com sucesso!");
                excluirFluxo = false;
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "fluxo-de-trabalho.xhtml" + "?faces-redirect=true";
    }

    public void excluirAtividade() {
        for (Atividade a : atividades) {
            if (a.getId().equals(atividade.getId())) {
                atividadesQueDevemSerExcluidas.add(a);
                atividades.remove(a);
                atividade = new Atividade();
                break;
            }
        }
    }

    //--------------------------------------------------------------------------
    public void upload(FileUploadEvent event) throws IOException {
        byte[] conteudo = event.getFile().getContents();
        documento.setNome(event.getFile().getFileName());
        documento.setConteudo(conteudo);
    }

    public void adicinarDocumento() {
        documentos.add(documento);
        documento = new Documento();
        documento.setStatus(true);
    }

    public void listarDocumentosPorFluxoDeTrabalho() {
        try {
            documentos = documentoDAO.listarDocumentosPorFluxoDeTrabalho(fluxoDeTrabalho);
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getCause().toString());
        }
    }

    public void excluirDocumento() {
        for (Documento d : documentos) {
            if (d.getId().equals(documento.getId())) {
                documentosQueDevemSerExcluidos.add(d);
                documentos.remove(d);
                documento = new Documento();
                break;
            }
        }
    }

    public void visualizarDocumento(Long idDocumento) throws DAOException {
        Documento doc = documentoDAO.buscarPorID(idDocumento);
        nomeArquivo = doc.getNome();

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
        response.setHeader("Content-disposition", "inline; filename=\"" + nomeArquivo + "\"");
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
    //--------------------------------------------------------------------------

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

    public List<FluxoDeTrabalho> getFluxosDeTrabalho() {
        return fluxosDeTrabalho;
    }

    public boolean isExcluirFluxo() {
        return excluirFluxo;
    }

    public void setExcluirFluxo(boolean excluirFluxo) {
        this.excluirFluxo = excluirFluxo;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }
}
