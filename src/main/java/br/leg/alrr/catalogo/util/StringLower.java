package br.leg.alrr.catalogo.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

/**
 * Converter que converte todas as letras de uma string em minúsculas.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see String
 */
@FacesConverter("lower")
public class StringLower implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return value != null ? value.toLowerCase() : null;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return value != null ? String.valueOf(value).toLowerCase() : null;
    }

}
