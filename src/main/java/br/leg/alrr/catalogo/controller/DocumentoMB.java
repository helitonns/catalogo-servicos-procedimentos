package br.leg.alrr.catalogo.controller;

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
 * 
 */

@ViewScoped
@Named
public class DocumentoMB implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@EJB
	private DocumentoDAO documentoDao;
	
	private Documento documento;
	private List<Documento> documentos;
	private long idDoc;
	private String nomeArquivo;
	
	private StreamedContent arquivo;
	
	
	
	
	//==========================================================================
    @PostConstruct
    public void init() {
        iniciar();
        listarTodosOsDocumentos();
    }
    
    private void iniciar(){
    	documento = new Documento();
    	documentos = new ArrayList<Documento>();
    	idDoc = 0l;
    	
    }
    
    
    
    public void upload(FileUploadEvent event) throws IOException{
       
        	
        	//arquivo = new DefaultStreamedContent(event.getFile().getInputstream());
        	byte[] conteudo = event.getFile().getContents();
        	nomeArquivo = event.getFile().getFileName();
        	System.out.println(nomeArquivo);
            documento.setConteudo(conteudo);
          
    }
    
    public String salvar() {
    	 try {
    		 
    		 
    		
    		 System.out.println(nomeArquivo);
    		 System.out.println(documento.getConteudo());
         	   if (documento.getId() != null) {
         		 
                 FacesUtils.addInfoMessage("Documento atualizada com sucesso!");
             } else {
             	documentoDao.salvar(documento);
                 FacesUtils.addInfoMessage("Documento salvo com sucesso!");
             }
             iniciar();
         } catch (DAOException e) {
             FacesUtils.addErrorMessage(e.getMessage());
         }
    	 
    	 return "documento.xhtml" + "?faces-redirect=true";
    }
   
    
   
    private void listarTodosOsDocumentos() {
        try {
            documentos = documentoDao.listarTodos();
        } catch (NullPointerException e) {
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
        }
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
    	System.out.println(doc.getConteudo());
    	System.out.println(doc.getId());

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
        response.setHeader("Content-disposition", "inline; filename=arquivo.docx");
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

	
	

	
	
	
	
}
