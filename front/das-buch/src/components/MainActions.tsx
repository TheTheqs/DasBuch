import PrimaryButton from "./PrimaryButton";
import { useUser } from "../context/User";
import { useTranslation } from "react-i18next";

interface MainActionsProps {
  name: string;
}

function MainActions({ name }: MainActionsProps) {
  const { user } = useUser();
  const { t } = useTranslation();

  return (
    <section className="text-dark py-5">
      <div
        className="container d-flex flex-column align-items-center justify-content-center text-center"
        style={{ minHeight: "40vh" }}
      >
        <h1 className="display-4 fw-bold mb-4">
          {t("main.welcome", { name })}
        </h1>

        <p className="lead mb-4">{t("main.info")}</p>

        <PrimaryButton to="/new" label={t("main.newReview")} />

        {user != null && (
          <PrimaryButton
            to={`/user/reviews/${user.id}`}
            label={t("main.myReviews")}
          />
        )}

        <PrimaryButton to="/user" label={t("main.searchUser")} />
      </div>
    </section>
  );
}

export default MainActions;
