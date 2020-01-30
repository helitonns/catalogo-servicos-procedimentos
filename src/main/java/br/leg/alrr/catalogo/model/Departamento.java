package br.leg.alrr.catalogo.model;

import br.leg.alrr.catalogo.util.BaseEntity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 * Entidade que representa um departamento nas regras de negócio.
 *
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 *
 */
@Audited
@AuditTable(value="departamento_auditoria", schema = "estrutura_organizacional")
@Entity
@Table(schema = "estrutura_organizacional")
public class Departamento implements BaseEntity, Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String telefone;

    private String email;

    private String chefe;

    @ManyToMany
    private List<Ator> atores;

    @ManyToOne(optional = true)
    private Departamento departamentoPai;

    private boolean status;

    private int nivel;

    //==========================================================================
    public Departamento() {
    }

    public Departamento(Long id) {
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getChefe() {
        return chefe;
    }

    public void setChefe(String chefe) {
        this.chefe = chefe;
    }

    public List<Ator> getAtores() {
        return atores;
    }

    public void setAtores(List<Ator> atores) {
        this.atores = atores;
    }

    public Departamento getDepartamentoPai() {
        return departamentoPai;
    }

    public void setDepartamentoPai(Departamento departamentoPai) {
        this.nivel = departamentoPai.getNivel() + 1;
        this.departamentoPai = departamentoPai;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public List<Ator> getAtoresNaoDuplicados() {

        boolean encontrouElemento = false;
        List<Ator> l = new ArrayList<>();

        for (Ator a : atores) {
            if (l.size() < 1) {
                l.add(a);
            } else {
                for (Ator aa : l) {
                    if (a.getId().equals(aa.getId())) {
                        encontrouElemento = true;
                        break;
                    }
                }

                if (!encontrouElemento) {
                    l.add(a);
                }
                encontrouElemento = false;
            }
        }

        return l;
    }

}
