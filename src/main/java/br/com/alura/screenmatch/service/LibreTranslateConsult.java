package br.com.alura.screenmatch.service;

import net.suuft.libretranslate.Language;
import net.suuft.libretranslate.Translator;

public class LibreTranslateConsult {

    public static String getTranslate(String texto) {

        Translator.setUrlApi("https://libretranslate.de/translate");

        var response = Translator.translate(Language.ENGLISH, Language.PORTUGUESE, texto);

        return response;
    }
}
