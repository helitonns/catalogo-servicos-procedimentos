package br.leg.alrr.catalogo.controller;

import br.leg.alrr.catalogo.model.Autorizacao;
import br.leg.alrr.catalogo.model.Privilegio;
import br.leg.alrr.catalogo.model.Sistema;
import br.leg.alrr.catalogo.model.Usuario;
import br.leg.alrr.catalogo.persistence.AutorizacaoDAO;
import br.leg.alrr.catalogo.persistence.PrivilegioDAO;
import br.leg.alrr.catalogo.persistence.SistemaDAO;
import br.leg.alrr.catalogo.persistence.UsuarioDAO;
import br.leg.alrr.catalogo.util.DAOException;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.io.Serializable;
import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author heliton
 */
@Named
@ViewScoped
public class AutorizacaoMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @EJB
    private AutorizacaoDAO autorizacaoDAO;

    @EJB
    private PrivilegioDAO permissaoDAO;

    @EJB
    private SistemaDAO sistemaDAO;

    @EJB
    private UsuarioDAO usuarioDAO;
    
//    @EJB
//    private UnidadeDAO unidadeDAO;
//    
//    @EJB
//    private UnidadeACDAO unidadeACDAO;
    
    private ArrayList<Privilegio> permissoes;
    private ArrayList<Sistema> sistemas;
    private ArrayList<Usuario> usuarios;
    private ArrayList<Autorizacao> autorizacoes;
