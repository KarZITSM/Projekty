import React from "react";
import { FixedSizeList as VirtualizedList } from "react-window";

function List({data=[], renderItem, renderEmpty}) {
    if(!data.length) return renderEmpty;
    

    return (
        <ul>
            {data.map((item, i)=> (
                <li key={i}>{renderItem}</li>
            ))}
        </ul>
    );

    
}

export default List;