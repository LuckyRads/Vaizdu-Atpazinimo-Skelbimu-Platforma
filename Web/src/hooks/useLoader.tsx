import React, { useState } from 'react';
import GlobalLoading from '../components/Loading/GlobalLoading';

type LoaderType = [React.ReactElement | null, () => void, () => void];

const useLoader = (): LoaderType => {
    const [loading, setLoading] = useState(false);
    return [
        <GlobalLoading loading={loading} />,
        (): void => setLoading(true),
        (): void => setLoading(false),
    ];
};

export default useLoader;
