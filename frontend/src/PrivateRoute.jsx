import { Navigate } from 'react-router-dom';

export default function PrivateRoute({ children }) {
    const isAuth = !!localStorage.getItem('token'); // или любая твоя логика авторизации

    if (!isAuth) {
        return <Navigate to="/login" replace />;
    }

    return children;
}