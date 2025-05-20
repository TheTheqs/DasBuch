import React from "react";

interface RecoveryRequestControlsProps {
  onShowAll: () => void;
  onShowPending: () => void;
}

const RecoveryRequestControls: React.FC<RecoveryRequestControlsProps> = ({
  onShowAll,
  onShowPending,
}) => {
  return (
    <div className="d-flex gap-3 mb-4">
      <button className="btn btn-primary" onClick={onShowPending}>
        Buscar Requisições Pendentes
      </button>
      <button className="btn btn-secondary" onClick={onShowAll}>
        Mostrar Todas as Requisições
      </button>
    </div>
  );
};

export default RecoveryRequestControls;