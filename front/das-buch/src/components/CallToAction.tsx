import PrimaryButton from "./PrimaryButton";

function CallToAction() {
  return (
    <section className="text-dark py-5">
      <div
        className="container d-flex flex-column align-items-center justify-content-center text-center"
        style={{ minHeight: "40vh" }}
      >
        <h1 className="display-4 fw-bold mb-3">Bem-vindo ao DasBuch 📚</h1>
        <p className="lead mb-4">
          Descubra, leia e compartilhe suas experiências literárias!
        </p>
        <PrimaryButton to="/signin" label="Comece agora" />

        <PrimaryButton to="/login" label="Fazer Login" />
      </div>
    </section>
  );
}

export default CallToAction;
