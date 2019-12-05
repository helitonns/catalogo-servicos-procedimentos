package br.leg.alrr.catalogo.model;

import br.leg.alrr.catalogo.util.BaseEntity;
import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entidade que representa um departamento nas regras de negócio.
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * 
 */
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class Departamento implements BaseEntity, Serializable{
    
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
}
