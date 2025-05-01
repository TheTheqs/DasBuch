import PaginationButton from "./PaginationButton";

interface PaginationBarProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
  }
  
  function PaginationBar({ currentPage, totalPages, onPageChange }: PaginationBarProps) {
    if (totalPages <= 1) return null;
  
    return (
      <div
        style={{
          width: "100%",
          display: "flex",
          justifyContent: "center",
          marginTop: "2rem",
          flexWrap: "wrap"
        }}
      >
        {Array.from({ length: totalPages }).map((_, index) => (
          <PaginationButton
            key={index}
            page={index}
            isCurrent={index === currentPage}
            onClick={() => onPageChange(index)}
          />
        ))}
      </div>
    );
  }
  
  export default PaginationBar;