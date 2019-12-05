package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Classe que representa uma atribuição de Departamento. É uma classe filha de Atribuicao.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Atribuicao
 * @see Departamento
 */
@Entity
@PrimaryKeyJoinColumn(name = "id")
@Table(schema = "catalogo_servicos_procedimentos")
public class AtribuicaoDepartamento extends Atribuicao implements Serializable {
    
    @ManyToOne
    private Departamento departamento;
    
    //==========================================================================

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
    
}
