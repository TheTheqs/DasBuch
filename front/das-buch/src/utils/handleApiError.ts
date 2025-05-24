import axios from "axios";

// Ajustado para respeitar a mensagem do backend SEM sobrescrever à toa
export function handleApiError(error: unknown): string {
  if (axios.isAxiosError(error)) {
    // Se houve resposta do servidor (status >= 400)
    if (error.response) {
      const data = error.response.data;

      // Se o back respondeu um objeto com message, retorna isso
      if (data && typeof data === "object" && "message" in data) {
        return (data.message);
      }

      // Se o body for string direto (sem objeto)
      if (typeof data === "string") {
        return data;
      }

      return "error.unknownMessage";
    }

    // Se foi erro de conexão, DNS ou algo do tipo
    return "error.serverUnavailable";
  }

  // Erros fora do Axios (problemas internos)
  return "error.unexpected";
}