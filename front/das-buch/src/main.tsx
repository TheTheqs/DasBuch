import { createRoot } from 'react-dom/client'
import 'bootstrap/dist/css/bootstrap.min.css';
import '@fortawesome/fontawesome-free/css/all.min.css';
import './index.css'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import { UserProvider } from './context/User.tsx';
import App from './App.tsx'

createRoot(document.getElementById('root')!).render(
  <UserProvider>
    <App />
  </UserProvider>,
)
