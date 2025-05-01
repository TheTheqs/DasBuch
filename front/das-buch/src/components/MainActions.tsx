import PrimaryButton from "./PrimaryButton";

interface MainActionsProps {
    name: string;
}

function MainActions({ name }: MainActionsProps) {
    return (
      <section className="text-dark py-5">
        <div className="container d-flex flex-column align-items-center justify-content-center text-center" style={{ minHeight: '40vh' }}>
          <h1 className="display-4 fw-bold mb-4">Bem-vindo {name}! 📑</h1>

          <p className="lead mb-4">
            Para detalhes sobre a aplicação ou entrar em contato com o desenvolvedor (Matheqs :D) visite a aba "Sobre".
          </p>

          <PrimaryButton to="/new" label="Novo Review" />

          <PrimaryButton to="/my" label="Meus Reviews" />

          <PrimaryButton to="/user" label="Buscar Usuário"/>

        </div>
      </section>
    );
}
  
export default MainActions;