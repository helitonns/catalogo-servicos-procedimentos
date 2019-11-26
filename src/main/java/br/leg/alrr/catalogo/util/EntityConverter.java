package br.leg.alrr.catalogo.util;

import java.io.Serializable;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * <p>
 * Converter usado nas páginas xhtml. Basicamente faz a conversão automática entre
 * uma String e um objeto. Para isso se utiliza da interface BaseEntity que traz a variável id e o método getId. Geralmente é usada para converter combobox.
 * </p>
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see BaseEntity
 */
@FacesConverter("entityConverter")
public class EntityConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext ctx, UIComponent component, String value) {
        if (value != null) {
            Object o = this.getAttributesFrom(component).get(value);
            if (o != null) {
                return o;
            }
        }
        return null;
    }

    @Override
    public String getAsString(FacesContext ctx, UIComponent component, Object value) {
        
        if (value != null && !"".equals(value) && value instanceof BaseEntity) {

            @SuppressWarnings("rawtypes")
            BaseEntity entity = (BaseEntity) value;

            // adiciona item como atributo do componente
            this.addAttribute(component, entity);
            Serializable codigo = entity.getId();


            return (codigo != null ? String.valueOf(codigo) : "");
        }

        return "";

    }

    @SuppressWarnings("rawtypes")
    protected void addAttribute(UIComponent component, BaseEntity o) {
        if (o.getId() != null) {
            this.getAttributesFrom(component).put(o.getId().toString(), o);
        }
    }

    protected Map<String, Object> getAttributesFrom(UIComponent component) {
        return component.getAttributes();
    }
}
