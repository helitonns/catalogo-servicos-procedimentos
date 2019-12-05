package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidade que representa um Ator no sistema. Ator é responsável por realizar uma atribuição, se confunde com as funções que determinado cargo realiza.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see AtribuicaoAtor
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class Ator implements Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descricao;
    
    @OneToOne
    private Documento documento;
    
    private boolean status;
    
    //==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
    
    
    
}
