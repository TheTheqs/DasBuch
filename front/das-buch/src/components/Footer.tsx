import { useTranslation } from "react-i18next";

function Footer() {
  const currentYear = new Date().getFullYear();
  const { t, i18n } = useTranslation();

  const handleChangeLanguage = (lang: string) => {
    i18n.changeLanguage(lang);
  };

  return (
    <footer className="bg-black text-white text-center py-4 mt-5 position-relative">
      <div className="container">
        <small>
          © {currentYear} <strong>Matheqs</strong>. {t("footer.rights")}
        </small>
      </div>

      {/* Menu de idioma dropup no canto inferior direito */}
      <div className="dropup position-absolute end-0 bottom-0 mb-3 me-3">
        <button
          className="btn btn-sm btn-outline-light dropdown-toggle"
          type="button"
          data-bs-toggle="dropdown"
          aria-expanded="false"
        >
          🌐
        </button>
        <ul className="dropdown-menu dropdown-menu-end">
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleChangeLanguage("pt")}
            >
              Português
            </button>
          </li>
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleChangeLanguage("en")}
            >
              English
            </button>
          </li>
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleChangeLanguage("es")}
            >
              Español
            </button>
          </li>
          <li>
            <button
              className="dropdown-item"
              onClick={() => handleChangeLanguage("de")}
            >
              Deutsch
            </button>
          </li>
        </ul>
      </div>
    </footer>
  );
}

export default Footer;
