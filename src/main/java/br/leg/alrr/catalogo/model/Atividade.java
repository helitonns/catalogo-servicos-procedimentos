package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * <p>
 * Entidade que representa uma atividade de um fluxo de trabalho nas regras de negócio. Atividade representa uma etapa de um fluxo de trabalho.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see FluxoDeTrabalho
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class Atividade implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private int ordem;
    
    private String descricao;
    
    private String resultado;
    
    @OneToOne
    private FluxoDeTrabalho fluxoDeTrabalho;
    
    @ManyToMany
    private List<Ator> atores;
    
    //==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public FluxoDeTrabalho getFluxoDeTrabalho() {
        return fluxoDeTrabalho;
    }

    public void setFluxoDeTrabalho(FluxoDeTrabalho fluxoDeTrabalho) {
        this.fluxoDeTrabalho = fluxoDeTrabalho;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }
    
    
}
