interface SearchResultGridProps {
    children: React.ReactNode;
  }
  
  function SearchResultGrid({ children }: SearchResultGridProps) {
    return (
      <div
        style={{
          display: "flex",
          flexDirection: "column",
          alignItems: "center",
          gap: "1rem"
        }}
      >
        {children}
      </div>
    );
  }
  
  export default SearchResultGrid;