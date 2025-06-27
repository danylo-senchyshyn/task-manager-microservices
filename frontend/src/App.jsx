import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Login from './Login';
import Register from './Register';
import PrivateRoute from './PrivateRoute';
import Dashboard from './Dashboard';

export default function App() {
    return (
        <Router>
            <Routes>
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />

                <Route
                    path="/dashboard"
                    element={
                        <PrivateRoute>
                            <Dashboard />
                        </PrivateRoute>
                    }
                />

                <Route
                    path="*"
                    element={
                        <div className="min-h-screen flex items-center justify-center">
                            <h1>Главная страница (пока пусто)</h1>
                        </div>
                    }
                />
            </Routes>
        </Router>
    );
}