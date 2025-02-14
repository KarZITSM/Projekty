import React from "react";
import TaskItem from "../TaskItem";

const TaskList = ({tasks = [], deleteTask, toggleTaskStatus, updateDifficulty}) => {
    return(
        <div className="List">
            {tasks.map((task) => (
                <TaskItem
                    key={task.id}
                    task={task}
                    deleteTask={deleteTask}
                    toggleTaskStatus={toggleTaskStatus}
                    updateDifficulty={updateDifficulty}
                />
            ))}
        </div>
    );
}

export default TaskList