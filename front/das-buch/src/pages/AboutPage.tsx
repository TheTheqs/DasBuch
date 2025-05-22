import { Trans, useTranslation } from "react-i18next";

function AboutPage() {
  const { t } = useTranslation();

  return (
    <div className="container py-5" style={{ maxWidth: "720px" }}>
      <h1 className="mb-4 text-center fw-bold">{t("about.title")}</h1>

      <p><Trans i18nKey="about.intro" components={[<strong />, <strong />]} /></p>
      <p>{t("about.background")}</p>
      <p><Trans i18nKey="about.focus" components={[<strong />]} /></p>
      <p>{t("about.contactIntro")}</p>

      <ul>
        <li>
          {t("about.contact.instagram")}:{" "}
          <a href="https://instagram.com/matheqs" target="_blank" rel="noreferrer">@matheqs</a>
        </li>
        <li>
          {t("about.contact.email")}:{" "}
          <a href="mailto:matheqs@gmail.com">matheqs@gmail.com</a>
        </li>
        <li>
          {t("about.contact.linkedin")}:{" "}
          <a href="https://www.linkedin.com/in/matheqs" target="_blank" rel="noreferrer">
            Matheus Barbosa
          </a>{" "}
          – <Trans i18nKey="about.linkedinNote" components={[<strong />]} />
        </li>
      </ul>

      <hr className="my-5" />

      <h2 className="mb-3">{t("about.termsTitle")}</h2>
      <p>{t("about.termsIntro")}</p>

      <ul>
        <li><Trans i18nKey="about.terms.data" components={[<strong />]} /></li>
        <li><Trans i18nKey="about.terms.security" components={[<strong />]} /></li>
        <li><Trans i18nKey="about.terms.storage" components={[<strong />]} /></li>
        <li><Trans i18nKey="about.terms.consent" components={[<strong />]} /></li>
      </ul>

      <p className="mt-4">{t("about.closing")}</p>
    </div>
  );
}

export default AboutPage;
