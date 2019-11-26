package br.leg.alrr.catalogo.util;

/**
 * Classe que manipula strings.
 * 
 * @author Heliton Nascimento
 * @since 2019-11-26
 * @version 1.0
 * @see String
 */
public class StringUtils {

    /**
     * Método que remove os acentos das palavras e substitui "ç" por "c" e "ñ" por "n".
     * @param s representa a string a ser manipulada.
     * @return retorna a string transformada.
     */
    public static String removerAcentos(String s) {
        String out = s;
        
        out = out.replaceAll("[àáâäã]", "a");
        out = out.replaceAll("[èéêëẽ]", "e");
        out = out.replaceAll("[ìíîïĩ]", "i");
        out = out.replaceAll("[òóôöõ]", "o");
        out = out.replaceAll("[ùúûüũ]", "u");
        out = out.replaceAll("ç", "c");
        out = out.replaceAll("ñ", "n");

        out = out.replaceAll("[ÀÁÂÄÃ]", "A");
        out = out.replaceAll("[ÈÉÊËẼ]", "E");
        out = out.replaceAll("[ÌÍÎÏĨ]", "I");
        out = out.replaceAll("[ÒÓÔÖÕ]", "O");
        out = out.replaceAll("[ÙÚÛÜŨ]", "U");
        out = out.replaceAll("Ç", "C");
        out = out.replaceAll("Ñ", "N");

        return out;
    }
    
    /**
     * Método que retorna a primeira palavra de uma string composta por mais de uma palavra.
     * @param s representa a string a ser manipulada.
     * @return retorna a primeira palavra.
     */
    public static String retornarPrimeiroNome(String s) {
        try {
            String[] resultado = s.split(" ");
            return resultado[0];
        } catch (Exception e) {
        }
        return null;
    }

}
