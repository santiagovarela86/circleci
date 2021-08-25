import { useState, useEffect } from 'react';

export default function useGrid<Data, Options>(request: (options: Options) => Promise<Array<Data>>, options: Options) {
    const [rows, setRows] = useState(Array<Data>());
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(false);

    var refresh = function(){
        setIsLoading(true);
        request(options).then(data => {
            setRows(data);
        })
            .catch(() => {
                setError(true);
            })
            .finally(() => {
                setIsLoading(false);
            })
    }

    useEffect(() => {
        request(options).then(data => {
            setRows(data);
        })
            .catch(() => {
                setError(true);
            })
            .finally(() => {
                setIsLoading(false);
            })
    }, [request, options])

    return [rows, isLoading, error, refresh];
}