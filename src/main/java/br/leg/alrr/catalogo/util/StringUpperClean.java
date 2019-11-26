package br.leg.alrr.catalogo.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converter que converte todas as letras de uma string em maiúsculas e retira acentos.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see String
 * @see StringUtils
 */
@FacesConverter("upperClean")
public class StringUpperClean implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? StringUtils.removerAcentos(value.toUpperCase()) : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value != null ? String.valueOf(value).toUpperCase() : null;
    }

}
