import { useState, useEffect } from 'react';
import RestProvider from '../rest/RestProvider';
import Country from '../models/Country';

export default function useCountries() {
    const [countries, setCountries] = useState(new Array<Country>());

    useEffect(() => {
        var rest = new RestProvider();
        rest.getCountries({})
            .then((countries: Array<Country>) => {
                setCountries(countries);
            })
            .catch((err: any) => {
                console.log(err);
            })
    }, [])

    return countries;
}