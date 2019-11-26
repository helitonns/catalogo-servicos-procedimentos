package br.leg.alrr.catalogo.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 *
 * @author Heliton
 */
@Table(name = "endereco",schema = "abrindo_caminhos")
@Entity
public class EnderecoAC implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String rua;
    private String complemento;
    private String numero;
    private String cep;

    @ManyToOne
    private BairroAC bairro;
    

    //========================================================================//
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public BairroAC getBairro() {
        return bairro;
    }

    public void setBairro(BairroAC bairro) {
        this.bairro = bairro;
    }

    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        
        if (bairro != null) {
            sb.append(bairro.getMunicipio().getNome()).append(", ");;
            sb.append(bairro.getNome()).append(", ");
        }
        
        if (rua != null) {
            sb.append(rua).append(", ");
        }
        if (numero != null) {
            sb.append(numero);
        }
        
        return sb.toString();
    }
}
