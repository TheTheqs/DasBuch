import { Link } from 'react-router-dom';

function Navbar() {
  return (
    <nav className="bg-dark navbar navbar-expand-md fixed-top p-0 navbar-dark">
      <div className="container">
        <button className="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navcol-1">
          <span className="visually-hidden">Toggle navigation</span>
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
              <Link to="/livros" className="nav-link">Livros</Link>
            </li>
            <li className="nav-item">
              <Link to="/autores" className="nav-link">Autores</Link>
            </li>
            <li className="nav-item">
              <Link to="/reviews" className="nav-link">Reviews</Link>
            </li>
            <li className="nav-item">
              <Link to="/sobre" className="nav-link">Sobre</Link>
            </li>
            <li className="nav-item">
              <Link to="/signin" className="nav-link">Sign In</Link>
            </li>
            <li className="nav-item">
              <Link to="/login" className="nav-link">Log In</Link>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  );
}

export default Navbar;