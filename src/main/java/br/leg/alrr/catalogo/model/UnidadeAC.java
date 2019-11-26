package br.leg.alrr.catalogo.model;

import br.leg.alrr.catalogo.util.BaseEntity;
import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 *
 * @author Heliton
 */
@Table(name = "unidade",schema = "abrindo_caminhos")
@Entity
public class UnidadeAC implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private EnderecoAC endereco;

    //=========================================================================//

    public UnidadeAC() {
    }

    public UnidadeAC(Long id) {
        this.id = id;
    }   
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public EnderecoAC getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoAC endereco) {
        this.endereco = endereco;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }
    
}
