import { Link } from "react-router-dom";
import { useUser } from "../context/User";
import { handleApiError } from "../utils/handleApiError";
import { useNavigate } from "react-router-dom";
import UserService from "../services/UserService";
import { useTranslation } from "react-i18next";

function Navbar() {
  const { user, clearUser } = useUser();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const handleLogout = async () => {
    try {
      await UserService.logout();
    } catch (error) {
      alert(handleApiError(error));
    } finally {
      clearUser();
      navigate("/");
    }
  };

  return (
    <nav className="bg-dark navbar navbar-expand-md fixed-top p-0 navbar-dark">
      <div className="container">
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navcol-1"
        >
          <span className="visually-hidden">{t("navbar.toggle")}</span>
          <span className="navbar-toggler-icon"></span>
        </button>
        <div id="navcol-1" className="collapse navbar-collapse">
          <ul className="navbar-nav flex-grow-1 justify-content-between">
            <li className="nav-item">
              <Link to="/" className="nav-link">
                <i className="fas fa-book fa-2x"></i>
              </Link>
            </li>

            <li className="nav-item">
              <Link to="/book" className="nav-link">
                {t("navbar.books")}
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/author" className="nav-link">
                {t("navbar.authors")}
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/review" className="nav-link">
                {t("navbar.reviews")}
              </Link>
            </li>
            <li className="nav-item">
              <Link to="/about" className="nav-link">
                {t("navbar.about")}
              </Link>
            </li>
            {/*If not logged*/}
            {user === null && (
              <>
                <li className="nav-item">
                  <Link to="/signin" className="nav-link">
                    {t("navbar.signin")}
                  </Link>
                </li>
                <li className="nav-item">
                  <Link to="/login" className="nav-link">
                    {t("navbar.login")}
                  </Link>
                </li>
              </>
            )}

            {/* If logged */}
            {user !== null && (
              <>
                <li className="nav-item">
                  <Link to="/profile" className="nav-link">
                    {user.name}
                  </Link>
                </li>
                <li className="nav-item">
                  <button
                    onClick={handleLogout}
                    className="btn btn-link nav-link"
                    style={{ textDecoration: "none" }}
                  >
                    {t("navbar.logout")}
                  </button>
                </li>
              </>
            )}
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;
