import CallToAction from "../components/CallToAction";
import MainActions from "../components/MainActions";
import { useUser } from "../context/User";

function HomePage() {
  const {user} = useUser();

  if(!user) {
    return (
      <div style={{ paddingTop: '60px' }}>
        <CallToAction />
      </div>
    );
  }

  return (
    <div style={{ paddingTop: '0px' }}>
      <MainActions name= {user.name} />
    </div>
  );
  }
  
export default HomePage;