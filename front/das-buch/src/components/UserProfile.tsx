import { UserDTO } from "../type/UserDTO";
import ProfileLine from "./ProfileLine";
import { useUser } from "../context/User";
import { Button } from "react-bootstrap";
import UserService from "../services/UserService";
import { useNavigate } from "react-router-dom";

interface UserProfileProps {
  relatedUser: UserDTO;
}

function UserProfile({ relatedUser }: UserProfileProps) {
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
        textAlign: "left",
      }}
    >
      <ProfileLine label="Name" content={relatedUser.name} />
      <ProfileLine label="Reviews" content={String(relatedUser.reviewCount)} />

      {(user?.role === "ADMIN" || user?.id === relatedUser.id) && (
        <>
          <ProfileLine label="ID" content={String(relatedUser.id)} />
          <ProfileLine label="Email" content={relatedUser.email} />
          <ProfileLine label="Role" content={relatedUser.role} />
          <Button
            variant="outline-danger"
            className="d-block mx-auto mt-4"
            onClick={async () => {
              const confirmed = window.confirm(
                "Tem certeza que deseja deletar este usuário?"
              );
              if (!confirmed) return;

              const message = await UserService.deleteUser(relatedUser.id);
              alert(message);

              if (user?.id === relatedUser.id) {
                await UserService.logout();
                navigate("/");
              } else {
                navigate(`/`);
              }
            }}
          >
            Deletar Perfil
          </Button>
        </>
      )}
    </div>
  );
}

export default UserProfile;
