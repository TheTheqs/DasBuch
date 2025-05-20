import React from "react";
import RecoveryRequestItem, { RecoveryRequest } from "./RecoveryRequestItem";

interface RecoveryRequestListProps {
  requests: RecoveryRequest[];
  onResolve: (id: number) => void;
  onDelete: (id: number) => void;
}

const RecoveryRequestList: React.FC<RecoveryRequestListProps> = ({
  requests,
  onResolve,
  onDelete,
}) => {
  return (
    <div>
      {requests.map((req) => (
        <RecoveryRequestItem
          key={req.id}
          request={req}
          onResolve={onResolve}
          onDelete={onDelete}
        />
      ))}
    </div>
  );
};

export default RecoveryRequestList;