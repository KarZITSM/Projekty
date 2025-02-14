import React, { useState } from 'react';
import { v4 } from 'uuid';

const AddTaskForm = ({AddTaskForm}) => {
  const [title, setTitle] = useState('');
  const [details, setDetails] = useState('');
  const [dueDate, setDueDate] = useState('');
  const [difficulty, setDifficulty] = useState(0);

  const handleSumbit = (e) => {
    e.preventDefault();
    AddTaskForm({
      id: v4(),
      title,
      details,
      dueDate,
      status: 'oczekujące',
      difficulty
    });
    setTitle('');
    setDetails('');
    setDueDate('');
    setDifficulty(0);
  }

  return(
      <form onSubmit={handleSumbit}>
        <input value={title} onChange={e => setTitle(e.target.value)} placeholder="Tytuł zadania" required />
        <input value={details} onChange={e => setDetails(e.target.value)} placeholder="Szczegóły zadania" required />
        <input type="datetime-local" value={dueDate} onChange={e => setDueDate(e.target.value)} required />
        <input type="number" min="0" max="10" value={difficulty} onChange={e => setDifficulty(e.target.value)} />
        <button type="submit">Dodaj zadanie</button>
      </form>
    );
}
  
  export default AddTaskForm;