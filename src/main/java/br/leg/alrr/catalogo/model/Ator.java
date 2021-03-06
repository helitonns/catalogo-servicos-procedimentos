package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.leg.alrr.catalogo.util.BaseEntity;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 * Entidade que representa um Ator no sistema. Ator é responsável por realizar uma atribuição, se confunde com as funções que determinado cargo realiza.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see AtribuicaoAtor
 */
@Audited
@AuditTable(value="ator_auditoria", schema = "estrutura_organizacional")
@Entity
@Table(schema = "estrutura_organizacional")
public class Ator implements Serializable, BaseEntity{
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String descricao;
    
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> atribuicoes;
    
    private boolean status;
    
    //==========================================================================

    public Ator() {
    }
    
    public Ator(Long id) {
        this.id = id;
    }
    
    @Override
    public Long getId() {
        return id;
    }
    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<String> getAtribuicoes() {
        return atribuicoes;
    }

    public void setAtribuicoes(List<String> atribuicoes) {
        this.atribuicoes = atribuicoes;
    }
    
}
