import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';

export default function Dashboard() {
    const [tasks, setTasks] = useState([]);
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            navigate('/login');
            return;
        }

        fetch('/api/tasks', {
            headers: {
                Authorization: `Bearer ${token}`,
            },
        })
            .then(async (res) => {
                if (!res.ok) {
                    const errorData = await res.json();
                    throw new Error(errorData.message || 'Ошибка при загрузке задач');
                }
                return res.json();
            })
            .then((data) => setTasks(data))
            .catch((err) => setError(err.message));
    }, [navigate]);

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login');
    };

    const getStatusColor = (status) => {
        switch (status.toLowerCase()) {
            case 'todo':
                return 'bg-yellow-100 text-yellow-800';
            case 'in_progress':
                return 'bg-blue-100 text-blue-800';
            case 'done':
                return 'bg-green-100 text-green-800';
            default:
                return 'bg-gray-200 text-gray-700';
        }
    };

    return (
        <motion.div
            initial={{ opacity: 0, y: 30 }}
            animate={{ opacity: 1, y: 0 }}
            className="min-h-screen flex flex-col bg-gray-100"
        >
            <header className="bg-purple-600 text-white px-6 py-4 flex justify-between items-center shadow-md">
                <h1 className="text-2xl font-bold">Панель задач</h1>
                <button
                    onClick={handleLogout}
                    className="bg-white text-purple-600 font-semibold px-4 py-2 rounded hover:bg-gray-100 transition"
                >
                    Выйти
                </button>
            </header>

            <main className="flex-1 p-6">
                {error && <div className="text-red-600 font-semibold mb-4">{error}</div>}
                {!error && tasks.length === 0 && <p className="text-gray-600">У вас пока нет задач.</p>}

                <div className="grid sm:grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
                    {tasks.map((task) => (
                        <div
                            key={task.id}
                            className="bg-white p-6 rounded-xl shadow hover:shadow-lg transition"
                        >
                            <h2 className="text-xl font-semibold mb-2 text-gray-800">{task.title}</h2>
                            <p className="text-gray-600 mb-4">{task.description}</p>
                            <span
                                className={`inline-block px-3 py-1 text-sm rounded-full font-medium ${getStatusColor(task.status)}`}
                            >
                                {task.status}
                            </span>
                        </div>
                    ))}
                </div>
            </main>
        </motion.div>
    );
}