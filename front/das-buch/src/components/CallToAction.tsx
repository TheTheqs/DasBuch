function CallToAction() {
    return (
      <section className="text-dark py-5">
        <div className="container d-flex flex-column align-items-center justify-content-center text-center" style={{ minHeight: '40vh' }}>
          <h1 className="display-4 fw-bold mb-3">Bem-vindo ao DasBuch 📚</h1>
          <p className="lead mb-4">
            Descubra, leia e compartilhe suas experiências literárias!
          </p>
          <a href="/signin" className="btn btn-darksoft btn-lg px-4">
            Comece agora
          </a>
        </div>
      </section>
    );
  }
  
  export default CallToAction;