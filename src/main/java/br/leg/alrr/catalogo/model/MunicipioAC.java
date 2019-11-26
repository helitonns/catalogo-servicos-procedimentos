package br.leg.alrr.catalogo.model;

import br.leg.alrr.catalogo.util.BaseEntity;
import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 *
 * @author Heliton
 */
@Table(name = "municipio",schema = "abrindo_caminhos")
@Entity
public class MunicipioAC implements Serializable, BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String nome;

    @OneToMany(mappedBy = "municipio")
    private List<BairroAC> bairros;

    //========================================================================//
    public MunicipioAC() {
    }

    public MunicipioAC(Long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long i) {
        this.id = i;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<BairroAC> getBairros() {
        return bairros;
    }

    public void setBairros(List<BairroAC> bairros) {
        this.bairros = bairros;
    }

    //========================================================================//
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        MunicipioAC other = (MunicipioAC) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "ID: " + id + " -- Nome: " + nome;
    }

}
