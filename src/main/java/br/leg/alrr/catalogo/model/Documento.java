package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Entidade que representa um documento no sistema. Documento será qualquer
 * arquivo que será salvo na forma de byte.
 *
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String localizacao;

    @Lob
    @Basic(fetch = FetchType.LAZY, optional = true)
    private byte[] conteudo;

    private boolean status;

    @ManyToOne
    private FluxoDeTrabalho fluxoDeTrabalho;
    
    @ManyToOne
    private Departamento departamento;
    //==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public byte[] getConteudo() {
        return conteudo;
    }

    public void setConteudo(byte[] conteudo) {
        this.conteudo = conteudo;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public FluxoDeTrabalho getFluxoDeTrabalho() {
        return fluxoDeTrabalho;
    }

    public void setFluxoDeTrabalho(FluxoDeTrabalho fluxoDeTrabalho) {
        this.fluxoDeTrabalho = fluxoDeTrabalho;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
}
