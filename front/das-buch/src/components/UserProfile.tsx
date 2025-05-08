import { UserDTO } from "../type/UserDTO";
import ProfileLine from "./ProfileLine";

interface UserProfileProps {
    user: UserDTO;
}

function UserProfile({user}: UserProfileProps) {
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
          textAlign: "left"
        }}
      >
        <ProfileLine label="ID" content={String(user.id)} />
        <ProfileLine label="Name" content={user.name} />
        <ProfileLine label="Email" content={user.email} />
        <ProfileLine label="Role" content={user.role} />
        <ProfileLine label="Reviews" content={String(user.reviewCount)} />        
      </div>
    );
}
  
export default UserProfile;