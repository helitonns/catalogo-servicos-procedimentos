package br.leg.alrr.catalogo.model;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;

/**
 * <p>
 * Entidade que representa um fluxo de trabalho nas regras de negócio. Fluxo de trabalho é um desdobramento de uma Atribuião. Seria um passo a passo para que
 * a atribuição seja realizada.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-12-05
 * @version 1.0
 * @see Atribuicao
 * @see Atribuicao
 */
@Audited
@AuditTable(value="fluxo_de_trabalho_auditoria", schema = "catalogo_servicos_procedimentos")
@Entity
@Table(schema = "catalogo_servicos_procedimentos")
public class FluxoDeTrabalho implements Serializable{
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    
    @ManyToOne(fetch = FetchType.EAGER)
    private Atribuicao atribuicao;
    
    @OneToMany(mappedBy = "fluxoDeTrabalho", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Atividade> atividades;
    
    @OneToMany(mappedBy = "fluxoDeTrabalho", cascade = CascadeType.ALL)
    private List<Documento> documentos;
    
    //==========================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Atribuicao getAtribuicao() {
        return atribuicao;
    }

    public void setAtribuicao(Atribuicao atribuicao) {
        this.atribuicao = atribuicao;
    }

    public List<Atividade> getAtividades() {
        return atividades;
    }

    public void setAtividades(List<Atividade> atividades) {
        this.atividades = atividades;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }
    
    public String getListarAtividades(){
        if (atividades.size() > 0) {
            StringBuilder sb = new StringBuilder();
            
            for (Atividade a : atividades) {
                sb.append("- ").append(a.getDescricao()).append("<br/>");
            }
            
            return sb.toString();
        }else{
            return "";
        }
    }
    
}
