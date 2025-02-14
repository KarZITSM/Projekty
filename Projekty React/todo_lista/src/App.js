import React, {useEffect, useState} from 'react';
import './App.css';
import TaskList from './components/TaskList';
import AddTaskForm from './components/AddTaskForm';
import { v4 } from 'uuid';

const App = () => {
  const [tasks, setTasks] = useState([]);

  useEffect(() => {
    fetch('/tasks.json')
      .then(response => response.json())
      .then(data => setTasks(data));
  }, []);

  const addTaskForm = (task) => {
    setTasks([...tasks, { ...task, id: v4() }]);
  };


  const deleteTask = (id) => {
    setTasks(tasks.filter((task) => task.id !== id));
  };

  // Funkcja aktualizacji statusu zadania
  const toggleTaskStatus = (id) => {
    setTasks(
      tasks.map((task) =>
        task.id === id
          ? { ...task, status: task.status === 'wykonane' ? 'oczekujące' : 'wykonane' }
          : task
      )
    );
  };

  // Funkcja zmiany trudności
  const updateDifficulty = (id, newDifficulty) => {
    setTasks(
      tasks.map((task) =>
        task.id === id && task.status === 'oczekujące'
          ? { ...task, difficulty: newDifficulty }
          : task
      )
    );
  };

  const checkForOverdueTasks = () => {
    const now = new Date();
    setTasks((prevTasks) =>
      prevTasks.map((task) =>
        task.status === 'oczekujące' && new Date(task.dueDate) < now
          ? { ...task, status: 'przeterminowane' }
          : task
      )
    );
  };

  // useEffect z interwałem, który sprawdza przeterminowane zadania co minutę
  useEffect(() => {
    const intervalId = setInterval(checkForOverdueTasks, 60000); // Sprawdza co minutę
    return () => clearInterval(intervalId); // Czyści interwał przy odmontowaniu komponentu
  }, [tasks]);

  return(
    <div className="App">
        <h1>Lista zadań do zrobienia</h1>
        <AddTaskForm AddTaskForm={addTaskForm}/>
        <TaskList
          tasks={tasks}
          deleteTask={deleteTask}
          toggleTaskStatus={toggleTaskStatus}  
          updateDifficulty={updateDifficulty}
        />
    </div>

  );
}

export default App;
