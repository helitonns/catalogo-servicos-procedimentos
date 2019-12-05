package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * <p>
 * Entidade que representa uma atribuição tanto para Ator quanto para Departamento. Esta é a classe mãe que está especializada em AtribuicaoAtor, que represeta
 * uma atribuição do ator, e em AtribuicaoDepartamento, que representa uma atribuição do departamento.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see AtribuicaoAtor
 * @see AtribuicaoDepartamento
 * @see Departamento
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
@Inheritance(strategy = InheritanceType.JOINED)
public class Atribuicao implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    
   //===========================================================================

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
    
    
    
}
