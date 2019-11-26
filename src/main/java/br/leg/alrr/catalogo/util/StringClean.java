package br.leg.alrr.catalogo.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converter que retira acentos e caractes especiais de um string.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see StringUtils
 */
@FacesConverter("clean")
public class StringClean implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component,String value) {
        return value != null ? StringUtils.removerAcentos(value) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component,Object value) {
        return value != null ? StringUtils.removerAcentos(String.valueOf(value)) : null;
    }

}
