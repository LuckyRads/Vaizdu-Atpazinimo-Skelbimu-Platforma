import { CardMedia } from '@mui/material';
import React from 'react';

type PropsType = {
    data: string;
};

export default function ImageBox(props: PropsType): React.ReactElement {
    return (
        <CardMedia
            component="img"
            sx={{
                height: 200,
                width: 200,
            }}
            src={`data:image/png;base64,${props.data}`}
        />
    );
}
