package br.leg.alrr.catalogo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;


/**
 * @Organization ALRR
 * @author Heliton Nascimento
 * @Matricula 14583
 */
@Table(schema = "catalogo_servicos_procedimentos")
@Entity
@PrimaryKeyJoinColumn(name = "id")
public class UsuarioComDepartamento extends Usuario implements Serializable{
    
    @ManyToOne
    private Departamento departamento;
    
    //========================================================================//
    public UsuarioComDepartamento() {
        super();
    }

    public UsuarioComDepartamento(Long id) {
        super(id);
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
}
