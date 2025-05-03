/* eslint-disable react-refresh/only-export-components */
import { createContext, useContext, useState, useEffect,ReactNode } from "react";
import { UserDTO } from "../type/UserDTO";

interface UserContextType {
  user: UserDTO | null;
  setUser: (user: UserDTO) => void;
  clearUser: () => void;
}

const UserContext = createContext<UserContextType | undefined>(undefined);

export function UserProvider({ children }: { children: ReactNode }) {
  const [user, setUserState] = useState<UserDTO | null>(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      setUserState(JSON.parse(savedUser));
    }
  }, []);

  const setUser = (user: UserDTO) => {
    setUserState(user);
    localStorage.setItem("user", JSON.stringify(user));
  };
  const clearUser = () => {
    setUserState(null);
    localStorage.removeItem("user");
  };

  return (
    <UserContext.Provider value={{ user, setUser, clearUser }}>
      {children}
    </UserContext.Provider>
  );
}

export function useUser() {
  const context = useContext(UserContext);
  if (!context) {
    throw new Error("useUser must be used within a UserProvider");
  }
  return context;
}
