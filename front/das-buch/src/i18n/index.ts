import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";

import en from "./locales/en.json";
import pt from "./locales/pt.json";
import es from "./locales/es.json";
import de from "./locales/de.json";

i18n
  .use(LanguageDetector) // detecta idioma do navegador
  .use(initReactI18next) // conecta com React
  .init({
    resources: {
      en: { translation: en },
      pt: { translation: pt },
      es: { translation: es },
      de: { translation: de },
    },
    fallbackLng: "en",
    interpolation: {
      escapeValue: false, // react já faz isso
    },
  });

export default i18n;
