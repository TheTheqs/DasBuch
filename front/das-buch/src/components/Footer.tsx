function Footer() {
    const currentYear = new Date().getFullYear();
  
    return (
      <footer className="bg-black text-white text-center py-4 mt-5">
        <div className="container">
          <small>
            © {currentYear} <strong>Matheqs</strong>. Todos os direitos reservados.
          </small>
        </div>
      </footer>
    );
  }
  
  export default Footer;