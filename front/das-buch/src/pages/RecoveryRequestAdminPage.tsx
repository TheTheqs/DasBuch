import React, { useState } from "react";
import RecoveryRequestControls from "../components/RecoveryRequestControls";
import RecoveryRequestList from "../components/RecoveryRequestList";
import { RecoveryRequest } from "../components/RecoveryRequestItem";
import RecoverEmailRequestService from "../services/RecoverEmailRequestService";
import { useUser } from "../context/User";

const RecoveryRequestAdminPage: React.FC = () => {
  const { user } = useUser();
  const [requests, setRequests] = useState<RecoveryRequest[]>([]);

  if (!user || user.role !== "ADMIN") {
    return (
      <div className="container py-4">
        <h3 className="text-danger">Acesso restrito</h3>
        <p>Você precisa ser um administrador para visualizar esta página.</p>
      </div>
    );
  }

  const fetchAll = async () => {
    try {
      const data = await RecoverEmailRequestService.getAll();
      setRequests(data);
    } catch (error) {
      console.error("Erro ao buscar todas as requisições:", error);
    }
  };

  const fetchPending = async () => {
    try {
      const data = await RecoverEmailRequestService.getPending();
      setRequests(data);
    } catch (error) {
      console.error("Erro ao buscar requisições pendentes:", error);
    }
  };

  const handleResolve = async (id: number) => {
    try {
      await RecoverEmailRequestService.markAsResolved(id);
      setRequests((prev) =>
        prev.map((r) => (r.id === id ? { ...r, resolved: true } : r))
      );
    } catch (error) {
      console.error("Erro ao marcar como resolvido:", error);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await RecoverEmailRequestService.deleteRequest(id);
      setRequests((prev) => prev.filter((r) => r.id !== id));
    } catch (error) {
      console.error("Erro ao deletar requisição:", error);
    }
  };

  return (
    <div className="container py-4">
      <h2 className="mb-4">Gerenciamento de Requisições de Recuperação</h2>
      <RecoveryRequestControls onShowAll={fetchAll} onShowPending={fetchPending} />
      <RecoveryRequestList
        requests={requests}
        onResolve={handleResolve}
        onDelete={handleDelete}
      />
    </div>
  );
};

export default RecoveryRequestAdminPage;