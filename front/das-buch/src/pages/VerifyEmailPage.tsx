import { useEffect, useRef, useState } from "react";
import { useSearchParams } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";
import { useTranslation } from "react-i18next";

export default function VerifyEmailPage() {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const calledRef = useRef(false);
  const { t } = useTranslation();

  useEffect(() => {
    const token = searchParams.get("token");

    if (!token) {
      setError(t("verifyEmail.tokenMissing"));
      return;
    }

    if (calledRef.current) return;
    calledRef.current = true;

    const verify = async () => {
      try {
        const responseMessage = await UserService.verifyUser(token);
        setMessage(responseMessage);
      } catch (err) {
        setError(t(handleApiError(err)));
      }
    };

    verify();
  }, [searchParams, t]);

  return (
    <div style={{ padding: "2rem", textAlign: "center" }}>
      <h1>{t("verifyEmail.title")}</h1>

      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}
