import PrimaryButton from "./PrimaryButton";
import { useTranslation } from "react-i18next";

function CallToAction() {
  const { t } = useTranslation();
  return (
    <section className="text-dark py-5">
      <div
        className="container d-flex flex-column align-items-center justify-content-center text-center"
        style={{ minHeight: "40vh" }}
      >
        <h1 className="display-4 fw-bold mb-3">{t("cta.welcome")}</h1>
        <p className="lead mb-4">{t("cta.description")}</p>
        <PrimaryButton to="/signin" label={t("cta.startNow")} />
        <PrimaryButton to="/login" label={t("cta.login")} />
      </div>
    </section>
  );
}

export default CallToAction;
