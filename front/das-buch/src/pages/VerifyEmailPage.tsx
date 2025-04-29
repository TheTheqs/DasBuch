import { useEffect, useRef,useState } from "react";
import { useSearchParams } from "react-router-dom";
import { handleApiError } from "../utils/handleApiError";
import UserService from "../services/UserService";

export default function VerifyEmailPage() {
  const [searchParams] = useSearchParams();
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const calledRef = useRef(false);

  useEffect(() => {
    const token = searchParams.get("token");

    if (!token) {
        setError("Token de verificação não encontrado.");
        return;
      }
    
    if (calledRef.current) return;
    calledRef.current = true;

    const verify = async () => {
      try {
        const responseMessage = await UserService.verifyUser(token);
        setMessage(responseMessage);
      } catch (error) {
        setError(handleApiError(error));
    }};

    verify();
  }, [searchParams]);

  return (
    <div style={{ padding: "2rem", textAlign: "center" }}>
      <h1>Verificação de Email</h1>

      {message && <p style={{ color: "green" }}>{message}</p>}
      {error && <p style={{ color: "red" }}>{error}</p>}
    </div>
  );
}