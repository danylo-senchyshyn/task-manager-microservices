import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';

export default function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const response = await fetch('/api/login', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ email, password }),
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Ошибка при входе');
            }

            const data = await response.json();
            handleLoginSuccess(data.token);
        } catch (err) {
            setError(err.message);
        } finally {
            setLoading(false);
        }
    };

    const handleLoginSuccess = (token) => {
        localStorage.setItem('token', token);
        navigate('/dashboard');
    };

    return (
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-r from-purple-600 via-pink-500 to-red-600 px-6">
            <motion.form
                onSubmit={handleSubmit}
                initial={{ opacity: 0, scale: 0.95 }}
                animate={{ opacity: 1, scale: 1 }}
                transition={{ duration: 0.5, ease: 'easeOut' }}
                className="bg-white bg-opacity-95 backdrop-blur-md p-8 rounded-2xl shadow-2xl max-w-md w-full border border-gray-200"
            >
                <h2 className="text-4xl font-extrabold mb-8 text-center text-gray-900">
                    Вход в систему
                </h2>

                {error && (
                    <div className="mb-6 px-4 py-3 text-red-700 bg-red-100 rounded-lg border border-red-400 font-semibold">
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
                    disabled={loading}
                    placeholder="Введите ваш email"
                    className="w-full mb-6 px-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
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
                    disabled={loading}
                    placeholder="Введите пароль"
                    className="w-full mb-8 px-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-600 transition"
                />

                <button
                    type="submit"
                    disabled={loading}
                    className={`w-full py-3 rounded-lg text-white font-semibold text-lg transition-transform ${
                        loading
                            ? 'bg-purple-400 cursor-not-allowed'
                            : 'bg-purple-700 hover:bg-purple-800 active:scale-95 shadow-lg'
                    }`}
                >
                    {loading ? 'Загрузка...' : 'Войти'}
                </button>

                <p className="mt-6 text-center text-gray-600 text-sm">
                    Нет аккаунта?{' '}
                    <button
                        type="button"
                        onClick={() => navigate('/register')}
                        className="text-purple-700 hover:underline font-semibold"
                    >
                        Зарегистрироваться
                    </button>
                </p>
            </motion.form>
        </div>
    );
}