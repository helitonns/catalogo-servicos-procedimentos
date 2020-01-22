package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 * Entidade que representa uma Dúvida Frequente no sistema.
 * 
 * @author Heliton Nascimento
 * @since 2020-01-22
 * @version 1.0
  */
@Audited
@AuditTable(value="duvida_frequente_auditoria", schema = "catalogo_servicos_procedimentos")
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class DuvidaFrequente implements Serializable {
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(columnDefinition = "text")
    private String duvida;
    
    @Column(columnDefinition = "text")
    private String resposta;
    
    private boolean status;
    
    @ManyToOne
    private Departamento departamento;
    
    //--------------------------------------------------------------------------

    public DuvidaFrequente() {
    }
    
    public DuvidaFrequente(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDuvida() {
        return duvida;
    }

    public void setDuvida(String duvida) {
        this.duvida = duvida;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    
}
