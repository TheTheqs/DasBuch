import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Navbar from "./components/Navbar";
import HomePage from "./pages/HomePage";
import Footer from "./components/Footer";
import SignInPage from "./pages/SignInPage";
import MessagePage from "./pages/MessagePage";
import LogInPage from "./pages/LogInPage";
import ForgotPasswordPage from "./pages/ForgotPasswordPage";
import UserPage from "./pages/UserPage";
import VerifyEmailPage from "./pages/VerifyEmailPage";
import ProfilePage from "./pages/ProfilePage";
import ResetPasswordPage from "./pages/ResetPasswordPage";
import NewReviewPage from "./pages/NewReviewPage";
import UserReviewsPage from "./pages/UserReviewsPage";
import ReviewPage from "./pages/ReviewPage";

function App() {
  return (
    <Router>
      <Navbar />
      <main>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/signin" element={<SignInPage />} />
          <Route path="/verify" element={<VerifyEmailPage />} />
          <Route path="/login" element={<LogInPage />} />
          <Route path="/user" element={<UserPage />} />
          <Route path="/profile" element={<ProfilePage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/reset" element={<ResetPasswordPage />} />
          <Route path="/review/:id" element={<ReviewPage />}/>
          <Route path="/user/reviews/:id" element={<UserReviewsPage />}/>
          <Route path="/new" element={<NewReviewPage />} />
          <Route path="/message" element={<MessagePage />} />
        </Routes>
      </main>
      <Footer />
    </Router>
  );
}

export default App;
