import { useTranslation } from "react-i18next";

interface UserCardProps {
  name: string;
  id: number;
  reviewCount: number;
}

function UserCard({ name, id, reviewCount }: UserCardProps) {
  const { t } = useTranslation();
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
      onClick={() => (window.location.href = `/user/reviews/${id}`)}
      onMouseEnter={(e) => (e.currentTarget.style.transform = "scale(1.02)")}
      onMouseLeave={(e) => (e.currentTarget.style.transform = "scale(1)")}
    >
      <h2 style={{ fontSize: "1.5rem", marginBottom: "0.5rem" }}>{name}</h2>
      <p style={{ margin: 0, fontSize: "0.9rem" }}>
        {t("userCard.idLabel", { id })}
      </p>
      <p style={{ margin: 0, fontSize: "0.9rem" }}>
        {t("userCard.reviewLabel", { count: reviewCount })}
      </p>
    </div>
  );
}

export default UserCard;
