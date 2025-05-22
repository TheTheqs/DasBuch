import React from "react";
import { useTranslation } from "react-i18next";

interface RecoveryRequestControlsProps {
  onShowAll: () => void;
  onShowPending: () => void;
}

const RecoveryRequestControls: React.FC<RecoveryRequestControlsProps> = ({
  onShowAll,
  onShowPending,
}) => {
  const { t } = useTranslation();
  return (
    <div className="d-flex gap-3 mb-4">
      <button className="btn btn-primary" onClick={onShowPending}>
        {t("recovery.showPending")}
      </button>
      <button className="btn btn-secondary" onClick={onShowAll}>
        {t("recovery.showAll")}
      </button>
    </div>
  );
};

export default RecoveryRequestControls;
