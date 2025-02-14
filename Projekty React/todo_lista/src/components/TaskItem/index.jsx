import React from "react";


const TaskItem = ({task, deleteTask, toggleTaskStatus, updateDifficulty}) => {
    const {id, title, details, dueDate, status, difficulty} = task;


    return(
        <div className={`task ${status}`}>
            <h3>{title}</h3>
            <p>{details}</p>
            <p>Termin: {new Date(dueDate).toLocaleString()}</p>
            <p>Trudność: {'★'.repeat(difficulty)}{'☆'.repeat(10 - difficulty)}</p>
            <button onClick={() => toggleTaskStatus(id)}>
                {status === 'wykonane' ? 'Oznacz jako niewykonane' : 'Oznacz jako wykonane'}
            </button>
            {status !== 'wykonane' && status !== 'przeterminowane' && (
                <>
                    <button onClick={() => updateDifficulty(id, Math.max(0, difficulty - 1))}>-</button>
                    <button onClick={() => updateDifficulty(id, Math.min(10, difficulty + 1))}>+</button>
                </>
            )}
            {(status === 'przeterminowane' || status === 'wykonane') && (
                <button onClick={() => deleteTask(id)}>Usuń</button>
            )}
        </div>
    );
}

export default TaskItem