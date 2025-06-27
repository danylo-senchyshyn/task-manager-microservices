import { useState } from 'react';
import { useNavigate } from 'react-router-dom';

export default function Register() {
    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);

        if (password !== confirmPassword) {
            setError('Пароли не совпадают');
            return;
        }

        setLoading(true);

        try {
            const response = await fetch('http://localhost:8081/api/users/register', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, username, password }),
            });

            const text = await response.text();

            let data = null;
            try {
                data = text ? JSON.parse(text) : null;
            } catch {
                // Если не JSON — оставить data null
            }

            if (!response.ok) {
                throw new Error(data?.message || 'Ошибка при регистрации');
            }

            navigate('/login');
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-purple-600 via-pink-500 to-red-500 p-6">
            <form
                onSubmit={handleSubmit}
                className="bg-white p-8 rounded-2xl shadow-2xl max-w-md w-full border border-gray-200"
            >
                <h2 className="text-4xl font-extrabold mb-8 text-center text-gray-900">
                    Регистрация
                </h2>

                {error && (
                    <div className="mb-6 text-red-700 bg-red-100 border border-red-400 px-4 py-3 rounded">
                        {error}
                    </div>
                )}

                <label htmlFor="email" className="block mb-2 font-semibold text-gray-700">
                    Email
                </label>
                <input
                    id="email"
                    type="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                    className="w-full px-4 py-3 mb-5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
                    disabled={loading}
                />

                <label htmlFor="username" className="block mb-2 font-semibold text-gray-700">
                    Имя пользователя
                </label>
                <input
                    id="username"
                    type="text"
                    value={username}
                    onChange={(e) => setUsername(e.target.value)}
                    required
                    className="w-full px-4 py-3 mb-5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
                    disabled={loading}
                />

                <label htmlFor="password" className="block mb-2 font-semibold text-gray-700">
                    Пароль
                </label>
                <input
                    id="password"
                    type="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required
                    className="w-full px-4 py-3 mb-5 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
                    disabled={loading}
                />

                <label
                    htmlFor="confirmPassword"
                    className="block mb-2 font-semibold text-gray-700"
                >
                    Подтвердите пароль
                </label>
                <input
                    id="confirmPassword"
                    type="password"
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    required
                    className="w-full px-4 py-3 mb-8 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
                    disabled={loading}
                />

                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full py-3 rounded-lg text-white text-lg font-semibold transition ${
                        loading ? 'bg-purple-400 cursor-not-allowed' : 'bg-purple-700 hover:bg-purple-800'
                    }`}
                >
                    {loading ? 'Регистрация...' : 'Зарегистрироваться'}
                </button>

                <p className="mt-6 text-center text-gray-600 text-sm">
                    Уже есть аккаунт?{' '}
                    <button
                        type="button"
                        onClick={() => navigate('/login')}
                        className="text-purple-700 font-semibold hover:underline"
                    >
                        Войти
                    </button>
                </p>
            </form>
        </div>
    );
}