import { BookDTO } from "../type/BookDTO";

interface BookCardProps {
  book: BookDTO;
}

function BookCard({ book }: BookCardProps) {
  return (
    <div
      style={{
        backgroundColor: "#f2f2f2",
        color: "#333",
        padding: "1rem",
        borderRadius: "8px",
        boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
        width: "100%",
        maxWidth: "320px",
        margin: "0.5rem auto",
        textAlign: "center",
        cursor: "pointer",
        transition: "transform 0.2s ease-in-out",
      }}
      onClick={() => window.location.href = `/book/${book.id}`}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 style={{ fontSize: "1.3rem", marginBottom: "0.4rem" }}>{book.title}</h2>
      <p style={{ margin: 0, fontSize: "0.85rem", color: "#666" }}>
        {book.authors.length > 0 ? book.authors.map((a) => a.name).join(", ") : "Autor desconhecido"}
      </p>
      <p style={{ marginTop: "0.5rem", fontSize: "0.85rem" }}>
        {book.reviewCount} {book.reviewCount === 1 ? "review" : "reviews"}
      </p>
    </div>
  );
}

export default BookCard;
