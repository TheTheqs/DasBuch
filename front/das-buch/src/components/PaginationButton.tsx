interface PaginationButtonProps {
    page: number;
    isCurrent?: boolean;
    onClick: () => void;
  }
  
  function PaginationButton({ page, isCurrent = false, onClick }: PaginationButtonProps) {
    return (
      <button
        onClick={onClick}
        disabled={isCurrent}
        style={{
          padding: "0.5rem 1rem",
          margin: "0 0.25rem",
          borderRadius: "4px",
          border: "1px solid #ccc",
          backgroundColor: isCurrent ? "#e0e0e0" : "#ffffff",
          color: "#333",
          cursor: isCurrent ? "default" : "pointer",
          fontWeight: isCurrent ? "bold" : "normal"
        }}
      >
        {page + 1}
      </button>
    );
  }
  
  export default PaginationButton;