import React, { useEffect, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import {
    LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend
} from 'recharts';
import { Select, MenuItem, CircularProgress } from '@material-ui/core';
import CountryList from '../../../../models/CountryList';
import RestProvider from '../../../../rest/RestProvider';
import CountryListPlot from '../../../../models/CountryListPlot';
import CountryPlot from '../../../../models/CountryPlot';
import StatisticsDay from '../../../../models/StatisticsDay';
import moment from 'moment';
import useCountries from '../../../../hooks/useCountries';
import Country from '../../../../models/Country';

const colors = [
    '#8884d8',
    '#82ca9d',
    '#ffc658',
    '#d0ed57',
    '#a4de6c',
    '#8dd1e1',
    '#83a6ed',
]

const generateData = (plot: CountryListPlot, view: string) => {
    var result = new Array<any>();
    plot.country_plot_list = plot.country_plot_list.sort((a, b) => {
        if (a.country.strategy < b.country.strategy) { return -1; }
        if (a.country.strategy > b.country.strategy) { return 1; }
        return 0;
    })
    plot.country_plot_list.forEach((countryPlot: CountryPlot) => {
        countryPlot.statistics_day_list.forEach((statistic: StatisticsDay) => {
            var date = moment(statistic.date).format("DD/MM/YYYY")
            var day = result.find(data => data.strategy === date);
            if (!day) {
                day = { strategy: date, day: statistic.date }
                result.push(day);
            }
            day[countryPlot.country.strategy] = statistic[view];
        })
    })
    result = result.sort((a, b) => {
        return new Date(a.day).getTime() - new Date(b.day).getTime();
    })
    return result;
}

export default function CompareStrategies(props: { drawerOpen: boolean }) {

    const [countryLibreCirculacion, setCountryLibreCirculacion] = useState(new Country());
    const [countryCuarentena, setCountryCuarentena] = useState(new Country());
    const [countryDistanciamientoSocial, setCountryDistanciamientoSocial] = useState(new Country());
    const [plot, setPlot] = useState(new CountryListPlot());
    const [data, setData] = useState(new Array<any>());
    const [view, setView] = useState("confirmed");
    const [isLoading, setIsLoading] = useState(false);
    const countries = useCountries();

    useEffect(() => {
        if (countryLibreCirculacion.name && countryCuarentena.name && countryDistanciamientoSocial.name) {
            setIsLoading(true);
            var countryList = new CountryList({ id: 0, name: "Lista Ficticia", countries: [countryLibreCirculacion, countryCuarentena, countryDistanciamientoSocial], user_id: 0, creation_date: "" });
            new RestProvider().getCountryListsPlot(countryList)
                .then((plot) => {
                    setPlot(plot);
                })
                .catch(() => {

                })
                .finally(() => {
                    setIsLoading(false);
                })
        }
    }, [countryLibreCirculacion, countryCuarentena, countryDistanciamientoSocial])

    useEffect(() => {
        setData(generateData(plot, view));
    }, [plot, view])

    return (
        <div>
            <Typography variant="h6" id="tableTitle" component="div">
                Comparar Estrategias
            </Typography>
            <div style={{ marginTop: "10px" }}>
                Elija una país para cada estrategia y compare sus gráficas.
            </div>
            <div style={{ display: "flex", alignItems: "center", marginTop: "20px" }}>
                <div>
                    <div>
                        Libre Circulación
                    </div>
                    <Select
                        fullWidth
                        label="libreCirculacion"
                        value={countryLibreCirculacion.iso_country_code}
                        onChange={(event: any) => setCountryLibreCirculacion(countries.find(country => country.iso_country_code === event.target.value) || new Country())}
                        disabled={isLoading}
                    >
                        {
                            countries.filter(country => country.strategy === "Libre Circulación").map((country) => {
                                return (
                                    <MenuItem value={country.iso_country_code}>{country.name}</MenuItem>
                                )
                            })
                        }
                    </Select>
                </div>
                <div style={{ marginLeft: "30px" }}>
                    <div>
                        Cuarentena
                    </div>
                    <Select
                        fullWidth
                        label="cuarentena"
                        value={countryCuarentena.iso_country_code}
                        onChange={(event: any) => setCountryCuarentena(countries.find(country => country.iso_country_code === event.target.value) || new Country())}
                        disabled={isLoading}
                    >
                        {
                            countries.filter(country => country.strategy === "Cuarentena").map((country) => {
                                return (
                                    <MenuItem value={country.iso_country_code}>{country.name}</MenuItem>
                                )
                            })
                        }
                    </Select>
                </div>
                <div style={{ marginLeft: "30px" }}>
                    <div>
                        Distanciamiento Social
                    </div>
                    <Select
                        fullWidth
                        label="distanciamiento-social"
                        value={countryDistanciamientoSocial.iso_country_code}
                        onChange={(event: any) => setCountryDistanciamientoSocial(countries.find(country => country.iso_country_code === event.target.value) || new Country())}
                        disabled={isLoading}
                    >
                        {
                            countries.filter(country => country.strategy === "Distanciamiento Social").map((country) => {
                                return (
                                    <MenuItem value={country.iso_country_code}>{country.name}</MenuItem>
                                )
                            })
                        }
                    </Select>
                </div>
                <div style={{ marginLeft: "30px" }}>
                    <div>
                        Estadística
                    </div>
                    <Select
                        label="view"
                        value={view}
                        onChange={(event: any) => setView(event.target.value)}
                        disabled={isLoading}
                    >
                        <MenuItem value={"confirmed"}>Confirmados</MenuItem>
                        <MenuItem value={"deaths"}>Muertos</MenuItem>
                        <MenuItem value={"recovered"}>Recuperados</MenuItem>
                    </Select>
                </div>
                {
                    isLoading &&
                    <CircularProgress
                        style={{ marginLeft: "30px" }}
                        size={20}
                    />
                }
            </div>
            <LineChart
                width={window.innerWidth - 48 - (props.drawerOpen ? 300 : 0)}
                height={window.innerHeight - 270}
                style={{ marginTop: "20px" }}
                data={data}
                margin={{
                    top: 5, right: 30, left: 20, bottom: 15,
                }}
            >
                <CartesianGrid strokeDasharray="0 0" />
                <XAxis dataKey="name" />
                <YAxis />
                <Tooltip />
                <Legend verticalAlign="top" height={25} />
                {plot.country_plot_list.map((countryPlot, index) => {
                    return (
                        <Line key={index} type="monotone" stroke={colors[index]} dataKey={countryPlot.country.strategy} />
                    )
                })}
            </LineChart>
        </div>
    )
}