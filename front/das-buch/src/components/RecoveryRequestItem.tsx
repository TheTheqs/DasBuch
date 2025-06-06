import React from "react";
import { useTranslation } from "react-i18next";

export interface RecoveryRequest {
  id: number;
  email: string;
  token: string;
  resolved: boolean;
  createdAt: string;
}

interface RecoveryRequestItemProps {
  request: RecoveryRequest;
  onResolve: (id: number) => void;
  onDelete: (id: number) => void;
}

const RecoveryRequestItem: React.FC<RecoveryRequestItemProps> = ({
  request,
  onResolve,
  onDelete,
}) => {
  const { t } = useTranslation();

  return (
    <div className="card mb-3 p-3 shadow-sm">
      <div className="d-flex justify-content-between align-items-center mb-2">
        <strong>{request.email}</strong>
        <span
          className={`badge ${
            request.resolved ? "bg-success" : "bg-warning text-dark"
          }`}
        >
          {request.resolved ? t("recovery.resolved") : t("recovery.pending")}
        </span>
      </div>
      <div className="mb-2">
        <small className="text-muted">
          {t("recovery.createdAt")}{" "}
          {new Date(request.createdAt).toLocaleString()}
        </small>
      </div>
      <div className="mb-3">
        <code className="bg-light p-2 rounded d-block">{request.token}</code>
      </div>
      <div className="d-flex gap-2">
        <button
          className="btn btn-outline-success btn-sm"
          onClick={() => onResolve(request.id)}
        >
          {t("recovery.markAsResolved")}
        </button>
        <button
          className="btn btn-outline-danger btn-sm"
          onClick={() => onDelete(request.id)}
        >
          {t("recovery.delete")}
        </button>
      </div>
    </div>
  );
};

export default RecoveryRequestItem;
