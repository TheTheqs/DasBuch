import axios from "axios";
import { RecoveryRequest } from "../components/RecoveryRequestItem";

class RecoverEmailRequestService {
  private api = axios.create({
    baseURL: "http://localhost:8080/api/recovery",
    withCredentials: true,
  });

  // Buscar todas as requisições
  async getAll(): Promise<RecoveryRequest[]> {
    const response = await this.api.get<RecoveryRequest[]>("");
    return response.data;
  }

  // Buscar somente as não resolvidas
  async getPending(): Promise<RecoveryRequest[]> {
    const response = await this.api.get<RecoveryRequest[]>("/pending");
    return response.data;
  }

  // Marcar como resolvido
  async markAsResolved(id: number): Promise<RecoveryRequest> {
    const response = await this.api.put<RecoveryRequest>(`/${id}/resolve?resolved=true`);
    return response.data;
  }

  // Deletar requisição
  async deleteRequest(id: number): Promise<void> {
    await this.api.delete(`/${id}`);
  }
}

export default new RecoverEmailRequestService();
