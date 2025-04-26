
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Navbar from './components/Navbar';
import HomePage from './pages/HomePage';
import Footer from './components/Footer';
import SignInPage from './pages/SignInPage';
import MessagePage from './pages/MessagePage';

function App() {
    return (
      <Router>
        <Navbar />
        <main>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/message" element={<MessagePage />} />
        </Routes>
        </main>
        <Footer />
      </Router>
    );
  }

export default App
