package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * <p>
 * Entidade que representa um fluxo de trabalho nas regras de negócio. Fluxo de trabalho é um desdobramento de uma Atribuião. Seria um passo a passo para que
 * a atribuição seja realizada.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Atribuicao
 * @see AtribuicaoDepartamento
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class FluxoDeTrabalho implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @ManyToOne
    private AtribuicaoDepartamento atribuicaoDepartamento;
    
    @ManyToMany
    private List<Ator> atores;
    
    @OneToMany
    private List<Documento> documentos;
    
    //==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public AtribuicaoDepartamento getAtribuicaoDepartamento() {
        return atribuicaoDepartamento;
    }

    public void setAtribuicaoDepartamento(AtribuicaoDepartamento atribuicaoDepartamento) {
        this.atribuicaoDepartamento = atribuicaoDepartamento;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
    
    
    
}
