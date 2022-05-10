import { Backdrop, CircularProgress } from '@mui/material';
import React from 'react';

export default function GlobalLoading(props: {
    loading: boolean;
}): React.ReactElement {
    return (
        <div>
            <Backdrop
                sx={{
                    color: '#fff',
                    zIndex: 99999,
                }}
                open={props.loading}>
                <CircularProgress size="6em" />
            </Backdrop>
        </div>
    );
}