//    private ArrayList<Unidade> unidades;
//    private ArrayList<UnidadeAC> unidadesAC;

    private Autorizacao autorizacao;

    private Long idUsuario;
    private Long idSistema;
    private Long idPermissao;
    private Long idUnidade;

    private boolean removerAutorizacao;

    //==========================================================================
    @PostConstruct
    public void init() {
        limparForm();

        
    }

    private void listarSistema() {
        try {
            sistemas = (ArrayList<Sistema>) sistemaDAO.listarTodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void carregarPermissoes(ValueChangeEvent event) {
        try {
            idSistema = Long.parseLong(event.getNewValue().toString());
            listarPermissoesPorSistema(new Sistema(idSistema));
        } catch (NumberFormatException e) {
            FacesUtils.addInfoMessage(e.getMessage());
        }
    }

    private void listarPermissoesPorSistema(Sistema s) {
        try {
            permissoes = (ArrayList<Privilegio>) permissaoDAO.listarTodosAtivosPeloSistema(s);
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    public void listarPermissoesPorSistema(Long id) {
        try {
            permissoes = (ArrayList<Privilegio>) permissaoDAO.listarTodosAtivosPeloSistema(new Sistema(id));
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarUsuarios() {
        try {
            usuarios = (ArrayList<Usuario>) usuarioDAO.listarTodosAtivos();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void listarAutorizacoes() {
        try {
            autorizacoes = (ArrayList<Autorizacao>) autorizacaoDAO.listarTodas();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

//    private void listarUnidadesEscolegis(){
//        try {
//            unidades = (ArrayList<Unidade>) unidadeDAO.listarTodos();
//        } catch (DAOException e) {
//            FacesUtils.addErrorMessage(e.getMessage());
//        }
//    }
//    
//    private void listarUnidadesAbrindoCaminhos(){
//        try {
//            unidadesAC = (ArrayList<UnidadeAC>) unidadeACDAO.listarTodos();
//        } catch (DAOException e) {
//            FacesUtils.addErrorMessage(e.getMessage());
//        }
//    }
    
    public String salvarAutorizacao() {
        try {
            autorizacao.setUsuario(new Usuario(idUsuario));
            autorizacao.setPrivilegio(new Privilegio(idPermissao));

            if (autorizacao.getId() != null) {
                autorizacaoDAO.atualizar(autorizacao);
                FacesUtils.addInfoMessageFlashScoped("Autorização atualizada com sucesso!");
            } else {
                if (!autorizacaoDAO.verificarSeHaAutorizacaoParaUsuarioNoSistema(autorizacao.getUsuario(), new Sistema(idSistema))) {
                    autorizacaoDAO.salvar(autorizacao);
                    FacesUtils.addInfoMessageFlashScoped("Autorização salva com sucesso!");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("O usuário já possui autorização no sistema!");
                }
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "autorizacao.xhtml" + "?faces-redirect=true";
    }
    
    public String salvarAutorizacaoParaEscolegis() {
        try {
            autorizacao.setUsuario(new Usuario(idUsuario));
            autorizacao.setPrivilegio(new Privilegio(idPermissao));

            if (autorizacao.getId() != null) {
                autorizacaoDAO.atualizar(autorizacao);
                FacesUtils.addInfoMessageFlashScoped("Autorização atualizada com sucesso!");
            } else {
                if (!autorizacaoDAO.verificarSeHaAutorizacaoParaUsuarioNoSistema(autorizacao.getUsuario(), new Sistema(idSistema))) {
                    autorizacaoDAO.salvar(autorizacao);
//                    unidadeDAO.salvarUnidadeParaUsuario(idUsuario, idUnidade);
                    FacesUtils.addInfoMessageFlashScoped("Autorização salva com sucesso!");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("O usuário já possui autorização no sistema!");
                }
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "autorizacao-escolegis.xhtml" + "?faces-redirect=true";
    }
    
    public String salvarAutorizacaoParaAbrindoCaminhos() {
        try {
            autorizacao.setUsuario(new Usuario(idUsuario));
            autorizacao.setPrivilegio(new Privilegio(idPermissao));

            if (autorizacao.getId() != null) {
                autorizacaoDAO.atualizar(autorizacao);
                FacesUtils.addInfoMessageFlashScoped("Autorização atualizada com sucesso!");
            } else {
                if (!autorizacaoDAO.verificarSeHaAutorizacaoParaUsuarioNoSistema(autorizacao.getUsuario(), new Sistema(idSistema))) {
                    autorizacaoDAO.salvar(autorizacao);
//                    unidadeACDAO.salvarUnidadeParaUsuario(idUsuario, idUnidade);
                    FacesUtils.addInfoMessageFlashScoped("Autorização salva com sucesso!");
                } else {
                    FacesUtils.addWarnMessageFlashScoped("O usuário já possui autorização no sistema!");
                }
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessageFlashScoped(e.getMessage());
            System.out.println(e.getCause());
        }
        return "autorizacao-abrindo-caminhos.xhtml" + "?faces-redirect=true";
    }

    public void removerAutorizacao() {
        try {
            if (removerAutorizacao) {
                autorizacaoDAO.remover(autorizacao);
                FacesUtils.addInfoMessage("Autorização removida com sucesso!");
            }
            limparForm();
        } catch (DAOException e) {
            FacesUtils.addErrorMessage(e.getMessage());
        }
    }

    private void limparForm() {
        idUsuario = 0l;
        idSistema = 0l;
        idPermissao = 0l;

        usuarios = new ArrayList<>();
        sistemas = new ArrayList<>();
        permissoes = new ArrayList<>();
        autorizacoes = new ArrayList<>();
//        unidades = new ArrayList<>();
//        unidadesAC = new ArrayList<>();

        autorizacao = new Autorizacao();
        autorizacao.setStatus(true);

        removerAutorizacao = false;
        
        listarAutorizacoes();
        listarSistema();
        listarUsuarios();
               
    }

    public String cancelar() {
        return "autorizacao.xhtml" + "?faces-redirect=true";
    }

    //==========================================================================
    public ArrayList<Sistema> getSistemas() {
        return sistemas;
    }

    public ArrayList<Privilegio> getPermissoes() {
        return permissoes;
    }

    public ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public Long getIdSistema() {
        return idSistema;
    }

    public void setIdSistema(Long idSistema) {
        this.idSistema = idSistema;
    }

    public Long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Long idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Long getIdPermissao() {
        return idPermissao;
    }

    public void setIdPermissao(Long idPermissao) {
        this.idPermissao = idPermissao;
    }

    public Autorizacao getAutorizacao() {
        return autorizacao;
    }

    public void setAutorizacao(Autorizacao autorizacao) {
        this.autorizacao = autorizacao;
    }

    public boolean isRemoverAutorizacao() {
        return removerAutorizacao;
    }

    public void setRemoverAutorizacao(boolean removerAutorizacao) {
        this.removerAutorizacao = removerAutorizacao;
    }

    public ArrayList<Autorizacao> getAutorizacoes() {
        return autorizacoes;
    }

    public Long getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Long idUnidade) {
        this.idUnidade = idUnidade;
    }

   
    
}
