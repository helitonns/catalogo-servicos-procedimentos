package br.leg.alrr.catalogo.util;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>Classe utilitária que facilita a realização de algumas funções quando se 
 * trabalha com JSF, como, por exemplo, exibir mensagens nas páginas xhtml.
 * </p>
 * 
 * @author Heliton Nasciemento
 * @since 2019-11-26
 * @version 1.0
 */
public class FacesUtils {

    /**
     * Contrutor vazio
     */
    private FacesUtils() {
    }

    //==========================================================================
    /**
     * Adiciona uma mensagem de erro sem especificar um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessage(String msg) {
        addErrorMessage(null, msg);
    }
    
    /**
     * Adiciona uma mensagem de erro especificando um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg));
    }
    
    /**
     * <p>
     * Adiciona uma mensagem de erro sem especificar um cliente. Método usado quando é necessáirio mudar de página. 
     * Quando se muda de página e não este método a mensagem não será exibna na página
     * seguinte.
     * </p>
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addErrorMessageFlashScoped(String msg) {
        addErrorMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
    //==========================================================================
    /**
     * Adiciona uma mensagem de informação sem especificar um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessage(String msg) {
        addInfoMessage(null, msg);
    }
    
    /**
     * Adiciona uma mensagem de informação especificando um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg));
    }
    
    /**
     * <p>
     * Adiciona uma mensagem de informação sem especificar um cliente. Método usado quando é necessáirio mudar de página. 
     * Quando se muda de página e não este método a mensagem não será exibna na página
     * seguinte.
     * </p>
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addInfoMessageFlashScoped(String msg) {
        addInfoMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
    //==========================================================================
    /**
     * Adiciona uma mensagem de alerta sem especificar um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessage(String msg) {
        addWarnMessage(null, msg);
    }
    
    /**
     * Adiciona uma mensagem de alerta especificando um cliente. Método usado quando não é necessáirio mudar de página.
     * 
     * @param clientId cliente da mensagem.
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessage(String clientId, String msg) {
        FacesContext.getCurrentInstance().addMessage(clientId, new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg));
    }
    
    /**
     * <p>
     * Adiciona uma mensagem de alerta sem especificar um cliente. Método usado quando é necessáirio mudar de página. 
     * Quando se muda de página e não este método a mensagem não será exibna na página
     * seguinte.
     * </p>
     * 
     * @param msg representa a mensagem a ser exibida.
     */
    public static void addWarnMessageFlashScoped(String msg) {
        addWarnMessage(null, msg);
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
    //==========================================================================
    /**
     * Método usado quando é necessário jogar um objeto na sessão.
     * @param nome representa o nome pelo qual o obejto será acessado quando já estiver na sessão.
     * @param o representa o objeto que será jogado na sessão.
     */
    public static void setBean(String nome, Object o) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(nome, o);
    }
    
    /**
     * Método usado quando é necessário recuperar um obejto que está ne sessão.
     * @param nome representa o nome do objeto na sessão.
     * @return retorna o objeto que estava na sessão.
     */
    public static Object getBean(String nome) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(nome);
    }

    /**
     * Método usado quando é necessário retirar da sessão um objeto que lá estava.
     * @param nome representa o nome do objeto que deve ser retirado da sessão. 
     */
    public static void removeBean(String nome) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(nome);
    }
    //==========================================================================
    /**
     * Método que retorna o IP da máquina que está acessando a aplicação.
     * @return IP da máquina.
     */
    public static String getIP() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return request.getRemoteAddr();
    }
    
    /**
     * Método que retorna a URL da página atual da aplicação.
     * @return URL da página.
     */
    public static String getURL() {
        FacesContext fc = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) fc.getExternalContext().getRequest();
        return request.getRequestURI();
    }
    //==========================================================================
}
