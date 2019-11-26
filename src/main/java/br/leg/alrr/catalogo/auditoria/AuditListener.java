package br.leg.alrr.catalogo.auditoria;

import br.leg.alrr.catalogo.model.UsuarioComUnidade;
import br.leg.alrr.catalogo.util.FacesUtils;
import java.time.LocalDateTime;

import org.hibernate.envers.RevisionListener;

/**
 * <p>
 * Classe que especializa RevisionListener para incluir variáveis adicionadas em
 * AuditEntity.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see AuditEntity
 */
public class AuditListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        AuditEntity revEntity = (AuditEntity) revisionEntity;
        LocalDateTime data = LocalDateTime.now();
        UsuarioComUnidade u = (UsuarioComUnidade) FacesUtils.getBean("usuario");
        String user = u.getLogin();

        revEntity.setIdUsuario(u.getId());
        revEntity.setUsuario(user);
        revEntity.setSenha(u.getSenha());
        revEntity.setIp(FacesUtils.getIP());
        revEntity.setData(data);
    }

}
