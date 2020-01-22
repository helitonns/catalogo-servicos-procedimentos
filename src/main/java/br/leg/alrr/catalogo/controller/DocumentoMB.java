package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Departamento;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;

import br.leg.alrr.catalogo.model.Documento;
import br.leg.alrr.catalogo.model.UsuarioComDepartamento;
import br.leg.alrr.catalogo.persistence.DepartamentoDAO;
import br.leg.alrr.catalogo.persistence.DocumentoDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;

/**
 * Classe de gerenciamento das regras de negócio para a entidade Documento.
 *
 * @author Rafael
 * @version 1.0
 * @since 2019-12-30
 * @see Documento
 *
 */
@ViewScoped
@Named
public class DocumentoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private DocumentoDAO documentoDao;
    
    @EJB
    private DepartamentoDAO departamentoDAO;

    private List<Documento> documentos;
    private List<Departamento> departamentos;
    
    private Documento documento;
    private Departamento departamento;
    
    private String nomeArquivo;
    private long idDoc;
    private boolean excluirDocumento;

    private StreamedContent arquivo;

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
                listarDocumentosPorDepartamento();
            } else{
                Autorizacao a = (Autorizacao) FacesUtils.getBean("autorizacao");
                if (!a.getPrivilegio().getDescricao().equalsIgnoreCase("SUPERADMIN")) {
                    UsuarioComDepartamento u = (UsuarioComDepartamento) FacesUtils.getBean("usuario");
                    departamento = u.getDepartamento();
                    listarDocumentosPorDepartamento();
                }
            }
        } catch (Exception e) {
            FacesUtils.addInfoMessage("Erro ao tentar acessar atribuções.");
        }
    }

    private void iniciar() {
        idDoc = 0l;
        
        documento = new Documento();
        documento.setStatus(true);
        departamento = new Departamento();
        
        documentos = new ArrayList<>();
        departamentos = new ArrayList<>();

    }

    private void listarDepartamentos(){
        try {
            departamentos = departamentoDAO.listarTodosAtivos();
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    private void listarDocumentosPorDepartamento(){
        try {
            documentos = documentoDao.listarDocumentosPorDepartamento(departamento);
        } catch (NullPointerException | DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
    }
    
    public void upload(FileUploadEvent event) throws IOException {
        //arquivo = new DefaultStreamedContent(event.getFile().getInputstream());
        byte[] conteudo = event.getFile().getContents();
        documento.setNome(event.getFile().getFileName());
        documento.setConteudo(conteudo);
    }

    public String salvar() {
        try {
            documento.setDepartamento(departamento);
            if (documento.getId() != null) {
                documentoDao.atualizar(documento);
                FacesUtils.addInfoMessageFlashScoped("Documento atualizada com sucesso!");
            } else {
                documentoDao.salvar(documento);
                FacesUtils.addInfoMessageFlashScoped("Documento salvo com sucesso!");
            }
            iniciar();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
        return "documento.xhtml" + "?faces-redirect=true";
    }
    
    public String excluirDocumento(){
        try {
            if (excluirDocumento) {
                documentoDao.removerDocumentoPorId(documento);
                FacesUtils.addInfoMessageFlashScoped("Dúvida excluída com sucesso!");
            }
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
        return "documento.xhtml" + "?faces-redirect=true";
    }


    public String cancelar() {
        return "documento.xhtml" + "?faces-redirect=true";
    }

    public byte[] toByteArrayUsingJava(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int reads = is.read();
        while (reads != -1) {
            baos.write(reads);
            reads = is.read();
        }
        return baos.toByteArray();
    }

    public void visualizarDoc() throws DAOException {

        Documento doc = documentoDao.buscarPorID(idDoc);
        nomeArquivo = doc.getNome();

        FacesContext fc = FacesContext.getCurrentInstance();

        // Obtem o HttpServletResponse, objeto responsável pela resposta do
        // servidor ao browser
        HttpServletResponse response = (HttpServletResponse) fc
                .getExternalContext().getResponse();

        // Limpa o buffer do response
        response.reset();

        // Seta o tipo de conteudo no cabecalho da resposta. No caso, indica que o
        // conteudo sera um documento pdf.
        response.setContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");

        // Seta o tamanho do conteudo no cabecalho da resposta. No caso, o tamanho
        // em bytes do arquivo
        response.setContentLength(doc.getConteudo().length);

        // Seta o nome do arquivo e a disposição: "inline" abre no próprio
        // navegador.
        // Mude para "attachment" para indicar que deve ser feito um download
        response.setHeader("Content-disposition", "inline; filename=\"" + nomeArquivo + "\"");
        try {

            // Envia o conteudo do arquivo PDF para o response
            response.getOutputStream().write(doc.getConteudo());

            // Descarrega o conteudo do stream, forçando a escrita de qualquer byte
            // ainda em buffer
            response.getOutputStream().flush();

            // Fecha o stream, liberando seus recursos
            response.getOutputStream().close();

            // Sinaliza ao JSF que a resposta HTTP para este pedido já foi gerada
            fc.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //=============================GETTERS E SETTERS=============================================
    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public StreamedContent getArquivo() {
        return arquivo;
    }

    public void setArquivo(StreamedContent arquivo) {
        this.arquivo = arquivo;
    }

    public long getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(long idDoc) {
        this.idDoc = idDoc;
    }

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
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

    public boolean isExcluirDocumento() {
        return excluirDocumento;
    }

    public void setExcluirDocumento(boolean excluirDocumento) {
        this.excluirDocumento = excluirDocumento;
    }
}
