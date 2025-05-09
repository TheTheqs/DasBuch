import { BookDTO } from "../type/BookDTO";
import { Button } from "react-bootstrap";
import { useUser } from "../context/User";
import { useNavigate } from "react-router-dom";
import BookService from "../services/BookService";

interface BookCardProps {
  book: BookDTO;
}

function BookCard({ book }: BookCardProps) {
  const { user } = useUser();
  const navigate = useNavigate();
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
      onClick={() => (window.location.href = `/book/${book.id}`)}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 style={{ fontSize: "1.3rem", marginBottom: "0.4rem" }}>
        {book.title}
      </h2>
      <p style={{ margin: 0, fontSize: "0.85rem", color: "#666" }}>
        {book.authors.length > 0
          ? book.authors.map((a) => a.name).join(", ")
          : "Autor desconhecido"}
      </p>
      <p style={{ marginTop: "0.5rem", fontSize: "0.85rem" }}>
        {book.reviewCount} {book.reviewCount === 1 ? "review" : "reviews"}
      </p>

      {user?.role === "ADMIN" && (
        <div className="d-flex gap-2">
          <Button
            variant="outline-primary"
            onClick={(e) => {
              e.stopPropagation();
              navigate(`/update_book/${book.id}`);
            }}
          >
            Editar
          </Button>

          <Button
            variant="outline-danger"
            onClick={async (e) => {
              e.stopPropagation();
              const confirmed = window.confirm(
                "Tem certeza que deseja deletar este livro?"
              );
              if (!confirmed) return;

              const message = await BookService.deleteBook(book.id);
              alert(message);

              navigate(`/`);
            }}
          >
            Deletar
          </Button>
        </div>
      )}
    </div>
  );
}

export default BookCard;
