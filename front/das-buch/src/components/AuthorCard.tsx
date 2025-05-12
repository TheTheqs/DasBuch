import { AuthorDTO } from "../type/AuthorDTO";
interface AuthorCardProps {
  author: AuthorDTO;
}

function AuthorCard({ author }: AuthorCardProps) {
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
      onClick={() => (window.location.href = `/author/${author.id}`)}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 style={{ fontSize: "1.3rem", marginBottom: "0.4rem" }}>
        {author.name}
      </h2>
      <p style={{ margin: 0, fontSize: "0.85rem", color: "#666" }}>
        {author.books.length > 0
          ? author.books.join(", ")
          : "Nenhum livro associado"}
      </p>
    </div>
  );
}

export default AuthorCard;
